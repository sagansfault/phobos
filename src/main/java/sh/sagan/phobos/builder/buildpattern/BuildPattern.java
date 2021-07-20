package sh.sagan.phobos.builder.buildpattern;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.bukkit.Location;

import java.util.List;

@FunctionalInterface
public interface BuildPattern {

    /**
     * Arranges a set of unordered block locations into an order in which they should be placed. For examples, view the
     * default build patters provided by Phobos which implement this interface
     *
     * @param toArrange The list of unordered blocks to arrange/order into a list to return
     * @param clipboard The clipboard from which these blocks came from. This may not be required by all build patterns.
     * @return A list of arranged/ordered block locations to build this schematic in
     */
    List<Location> arrange(List<Location> toArrange, Clipboard clipboard);
}
