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

    /**
     * translate from 2d coordinate to 1d array index.
     * @param x first dimension (east/west) value
     * @param y second dimension (north/south) value
     * @return index into single dimension tiles array.
     */
    private int CoordsToIndex(int x, int y) {
        return (this.width*y)+x;
    }

    /**
     * Get the tile for coordinate (x,y).
     * @param x first dimension (east/west) value
     * @param y second dimension (north/south) value
     * @return tile for the world coordinate requested or null optional if the coordinate parameters were invalid.
     */
    public Optional<Tile> getTile(int x, int y) {
        if (x > -1 && x < this.width && y > -1 && y < this.height) {
            return Optional.ofNullable(this.tiles.get(this.CoordsToIndex(x,y)));
        }
        else {
            return Optional.empty();
        }
    }

    /**
     * Set the tile for coordinate (x,y)
     * @param x first dimension (east/west) value
     * @param y second dimension (north/south) value
     * @param tile to be put at the location.
     */
    public void setTile(int x, int y, Tile tile) {
        if (x > -1 && x < this.width && y > -1 && y < this.height) {
            this.tiles.set(CoordsToIndex(x, y), tile);
        }
    }

    /**
     * Max distance east to west (in tiles) of this world.
     * @return width of the world
     */
    public int getWidth() {
        return width;
    }

    /**
     * Max distance north to south (in tiles) of this world.
     * @return height of the world
     */
    public int getHeight() {
        return height;
    }
}
