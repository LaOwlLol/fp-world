package fauxpas.entities;

import fauxpas.filters.Filter;
import fauxpas.filters.RandomNoise;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import java.nio.file.Paths;

public class DepthMap {

    private Image depthImage;

    public DepthMap(int width, int height) {
        this.depthImage = new WritableImage(width, height);
        RandomNoise noiseGen = new RandomNoise();
        this.applyFilter(noiseGen);
    }

    public DepthMap(Image depthImage) {
        this.depthImage = depthImage;
    }

    public Image getImage() {
        return depthImage;
    }

    /**
     * Get the intensity value (synonymous with depth) of a pixel
     * @param x coordinate of pixel to get
     * @param y coordinate of pixel to get
     * @return intensity of pixel;
     */
    public double getIntensity(int x, int y) {
        //todo this is excessive (assuming the pixel is already gray) and hacky (assumes r = b = g)
        return depthImage.getPixelReader().getColor(x, y).grayscale().getBlue();
    }

    public void applyFilter(Filter filter) {
        this.depthImage = filter.apply(depthImage);
    }

}