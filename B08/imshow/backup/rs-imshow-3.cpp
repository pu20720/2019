// License: Apache 2.0. See LICENSE file in root directory.
// Copyright(c) 2017 Intel Corporation. All Rights Reserved.

#include <librealsense2/rs.hpp> // Include RealSense Cross Platform API
#include <opencv2/opencv.hpp>   // Include OpenCV API

#include "plane_detection.h"

PlaneDetection plane_detection;

//-----------------------------------------------------------------
// MRF energy functions
MRF::CostVal dCost(int pix, int label)
{
        return plane_detection.dCost(pix, label);
}

MRF::CostVal fnCost(int pix1, int pix2, int i, int j)
{
        return plane_detection.fnCost(pix1, pix2, i, j);
}

void runMRFOptimization()
{
        DataCost *data = new DataCost(dCost);
        SmoothnessCost *smooth = new SmoothnessCost(fnCost);
        EnergyFunction *energy = new EnergyFunction(data, smooth);
        int width = kDepthWidth, height = kDepthHeight;
        MRF* mrf = new Expansion(width * height, plane_detection.plane_num_ + 1, energy);
        // Set neighbors for the graph
        for (int row = 0; row < height; row++)
        {
                for (int col = 0; col < width; col++)
                {
                        int pix = row * width + col;
                        if (col < width - 1) // horizontal neighbor
                                mrf->setNeighbors(pix, pix + 1, 1);
                        if (row < height - 1) // vertical
                                mrf->setNeighbors(pix, pix + width, 1);
                        if (row < height - 1 && col < width - 1) // diagonal
                                mrf->setNeighbors(pix, pix + width + 1, 1);
                }
        }
        mrf->initialize();
        mrf->clearAnswer();
        float t;
        mrf->optimize(5, t);  // run for 5 iterations, store time t it took
        MRF::EnergyVal E_smooth = mrf->smoothnessEnergy();
        MRF::EnergyVal E_data = mrf->dataEnergy();
        cout << "Optimized Energy: smooth = " << E_smooth << ", data = " << E_data << endl;
        cout << "Time consumed in MRF: " << t << endl;

        // Get MRF result
        for (int row = 0; row < height; row++)
        {
                for (int col = 0; col < width; col++)
                {
                        int pix = row * width + col;
                        plane_detection.opt_seg_img_.at<cv::Vec3b>(row, col) = plane_detection.plane_colors_[mrf->getLabel(pix)];
                        plane_detection.opt_membership_img_.at<int>(row, col) = mrf->getLabel(pix);
                }
        }
        delete mrf;
        delete energy;
        delete smooth;
        delete data;
}
//-----------------------------------------------------------------


void printUsage()
{
        cout << "Usage: RGBDPlaneDetection <-o> color_image depth_image output_folder" << endl;
        cout << "-o: run MRF-optimization based plane refinement" << endl;
}

int main(int argc, char * argv[]) try
{
    // Declare RealSense pipeline, encapsulating the actual device and sensors
    rs2::pipeline pipe;
    rs2::config pipe_config;
    pipe_config.enable_stream(RS2_STREAM_DEPTH, 640, 480, RS2_FORMAT_Z16, 30);
    pipe_config.enable_stream(RS2_STREAM_COLOR , 1920, 1080, RS2_FORMAT_BGR8, 30);
    // Start streaming with default recommended configuration
    //pipe.start();
    rs2::pipeline_profile profile = pipe.start(pipe_config);
    auto depth_stream = profile.get_stream(RS2_STREAM_DEPTH).as<rs2::video_stream_profile>();

    using namespace cv;
    /*const auto window1_name = "PlaneDetection";
    namedWindow(window1_name, WINDOW_AUTOSIZE);
    const auto window2_name = "ColorImage";
    namedWindow(window2_name, WINDOW_AUTOSIZE);*/

    //for (auto i = 0; i < 30; ++i) {}
    //while (waitKey(1) < 0 && getWindowProperty(window1_name, WND_PROP_AUTOSIZE) >= 0)
    //{
    
        rs2::frameset data = pipe.wait_for_frames(); // Wait for next set of frames from the camera
        for (auto i = 0; i < 30; ++i) data = pipe.wait_for_frames();
        rs2::frame color = data.get_color_frame();
        rs2::frame depth = data.get_depth_frame();

        // Query frame size (width and height)
        //const int W = color.as<rs2::video_frame>().get_width();
        //const int H = color.as<rs2::video_frame>().get_height();
        //const int w = depth.as<rs2::video_frame>().get_width();
        //const int h = depth.as<rs2::video_frame>().get_height();

        // Create OpenCV matrix of size (w,h) from the colorized depth data
        Mat color_image(Size(1920, 1080), CV_8UC3, (void*)color.get_data(), Mat::AUTO_STEP);
        Mat depth_image(Size(640, 480), CV_16UC1, (void*)depth.get_data(), Mat::AUTO_STEP);
        //cvtColor(color_image, color_image, cv::COLOR_BGR2RGB);

        imwrite("output-Color.png", color_image);
        imwrite("output-Depth.png", depth_image);

        std::cout << "Save done.\n";

        //RGBDPlaneDetection
        /*if (argc != 4 && argc != 5)
        {
                printUsage();
                return -1;
        }*/
        //bool run_mrf = false; //string(argv[1]) == "-o" ? true : false;
        //string color_filename = "output-Color.png"; //run_mrf ? string(argv[2]) : string(argv[1]);
        //string depth_filename = "output-Depth.png"; //run_mrf ? string(argv[3]) : string(argv[2]);
        //string output_folder = run_mrf ? string(argv[4]) : string(argv[3]);
        //string output_folder = "/home/demo/RealtimeRGBD/output";

        /*plane_detection.readColorImage(color_image); //(color_filename);
        plane_detection.readDepthImage(depth_image); //(depth_filename);
        plane_detection.runPlaneDetection();*/

        /*if (run_mrf)
        {
                plane_detection.prepareForMRF();
                runMRFOptimization();
        }*/
        //int pos = color_filename.find_last_of("/\\");
        //string frame_name = color_filename.substr(pos + 1);
        //frame_name = frame_name.substr(0, frame_name.length() - 10);
        /*cv::Mat output_image = plane_detection.writeOutputFiles(output_folder, "test", run_mrf);
        imshow(window1_name, output_image);
        imshow(window2_name, color_image);
        std::cout << "Done.\n";*/
    //}
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

