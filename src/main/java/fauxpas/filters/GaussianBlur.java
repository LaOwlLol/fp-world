package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.swing.*;

public class GaussianBlur implements Filter {

    private static final int RED = 0;
    private static final int BLUE = 1;
    private static final int GREEN = 2;
    private double kernalValue;
    private double standardDeviation;
    private double [][] kernal;
    private int width;
    private final int mid;


    public GaussianBlur(int width, double standardDeviation) {

        //todo all good if no true center to kernel?
        if (width % 2 == 0) {
            System.err.println("GaussianBlur width requested is not an odd value. adding 1 to normalize things.");
            width = width+1;
        }

        this.standardDeviation = standardDeviation;
        this.width = width;
        this.kernal = new double[this.width][this.width];

        this.mid = width/2;

        //pre calculate parts of gaussian equation that don't contain x,y
        double expDenom = Math.PI * Math.pow(this.standardDeviation,2);
        double outerDenom = 2.0 * expDenom;

        //intialize kernal
        this.kernalValue = 0.0;
        for (int y = 0; y < width; ++y) {
            for (int x = 0; x < width; ++x) {
                int i = x - mid;
                int j = y - mid;
                double expNumer = Math.pow(i, 2) + Math.pow(j, 2);
                double kvalue = (1.0/outerDenom) * Math.exp(expNumer/expDenom);
                this.kernal[x][y] = kvalue;
                this.kernalValue += kvalue;
            }
        }


    }

    @Override
    public Image apply(Image target) {

        PixelReader targetReader = target.getPixelReader();
        WritableImage buffer = new WritableImage((int) target.getWidth(), (int) target.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        double[][][] convolutionKernal;

        double [] sum;

        for (int imageY = 0; imageY < target.getHeight(); ++imageY) {
            for (int imageX = 0; imageX < target.getWidth(); ++imageX) {

                convolutionKernal = computeKernal(target, targetReader, imageY, imageX);

                //sum pass;
                sum = sumKernal(convolutionKernal);

                //apply

                bufferWriter.setColor(imageX, imageY,
                      new Color(sum[RED]/this.kernalValue,
                            sum[GREEN]/this.kernalValue,
                            sum[BLUE]/this.kernalValue,
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
        for (int kernalY = 0; kernalY < this.width; ++kernalY ) {
            for (int kernalX = 0; kernalX < this.width; ++kernalX) {
                sum[RED] += tempKernal[kernalX][kernalY][RED];
                sum[BLUE] += tempKernal[kernalX][kernalY][BLUE];
                sum[GREEN] += tempKernal[kernalX][kernalY][GREEN];
            }
        }
        return sum;
    }

    private double[][][] computeKernal(Image target, PixelReader targetReader, int imageY, int imageX) {
        double[][][] tempKernal = new double[this.width][this.width][GREEN+1];

        //multiply pass
        for (int kernalY = 0; kernalY < this.width; ++kernalY ) {
            for (int kernalX = 0; kernalX < this.width; ++kernalX) {

                int i = kernalX - this.mid;
                int j = kernalY - this.mid;

                if ((imageX+i) > 0 && (imageX+i) < target.getWidth() &&
                      (imageY+j) > 0 && (imageY+j) < target.getHeight()) {
                    tempKernal[kernalX][kernalY][RED] = targetReader.getColor(imageX+i,imageY+j).getRed() *
                          this.kernal[kernalX][kernalY];

                    tempKernal[kernalX][kernalY][BLUE] = targetReader.getColor(imageX+i,imageY+j).getBlue() *
                          this.kernal[kernalX][kernalY];

                    tempKernal[kernalX][kernalY][GREEN] = targetReader.getColor(imageX+i,imageY+j).getGreen() *
                          this.kernal[kernalX][kernalY];
                }

            }
        }
        return tempKernal;
    }
}
