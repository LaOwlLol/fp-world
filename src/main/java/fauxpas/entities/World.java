package fauxpas.entities;

import java.util.ArrayList;
import java.util.Optional;

public class World {

    /**
     * One greater than the max x value.
     */
    private int width;

    /**
     * One greater than the max y value.
     */
    private int height;

    /**
     * Tiles of the world.
     */
    private ArrayList<Tile> tiles;

    /**
     * Construct a World object with dimensions _width by _height where all spaces are the _default tile.
     * @param _width
     * @param _height
     * @param _default
     */
    public World(int _width, int _height, Tile _default) {
        this.width = _width;
        this.height = _height;

        this.tiles = new ArrayList<>();

        for (int i = 0; i < this.width*this.height; ++i) {
            tiles.add(_default);
        }
    }

    /**
     * Get the distance form the east most point to west most point of the world map.
     * @return width of the world.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the distance form the north most point to south most point of the world map.
     * @return height of the world.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the tile for coordinate (x,y).
     * @param x first dimension (east/west) value
     * @param y second dimension (north/south) value
     * @return tile for the world coordinate requested or null optional if the coordinate parameters were invalid.
     */
    public Optional<Tile> getTile(int x, int y) {
        //tiles arraylist is one dimensional so translate to 1d coordinate.
        if ( x < this.width && y < this.height && this.ToSingleDimension(x,y) < tiles.size()) {
            return Optional.of(tiles.get(this.ToSingleDimension(x,y)));
        }
        else {
            return Optional.empty();
        }
    }

    /**
     * translate from 2d coordinate to 1d array index.
     * @param x first dimension (east/west) value
     * @param y second dimension (north/south) value
     * @return index into single dimension tiles array.
     */
    private int ToSingleDimension(int x, int y) {
        return (y*width)+x;
    }

}
