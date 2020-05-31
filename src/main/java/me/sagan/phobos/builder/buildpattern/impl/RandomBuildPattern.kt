package me.sagan.phobos.builder.buildpattern.impl

import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.math.BlockVector3
import me.sagan.phobos.builder.buildpattern.BuildPattern
import kotlin.math.max
import kotlin.random.Random

class RandomBuildPattern(private val blocksPerIteration: Int = 1) : BuildPattern(Direction.X_AXIS) {

    override fun sort(toSort: List<BlockVector3>, clipboard: Clipboard): List<List<BlockVector3>> {
        val toReturn: MutableList<MutableList<BlockVector3>> = mutableListOf()

        val iterable = ArrayList(toSort)

        val iterationActual = max(1, blocksPerIteration)

        outer@ while(iterable.isNotEmpty()) {
            val toAdd: MutableList<BlockVector3> = mutableListOf()

            for (i in 0 until iterationActual) {
                if (iterable.isEmpty()) break@outer

                toAdd.add(iterable.removeAt(Random.nextInt(iterable.size)))
            }
        }

        return toReturn
    }
}