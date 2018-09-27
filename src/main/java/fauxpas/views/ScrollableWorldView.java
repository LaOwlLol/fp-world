package fauxpas.views;

import fauxpas.collections.TileImageDirectory;
import fauxpas.entities.World;
import javafx.scene.canvas.GraphicsContext;

public class ScrollableWorldView {

    /**
     * current x coordinate (in tiles) in the world;
     */
    private int x;

    /**
     * current y coordinate (in tiles) in the world.
     */
    private int y;

    /**
     *  width (in tiles) of the view port.
     */
    private int width;

    /**
     * height (in tiles) of the view port.
     */
    private int height;

    public ScrollableWorldView(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Draw a window of a World from x,y to (x+width),(y+height) using the asset directory provided.
     * @param gc the graphics context to draw on.
     * @param world the worlds to draw a window of.
     * @param assets the assets to use for tiles.
     */
    public void render(GraphicsContext gc, World world, TileImageDirectory assets) {

        for (int j = 0; j < this.height; ++j) {
            for (int i = 0; i < this.width; i++) {
                int finalI = i;
                int finalJ = j;
                int dim = assets.getAssetDimension();
                world.getTile(this.x+i, this.y+j).ifPresent(

                      tile -> assets.getTileAsset(tile).ifPresent(
                            image -> gc.drawImage( image, (this.x+finalI)*dim, (this.y+finalJ)*dim )
                      )

                );
            }
        }

    }
}
