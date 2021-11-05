//
// Created by Pai Peng on 2021/11/5.
//

#include "copencv_jni.h"
#include <jni.h>
#include <string>

#include "copencv.hpp"

extern "C" JNIEXPORT jstring JNICALL
Java_com_paipeng_copencv_COpenCVApi_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_paipeng_copencv_COpenCVApi_getOpenCVVersion(JNIEnv *env, jobject thiz) {
    std::string hello = get_opencv_version();
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_paipeng_copencv_COpenCVApi_getOpenCVBuildInfo(JNIEnv *env, jobject thiz) {
    std::string hello = get_opencv_build_info();
    return env->NewStringUTF(hello.c_str());
}