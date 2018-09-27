package fauxpas.entities;

import java.util.ArrayList;
import java.util.Optional;

public class World {

    /**
     * Tiles of the world as a one dimensional array.
     */
    private ArrayList<Tile> tiles;

    /**
     * Distance from left to right in tiles.
     */
    private int width;


    /**
     * Distance from top to bottom in tiles.
     */
    private int height;

    /**
     * Construct an new 2D world of tiles.
     * @param width Distance from left to right in tiles.
     * @param height Distance from top to bottom in tiles.
     * @param base the default tile which is used to initially fill the 2D world (width by height).
     */
    public World(int width, int height, Tile base) {
        this.width = width;
        this.height = height;

        this.tiles = new ArrayList<>();

        for (int j = 0; j < this.height; ++j) {
            for (int i = 0; i < this.width; ++i) {
                this.tiles.add(base);
            }
        }
    }

    private int CoordsToIndex(int x, int y) {
        return (this.width*y)+x;
    }

    public Optional<Tile> getTile(int x, int y) {
        if (x > -1 && x < this.width && y > -1 && y < this.height) {
            return Optional.ofNullable(this.tiles.get(this.CoordsToIndex(x,y)));
        }
        else {
            return Optional.empty();
        }
    }
}
