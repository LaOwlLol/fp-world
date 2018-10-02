package fauxpas.views;

import fauxpas.collections.TileImageDirectory;
import fauxpas.entities.World;
import javafx.scene.canvas.GraphicsContext;

public class MiniMapWorldView {

    private final TileImageDirectory assets;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private World world;

    /**
     *
     * @param x coordinate  (in tiles) of the preview.
     * @param y coordinate (in tiles) of the preview.
     * @param width (in pixels) of the view port.
     * @param height (in pixels) of the view port.
     * @param world to view and render.
     * @param assets map from tile to image.
     */
    public MiniMapWorldView(int x, int y, int width, int height, World world, TileImageDirectory assets) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.world = world;
        this.assets = assets;
    }


    public void render(GraphicsContext gc) {
        int w = this.getTileWidth();
        int h = this.getTileHeight();

        for (int j = 0; j < this.world.getHeight(); ++j) {
            for (int i = 0; i < this.world.getWidth(); ++i) {
                int finalI = i;
                int finalJ = j;

                this.world.getTile(i, j).ifPresent(

                      tile -> this.assets.get(tile).ifPresent(
                            image -> {
                                gc.drawImage(image, (finalI) * w, (finalJ) * h);
                            }
                      )
                );
            }
        }
    }

    private int getTileWidth() {
        return (int)(((float)this.width/(world.getWidth()*assets.getTileDimension()))*assets.getTileDimension());
    }

    private int getTileHeight() {
        return (int)(((float)this.height/(world.getHeight()*assets.getTileDimension()))*assets.getTileDimension());
    }

}
