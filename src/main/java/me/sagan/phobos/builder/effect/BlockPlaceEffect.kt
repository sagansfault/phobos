package me.sagan.phobos.builder.effect

import org.bukkit.Location

abstract class BlockPlaceEffect {

    /**
     * Called when a block is placed.
     *
     * @param location The location the block was placed in
     */
    abstract fun onPlace(location: Location)
}