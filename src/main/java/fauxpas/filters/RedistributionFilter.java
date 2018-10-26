package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class RedistributionFilter implements Filter {

    private final double pow;

    public RedistributionFilter() {
        this.pow = 2.0;
    }

    public RedistributionFilter(double power) {
        this.pow = power;
    }

    @Override
    public Image apply(Image image) {

        WritableImage buffer = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        PixelReader imageReader = image.getPixelReader();

        for (int j = 0; j < image.getHeight(); ++j) {
            for (int i = 0; i < image.getWidth(); ++i) {
                Color imageColor = imageReader.getColor(i, j);
                bufferWriter.setColor(i, j,
                      new Color(Math.pow( imageColor.getRed(), this.pow ),
                            Math.pow( imageColor.getGreen(), this.pow ),
                            Math.pow( imageColor.getBlue(), this.pow ),
                      1.0
                      )
                );
            }
        }

        return buffer;
    }
}
