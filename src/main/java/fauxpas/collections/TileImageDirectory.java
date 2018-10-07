package fauxpas.collections;

import fauxpas.entities.Tile;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    //private static final String KEY_DELIMITER = ",";
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
     * Get the keys in the directory.
     * @return Tiles in the directory.
     */
    public List<Tile> getKeys() {
        return this.keytoImage.keySet().stream().map(this::KeyToTile)
              .filter(Optional::isPresent)
              .map(Optional::get)
              .collect(Collectors.toList());
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
     * Translate from Tile object to key name.
     * @param tile to translate
     * @return key to the tile.
     */
    private String TileToKey(Tile tile) {
        return Tile.toCSV(tile);
    }

    /**
     * Translate from key name to Tile object.
     * @param key to translate
     * @return Optional Tile object or null if invalid string given.
     */
    private Optional<Tile> KeyToTile(String key) {
        return Tile.fromCSV(key);
    }

    /**
     * Get the width/height of tiles in the collection.
     * @return dimension of tile image assets in the collection.
     */
    public int getTileDimension() {
        return tileDimension;
    }


    /**
     * Quiet LoadFromFileSystem:
     * Delegates to LoadFromFileSystem(String , int , boolean ) with verbose set to false.
     * @param parentDirectoryPath path to directory to import from.
     * @param dims dimension of images in child directories.
     * @return a new TileImageDirectory of tiles mapped to images in child directories.
     */
    public static TileImageDirectory LoadFromFileSystem(String parentDirectoryPath, int dims) {
        return LoadFromFileSystem(parentDirectoryPath, dims, false);
    }

    /**
     *  Create an TileImageDirectory from files on the local file system.  The returned TileImageDirectory will contain
     * tile to images mappings for all images in all child directories under the parent directory path given.
     * Note that images in the parent directory will be ignored, only images in child directories are added.
     * Tiles added to the mapping will have types matching the name of the child directory and index matching
     * the path of the image.
     *
     * @param parentDirectoryPath path to directory to import from.
     * @param dims dimension of images in child directories.
     * @param verbose if true output items found to system.out
     * @return a new TileImageDirectory of tiles mapped to images in child directories.
     */
    public static TileImageDirectory LoadFromFileSystem(String parentDirectoryPath, int dims, boolean verbose) {

        TileImageDirectory construct = new TileImageDirectory(dims);

        if (verbose) {
            System.out.println("Attempting to load TileImageDirectory file system at: " + parentDirectoryPath);
        }

        try (Stream<Path> subdirs = Files.find( Paths.get(parentDirectoryPath), 1,
                    (resultPath, attributes) -> attributes.isDirectory() )) {

            //hack remove the top directory (todo better)
            Stream<Path> filteredSubdirs = subdirs.filter(dir -> !dir.toString().equals(parentDirectoryPath));

            filteredSubdirs.forEach(dir -> LoadImagesInFileSystemDirectory(construct, dir, verbose));

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return construct;
    }

    /**
     * Add images in a file system directory to the given TileImageDirectory.
     * Tiles added to the mapping will have types matching the name of the given directory and index matching
     * the path of the image.
     *
     * @param assets Tile to image mapping to add to.
     * @param dir File system directory to import from.
     * @param verbose if true output items found to system.out
     */
    private static void LoadImagesInFileSystemDirectory(TileImageDirectory assets, Path dir, boolean verbose) {

        String type = FileName(dir);

        if (verbose) {
            System.out.println("Found new tile type: "+ type + "-> " + dir);
        }
        try (Stream<Path> list = Files.list(dir) ) {
            list.forEach( path -> {
                if (verbose) {
                    System.out.println("\t new tile: " + type + ", " + path.toString());
                }
                assets.map(new Tile(type, path.toString()), new Image(path.toUri().toString()));
            } );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the name of the file named by the path.
     * @param path to parse.
     * @return Name of file or path given.
     */
    private static String FileName(Path path) {
        return (new File(path.toUri())).getName();
    }

    /**
     * Get the name of the parent directory of the path.
     * @param path to parse.
     * @return Name of the parent directory to path given.
     */
    private static String ParentDirectoryName(Path path) {
        return (new File(path.toUri())).getParentFile().getName();
    }

    /**
     * Get the name of the parent directory of the path.
     * @param path to parse.
     * @return Name of the parent directory to path given.
     */
    private static String ParentDirectoryName(String path) {
        return ParentDirectoryName(Paths.get(path));
    }
}
