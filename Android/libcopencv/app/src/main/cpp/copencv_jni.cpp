//
// Created by Pai Peng on 2021/11/5.
//

#include "copencv_jni.h"
#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_paipeng_copencv_COpenCVApi_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}