package sh.sagan.phobos.builder.buildpattern.impl;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.bukkit.Location;
import sh.sagan.phobos.Util;
import sh.sagan.phobos.builder.buildpattern.BuildPattern;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class EdgeToEdgeLayered extends BuildPattern {

    private final Direction direction;

    public EdgeToEdgeLayered(Direction direction) {
        this.direction = direction;
    }

    public EdgeToEdgeLayered() {
        this(Direction.Y_POSITIVE);
    }

    @Override
    public List<List<Location>> arrange(List<Location> toArrange, Clipboard clipboard) {
        List<Location> sorted = new ArrayList<>(toArrange);
        Function<Location, Integer> splitOn;
        switch (direction) {
            case X_POSITIVE -> {
                sorted.sort(Comparator.comparingInt(Location::getBlockX));
                splitOn = Location::getBlockX;
            }
            case Z_POSITIVE -> {
                sorted.sort(Comparator.comparingInt(Location::getBlockZ));
                splitOn = Location::getBlockZ;
            }
            case Y_NEGATIVE -> {
                sorted.sort(Comparator.comparingInt(Location::getBlockY).reversed());
                splitOn = Location::getBlockY;
            }
            case X_NEGATIVE -> {
                sorted.sort(Comparator.comparingInt(Location::getBlockX).reversed());
                splitOn = Location::getBlockX;
            }
            case Z_NEGATIVE -> {
                sorted.sort(Comparator.comparingInt(Location::getBlockZ).reversed());
                splitOn = Location::getBlockZ;
            }
            // handles default and Y_POSITIVE case
            default -> {
                sorted.sort(Comparator.comparingInt(Location::getBlockY));
                splitOn = Location::getBlockY;
            }
        }

        return Util.splitOnNewValue(sorted, splitOn);
    }

    public enum Direction {
        Y_POSITIVE, X_POSITIVE, Z_POSITIVE, Y_NEGATIVE, X_NEGATIVE, Z_NEGATIVE
    }
}
