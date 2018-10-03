package fauxpas.views;

import fauxpas.collections.TileImageDirectory;
import fauxpas.entities.World;
import javafx.scene.canvas.GraphicsContext;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.function.Consumer;

public class MiniMapWorldView implements PropertyChangeListener {

    private final TileImageDirectory assets;
    private int x;
    private int y;
    private final int width;
    private final int height;
    private World world;
    private HashMap<String, Consumer<PropertyChangeEvent>> eventConsumers;

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

        eventConsumers.put("x", event-> this.x = (int) event.getNewValue());
        eventConsumers.put("y", event-> this.y = (int) event.getNewValue());
    }

    public void render(GraphicsContext gc) {
        int w = (int)(((float)this.width/(gc.getCanvas().getWidth()))*assets.getTileDimension());
        int h = (int)(((float)this.height/(gc.getCanvas().getHeight()))*assets.getTileDimension());

        for (int j = 0; j < this.world.getHeight(); ++j) {
            for (int i = 0; i < this.world.getWidth(); ++i) {
                int finalI = i;
                int finalJ = j;

                this.world.getTile(i, j).ifPresent(

                      tile -> this.assets.get(tile).ifPresent(
                            image -> gc.drawImage(image, (finalI) * w, (finalJ) * h)
                      )
                );
            }
        }

        gc.strokeRect(this.x*w, this.y*h, this.width*w, this.height*h);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
         this.eventConsumers.get(event.getPropertyName()).accept(event);
    }
}
