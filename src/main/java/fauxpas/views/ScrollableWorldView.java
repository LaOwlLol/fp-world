package fauxpas.views;

import fauxpas.collections.TileImageDirectory;
import fauxpas.entities.World;
import javafx.scene.canvas.GraphicsContext;

public class ScrollableWorldView {

    /**
     * The current x-coordinate in the world (int tiles).
     */
    private int x;

    /**
     * The current y-coordinate in the world (int tiles).
     */
    private int y;

    /**
     * distance (in tiles) from left edge of the view to right edge.
     */
    private int width;

    /**
     * distance (int tiles) from the top edge of the view to the left edge.
     */
    private int height;

    /**
     * Construct a ScrollableWorldView with location and dimensions.
     * @param x first dimension (east/west) coordinate of the scroll view.
     * @param y second dimension (north/south) coordinate
     * @param width distance from left to right of the view.
     * @param height distance from top to bottom of the view.
     */
    public ScrollableWorldView(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Draw the view to an graphics context.
     * @param gc graphics context to draw to.
     * @param w world to view.
     * @param assets collection of tiles to use.
     */
    public void render(GraphicsContext gc, World w, TileImageDirectory assets) {

        for (int j = 0; j < this.height; ++j) {
            for (int i = 0; i < this.width; ++i) {
                int finalI = i;
                int finalJ = j;
                w.getTile(x+i, y+j).ifPresent( (tile) -> {
                    assets.getTileImage(tile).ifPresent(
                            (image)-> gc.drawImage(image, (x+finalI)*assets.getCellDimensions(), (y+ finalJ)*assets.getCellDimensions())
                    );
                }) ;
            }
        }

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
