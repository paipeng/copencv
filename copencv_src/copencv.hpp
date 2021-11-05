//
//  copencv.hpp
//  copencv
//
//  Created by Pai Peng on 2021/11/4.
//

#ifndef copencv_hpp
#define copencv_hpp

#ifdef __cplusplus
#undef NO
#undef YES
#include <opencv2/opencv.hpp>
#endif
#include <stdio.h>


using namespace std;

#if defined(c_plusplus) || defined(__cplusplus)
extern "C" {
#endif

int add(int a, int b);
int invert_image(char* data, int width, int height, int image_format);
int detect_square(char* data, int width, int height, int image_format, int* square);

#if defined(c_plusplus) || defined(__cplusplus)
}
#endif

#endif /* copencv_hpp */
