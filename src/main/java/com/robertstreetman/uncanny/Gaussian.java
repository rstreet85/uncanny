/*
 * Copyright 2018 Robert Streetman
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.robertstreetman.uncanny;

/**
 * This class contains methods for masking image arrays with Gaussian masks.
 * Instead of convolving each pixel pixel with a 2D Gaussian kernel, it convolves
 * the image horizontally and vertically with a 1D Gaussian kernel.
 * 
 * @author robert
 */

/*
TODO
-These can be combined into single method with more generic implementation
-Use height/width and radius for bounds checking
*/

public class Gaussian {
    //This seems like a very costly operation, only doing this once.
    private static final double SQRT2PI = Math.sqrt(2 * Math.PI);
    
    /**
     * Send this method an int[][][] RGB array, an int radius, and a double intensity to blur the
     * image with a Gaussian filter of that radius and intensity.
     * 
     * @param raw       int[][][], an array of RGB values to be blurred
     * @param rad       int, the radius of the Gaussian filter (filter width = 2 * r + 1)
     * @param intens    double, the intensity of the Gaussian blur
     * @return outRGB   int[][][], an array of RGB values from blurring input image with Gaussian filter
     */
    public static int[][][] BlurRGB(int[][][] raw, int rad, double intens) {
        int height = raw.length;
        int width = raw[0].length;
        double intensSquared2 = 2 * intens * intens;
        //This also seems very costly, do it as little as possible
        double invIntensSqrPi = 1 / (SQRT2PI * intens);
        double norm = 0.;
        double[] mask = new double[2 * rad + 1];
        int[][][] outRGB = new int[height - 2 * rad][width - 2 * rad][3];
        
        //Create Gaussian kernel
        for (int x = -rad; x < rad + 1; x++) {
            double exp = Math.exp(-((x * x) / intensSquared2));
            
            mask[x + rad] = invIntensSqrPi * exp;
            norm += mask[x + rad];
        }
        
        //Convolve image with kernel horizontally
        for (int r = rad; r < height - rad; r++) {
            for (int c = rad; c < width - rad; c++) {
                double[] sum = new double[3];
                
                for (int mr = -rad; mr < rad + 1; mr++) {
                    for (int chan = 0; chan < 3; chan++) {
                        sum[chan] += (mask[mr + rad] * raw[r][c + mr][chan]);
                    }
                }
                
                //Normalize channels after blur
                for (int chan = 0; chan < 3; chan++) {
                    sum[chan] /= norm;
                    outRGB[r - rad][c - rad][chan] = (int) Math.round(sum[chan]);
                }
            }
        }
        
        //Convolve image with kernel vertically
        for (int r = rad; r < height - rad; r++) {
            for (int c = rad; c < width - rad; c++) {
                double[] sum = new double[3];
                
                for (int mr = -rad; mr < rad + 1; mr++) {
                    for(int chan = 0; chan < 3; chan++) {
                        sum[chan] += (mask[mr + rad] * raw[r + mr][c][chan]);
                    }
                }
                
                //Normalize channels after blur
                for (int chan = 0; chan < 3; chan++) {
                    sum[chan] /= norm;
                    outRGB[r - rad][c - rad][chan] = (int) Math.round(sum[chan]);
                }
            }
        }
        
        return outRGB;
    }
    
    /**
     * Send this method an int[][] grayscale array, an int radius, and a double intensity to blur the
     * image with a Gaussian filter of that radius and intensity.
     * 
     * @param raw       int[][], an array of grayscale values to be blurred
     * @param rad       int, the radius of the Gaussian filter (filter width = 2 * r + 1)
     * @param intens    double, the intensity of the Gaussian blur
     * @return outRGB   int[][], an array of grayscale values from blurring input image with Gaussian filter
     */
    public static int[][] BlurGS (int[][] raw, int rad, double intens) {
        int height = raw.length;
        int width = raw[0].length;
        double norm = 0.;
        double intensSquared2 = 2 * intens * intens;
        //This also seems very costly, do it as little as possible
        double invIntensSqrPi = 1 / (SQRT2PI * intens);
        double[] mask = new double[2 * rad + 1];
        int[][] outGS = new int[height - 2 * rad][width - 2 * rad];
        
        //Create Gaussian kernel
        for (int x = -rad; x < rad + 1; x++) {
            double exp = Math.exp(-((x * x) / intensSquared2));
            
            mask[x + rad] = invIntensSqrPi * exp;
            norm += mask[x + rad];
        }
        
        //Convolve image with kernel horizontally
        for (int r = rad; r < height - rad; r++) {
            for (int c = rad; c < width - rad; c++) {
                double sum = 0.;
                
                for (int mr = -rad; mr < rad + 1; mr++) {
                    sum += (mask[mr + rad] * raw[r][c + mr]);
                }
                
                //Normalize channel after blur
                sum /= norm;
                outGS[r - rad][c - rad] = (int) Math.round(sum);
            }
        }
        
        //Convolve image with kernel vertically
        for (int r = rad; r < height - rad; r++) {
            for (int c = rad; c < width - rad; c++) {
                double sum = 0.;
                
                for(int mr = -rad; mr < rad + 1; mr++) {
                    sum += (mask[mr + rad] * raw[r + mr][c]);
                }
                
                //Normalize channel after blur
                sum /= norm;
                outGS[r - rad][c - rad] = (int) Math.round(sum);
            }
        }
        
        return outGS;
    }
}
