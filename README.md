# phobos 🪐
A stylistic asynchronously processed mine craft world edit schematic builder, providing cool ways to build schematics with effects and pattern all while the sorting and processing happens on another thread

## What is phobos?
Phobos aims to add funtionality for developers to build world edit shematics (`.schem` files) with some style in their worlds. The following are some
basic examples of some of the build patterns that are currently in phobos as of `v2.0.0`

| <img src="https://media.giphy.com/media/jUi2OeBzildNeoUkwJ/giphy.gif" width="265">| <img src="https://media.giphy.com/media/Q86SsAIlpWxI1pMSzf/giphy.gif" width="265"> | <img src="https://media.giphy.com/media/TdpNk4BV3MUO5HBqvS/giphy.gif" width="265"> |
|:---:|:---:|:---:|
| <img src="https://media.giphy.com/media/l3yu328UNbwjAMfblq/giphy.gif" width="265"> | <img src="https://media.giphy.com/media/gLit8fDkNyP9HMr6Zu/giphy.gif" width="265"> | <img src="https://media.giphy.com/media/Q86SsAIlpWxI1pMSzf/giphy.gif" width="265"> |

## Installing
Shade it into your project or provide it at runtime as a plugin ;)
<br>
Phobos, at the time of writing this, uses **WorldEdit** `v7.2.5`
```xml
<dependency>
    <groupId>sh.sagan</groupId>
    <artifactId>phobos</artifactId>
    <version>2.0.0</version> <!-- at the time of writing, current version is 2.0.0. Check the pom for the latest version -->
    <!-- <scope>provided</scope> if this will be used as a plugin -->
</dependency>
```

## Contributing
Contributions are always welcome via PRs. If it's a large one, open an issue and discuss/explain
what you're doing and why.

## Usage
To build a schematic:
```java
SchemBuilder.from(
        myPluginInstance, 
        Path.of("C:\\path\\to\\schematic\\house.schem").toFile()
        ).ifPresentOrElse(schemBuilder -> {
            schemBuilder.buildAt(someLocation);
        }, () -> {
            System.out.println("Oh no! Path invalid or loading error :(");
        });
```

Easy right? However, this doesn't do anything cool that phobos was made for. Maybe something like this:

```java
SchemBuilder.from(
        myPluginInstance, 
        Path.of("C:\\path\\to\\schematic\\house.schem").toFile()
        ).ifPresentOrElse(schemBuilder -> {
            schemBuilder
                .buildPattern(new RandomBuildPattern())
                .placeEffect(new GreenSparkleEffect())
                .ticksBetweenIterations(5)
                .blocksPerIteration(2)
                .ignoreAir(true)
                .buildAt(someLocation);
        }, () -> {
            System.out.println("Oh no! Path invalid or loading error :(");
        });
```
This will build the same schematic but with the following characteristics accordingly:
1. Will place blocks randomly (the build pattern)
2. Green sparkles will be played at a location around the block (the place effect)
3. Will wait 5 ticks before placing a set of blocks (ticks between iterations)
4. Will place 2 blocks at a time (blocks per iteration)
5. Will ignore air blocks in the clipboard and not place them (ignore air)

The power is yours now. You can make custom block place effect and build pattern classes implementing
`PlaceEffect` and `BuildPattern` respectively. Look at existing examples in phobos for an idea of what these do

***[NOTE]***: You also get access to the `Clipboard` loaded into the schematic builder via
`SchemBuilder#.getClipboard()`. With this you can apply rotations and transformations to the
clipboard before it is built.

These are functional interfaces so you can also inline them:
```java
SchemBuilder.from(
        myPluginInstance, 
        Path.of("C:\\path\\to\\schematic\\house.schem").toFile()
        ).ifPresentOrElse(schemBuilder -> {
            schemBuilder
                .buildPattern((location, clipboard) -> {/* whatever I want */})
                .placeEffect((location, clipboard) -> {/* whatever I want */})
                .buildAt(someLocation);
        }, () -> {
            System.out.println("Oh no! Path invalid or loading error :(");
        });
```
(All sorting/arranging done in these build patterns is done async)
## Have Fun!