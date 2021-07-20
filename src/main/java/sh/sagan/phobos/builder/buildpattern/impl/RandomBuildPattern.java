package sh.sagan.phobos.builder.buildpattern.impl;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.bukkit.Location;
import sh.sagan.phobos.builder.buildpattern.BuildPattern;

import java.util.Collections;
import java.util.List;

public class RandomBuildPattern implements BuildPattern {

    @Override
    public List<Location> arrange(List<Location> toArrange, Clipboard clipboard) {
        Collections.shuffle(toArrange);
        return toArrange;
    }
}
