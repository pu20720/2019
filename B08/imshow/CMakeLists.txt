cmake_minimum_required (VERSION 3.1)

project(RealSenseImShowExample)

set(CMAKE_EXPORT_COMPILE_COMMANDS 1)
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_FLAGS "-fpermissive")
set(CMAKE_CXX_COMPILER /usr/bin/g++)
set(CMAKE_BUILD_TYPE "Release")

# put FindGLFW.cmake in the source directory of CMakeLists.txt
find_package(OpenCV REQUIRED)

include_directories(${OpenCV_INCLUDE_DIRS} /usr/include/eigen3)
include_directories(include/peac)
include_directories(include/MRF2.2)

link_directories(include/MRF2.2)

file(GLOB sources "*.cpp")

add_executable(rs-imshow ${sources})

target_link_libraries(rs-imshow ${OpenCV_LIBS} libMRF.a ${DEPENDENCIES})

set_target_properties (rs-imshow PROPERTIES
	FOLDER "Examples/OpenCV"
)

install(
	TARGETS

	rs-imshow

	RUNTIME DESTINATION
	${CMAKE_INSTALL_PREFIX}/bin
)
