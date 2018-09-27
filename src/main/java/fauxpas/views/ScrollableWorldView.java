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

    /**
     * Get the x coordinate.
     * @return x location on the x axis (in tiles).
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y coordinate.
     * @return y location on the y axis (in tiles).
     */
    public int getY() {
        return y;
    }

    /**
     * Move the view on the x axis.
     * @param delta change to the x coordinate.
     */
    public void scrollX(int delta) {
        if (delta+x > 0 && delta+x < width) {
            x = delta +x;
        }
    }

    /**
     * Move the view on the y axis.
     * @param delta change to the y coordinate.
     */
    public void scrollY(int delta) {
        if (delta+y > 0 && delta+y < height) {
            y = delta + y;
        }
    }

    /**
     * Move the view on either axis.
     * @param deltaX change to the x coordinate.
     * @param deltaY change to the y coordinate.
     */
    public void scroll(int deltaX, int deltaY) {
        scrollX(deltaX);
        scrollY(deltaY);
    }

}