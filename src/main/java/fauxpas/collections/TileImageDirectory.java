package fauxpas.collections;

import fauxpas.entities.Tile;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Optional;

/**
 * A Mapping (directory) of Tile to Image.
 *
 * This Mapping is implemented as java.util.HashMap.  Tiles are translated to String keys by
 * the private helper function TileToKey(Tile) on each map and get call.
 *
 * When drawing mapped images, the public helper getTileDimension() may be useful.  Drawing code may assume that tiles are square
 * (meaning width is equal to height). Or alternatively as long as dimensions are proportional user code can give the greatest
 * common factor of width and height as tileDimension and make use of this fact when drawing images returned by get(Tile).
 *
 */
public class TileImageDirectory {

    private HashMap<String, Image> keytoImage;
    private int tileDimension;

    /**
     * Create an empty directory.
     * @param tileDimension width and height of tile assets.
     */
    public TileImageDirectory(int tileDimension) {
        this.keytoImage = new HashMap<>();
        this.tileDimension = tileDimension;
    }

    /**
     * Map a tile to an image.
     * @param key tile to add to key set.
     * @param value image to map to the key.
     */
    public void map(Tile key, Image value) {
        this.keytoImage.put(TileToKey(key), value);
    }

    /**
     * Get the image asset for a file.
     * @param key tile to look up.
     * @return Optional to Image if key is in the set, otherwise an empty optional is returned.
     */
    public Optional<Image> get(Tile key) {
        return Optional.ofNullable(this.keytoImage.get(TileToKey(key)));
    }

    /**
     * Translate from Tile to key name.
     * @param tile tile to translate
     * @return key to the tile.
     */
    private String TileToKey(Tile tile) {
        return tile.getType()+","+tile.getIndex();
    }

    /**
     * Get the width/height of tiles in the collection.
     * @return dimension of tile image assets in the collection.
     */
    public int getTileDimension() {
        return tileDimension;
    }
}
