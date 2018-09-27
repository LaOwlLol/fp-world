package fauxpas.collections;

import fauxpas.entities.Tile;
import javafx.scene.image.Image;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;

public class TileImageDirectory {

    private HashMap<String, Image> keytoImage;
    private int cellDims;

    /**
     * Create an empty directory.
     * @param cellDims the dimension of assets in the tile set.
     */
    public TileImageDirectory(int cellDims) {
        this.keytoImage = new HashMap<>();
    }

    //TODO construct a directory from a file.
    //TODO save a directory to a file.

    /**
     * Add a tile with asset to the collection.
     * @param tile tile to associate with an image asset.
     * @param path path to the image asset.
     */
    public void addTile(Tile tile, Path path) {
        this.keytoImage.put(TileToKey(tile), new Image(path.toString()));
    }

    /**
     * Get the image asset for a file.
     * @param tile tile to look up.
     * @return Image for the tile.
     */
    public Optional<Image> getTileImage(Tile tile) {
        return Optional.ofNullable(this.keytoImage.get(TileToKey(tile)));
    }

    /**
     * Translate from Tile to key name.
     * @param tile tile to translate
     * @return key to the tile.
     */
    private String TileToKey(Tile tile) {
        return tile.getSet()+","+tile.getValue();
    }

    public int getCellDimensions() {
        return cellDims;
    }
}
