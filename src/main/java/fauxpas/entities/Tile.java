package fauxpas.entities;

import java.util.Optional;

public class Tile {

    /**
     * The tile type (synonymous set,style, or theme).
     */
    private String type;

    /**
     * Which item in the type group.
     */
    private String index;

    public Tile(String type, String index) {
        this.type = type;
        this.index = index;
    }

    /**
     * Get the type identifier.
     * @return tile type.
     */
    public String getType() {
        return type;
    }

    /**
     * Get the index into this tile's type.
     * @return index item in tile set.
     */
    public String getIndex() {
        return index;
    }

    /**
     * Get a comma separated values string for a Tile.
     * @param tile to translate to csv.
     * @return a string formatted 'tileType,tileIndex'.
     */
    public static String toCSV(Tile tile) {
        return tile.getType() + "," + tile.getIndex();
    }

    /**
     * Get a Tile from a comma separated values string.
     * @param csv comma separated values to translate.
     * @return Optional Tile object or null if invalid string given.
     */
    public static Optional<Tile> fromCSV(String csv) {
        if (csv.split(",").length == 2) {
            return Optional.of(new Tile(csv.split(",")[0], csv.split(",")[1]));
        }
        else {
            return Optional.empty();
        }
    }

}
