package fauxpas.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Random;

public class RandomNoise implements Filter {
    @Override
    public Image apply(Image image) {

        WritableImage buffer = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();
        Random random = new Random(System.currentTimeMillis());
        for (int j = 0; j < image.getHeight(); ++j) {
            for (int i = 0; i < image.getWidth(); ++i) {
                double color = Math.abs(Math.sin(random.nextGaussian()));
                bufferWriter.setColor(i, j, new Color(color, color, color, 1.0));
            }
        }

        return buffer;
    }
}
