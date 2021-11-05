package com.paipeng.copencv;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class COpenCVApiTest extends TestCase {
    private COpenCVApi cOpenCVApi;

    @Before
    public void setup() {
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
}