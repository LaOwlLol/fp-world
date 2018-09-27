package fauxpas.entities;

public class Tile {

    /**
     * Grouping or theme identifier for set this tile belongs to.
     */
    private int set;

    /**
     * Image identifier or index into the tile set.
     */
    private int value;

    public Tile(int _set, int _value) {
        this.set = _set;
        this.value = _value;
    }

    /**
     * Get the set this tile belongs to (identifies which group/theme of tiles).
     * @return
     */
    public int getSet() {
        return set;
    }

    /**
     * Get value of this tile (identifies which item in the tile set)
     * @return
     */
    public int getValue() {
        return value;
    }
}
