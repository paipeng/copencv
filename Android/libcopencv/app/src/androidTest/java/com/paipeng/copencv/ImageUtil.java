package com.paipeng.copencv;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import androidx.test.InstrumentationRegistry;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ImageUtil {


    public static Bitmap readBitmapByResourceId(int resourceId) {
        Context appContext = InstrumentationRegistry.getContext();
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeResource(appContext.getResources(), resourceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static byte[] readFromFile(String filename) throws IOException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Resources res = appContext.getResources();
        AssetManager assetManager = res.getAssets();
        //assetManager.list("");
        InputStream inputStream = assetManager.open(filename);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();

    }

    public static byte[] bitmapToGrayByteArray(Bitmap bm) {
        // Create the buffer with the correct size
        int iBytes = bm.getWidth() * bm.getHeight();
        byte[] res = new byte[iBytes];
        Bitmap.Config format = bm.getConfig();
        if (format == Bitmap.Config.ARGB_8888) {
            ByteBuffer buffer = ByteBuffer.allocate(iBytes * 4);
            // Log.e("DBG", buffer.remaining()+""); -- Returns a correct number based on dimensions
            // Copy to buffer and then into byte array
            bm.copyPixelsToBuffer(buffer);
            byte[] arr = buffer.array();
            for (int i = 0; i < iBytes; i++) {
                int A, R, G, B;
                R = (int) (arr[i * 4]) & 0xff;
                G = (int) (arr[i * 4 + 1]) & 0xff;
                B = (int) (arr[i * 4 + 2]) & 0xff;
                //A=arr[i*4+3];
                byte r = (byte) ((R + G + B) / 3);
                res[i] = r;
            }
        } else if (format == Bitmap.Config.RGB_565) {
            ByteBuffer buffer = ByteBuffer.allocate(iBytes * 2);
            // Log.e("DBG", buffer.remaining()+""); -- Returns a correct number based on dimensions
            // Copy to buffer and then into byte array
            bm.copyPixelsToBuffer(buffer);
            byte[] arr = buffer.array();
            for (int i = 0; i < iBytes; i++) {
                float A, R, G, B;
                R = ((arr[i * 2] & 0xF8));
                G = ((arr[i * 2] & 0x7) << 5) + ((arr[i * 2 + 1] & 0xE0) >> 5);
                B = ((arr[i * 2 + 1] & 0x1F) << 3);
                byte r = (byte) ((R + G + B) / 3);
                res[i] = r;
            }
        }
        // Log.e("DBG", buffer.remaining()+""); -- Returns 0
        return res;
    }

    public static Bitmap convertDataToBitmap(byte[] data, int imageWidth, int imageHeight) {
        try {
            //Create bitmap with width, height, and 4 bytes color (RGBA)
            Bitmap bmp = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888);
            final int pixCount = imageWidth * imageHeight;
            int[] intGreyBuffer = new int[pixCount];
            for (int i = 0; i < pixCount - 1; i++) {

                int greyValue = (int) data[i] & 0xff;
                intGreyBuffer[i] = 0xff000000 | (greyValue << 16) | (greyValue << 8) | greyValue;
            }

            bmp.setPixels(intGreyBuffer, 0, imageWidth, 0, 0, imageWidth, imageHeight);
            return bmp;
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
            return null;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }



    public static Bitmap getBitmapFromAsset(String filePath) {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Resources res = appContext.getResources();
        AssetManager assetManager = res.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }



    public static String saveImage(Bitmap bmp) {
        if (bmp == null) {
            return null;
        }
        File appDir = new File(Environment.getExternalStorageDirectory(), "OpenCV");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Environment.getExternalStorageDirectory() + "/OpenCV" + "/" + fileName;
    }
}
