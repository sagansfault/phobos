package me.sagan.phobos.builder.buildpattern

import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.math.BlockVector3

abstract class BuildPattern(private val dir: Direction) {

    /**
     * Sorts the general list of block vectors into iterations to which they will be placed with. (ie.) If you want to
     * paste 3 blocks at a time then you'd need to make x amount of lists each with 3 vectors in them
     *
     * @param toSort The raw list of vectors to sort
     * @param clipboard The clipboard
     * @return A sorted list of the block vectors to be placed per iteration
     */
    abstract fun sort(toSort: List<BlockVector3>, clipboard: Clipboard): List<List<BlockVector3>>

    enum class Direction {
        X_AXIS, Y_AXIS, Z_AXIS;

        fun getBlockVectorComponent(vector: BlockVector3): Int = when (this) {
            X_AXIS -> vector.blockX
            Y_AXIS -> vector.blockY
            Z_AXIS -> vector.blockZ
        }
    }
}