package sh.sagan.phobos.builder.buildpattern.impl;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.bukkit.Location;
import sh.sagan.phobos.builder.buildpattern.BuildPattern;

import java.util.List;

public class DefaultBuildPattern implements BuildPattern {

    @Override
    public List<Location> arrange(List<Location> toArrange, Clipboard clipboard) {
        return toArrange;
    }
}
