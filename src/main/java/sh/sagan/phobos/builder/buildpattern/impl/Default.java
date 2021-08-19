package sh.sagan.phobos.builder.buildpattern.impl;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.bukkit.Location;
import sh.sagan.phobos.Util;
import sh.sagan.phobos.builder.buildpattern.BuildPattern;

import java.util.Collections;
import java.util.List;

public class Default extends BuildPattern {

    @Override
    public List<List<Location>> arrange(List<Location> toArrange, Clipboard clipboard) {
        if (blocksPerIteration == Integer.MAX_VALUE) {
            return Collections.singletonList(toArrange);
        } else {
            return Util.split(toArrange, blocksPerIteration);
        }
    }
}
