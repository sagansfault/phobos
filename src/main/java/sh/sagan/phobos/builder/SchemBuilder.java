package sh.sagan.phobos.builder;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import sh.sagan.phobos.builder.buildpattern.BuildPattern;
import sh.sagan.phobos.builder.palceeffect.PlaceEffect;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SchemBuilder {

    private final Clipboard clipboard;
    private final JavaPlugin plugin;

    private int ticksBetweenIterations = 1;
    private boolean ignoreAir = false;
    private BuildPattern buildPattern = (toArrange, clipboard) -> toArrange;
    private PlaceEffect placeEffect = (blockLocation, clipboard) -> {};
    private int blocksPerIteration = Integer.MAX_VALUE;

    private SchemBuilder(JavaPlugin plugin, Clipboard clipboard) {
        this.plugin = plugin;
        this.clipboard = clipboard;
    }

    /**
     * Attempts to construct a new builder instance from the given main plugin and the schematic file. If a builder
     * could not be constructed (invalid file, loading issues), an empty optional is returned.
     *
     * @param plugin The main plugin
     * @param file The schematic file. ({@code Path.of("C:\\schematics\\house.schem").toFile()})
     * @return An optional containing the constructed SchemBuilder is successful, empty otherwise.
     */
    public static Optional<SchemBuilder> from(JavaPlugin plugin, File file) {
        Clipboard clipboard;
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        if (format == null) {
            return Optional.empty();
        }

        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();
        } catch (IOException e) {
            return Optional.empty();
        }

        return Optional.of(new SchemBuilder(plugin, clipboard));
    }

    /**
     * Sets how many ticks long the period between iterations should be.
     *
     * @param ticks The ticks to wait between iterations
     * @return This builder instance.
     */
    public SchemBuilder ticksBetweenIterations(int ticks) {
        this.ticksBetweenIterations = Math.min(Math.max(1, ticks), 2400);
        return this;
    }

    /**
     * Specifies whether this builder should ignore air when building. When true, this will not alter blocks in the
     * place of air.
     *
     * @param ignoreAir Whether to ignore air blocks when building
     * @return This builder instance.
     */
    public SchemBuilder ignoreAir(boolean ignoreAir) {
        this.ignoreAir = ignoreAir;
        return this;
    }

    /**
     * Sets the build pattern for this builder instance.
     *
     * @param buildPattern The build pattern to use for this builder
     * @return This builder instance.
     */
    public SchemBuilder buildPattern(BuildPattern buildPattern) {
        if (buildPattern != null) {
            this.buildPattern = buildPattern;
        }
        return this;
    }

    /**
     * Sets the place effect for this builder. This effect will be run every time a *single* block is placed.
     *
     * @param placeEffect The place effect to use
     * @return This builder instance.
     */
    public SchemBuilder placeEffect(PlaceEffect placeEffect) {
        if (placeEffect != null) {
            this.placeEffect = placeEffect;
        }
        return this;
    }

    /**
     * Sets the blocks per iteration of this builder. Default is {@code Integer.MAX_VALUE} which will paste all the
     * blocks in one iteration (at once)
     *
     * @param blocksPerIteration The blocks to place per iteration
     * @return This builder instance
     */
    public SchemBuilder blocksPerIteration(int blocksPerIteration) {
        this.blocksPerIteration = Math.max(1, blocksPerIteration);
        return this;
    }

    /**
     * Gets the clipboard object used for this schematic. Use this to alter the clipboard before running the builder
     *
     * @return The clipboard for this loaded schematic.
     */
    public Clipboard getClipboard() {
        return clipboard;
    }

    /**
     * Executes this schem builder, building the schematic at the specified location with all the prior specifications.
     *
     * @param location The location/origin to build this schematic at
     */
    public void buildAt(Location location) {
        if (location.getWorld() == null) {
            return;
        }

        BlockVector3 buildAt = BukkitAdapter.asBlockVector(location);

        new BukkitRunnable(){
            @Override
            public void run() {
                List<Location> toArrange = new ArrayList<>();
                if (ignoreAir) {
                    clipboard.getRegion().forEach(bv -> {
                        if (!clipboard.getBlock(bv).getBlockType().getMaterial().isAir()) {
                            toArrange.add(BukkitAdapter.adapt(location.getWorld(), bv));
                        }
                    });
                } else {
                    clipboard.getRegion().forEach(bv -> toArrange.add(BukkitAdapter.adapt(location.getWorld(), bv)));
                }

                List<BlockVector3> arranged = buildPattern.arrange(toArrange, clipboard).stream()
                        .map(BukkitAdapter::asBlockVector)
                        .collect(Collectors.toList());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (arranged.isEmpty()) {
                            this.cancel();
                            return;
                        }

                        List<BlockVector3> set = new ArrayList<>();
                        if (blocksPerIteration > arranged.size()) {
                            set = new ArrayList<>(arranged);
                            arranged.clear();
                        } else {
                            for (int i = 0; i < blocksPerIteration; i++) {
                                BlockVector3 found;
                                try {
                                    found = arranged.remove(0);
                                } catch (IndexOutOfBoundsException | UnsupportedOperationException exception) {
                                    break;
                                }
                                set.add(found);
                            }
                        }

                        Map<BlockVector3, BlockVector3> toPlace = new HashMap<>();
                        set.forEach(blockVector3 -> {
                            BlockVector3 target = blockVector3.add(buildAt.subtract(clipboard.getOrigin()));
                            toPlace.put(blockVector3, target);
                        });

                        placeBlocks(toPlace, BukkitAdapter.adapt(location.getWorld()));
                    }
                }.runTaskTimer(plugin, 0, ticksBetweenIterations);
            }
        }.runTaskAsynchronously(plugin);
    }

    private void placeBlocks(Map<BlockVector3, BlockVector3> clipboardBlockLocToTarget, World world) {
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
            clipboardBlockLocToTarget.forEach((clipboardLoc, target) -> {
                try {
                    editSession.setBlock(target, this.clipboard.getBlock(clipboardLoc));
                    placeEffect.onPlace(BukkitAdapter.adapt(BukkitAdapter.adapt(world), target), this.clipboard);
                } catch (MaxChangedBlocksException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
