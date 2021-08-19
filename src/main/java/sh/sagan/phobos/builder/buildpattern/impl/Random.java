package sh.sagan.phobos.builder.buildpattern.impl;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.bukkit.Location;
import sh.sagan.phobos.Util;
import sh.sagan.phobos.builder.buildpattern.BuildPattern;

import java.util.Collections;
import java.util.List;

public class Random extends BuildPattern {

    @Override
    public List<List<Location>> arrange(List<Location> toArrange, Clipboard clipboard) {
        Collections.shuffle(toArrange);
        if (super.blocksPerIteration == Integer.MAX_VALUE) {
            return Collections.singletonList(toArrange);
        } else {
            return Util.split(toArrange, super.blocksPerIteration);
        }
    }
}
