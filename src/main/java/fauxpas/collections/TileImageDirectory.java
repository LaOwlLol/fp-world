package fauxpas.collections;

import fauxpas.entities.Tile;
import javafx.scene.image.Image;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;

public class TileAssetDirectory {

    private HashMap<String, Image> keytoImage;

    public TileAssetDirectory() {
        this.keytoImage = new HashMap<>();
    }

    public addTile(Tile tile, Path assetPath) {
        this.keytoImage.put(TileToKey(), new Image(assetPath.toString()));
    }

    public Optional<Image> getTileAsset(Tile tile) {
        return Optional.ofNullable(this.keytoImage.get(TileToKey(tile)));
    }

    private String TileToKey(Tile tile) {
        return tile.getSet()+","+tile.getValue();
    }
}
