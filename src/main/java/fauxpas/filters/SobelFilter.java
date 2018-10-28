package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class SobelFilter implements Filter, Convolution {

    private static final int RED = 0;
    private static final int BLUE = 1;
    private static final int GREEN = 2;
    private final int WIDTH = 3;
    double[][] horzKernal;
    double[][] vertKernal;


    public SobelFilter() {
        this.horzKernal = new double[WIDTH][WIDTH];
        this.horzKernal[0][0] = -0.5;
        this.horzKernal[1][0] = -1;
        this.horzKernal[2][0] = -0.5;
        this.horzKernal[0][2] = 0.5;
        this.horzKernal[1][2] = 1;
        this.horzKernal[2][2] = 0.5;

        this.vertKernal = new double[WIDTH][WIDTH];
        this.vertKernal[0][0] = -0.5;
        this.vertKernal[0][1] = -1;
        this.vertKernal[0][2] = -0.5;
        this.vertKernal[2][0] = 0.5;
        this.vertKernal[2][1] = 1;
        this.vertKernal[2][2] = 0.5;
    }

    @Override
    public Image apply(Image target) {
        PixelReader targetReader = target.getPixelReader();
        WritableImage buffer = new WritableImage((int) target.getWidth(), (int) target.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        double[][][] horzConvolutionKernel;
        double[][][] vertConvolutionKernel;

        double [] horzSum;
        double [] vertSum;

        for (int imageY = 0; imageY < target.getHeight(); ++imageY) {
            for (int imageX = 0; imageX < target.getWidth(); ++imageX) {

                horzConvolutionKernel = computeKernel(target, targetReader, horzKernal, imageY, imageX);
                vertConvolutionKernel = computeKernel(target, targetReader, vertKernal, imageY, imageX);

                //sum pass;
                horzSum = sumKernel(horzConvolutionKernel);
                vertSum = sumKernel(vertConvolutionKernel);

                //apply

                bufferWriter.setColor(imageX, imageY,
                      new Color(
                            Math.min(1.0, Math.sqrt( Math.pow( vertSum[RED], 2) + Math.pow(horzSum[RED], 2) )),
                            Math.min(1.0, Math.sqrt( Math.pow( vertSum[GREEN], 2) + Math.pow(horzSum[GREEN], 2) )),
                            Math.min(1.0, Math.sqrt( Math.pow( vertSum[BLUE], 2) + Math.pow(horzSum[BLUE], 2) )),
                            1.0)
                );
            }
        }

        return buffer;
    }

    private double[] sumKernel(double[][][] tempKernal) {
        double [] sum = new double[GREEN+1];
        sum[RED] = 0.0;
        sum[BLUE] = 0.0;
        sum[GREEN] = 0.0;
        for (int kernelY = 0; kernelY < WIDTH; ++kernelY ) {
            for (int kernelX = 0; kernelX < WIDTH; ++kernelX) {
                sum[RED] += tempKernal[kernelX][kernelY][RED];
                sum[BLUE] += tempKernal[kernelX][kernelY][BLUE];
                sum[GREEN] += tempKernal[kernelX][kernelY][GREEN];
            }
        }
        return sum;
    }

    @Override
    public double[][][] computeKernel(Image target, PixelReader targetReader, double[][] convolution, int imageY, int imageX) {
        double[][][] tempKernel = new double[WIDTH][WIDTH][GREEN+1];

        //multiply pass
        for (int kernelY = 0; kernelY < WIDTH; ++kernelY ) {
            for (int kernelX = 0; kernelX < WIDTH; ++kernelX) {

                int i = kernelX - (WIDTH/2);
                int j = kernelY - (WIDTH/2);

                if ((imageX+i) > 0 && (imageX+i) < target.getWidth() &&
                      (imageY+j) > 0 && (imageY+j) < target.getHeight()) {
                    tempKernel[kernelX][kernelY][RED] = targetReader.getColor(imageX+i,imageY+j).getRed() *
                          convolution[kernelX][kernelY];

                    tempKernel[kernelX][kernelY][BLUE] = targetReader.getColor(imageX+i,imageY+j).getBlue() *
                          convolution[kernelX][kernelY];

                    tempKernel[kernelX][kernelY][GREEN] = targetReader.getColor(imageX+i,imageY+j).getGreen() *
                          convolution[kernelX][kernelY];
                }

            }
        }
        return tempKernel;
    }
}
