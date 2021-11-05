package com.paipeng.copencv;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.ActivityCompat;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class COpenCVApiTest extends TestCase {
    private COpenCVApi cOpenCVApi;

    @Before
    public void setup() {
        MyPermissionRequester.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
        int grantResult = ActivityCompat.checkSelfPermission(InstrumentationRegistry.getTargetContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Assert.assertEquals(PackageManager.PERMISSION_GRANTED, grantResult);
        grantResult = ActivityCompat.checkSelfPermission(InstrumentationRegistry.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Assert.assertEquals(PackageManager.PERMISSION_GRANTED, grantResult);

        cOpenCVApi = new COpenCVApi();
    }

    @Test
    public void testStringFromJNI() {
        String o = cOpenCVApi.stringFromJNI();
        System.out.println("StringFromJNI: " + o);
        assertEquals(o, "Hello from C++");
    }

    @Test
    public void testGetOpenCVVersion() {
        String openCVVersion = cOpenCVApi.getOpenCVVersion();
        System.out.println("StringFromJNI: " + openCVVersion);
        assertEquals(openCVVersion, "4.1.0");
    }

    @Test
    public void testGetOpenCVBuildInfo() {
        String openCVBuildInfo = cOpenCVApi.getOpenCVBuildInfo();
        System.out.println("StringFromJNI: " + openCVBuildInfo);
        Assert.assertTrue(openCVBuildInfo.contains("Version control:               4.1.0"));
    }


    @Test
    public void testInvertImage() {
        //Bitmap bitmap = readBitmapByResourceId(R.drawable.bright);
        Bitmap bitmap = ImageUtil.getBitmapFromAsset("images/ds_image_90.jpg");
        Assert.assertNotNull(bitmap);


        byte[] bytes = ImageUtil.bitmapToGrayByteArray(bitmap);
        Assert.assertNotNull(bytes);
        int ret = cOpenCVApi.invertImage(bytes, bitmap.getWidth(), bitmap.getHeight(), 3);
        Assert.assertEquals(0, ret);

        Bitmap invertedBitmap = ImageUtil.convertDataToBitmap(bytes, bitmap.getWidth(), bitmap.getHeight());
        Assert.assertNotNull(invertedBitmap);
        String fileName = ImageUtil.saveImage(invertedBitmap);
        Assert.assertTrue(fileName.contains("/storage/emulated/0/OpenCV"));
    }
}