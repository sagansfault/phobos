# phobos
A stylistic asynchronously processed mine craft world edit schematic builder, providing cool ways to build schematics with effects and pattern all while the sorting and processing happens on another thread

## What is phobos?
Phobos aims to add funtionality for developers to build world edit shematics (`.schem` files) with some style in their worlds. The following are some
basic examples of some of the build patterns that are currently in phobos as of `v1.0.0`

| Closing <img src="https://media.giphy.com/media/jUi2OeBzildNeoUkwJ/giphy.gif" width="265">| Spiral <img src="https://media.giphy.com/media/Q86SsAIlpWxI1pMSzf/giphy.gif" width="265"> | Random <img src="https://media.giphy.com/media/TdpNk4BV3MUO5HBqvS/giphy.gif" width="265"> |
|:---:|:---:|:---:|
| **End To End** <img src="https://media.giphy.com/media/l3yu328UNbwjAMfblq/giphy.gif" width="265"> | **End To End Iteration** <img src="https://media.giphy.com/media/gLit8fDkNyP9HMr6Zu/giphy.gif" width="265"> | **Closing Iteration** <img src="https://media.giphy.com/media/Q86SsAIlpWxI1pMSzf/giphy.gif" width="265"> |

## Installing
 1. Clone the repository using (preferably using ssh): `git clone git@github.com:sagan1/phobos.git`
 2. Enter the directory with `cd phobos`, then install phobos with maven using `mvn clean package install`
 3. Add phobos to your project's `pom.xml`'s dependencies:
```xml
<dependency>
    <groupId>me.sagan</groupId>
    <artifactId>phobos</artifactId>
    <version>1.0.0</version> <!-- at the time of writing, current version is 1.0.0. Check the pom for the latest version -->
    <scope>provided</scope>
</dependency>
````
4. Phobos uses WorldEdit's Develper API, find it here: https://worldedit.enginehub.org/en/latest/api/

## Using Phobos
Phobos was built with simplicity and ease of use as its number 1. It's based off a polymorphic structure in which `BuildPattern` and `BlockPlaceEffect` are extendable classes
that you can override. There are already many examples of this as you can see in the gifs above.

## Building a Schematic
Building schematics are only supposed to be easy. At ***minimum*** all you need to do is instantiate a new instance of `SchemBuilder` and call the `buildAt()` method:
```java
new SchemBuilder(mySchematicFile).buildAt(someLocation);
```
Done!
<br>
This by itself is a very basic implementation of the schematic builder and doesn't actually use any specific powers of phobos.
However, before calling `buildAt()` you can specify things like the `BuildPatten`, `BlockPlaceEffect`, the tikcs between placements, and whether to `ignoreAir` when placing blocks
<br>
A more filled out example would be:
```java
new SchematicBuilder(mySchematicFile)
  .buildPattern(RandomBuildPattern(3))
  .ticksBetweenIterations(1)
  .ignoreAir(true)
  .blockPlaceEffect(GreenSparklePlaceEffect())
  .buildAt(someLocation);
```
Simple right!


## BuildPattern
Build patterns essentially tell the schematic builder what order to place the blocks in. The build pattern takes in a list of 
`BlockVector3` (just a location abstraction from world edit) and sorts them accordingly, returning a list of lists of block vectors `List<List<BlockVector3>>`
<br>
The list of lists is the order in which the schematic builder will place the blocks. It will iterate over all the lists in the list and place
all the blocks in the list at the same time. Then wait the `ticksPerIteration` before placing the next list in the list.
<br>
### Direction
BuildPattern takes a `Direction` as a constructor parameter which is an enum: `X_AXIS, Y_AXIS, Z_AXIS`. This can be used to determine which direction
the build pattern operates in aswell as some way to sort them. The `Direction` enum also has a handy dandy method for getting the relevant
component of a `BlockVector3`: `public int getBlockVectorComponent(BlocKVector3 blockVector)` returns `getX()` for the `X_AXIS` of the block vector and so on for the other components etc.
An example of using direction to sort a list of block vectors (based on their components)
```java
List<BlockVector3> toSort = new ArrayList(someBlockVectors);
toSort.sort(Comparator.comparingInt(bv -> direction.getBlockVectorComponent(bv));
```
Now this list is sorted based on its direction and components. If the direction was `Y_AXIS` for example, the list would be sorted based on each blockvector's `getY()` method

Here's an example. Say we want to make a build pattern that places **3** random blocks at a time. (phobos was written in kotlin but this example will be in java)

```java
public class MyRandomBuildPattern extends BuildPattern {

    public MyRandomBuildPattern(Direction direction) {
      super(direction);
    }

    // Notice we get a clipboard instance here. This might not be useful for most buildpatterns but it is there none the less
    @Override
    public List<List<BlockVector3>> sort(List<BlockVector3> toSort, Clipboard clipboard) {
        // define lists that we can remove from and return
        List<List<BlockVector3>> toReturn = new ArrayList();
        List<BlockVector3> iterable = new ArrayList(toSort);
        
        // keep going until none left
        while (!iterable.isEmpty()) {
        
            // the list of blocks we will add to our list of lists
            List<BlockVector3> toAdd = new ArrayList();
            
            // get 3 random block vectors (locations)
            for (int i = 0; i < 3; i++) {
                // if at any time during getting these 3 the iterable list becomes empty (no more), stop and break
                if (iterable.isEmpty) break;
                
                // The random loc chosen and removing that from the iterable list as well as adding to the list will we will add to the list of lists
                BlockVector3 randomLoc = iterable.get(someRandomInstance.nextInt(iterable.size());
                iterable.remvoe(randomLoc);
                toAdd.add(randomLoc);
            }
            
            // finally adding the list of blocks (3 random blockvectors) to the final list to return
            toReturn.add(toAdd);
        }

        return toReturn;
    }
}
```
There are more examples in the default buildpatterns of phobos. Though they are written in kotlin, they can still be useful for seeing logic
<br>
**NOTE:** Some of these may involve heavy computions and iterations but that's power of phobos; all this sorting and arranging is done **async**

## BlockPlaceEffect
Block place effects are simply just a cosmetic addition to phobos in order to allow for effects and other logic to run when a block is place
Simply extend `BlockPlaceEffect` and define the logic in the overridden method.
<br>
Here's an example that plays green sparkles when a block is placed
```java
public class GreenSparklePlaceEffect extends BlockPlaceEffect {
    @Override
    public void onPlace(Location location) {
        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location.clone().add(0.5, 0.5, 0.5)
          .add(someRandomInstance.nextDouble(-1.2, 1.2), someRandomInstance.nextDouble(-1.2, 1.2), someRandomInstance.nextDouble(-1.2, 1.2));
    }
}
```
**NOTE:** Block place effect will run for every *block* placed, not just on every iteration of blocks being placed.
*Also*, block place effects are not run async (obviously)

