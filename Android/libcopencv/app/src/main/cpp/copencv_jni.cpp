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

extern "C"
JNIEXPORT jint JNICALL
Java_com_paipeng_copencv_COpenCVApi_invertImage(JNIEnv *env, jobject thiz, jbyteArray data,
                                                jint width, jint height, jint image_format) {
    jbyte *data_ = env->GetByteArrayElements(data, NULL);

    int ret = invert_image(reinterpret_cast<char *>(data_), width, height, image_format);

    env->ReleaseByteArrayElements(data, data_, 0);
    return ret;
}