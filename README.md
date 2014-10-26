#Overview

This is a very friendly, easy-to-use GUI program for taking a set of images and/or text, resizing them to have uniform dimensions, and placing them on a sheet of paper in such a way that they can be readily printed, laminated and cut out (I often find myself skipping the cutting phase and just using the paper for slapjack-style games in the classroom, however). Your work-in-progress can be saved to a file (in addition to being outputted to a PNG), so that if you are in the process of making an elaborate sheet, you don't have to worry about accidentally closing the program and needing to repeat your work.

#Compiling and Running the Code

Assuming that you have the Gradle build tool installed, just run `gradle build` from the command line inside the main project directory! You'll get a runnable jar inside build/distributions. You can also run `gradle jfxDeploy` if you'd like to build native packages to run on a machine without Java installed. They'll be built inside build/distributions/bundles.