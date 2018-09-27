package fauxpas.collections;

import fauxpas.entities.Tile;
import javafx.scene.image.Image;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;

public class TileImageDirectory {

    private HashMap<String, Image> keytoImage;

    private int AssetDimension;

    public TileImageDirectory(int assetDimension) {
        this.keytoImage = new HashMap<>();
        this.AssetDimension = assetDimension;
    }

    public void addTile(Tile tile, Path assetPath) {
        this.keytoImage.put(TileToKey(tile), new Image(assetPath.toString()));
    }

    public Optional<Image> getTileAsset(Tile tile) {
        return Optional.ofNullable(this.keytoImage.get(TileToKey(tile)));
    }

    private String TileToKey(Tile tile) {
        return tile.getType()+","+tile.getIndex();
    }

    public int getAssetDimension() {
        return AssetDimension;
    }
}
