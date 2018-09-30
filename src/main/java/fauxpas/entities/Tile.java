package fauxpas.entities;

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
}
