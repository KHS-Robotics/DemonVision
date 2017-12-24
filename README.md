# DemonVision
Vision library to process targets using OpenCV

## Vision Library Using OpenCV 3.1

This is our vision library to handle capturing, processing, and sending outputs of the processed images. DemonVision is supported on the following platforms:

* Windows
* Raspberry Pi running Raspbian
* Generic Armhf devices (such as the BeagleBone Black or the Jetson)

## Working on this project in Eclipse
Enter the root of the project in terminal. You will need to run the gradle permissions script by running `sudo sh setGradlePermissions`. Once finished, run `gradle eclipse`. Finally, you are ready to import the project into Eclipse. Open Eclipse and from the menu, select File > Import > Gradle > Gradle Project > Next > Browse and Enter Project Location > Finish.

## Choosing which system to build for
As there is no way to autodetect which system you want to build for, such as building for a Raspberry Pi on a windows desktop, you have to manually select which system you want to build for.
To do this, open the `build.gradle` file. Near the top at line 10 starts a group of comments explaining what to do. For a basic rundown, there are 3 lines that start with `ext.buildType =`. 
To select a device, just uncomment the system you want to build for. 

Note it is possible to easily switch which system you want to target. To do so, just switch which build type is uncommented. When you do this, you will have to run a clean `gradlew clean` in order to
clear out any old artifacts. 

## Building and running on the local device
If you are running the build for your specific platform on the device you plan on running, you can use `gradlew run` to run the code directly. You can also run `gradlew build` to run a build.
When doing this, the output files will be placed into `output\`. From there, you can run either the .bat file on windows or the shell script on unix in order to run your project.

## Building for another platform
If you are building for another platform, trying to run `gradlew run` will not work, as the OpenCV binaries will not be set up correctly. In that case, when you run `gradlew build`, a zip file
is placed in `output\`. This zip contains the built jar, the OpenCV library for your selected platform, and either a .bat file or shell script to run everything. All you have to do is copy
this file to the system, extract it, then run the .bat or shell script to run your program

## Other configuration options
The build script provides a few other configuration options. These include selecting the main class name, and providing an output name for the project.
Please see the `build.gradle` file for where to change these. 