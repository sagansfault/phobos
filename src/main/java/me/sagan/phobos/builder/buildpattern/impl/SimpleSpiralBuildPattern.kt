package me.sagan.phobos.builder.buildpattern.impl

import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.math.Vector3
import me.sagan.phobos.builder.buildpattern.BuildPattern

class SimpleSpiralBuildPattern() : BuildPattern(Direction.Y_AXIS) {

    override fun sort(toSort: List<BlockVector3>, clipboard: Clipboard): List<List<BlockVector3>> {
        val toReturn: MutableList<MutableList<BlockVector3>> = mutableListOf()
        val iterable = ArrayList(toSort).sortedBy { it.blockY }.toMutableList()

        val centre: Vector3 = clipboard.region.center

        while (iterable.isNotEmpty()) {
            val quadOne = iterable.asSequence().filter { it.y == iterable[0].y && it.x >= centre.x && it.z >= centre.z }.toMutableList()
            iterable.removeAll(quadOne)

            val quadTwo = iterable.asSequence().filter { it.y == iterable[0].y && it.x >= centre.x && it.z < centre.z }.toMutableList()
            iterable.removeAll(quadTwo)

            val quadThree = iterable.asSequence().filter { it.y == iterable[0].y && it.x < centre.x && it.z < centre.z }.toMutableList()
            iterable.removeAll(quadThree)

            val quadFour = iterable.asSequence().filter { it.y == iterable[0].y && it.x < centre.x && it.z >= centre.z }.toMutableList()
            iterable.removeAll(quadFour)

            toReturn.add(quadOne)
            toReturn.add(quadTwo)
            toReturn.add(quadThree)
            toReturn.add(quadFour)
        }

        return toReturn
    }
}