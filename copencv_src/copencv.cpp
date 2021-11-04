//
//  copencv.cpp
//  copencv
//
//  Created by Pai Peng on 2021/11/4.
//

#include "copencv.hpp"
#ifdef __cplusplus
#include <opencv2/opencv.hpp>
#include <opencv2/objdetect.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/imgcodecs/legacy/constants_c.h>
#include <stdlib.h>


#if DEBUG
#if TARGET_OS_IPHONE
#include <TargetConditionals.h>
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#include <opencv2/imgcodecs/ios.h>
#elif TARGET_OS_MAC
//#import "NSImage+Mat.hpp"
//#import "NSImage+CMatrix.h"
#endif
#endif

#endif


int add(int a, int b) {
    return a + b;
}

int invert_image(char* data, int width, int height, int image_format) {
    cv::Mat src(height, width, CV_8U, data);
    cv::Mat dst;
    
    cv::bitwise_not(src, dst);
    
    
    memcpy(data, dst.ptr(0), sizeof(unsigned char)* width * height * 1);
    return 0;
}
