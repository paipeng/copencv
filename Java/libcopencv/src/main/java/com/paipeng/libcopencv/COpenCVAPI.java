package com.paipeng.libcopencv;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

public class COpenCVAPI {
    public static Logger logger = LoggerFactory.getLogger(COpenCVAPI.class);
    private static final String LIB_NAME = "copencv";


    private static final String LIB_PATH = "./libs";
    //private static final String LIB_PATH = "/Users/paipeng/Library/Developer/Xcode/DerivedData/copencv-dxgxqducrsqrjefrdakaurpmzmqc/Build/Products/Debug";
    private static COpenCVAPI INSTANCE;

    public static COpenCVAPI getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new COpenCVAPI();
        }

        return INSTANCE;
    }

    private static void setupNativeLibrary() {
        logger.trace("setupNativeLibrary");
        NativeLibrary.addSearchPath(LIB_NAME, LIB_PATH);
    }

    static {
        setupNativeLibrary();
    }

    //Load DLL Library
    public interface OpenCVLib extends Library {
        OpenCVLib INSTANCE = Native.load(LIB_NAME, OpenCVLib.class);

        int add(int a, int b);
        int invert_image(byte[] data, int width, int height, int image_format);

        int detect_square(byte[] data, int width, int height, int image_format, int[] detected_rect);
    }

    public int add(int a, int b) {
        return OpenCVLib.INSTANCE.add(a, b);
    }

    public BufferedImage invertImage(BufferedImage bufferedImage) {
        byte[] data = ImageUtil.convertBufferedImageToBytes(bufferedImage);
        int ret = OpenCVLib.INSTANCE.invert_image(data, bufferedImage.getWidth(), bufferedImage.getHeight(), 3);


        logger.trace("invert_image ret: " + ret);

        return ImageUtil.convert(data, bufferedImage.getWidth(), bufferedImage.getHeight());
    }

    public int[] detectSquare(BufferedImage bufferedImage) {
        logger.trace("detectSquare");
        int[] detected_rect = new int[4];
        detected_rect[0] = 0;
        detected_rect[1] = 0;
        detected_rect[2] = 0;
        detected_rect[3] = 0;

        byte[] data = ImageUtil.convertBufferedImageToBytes(bufferedImage);
        logger.trace("detectSquare ... ");
        int ret = OpenCVLib.INSTANCE.detect_square(data, bufferedImage.getWidth(), bufferedImage.getHeight(), 3, detected_rect);
        logger.trace("detectSquare ret: " + ret);
        logger.trace(String.format("detected_rect: %d-%d %d-%d", detected_rect[0], detected_rect[1], detected_rect[2], detected_rect[3]));
        if (ret == 0) {
            return detected_rect;
        } else {
            return null;
        }
    }
}
