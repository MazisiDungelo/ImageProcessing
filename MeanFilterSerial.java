import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Scanner;

public class MeanFilterSerial
{
/*
 * This class creates process the image such that resulting pixels of the image 
 * are mean of the surronding pixels within a specified window size
 * Done in serial
 */
   
    public static void main(String[] args)throws IOException
    { 
         
         Scanner input = new Scanner(System.in);
         System.out.println("Enter <input image> <output image> <window size>");
         String inputImage = input.next();
         File fileInput = new File(inputImage);
         String fileOutput = input.next();
         int windowSize = input.nextInt();
         input.close();
         // Read the input image
         try 
         {
            BufferedImage image= ImageIO.read(fileInput);
            int width = image.getWidth();
            int height = image.getHeight();

            // Create an output image of same dimensions
            BufferedImage out_image= new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            
            // Track processing time
            long startTime = System.currentTimeMillis();
            // Initialise total pixels of each colour 
            int redTotal=0;int greenTotal=0;int blueTotal=0;int alphaTotal = 0;
            // Initialise mean of each colour
            int redMean=0; int greenMean=0; int blueMean=0;int alphaMean = 0;
            int counter=0;
            // Calculate the position of target pixels (one to be change to mean pixel)
            int w = (int)Math.floor(windowSize/2);
            // Loop through the pixels in the whole image
            for (int j=0; j<image.getHeight()-windowSize+1; j++)
            {
               for (int k=0; k<image.getWidth()-windowSize+1; k++)
               {       
                  // Loop through the pixels in the window                  
                  for (int y=j; y<j+windowSize; y++)
                  {
                     for (int x=k; x<k+windowSize; x++)
                     { 
                           // Get the pixel value
                           int p = image.getRGB(x,y);
                     
                           // Extract ARGB components
                           int a = (p>>24) & 0xff; // get alpha
                           int r = (p>>16) & 0xff; // get red
                           int g = (p>>8) & 0xff; // get green
                           int b =  p & 0xff; // get blue

                           redTotal += r;greenTotal += g;blueTotal += b;alphaTotal += a; // add these components to total
                           counter++;
                           
                     }
                     
                  }  
                  // Calculate average of the pixels in the window                
                  redMean = redTotal/counter;
                  greenMean = greenTotal/counter;
                  blueMean = blueTotal/counter;
                  alphaMean = alphaTotal/counter;
                     
                  // Create a new pixel value
                  int p = (alphaMean<<24) | (redMean<<16) | (greenMean<<8) | blueMean;
                  // Set the mean pixel to the target position (central pixel of the window)
                  out_image.setRGB(k+w,j+w,p);
                  
                  // Reset counter, total and mean for the next window
                  counter=0;
                  redMean = 0; greenMean = 0; blueMean = 0;alphaMean = 0;
                  redTotal = 0;greenTotal = 0;blueTotal = 0;alphaTotal = 0;
               
               }
            }
            // Measure the time after processing 
            long endTime = System.currentTimeMillis();
            // Save the output image of a file
            File file = new File(fileOutput);
            ImageIO.write(out_image, "jpg", file);

            System.out.println("Image filter took " +  (endTime - startTime) + " milliseconds" );
         }
         catch (Exception e)
         {
            System.out.println("Error encountered while handling file: "+ e);
         }
    
    }      

}    