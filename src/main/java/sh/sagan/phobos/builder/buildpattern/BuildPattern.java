package sh.sagan.phobos.builder.buildpattern;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.bukkit.Location;

import java.util.List;

public abstract class BuildPattern {

    /* The blocks to place per iteration. This may or may not be used by child implementations */
    protected final int blocksPerIteration;

    /**
     * Constructs a new BuildPattern.
     *
     * @param blocksPerIteration The blocks to place per each iteration. This may or may no do anything depending on
     *                           the child implementation.
     */
    public BuildPattern(int blocksPerIteration) {
        this.blocksPerIteration = Math.max(1, blocksPerIteration);
    }

    public BuildPattern() {
        this(Integer.MAX_VALUE);
    }

    /**
     * Arranges a set of unordered block locations into an order in which they should be placed. For examples, view the
     * default build patters provided by Phobos which implement this interface
     *
     * @param toArrange The list of unordered blocks to arrange/order into a list to return
     * @param clipboard The clipboard from which these blocks came from. This may not be required by all build patterns.
     * @return A list of arranged/ordered block locations to build this schematic in
     */
    public abstract List<List<Location>> arrange(List<Location> toArrange, Clipboard clipboard);
}
