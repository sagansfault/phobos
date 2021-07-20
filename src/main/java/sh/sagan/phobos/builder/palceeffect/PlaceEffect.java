package sh.sagan.phobos.builder.palceeffect;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.bukkit.Location;

@FunctionalInterface
public interface PlaceEffect {

    /**
     * A function to be run whenever a single block is placed. This effect can literally be anything but should probably
     * be a sound/particle effect
     *
     * @param blockLocation The location this block will be placed at
     * @param clipboard The clipboard for this schematic builder
     */
    void onPlace(Location blockLocation, Clipboard clipboard);
}
