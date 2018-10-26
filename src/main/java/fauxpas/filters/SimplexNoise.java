package fauxpas.filters;

import fauxpas.fastnoise.FastNoise;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Random;

public class SimplexNoise implements Filter {

    private FastNoise fastNoise;
    private float frequency;

    public SimplexNoise() {
        fastNoise = new FastNoise(new Random(System.currentTimeMillis()).nextInt());
        fastNoise.SetNoiseType(FastNoise.NoiseType.Simplex);
        this.frequency = 1;
    }

    public SimplexNoise(float frequency) {
        this();
        this.frequency = frequency;
    }

    @Override
    public Image apply(Image image) {

        WritableImage buffer = new WritableImage((int)image.getWidth(), (int)image.getHeight());
        PixelWriter bufferWriter = buffer.getPixelWriter();

        for (int j = 0; j < image.getHeight(); ++j) {
            for (int i = 0; i < image.getWidth(); ++i) {
                double color =  Math.abs(fastNoise.GetNoise(this.frequency * i, this.frequency * j));
                bufferWriter.setColor(i, j, new Color(color, color, color, 1.0));
            }
        }

        return buffer;
    }
}
