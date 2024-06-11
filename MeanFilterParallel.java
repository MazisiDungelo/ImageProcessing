import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import javax.imageio.ImageIO;
import java.util.Scanner;


public class MeanFilterParallel extends RecursiveAction{
/*
 * This class creates image such that resulting image pixels
 * are mean/average of the surrounding pixels within a specified window
 * done in Parallel using Fork/Join framework
 */
    private BufferedImage inputImg; // 
    private BufferedImage dstImg;
    private int window; 
    private int widthlo = 0;
    private int widthhi = 0;
    private int heightlo =0;
    private int heighthi = 0;

    public MeanFilterParallel(BufferedImage inputImage,BufferedImage dstImage, int window,int low1, int low2,int hi1,int hi2) {
        /*
         * Create instance of a class with specified bounds
         */
         inputImg = inputImage;
         dstImg = dstImage;
         this.window = window;
         widthlo=low1;
         widthhi = hi1;
         heightlo = low2;
         heighthi = hi2;
    }
    public MeanFilterParallel(BufferedImage inputImage,BufferedImage dstImage, int window)
    {
        /*
         * Create instance of class 
         */
         this.window = window;
         inputImg = inputImage;
         dstImg = dstImage;
         widthlo = 0;
         widthhi = inputImg.getWidth();
         heightlo = 0;
         heighthi = inputImg.getHeight();
    }
    // Average pixels from source, write results into destination.
    protected static int sThreshold = 10000;

    @Override
    protected void compute() {
        // Check if the task(image) is small enough to be porcessed
        // by checking of the width and height are below the threshold
        if ((widthhi-widthlo) < sThreshold && (heighthi-heightlo) < sThreshold) {
            // Initialize total and mean of pixels for each color
            int redTotal = 0;int greenTotal = 0;int blueTotal = 0;int alphaTotal = 0;
            int redMean = 0;int greenMean = 0;int blueMean = 0;int alphaMean = 0;
            int counter=0;
            // Calculate the position of target pixel
            int w = (int)Math.floor(window/2);
            // Loop through teh pixels in the whole image
            for (int j=heightlo; j<heighthi-window+1; j++)
            {
               for (int k=widthlo; k<widthhi-window+1; k++)
               {
                 // Loop through the pixels in thw window
                  for (int y=j; y<j+window; y++)
                  {
                      for (int x=k; x<k+window; x ++)
                      { 
                        // Get the pixel value
                        int p = inputImg.getRGB(x,y);
                        // Extract ARGB components
                        int a = (p>>24) & 0xff; // get alpha
                        int r = (p>>16) & 0xff; // get red
                        int g = (p>>8) & 0xff; // get green
                        int b =  p & 0xff; // get blue

                        redTotal += r;greenTotal += g;blueTotal += b;alphaTotal += a; // add these components to total
                        counter++;
                        
                     }
                 
                  }
                  // Caculate average of the pixels in thw window
                  redMean = redTotal/counter;
                  greenMean = greenTotal/counter;
                  blueMean = blueTotal/counter;
                  alphaMean = alphaTotal/counter;
                  // Create a new pixel value
                  int p = (alphaMean<<24) | (redMean<<16) | (greenMean<<8) | blueMean;
                  // Set the mean pixel to the target position (central pixel of the window)
                  dstImg.setRGB(k+w,j+w,p);      
                  // Reset the mean totals and counter
                  counter=0;redMean = 0;greenMean = 0;blueMean = 0;alphaMean = 0;
                  redTotal = 0;greenTotal = 0;blueTotal = 0;alphaTotal = 0;
               }
            }
        }
        else
        {
            // If task(image) it too large then split into smaller taks
            MeanFilterParallel left  = new MeanFilterParallel(inputImg,dstImg, window,widthlo, heightlo, (widthhi+widthlo)/2, (heighthi+heightlo)/2);
            MeanFilterParallel right =  new MeanFilterParallel(inputImg,dstImg, window, ((widthhi+widthlo)/2) , ((heighthi+heightlo)/2), widthhi, heighthi);
            left.fork();
            right.compute();
            left.join();
        }
    }   
       public static void main(String[] args) throws Exception 
       {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter <input image> <output image> <window size> ");
        String srcName = input.next();
        String dstName = input.next();
        int window = input.nextInt();
        input.close();
        // Read an input image
        try 
        {
            File srcFile = new File(srcName);
            BufferedImage image = ImageIO.read(srcFile);
            // Create an output image of the same dimensions
            BufferedImage dstImage =
                new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);   
            // Initilaize mean filter task    
            MeanFilterParallel fb = new MeanFilterParallel(image,dstImage, window);
            // Get number of processors available for parallel task
            int processors = Runtime.getRuntime().availableProcessors();
            System.out.println(Integer.toString(processors) + " processor"
                + (processors != 1 ? "s are " : " is ")
                + "available");
            // Create ForkJoin pool
            ForkJoinPool pool = new ForkJoinPool();
            // Track starting time of the task processing
            long startTime = System.currentTimeMillis();
            pool.invoke(fb);
            fb.join();
            pool.close();
            // Measure end time of processing
            long endTime = System.currentTimeMillis();
            
            System.out.println("Image filter took " + (endTime - startTime) + 
                " milliseconds.");
            // Save  the output image to a file
            File file = new File(dstName);
            ImageIO.write(dstImage, "jpg", file );
        } 
        catch (Exception e)
        {
            System.err.println("Error enclountered while handling file: "+e);
        }
    }     
}  