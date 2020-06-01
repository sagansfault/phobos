package me.sagan.phobos.builder.buildpattern.impl

import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.math.BlockVector3
import me.sagan.phobos.builder.buildpattern.BuildPattern

class ClosingBuildPattern(private val dir: Direction = Direction.X_AXIS, private val blocksPerIteration: Int = -1) : BuildPattern(dir) {

    override fun sort(toSort: List<BlockVector3>, clipboard: Clipboard): List<List<BlockVector3>> {
        val toReturn: MutableList<MutableList<BlockVector3>> = mutableListOf()

        val iterable = ArrayList(toSort).sortedBy { dir.getBlockVectorComponent(it) }.toMutableList()

        if (blocksPerIteration == -1) {
            while (iterable.isNotEmpty()) {

                val toAdd = iterable.asSequence().filter {
                    dir.getBlockVectorComponent(it) == dir.getBlockVectorComponent(iterable[iterable.size - 1]) ||
                            dir.getBlockVectorComponent(it) == dir.getBlockVectorComponent(iterable[0]) }.toMutableList()
                iterable.removeAll(toAdd)

                toReturn.add(toAdd)
            }
        } else {
            while (iterable.isNotEmpty()) {

                val toAdd = mutableListOf<BlockVector3>()

                for (i in 0 until blocksPerIteration) {
                    toAdd.add(iterable.removeAt(0))
                    if (iterable.isNotEmpty()) toAdd.add(iterable.removeAt(iterable.size - 1))

                    toReturn.add(toAdd)

                    if (iterable.isEmpty()) break
                }
            }
        }

        return toReturn
    }
}