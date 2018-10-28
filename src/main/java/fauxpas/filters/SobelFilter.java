package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class SobelFilter implements Filter {

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

        double[][][] horzConvolutionKernal;
        double[][][] vertConvolutionKernal;

        double [] horzSum;
        double [] vertSum;

        for (int imageY = 0; imageY < target.getHeight(); ++imageY) {
            for (int imageX = 0; imageX < target.getWidth(); ++imageX) {

                horzConvolutionKernal = computeKernal(target, targetReader, horzKernal, imageY, imageX);
                vertConvolutionKernal = computeKernal(target, targetReader, vertKernal, imageY, imageX);

                //sum pass;
                horzSum = sumKernal(horzConvolutionKernal);
                vertSum = sumKernal(vertConvolutionKernal);

                //apply

                bufferWriter.setColor(imageX, imageY,
                      new Color( Math.sqrt( Math.pow( vertSum[RED], 2) + Math.pow(horzSum[RED], 2) ),
                            Math.sqrt( Math.pow( vertSum[GREEN], 2) + Math.pow(horzSum[GREEN], 2) ),
                            Math.sqrt( Math.pow( vertSum[BLUE], 2) + Math.pow(horzSum[BLUE], 2) ),
                            1.0)
                );
            }
        }

        return buffer;
    }

    private double[] sumKernal(double[][][] tempKernal) {
        double [] sum = new double[GREEN+1];
        sum[RED] = 0.0;
        sum[BLUE] = 0.0;
        sum[GREEN] = 0.0;
        for (int kernalY = 0; kernalY < WIDTH; ++kernalY ) {
            for (int kernalX = 0; kernalX < WIDTH; ++kernalX) {
                sum[RED] += tempKernal[kernalX][kernalY][RED];
                sum[BLUE] += tempKernal[kernalX][kernalY][BLUE];
                sum[GREEN] += tempKernal[kernalX][kernalY][GREEN];
            }
        }
        return sum;
    }

    private double[][][] computeKernal(Image target, PixelReader targetReader, double[][] kernal, int imageY, int imageX) {
        double[][][] tempKernal = new double[WIDTH][WIDTH][GREEN+1];

        //multiply pass
        for (int kernalY = 0; kernalY < WIDTH; ++kernalY ) {
            for (int kernalX = 0; kernalX < WIDTH; ++kernalX) {

                int i = kernalX - (WIDTH/2);
                int j = kernalY - (WIDTH/2);

                if ((imageX+i) > 0 && (imageX+i) < target.getWidth() &&
                      (imageY+j) > 0 && (imageY+j) < target.getHeight()) {
                    tempKernal[kernalX][kernalY][RED] = targetReader.getColor(imageX+i,imageY+j).getRed() *
                          kernal[kernalX][kernalY];

                    tempKernal[kernalX][kernalY][BLUE] = targetReader.getColor(imageX+i,imageY+j).getBlue() *
                          kernal[kernalX][kernalY];

                    tempKernal[kernalX][kernalY][GREEN] = targetReader.getColor(imageX+i,imageY+j).getGreen() *
                          kernal[kernalX][kernalY];
                }

            }
        }
        return tempKernal;
    }
}
