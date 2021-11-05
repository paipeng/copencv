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

int check_bright(cv::Mat &mat, float percent) {
    if (percent > 1 || percent < 0) {
        percent = 1;
    }
    int width = (int) (mat.cols * percent);
    int height = (int) (mat.rows * percent);
    int startX = (mat.cols - width) / 2;
    int startY = (mat.rows - height) / 2;
    cv::Rect rect = cv::Rect(startX, startY, width, height);
    
    cv::Scalar mean = cv::mean(mat(rect));
    return mean.val[0];
}


bool isCntValid(vector<cv::Point> cnt, int rows, int cols) {
    int dis = 5;
    for (cv::Point point : cnt) {
        if (point.x < dis || point.y < dis || point.x > cols - dis || point.y > rows - dis) {
            return false;
        }
    }
    return true;
}

bool isValidWidth(int squareWidth, int picWidth, int squareHeight, int picHeight) {
    return squareWidth > picWidth / 4 && squareHeight > picHeight / 6;
}


double angleOfPoint(cv::Point pt1, cv::Point pt2, cv::Point pt0) {
    double dx1 = pt1.x - pt0.x;
    double dy1 = pt1.y - pt0.y;
    double dx2 = pt2.x - pt0.x;
    double dy2 = pt2.y - pt0.y;
    return (dx1*dx2 + dy1*dy2)/sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
}

void drawSquares(cv::Mat &image, const vector<vector<cv::Point>> &squares) {
    for (int i = 0; i < squares.size(); i++) {
        const cv::Point* p = &squares[i][0];
        int n = (int)squares[i].size();
        cv::polylines(image, &p, &n, 1, true, cv::Scalar(0,255,255), 1, cv::LINE_AA);
    }
}


void drawSquare(cv::Mat& image, const vector<cv::Point>& square) {
    const cv::Point* p = &square[0];
    int n = (int)square.size();
    polylines(image, &p, &n, 1, true, cv::Scalar(0,255,255), 10, cv::LINE_AA);
}


vector<vector<cv::Point> > traverse_points_find_square(cv::Mat gray, float cosin) {
    vector<vector<cv::Point> > squares;
    vector<vector<cv::Point> > contours;
    vector<cv::Point> approx;
    double maxCosine = 0;

    cv::findContours(gray, contours, cv::RETR_LIST, cv::CHAIN_APPROX_SIMPLE);
    
    
    for (vector<cv::Point> points : contours) {
        approxPolyDP(points, approx, arcLength(points, true) * 0.1, true);
        //printf("found countour: %ld\n", approx.size());
        if (approx.size() == 4
            && fabs(contourArea(approx)) > 1000
            && isContourConvex(approx)
            // && isCntValid(approx, gray.rows, gray.cols)
            //&& isValidWidth(abs(approx[2].x - approx[0].x), gray.cols, abs(approx[2].y - approx[0].y), gray.rows)
            ) {
            printf("found  rectangle countour size: %f\n", fabs(contourArea(approx)));
            drawSquare(gray, approx);

            for (int j = 2; j < 5; j++) {
                double angle = angleOfPoint(approx[j%4], approx[j-2], approx[j-1]);
                double cosine = fabs(angle);
                maxCosine = MAX(maxCosine, cosine);
            }
            if (maxCosine < cosin) {
                // found square !
                squares.push_back(approx);
            } else {
                squares.push_back(approx);
            }
        }
    }

    return squares;
}

int detect_square(const cv::Mat mat, vector<vector<cv::Point> >& squares, int brightness, const int gauss_filter_size = 17, float cosin = 0.3) {
    cv::Mat destination, gray;
    cv::Mat element2 = cv::getStructuringElement(cv::MORPH_RECT, cv::Size(3, 3));
    cv::GaussianBlur(mat, destination, cv::Size(gauss_filter_size, gauss_filter_size), cv::BORDER_CONSTANT);
    
    cv::threshold(destination, gray, brightness*1.2, 255, cv::THRESH_BINARY);
    
    vector<vector<cv::Point> > ret = traverse_points_find_square(gray, cosin);
    for (vector<cv::Point> vp: ret) {
        squares.push_back(vp);
    }
    return 0;
}


