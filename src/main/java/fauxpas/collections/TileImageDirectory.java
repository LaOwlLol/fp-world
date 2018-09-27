package fauxpas.collections;

import fauxpas.entities.Tile;
import javafx.scene.image.Image;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;

public class TileImageDirectory {

    private HashMap<String, Image> keytoImage;

    private int AssetDimension;
    boolean debug;

    public TileImageDirectory(int assetDimension) {
        this.keytoImage = new HashMap<>();
        this.AssetDimension = assetDimension;
        this.debug = false;
    }

    public TileImageDirectory(int assetDimension, boolean debug) {
        this.keytoImage = new HashMap<>();
        this.AssetDimension = assetDimension;
        this.debug = debug;
    }

    public void addTile(Tile tile, Image image) {
        this.keytoImage.put(TileToKey(tile), image);
    }

    public Optional<Image> getTileAsset(Tile tile) {
        if (this.debug) {
            System.out.println("Fetching '" + TileToKey(tile) + "' : " + keytoImage.containsKey(TileToKey(tile)));
        }
        return Optional.ofNullable(this.keytoImage.get(TileToKey(tile)));
    }

    private String TileToKey(Tile tile) {
        return tile.getType()+","+tile.getIndex();
    }

    public int getAssetDimension() {
        return AssetDimension;
    }
}
