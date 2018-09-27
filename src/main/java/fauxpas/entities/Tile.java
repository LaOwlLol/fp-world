package fauxpas.entities;

public class Tile {

    /**
     * The tile type (synonymous set,style, or theme).
     */
    private int type;

    /**
     * Which item in the type group.
     */
    private int index;

    public Tile(int type, int index) {
        this.type = type;
        this.index = index;
    }

    /**
     * Get the type identifier.
     * @return tile type.
     */
    public int getType() {
        return type;
    }

    /**
     * Get the index into this tile's type.
     * @return
     */
    public int getIndex() {
        return index;
    }
}
