package me.sagan.phobos.builder.buildpattern.impl

import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.math.BlockVector3
import me.sagan.phobos.builder.buildpattern.BuildPattern


class DefaultBuildPattern() : BuildPattern(Direction.X_AXIS) {

    override fun sort(toSort: List<BlockVector3>, clipboard: Clipboard): List<List<BlockVector3>> {
        return listOf(toSort)
    }
}