package com.paipeng.copencv;


public class COpenCVApi {
    static {
        try {
            System.loadLibrary("copencv");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public COpenCVApi() {

    }

    public native String stringFromJNI();
    public native String getOpenCVVersion();
    public native String getOpenCVBuildInfo();
}