int calculte_bright_diff2(const cv::Mat mat, const int block_col, const int block_row, int* detect_block_values, int offset) {
    int max_mean = 0;
    int min_mean = 0;
    int c = mat.cols/block_col;
    int r = mat.rows/block_row;
    int count = 0;
    int sum = 0;
    //char file[512];
    //char text[16];
    int bright = 0;
    cv::Mat colorMat;
    cv::cvtColor(mat, colorMat, cv::COLOR_GRAY2BGR);
    for (int i = offset; i < block_row -offset ; i++) {
        for (int j = offset; j < block_col -offset; j++) {
            cv::Rect rect = cv::Rect(j*c, i*r, c, r);
            
            cv::Scalar mean = cv::mean(mat(rect));
            bright = mean.val[0];
            
            if (detect_block_values != NULL) {
                detect_block_values[i*block_col+j] = bright;
            }
            sum += bright;
            if (bright > 220) {
                count = count + 1;
            }
            if (max_mean == 0 || max_mean < bright) {
                max_mean = bright;
            }
            if (min_mean == 0 || min_mean > bright) {
                min_mean = bright;
            }
        }
    }
    
    sum = sum/(block_col-offset*2)/(block_row-offset*2);
    if (detect_block_values != NULL) {
        for (int i = offset; i < block_row -offset ; i++) {
            for (int j = offset; j < block_col -offset; j++) {
                detect_block_values[i*block_col+j] -= sum;
            }
        }
    }
    
    if (count > 5) {
        return 1001;
    }

    return max_mean - min_mean;
}

int find_squares(cv::Mat mat, vector<cv::Point>& maxAreaCnt, int brightness, const int gauss_filter_size = 17) {
    vector<vector<cv::Point> > squares;
    int ret = detect_square(mat, squares, brightness, gauss_filter_size, 0.3);
    if (ret == 0 && squares.size() > 0) {
        double maxArea = 0.0;
        for (vector<cv::Point> points : squares) {
            cv::Rect r = boundingRect(points);
            cv::Mat m = mat(r);
            // diff
            int d = calculte_bright_diff2(m, 6, 6, NULL, 0);
            if (maxArea == 0) {
                maxAreaCnt = points;
                maxArea = d;
            } else {
                if (maxArea > d) {
                    maxAreaCnt = points;
                    maxArea = d;
                }
            }
        }

        return 0;
    } else {
        return -1;
    }
}

cv::Rect safe_rect(int width, int height, cv::Rect rect) {
    cv::Rect safe_rect = rect;
    if (safe_rect.x < 0) {
        safe_rect.x = 0;
    }
    if (safe_rect.y < 0) {
        safe_rect.y = 0;
    }
    if (safe_rect.x + safe_rect.width > width) {
        safe_rect.width = width - safe_rect.x;
    }
    if (safe_rect.y + safe_rect.height > height) {
        safe_rect.height = height - safe_rect.y;
    }
    return safe_rect;
}

int detect_square(char* data, int width, int height, int image_format, int* detected_rect) {
    cv::Mat mat(height, width, CV_8U, data);
    cv::Mat resizeMat;
    vector<cv::Point> result;
    int gauss_filter_size = 5;
    
    int brightness = check_bright(mat, 0.2);
    printf("brightness: %d\n", brightness);
    
    
    // resize -> small
    float resize_factor = 0.25;
    
    cv::resize(mat, resizeMat, cv::Size(mat.cols*resize_factor, mat.rows*resize_factor), 0, 0, cv::INTER_NEAREST);
    
    int ret  = find_squares(resizeMat, result, brightness, gauss_filter_size);
    
    printf("find_squares: %d (result size: %ld)\n", ret, result.size());
    if (ret == 0 && result.size() == 4) {
        cv::Rect boundRect = minAreaRect(result).boundingRect();

        cv::Rect rect = safe_rect(mat.cols, mat.rows, boundRect);

        detected_rect[0] = rect.tl().x/resize_factor;
        detected_rect[1] = rect.tl().y/resize_factor;
        detected_rect[2] = rect.size().width/resize_factor;
        detected_rect[3] = rect.size().height/resize_factor;
        return 0;
    } else {
        return -1;
    }
}
