# ImageProcessing
This task accepts input image, process it and write the change into another image (output image).
The new image is processed such that it has mean value of surrounding pixel values.
This is done in Serial and Parallel.

# Processing task
Each of the two version of a program (Serial and Parallel) accepts 3 inputs <path/to/input/image> <output image name> <window size>
Window size represent the size of the square to which the average value of surrounding pixels will be taken
e.g if window size is 3 the square is 3*3 meaning the middle pixel at (1,1) will now have a pixel value equal to the average value of all the pixels in that window (square) including itself.
Pixels at the edge of the image will be blanked since they do not have surrounding pixels.
Window size has to be an odd number.

# Serial version
This version loops through the (x,y) of the whole image and then loops through (x,y) of the window and then it targets the pixels in the middle, calculate mean value of the pixels in the window, then replace pixel value of the middle pixel with the mean value.

# Parallel
This version has additional varaibles which help divide the task so that the preocessing can be parallel. This value are:
 -minumum width (widthlo) - represents the minimum width of the image that can be processed
 -maximum width (widthhi) - represents the maximum width of the image that can be processed
 -minumum height (heightlo) - represents the minimum width of the image that can be processed
 -maximum height (heighthi) - represents the maximum width of the image that can be processed
 Threshold value- this value limits the width and height  that can be processed , if maximum width-minimum width is less threshold then it can be processed otherwise task is divided
 same goes for height

 # Output
 Serial - The program outputs the new image with average (mean) pixels and time it took to process 
 Parallel - Output new image with average pixels, number of processors and time it took to process

 # Verification
The output image should have blank edges since they don't have surrouding pixels and also image should be more blur as compared to the input image
Higher window size will make image have more blank pixels
In most cases the Parallel version whould be quicker than the Serial version


# Invokation
1. Compile both version by <make>
2. <make run-serial> for running serial version and <make run-parallel> for runing parallel version
3. Input image path, output image and its extension as 'jpg' and window size as a number
