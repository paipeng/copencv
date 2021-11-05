# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)
add_definitions(-DTARGET_OS_ANDROID=1)
# Declares and names the project.

project("copencv")


# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( lib_opencv SHARED IMPORTED )
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libopencv_java4.so)
set(OPENCV_LINK_DIR "")
link_directories(${OPENCV_LINK_DIR})

include_directories(
        /usr/local/flac
        include
        src
        ./
        ../../../../../../copencv_src/
)

file(GLOB_RECURSE LIB_COPENCV_JNI
        "native-lib.cpp"
        "copencv_jni.cpp"
        )

file(GLOB_RECURSE LIB_COPENCV
        "src/cpp"
        "src/cpp/s2i_opencv_src"

        "../../../../../../copencv_src/copencv.cpp"
        )

add_library( # Sets the name of the library.
             copencv

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
                ${LIB_COPENCV_JNI}
                ${LIB_COPENCV}
              )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       copencv
                        # OpenCV shared lib
                        lib_opencv
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )