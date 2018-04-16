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
 * This class contains methods for masking an image array with horizontal and vertical Sobel masks.
 * 
 * @author robert
 */

public class Sobel {
    //The masks for each Sobel convolution
    private static final int[][] MASK_H = { {-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1} };
    private static final int[][] MASK_V = { {-1, -2, -1}, {0, 0, 0}, {1, 2, 1} };
    
    /**
     * Send this method an int[][] array of grayscale pixel values to get a an image resulting
     * from the convolution of this image with the horizontal Sobel mask.
     * 
     * @param raw   int[][], array of grayscale pixel values 0-255
     * @return out  int[][], output array of convolved image.
     */
    public static int[][] Horizontal(int[][] raw) {
        int[][] out = null;
        int height = raw.length;
        int width = raw[0].length;
        
        if (height > 2 && width > 2) {
            out = new int[height - 2][width - 2];
        
            for (int r = 1; r < height - 1; r++) {
                for (int c = 1; c < width - 1; c++) {
                    int sum = 0;

                    for (int kr = -1; kr < 2; kr++) {
                        for (int kc = -1; kc < 2; kc++) {
                            sum += (MASK_H[kr + 1][kc + 1] * raw[r + kr][c + kc]);
                        }
                    }

                    out[r - 1][c - 1] = sum;
                }
            }
        }
        
        return out;
    }
    
    /**
     * Send this method an int[][] array of grayscale pixel values to get a an image resulting
     * from the convolution of this image with the vertical Sobel mask.
     * 
     * @param raw   int[][], array of grayscale pixel values 0-255
     * @return out  int[][], output array of convolved image.
     */
    public static int[][] Vertical(int[][] raw) {
        int[][] out = null;
        int height = raw.length;
        int width = raw[0].length;
        
        if (height > 2 || width > 2) {
            out = new int[height - 2][width - 2];
        
            for (int r = 1; r < height - 1; r++) {
                for (int c = 1; c < width - 1; c++) {
                    int sum = 0;

                    for (int kr = -1; kr < 2; kr++) {
                        for (int kc = -1; kc < 2; kc++) {
                            sum += (MASK_V[kr + 1][kc + 1] * raw[r + kr][c + kc]);
                        }
                    }

                    out[r - 1][c - 1] = sum;
                }
            }
        }
        
        return out;
    }
}
