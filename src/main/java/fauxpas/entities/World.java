package fauxpas.entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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
            this.setTile(CoordsToIndex(x, y), tile);
        }
    }

    /**
     * Set the nth tile in the set.
     * @param n which tile to set.
     * @param tile new value
     */
    private void setTile(int n, Tile tile) {
        if (n > -1 && n < this.tiles.size()) {
            this.tiles.set(n, tile);
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

    /**
     * Write the contents of a World object to a file at path
     * @param world to write to file.
     * @param path path of file to write to.
     */
    public static void WriteToFile(World world, String path) {
        AtomicReference<String> collection = new AtomicReference<>(new String());

        collection.set(collection.get().concat(String.valueOf(world.width)));
        collection.set(collection.get().concat(System.lineSeparator()));
        collection.set(collection.get().concat(String.valueOf(world.height)));
        collection.set(collection.get().concat(System.lineSeparator()));
        world.tiles.forEach( t -> {
            collection.set(collection.get().concat(Tile.toCSV(t)));
            collection.set(collection.get().concat(System.lineSeparator()));
        } );

        try {
            Files.write(Paths.get(path), collection.get().getBytes());
        }
        catch (IOException e) {
            //TODO what went wrong, and how to respond.
            e.printStackTrace();
        }

    }

    /**
     * Construct a World by reading from a file.
     * @param path to file to read.
     * @return Optional newly constructed world or empty optional.
     */
    public static Optional<World> ReadFromFile(String path) {
        try  {
            List<String> content = Files.readAllLines(Paths.get(path));
            int width = Integer.parseInt(content.remove(0));
            int height = Integer.parseInt(content.remove(0));

            Tile fail_default_tile = new Tile("default", "empty");

            World inited = new World(width, height, Tile.fromCSV(content.get(0)).orElse(fail_default_tile));
            AtomicInteger n = new AtomicInteger(0);
            content.forEach( tileStr -> {
                inited.setTile(n.get(), Tile.fromCSV(tileStr).orElse(fail_default_tile));
                n.getAndIncrement();
            });

            return Optional.of(inited);
        }
        catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

}
