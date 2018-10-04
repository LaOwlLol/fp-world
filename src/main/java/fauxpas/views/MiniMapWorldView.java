package fauxpas.views;

import fauxpas.collections.TileImageDirectory;
import fauxpas.entities.World;
import javafx.scene.canvas.GraphicsContext;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * A mini map view on a World.
 *
 * This view renders a the entire World scaled down to fit into a canvas.
 * this class is intended to provide "mini-map" ie where canvas.getWidth() is less than
 * tileImgDir.getTileDimension()*world.getWidth().
 *
 * The position of a ScrollableWorld view can be tracked and drawn by passing a MiniMapWorldView to the
 * registerChangeListener of the ScrollableWorld, and setting the MiniMapWorldView's setTrackScrollView with true.
 *
 * Rendering this view requires providing a GraphicsContext to render to.
 *
 */
public class MiniMapWorldView implements PropertyChangeListener {

    private final TileImageDirectory assets;
    private int x;
    private int y;
    private final int width;
    private final int height;
    private World world;
    private HashMap<String, Consumer<PropertyChangeEvent>> eventConsumers;
    private boolean trackScrollView;

    /**
     *
     * @param x coordinate  (in tiles) of the preview.
     * @param y coordinate (in tiles) of the preview.
     * @param width (in tiles) of the preview.
     * @param height (in tiles) of the preview.
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
        this.eventConsumers = new HashMap<>();
        this.trackScrollView = false;

        eventConsumers.put("x", event-> this.x = (int) event.getNewValue());
        eventConsumers.put("y", event-> this.y = (int) event.getNewValue());
    }

    /**
     * Draw this view to a  canvas graphics context.
     * @param gc graphics context to draw to.
     */
    public void render(GraphicsContext gc) {
        int w = (int) (assets.getTileDimension() *
              ( (float) (gc.getCanvas().getWidth()) / (assets.getTileDimension()*world.getWidth()) ));
        int h = (int) (assets.getTileDimension() *
              ( (float) (gc.getCanvas().getHeight()) / (assets.getTileDimension()*world.getHeight()) ));

        for (int j = 0; j < this.world.getHeight(); ++j) {
            for (int i = 0; i < this.world.getWidth(); ++i) {
                int finalI = i;
                int finalJ = j;

                this.world.getTile(i, j).ifPresent(

                      tile -> this.assets.get(tile).ifPresent(
                            image -> gc.drawImage(image, (finalI) * w, (finalJ) * h, w, h)
                      )
                );
            }
        }
        if (trackScrollView) {
            gc.strokeRect(this.x * w, this.y * h, this.width * w, this.height * h);
        }
    }

    /**
     * is this view drawing the position of a ScrollableWorldView.
     * @return false by default true if set.
     */
    public boolean isTrackScrollView() {
        return trackScrollView;
    }

    /**
     * Enable/Disable to draw the position of a ScrollableWorldView.
     * note: *this does not register this view as a PropertyChangeListener. Only enables drawing.
     * @param trackScrollView whether to draw position.
     */
    public void setTrackScrollView(boolean trackScrollView) {
        this.trackScrollView = trackScrollView;
    }

    /**
     * Consume a PropertyChangeEvent. (override for PropertyChangeListener)
     * @param event to consume.
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
         this.eventConsumers.get(event.getPropertyName()).accept(event);
    }
}
