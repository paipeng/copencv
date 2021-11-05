package com.paipeng.libcopencv;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ImageUtil {

    public static byte[] convertBufferedImageToBytes(BufferedImage bufferedImage) {
        byte[] imageBytes = null;
        if (bufferedImage == null) {
            return imageBytes;
        } else {
            byte[] imageTmp = null;
            int x;
            int i;
            if (bufferedImage.getType() == 12) {
                imageTmp = new byte[bufferedImage.getHeight() * bufferedImage.getWidth()];

                for(x = 0; x < bufferedImage.getHeight(); ++x) {
                    for(i = 0; i < bufferedImage.getWidth(); ++i) {
                        imageTmp[x * bufferedImage.getWidth() + i] = (byte)bufferedImage.getRGB(i, x);
                    }
                }
            } else if (bufferedImage.getType() != 5 && bufferedImage.getType() != 0 && bufferedImage.getType() != 1 && bufferedImage.getType() != 2) {
                if (bufferedImage.getType() == 10) {
                    imageTmp = new byte[bufferedImage.getHeight() * bufferedImage.getWidth()];

                    for(x = 0; x < bufferedImage.getWidth(); ++x) {
                        for(i = 0; i < bufferedImage.getHeight(); ++i) {
                            imageTmp[i * bufferedImage.getWidth() + x] = (byte)bufferedImage.getRaster().getSample(x, i, 0);
                        }
                    }

                    return imageTmp;
                }
            } else {
                imageTmp = new byte[bufferedImage.getHeight() * bufferedImage.getWidth()];

                for(x = 0; x < bufferedImage.getWidth(); ++x) {
                    for(i = 0; i < bufferedImage.getHeight(); ++i) {
                        int rgb = bufferedImage.getRGB(x, i);
                        int r = rgb >> 16 & 255;
                        int g = rgb >> 8 & 255;
                        int b = rgb & 255;
                        int grayLevel = (r + g + b) / 3;
                        imageTmp[i * bufferedImage.getWidth() + x] = (byte)grayLevel;
                    }
                }
            }

            if (imageTmp != null && imageTmp.length > 0) {
                x = imageTmp.length / (bufferedImage.getType() == 12 ? 2 : 1);
                imageBytes = new byte[x];
                if (bufferedImage.getType() == 12) {
                    for(i = 0; i < x; i += 2) {
                        imageBytes[i / 2] = imageTmp[i];
                    }
                } else {
                    for(i = 0; i < x; ++i) {
                        imageBytes[i] = imageTmp[i];
                    }
                }
            }

            return imageBytes;
        }
    }

    public static BufferedImage convert(byte[] data, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, 10);
        img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(data, width * height), new Point()));
        return img;
    }

    public static BufferedImage readBufferedImage(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedImage bufferedImage = ImageIO.read(file);
        return bufferedImage;
    }

    public static boolean saveBufferedImage(BufferedImage bufferedImage, String fileName) throws IOException {
        return ImageIO.write(bufferedImage, "bmp", new File(fileName));
    }


    public static BufferedImage cropImage(BufferedImage bufferedImage, int cropX, int cropY, int cropWidth, int cropHeight) {
        BufferedImage bi = bufferedImage.getSubimage(cropX, cropY, cropWidth, cropHeight);
        return bi;
    }


    public static BufferedImage drawRect(BufferedImage bufferedImage, int x, int y, int width, int height) {
        Graphics2D graph = bufferedImage.createGraphics();
        graph.setColor(Color.BLACK);
        graph.drawRect(x, y, width, height);
        graph.dispose();
        return bufferedImage;
    }
}
