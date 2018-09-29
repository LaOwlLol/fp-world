package fauxpas.views;

import fauxpas.collections.TileImageDirectory;
import fauxpas.entities.World;
import javafx.scene.canvas.GraphicsContext;

/**
 * A scrollable window view on a World.
 *
 * This view renders a width by height window with it's upper left corner at x,y in the world.
 *
 * The position of x,y coordinate can he changed to 'scroll' the view around the World.
 *
 * Rending this view requires providing a GraphicsContext to render to, World to render, and a TileImageDirectory to
 * use for tile values in the World.
 *
 * This view assumes tiles are square.
 */
public class ScrollableWorldView {

    private final TileImageDirectory assets;
    private World world;

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

    /**
     *
     * @param x coordinate (in tiles) in the world.
     * @param y coordinate (in tiles) in the world.
     * @param width (in tiles) of the view port.
     * @param height (in tiles) of the view port.
     * @param world to view and render.
     * @param assets map from tile to image.
     */
    public ScrollableWorldView(int x, int y, int width, int height, World world, TileImageDirectory assets) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.world = world;
        this.assets = assets;
    }

    /**
     * Draw a window of our World from x,y to (x+width),(y+height) using our assets directory.
     * @param gc the graphics context to draw on.
     */
    public void render(GraphicsContext gc) {
        for (int j = 0; j < this.height; ++j) {
            for (int i = 0; i < this.width; i++) {
                int finalI = i;
                int finalJ = j;
                int dim = assets.getTileDimension();
                this.world.getTile(this.x+i, this.y+j).ifPresent(

                      tile -> this.assets.get(tile).ifPresent(
                            image -> {
                                gc.drawImage(image, (finalI) * dim, (finalJ) * dim);
                            }
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
        if ( (x + delta) > -1 && (x + delta) > this.world.getWidth() ) {
            x = x + delta;
        }
    }

    /**
     * Move the view on the y axis.
     * @param delta change to the y coordinate.
     */
    public void scrollY(int delta) {
        if ( (y + delta) > -1 && (y + delta) < this.world.getHeight() ) {
            y = y + delta;
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
