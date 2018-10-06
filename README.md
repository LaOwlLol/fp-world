# fp-world

A library for rendering 2D tiled worlds.

## Version 0.1.0

This API provides an in memory model of a world and some objects to help render the world.

For a more complete demonstration of this API checkout my Tile Editor project [fp-world-tiler](https://github.com/LaOwlLol/fp-world-tiler)

In most cases an end user of the library (at this point in time) should construct a World. Create a TileImageDirectory using the helper method LoadFromFileSystem.  Create a view. Finally pass the world and assets to the view's render method. 

What follows is a description of all the classes in the API.

### World Model

##### Tile:
A tile represents two things:
* A tile type to identify which tile theme or group.
* A tile index to identify which tile in the type.

```java
//create tile sets
Tile blank = new Tile("water",  "a");
Tile blank = new Tile("water",  "b");

//or another themed set 
Tile grass = new Tile("grass",  "center"); 
```

##### World
A world is a 2d plane of tiles. Worlds have two properties  width (west/left to east/right) and height (north/top to south/bottom) measured in tiles. 

Worlds contain a tile collection and provide a get and set method for accessing tiles with x,y coordinates.

```java
//construct a world with blank tile.
World w = new World(30,30, blank);

//set a tile to grass.
w.setTile(1,0, grass);
```

Worlds can be read from a file or write to a file to save and load progress.

```java
World w = World.ReadFromFile(Paths.get(System.getProperty("user.home"), "MyWorlds", "world_1").toString()).orElse(null);

World.WriteToFile(world,  Paths.get(System.getProperty("user.home"), "MyWorlds", "world_1").toString() );
```


### Asset Management 

Tiles do not contain an asset to render. A TileImageDirectory maps a tile to an image.

All tiles in a TileImageDirectory should have the same dimensions and be square (width == height).  The constructor requires the dimensions of images.
```java
TileImageDirectory assets = new TileImageDirectory(50);
```

Once constructed tiles can me mapped to images.

```java
assets.map(blank, new Image( Paths.get(System.getProperty("user.home"),"MyAssets", "water", "water_0.png" ).toUri().toString()));
assets.map(grass, new Image( Paths.get(System.getProperty("user.home"),"MyAssets", "grass", "grass_0.png" ).toUri().toString()));
```

The an image can be retrieved using the tile as a key.

```java
Image GrassImage = assets.get(grass).orElse(null);
```

This is all pretty low level and demonstrates how views can retrieve images with a tile reference.

A more practical use of the TileImageDirectory is to load an entire directory of images form a file system.

```java
TileImageDirectory assets = TileImageDirectory.LoadFromFileSystem(Paths.get(System.getProperty("user.home"), "MyAssets").toString(), tile_Dim, true);
```

The LoadFromFileSystem helper method takes a string path to a directory and walks it's subdirectories mapping all the images if fines to new tiles with type and matching the subdirectory name and index matching the full path to the time.
This is intended to be a powerful way to construct many themed sets of tiles in one go.

### View  

Views are used to render a world to a canvas using an asset collection. 

```java
//construct a view on the world using the asset set.
ScrollableWorldView view = new ScrollableWorldView(0,0, 16, 9, world, assets);

//construct the canvas view will draw on.
Canvas canvas = new Canvas(assets.getTileDimension()*view.getWidth(),assets.getTileDimension()*view.getHeight());

AnimationTimer animTimer = new AnimationTimer() {
       @Override
       public void handle(long now) {
           canvas.getGraphicsContext2D().clearRect(0,0,assets.getTileDimension()*view.getWidth(),
                 assets.getTileDimension()*view.getHeight());
           view.render(canvas.getGraphicsContext2D());
       }
   };

animTimer.start();
```


