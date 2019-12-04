// License: Apache 2.0. See LICENSE file in root directory.
// Copyright(c) 2017 Intel Corporation. All Rights Reserved.

#include <librealsense2/rs.hpp> // Include RealSense Cross Platform API
#include <opencv2/opencv.hpp>   // Include OpenCV API

int main(int argc, char * argv[]) try
{
    // Declare RealSense pipeline, encapsulating the actual device and sensors
    rs2::pipeline pipe;
    // Start streaming with default recommended configuration
    pipe.start();

    using namespace cv;
    
    rs2::frameset data = pipe.wait_for_frames(); // Wait for next set of frames from the camera
    for (auto i = 0; i < 30; ++i) data = pipe.wait_for_frames();
    rs2::frame color = data.get_color_frame();
    rs2::frame depth = data.get_depth_frame();

    // Query frame size (width and height)
    const int W = color.as<rs2::video_frame>().get_width();
    const int H = color.as<rs2::video_frame>().get_height();
    const int w = depth.as<rs2::video_frame>().get_width();
    const int h = depth.as<rs2::video_frame>().get_height();

    // Create OpenCV matrix of size (w,h) from the colorized depth data
    Mat color_image(Size(W, H), CV_8UC3, (void*)color.get_data(), Mat::AUTO_STEP);
    Mat depth_image(Size(w, h), CV_16UC1, (void*)depth.get_data(), Mat::AUTO_STEP);
    cvtColor(color_image, color_image, cv::COLOR_BGR2RGB);

    imwrite("output-Color.png", color_image);
    imwrite("output-Depth.png", depth_image);
    
    //std::cout << "Save done.\n";
    short * p = (short*)depth.get_data();
    short Val = *(p+(h/2)*w+(w/2));
    std::cout << Val;//depth.get_data();
    std::cout << "\n";
    return EXIT_SUCCESS;
}
catch (const rs2::error & e)
{
    std::cerr << "RealSense error calling " << e.get_failed_function() << "(" << e.get_failed_args() << "):\n    " << e.what() << std::endl;
    return EXIT_FAILURE;
}
catch (const std::exception& e)
{
    std::cerr << e.what() << std::endl;
    return EXIT_FAILURE;
}



