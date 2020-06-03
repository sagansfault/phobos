package me.sagan.phobos.builder

import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.extent.clipboard.Clipboard
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.math.BlockVector3
import me.sagan.phobos.Phobos
import me.sagan.phobos.builder.buildpattern.BuildPattern
import me.sagan.phobos.builder.buildpattern.impl.DefaultBuildPattern
import me.sagan.phobos.builder.effect.BlockPlaceEffect
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.io.FileInputStream

class SchemBuilder(file: File) {

    private var clipboard: Clipboard? = null
    private var ticksBetweenIterations: Long = 2
    private var ignoreAir: Boolean = true
    private var buildPattern: BuildPattern = DefaultBuildPattern()
    private var blockPlaceEffect: BlockPlaceEffect? = null

    init {
        ClipboardFormats.findByFile(file)!!.getReader(FileInputStream(file)).use {
            this.clipboard = it!!.read()
        }
    }

    fun ticksBetweenIterations(ticks: Long): SchemBuilder {
        this.ticksBetweenIterations = ticks
        return this
    }

    fun ignoreAir(ignore: Boolean): SchemBuilder {
        this.ignoreAir = ignore
        return this
    }

    fun buildPattern(buildPattern: BuildPattern): SchemBuilder {
        this.buildPattern = buildPattern
        return this
    }

    fun blockPlaceEffect(blockPlaceEffect: BlockPlaceEffect): SchemBuilder {
        this.blockPlaceEffect = blockPlaceEffect
        return this
    }

    fun buildAt(origin: Location) {

        object : BukkitRunnable() {
            override fun run() {

                // start off by running heavy computations async
                val clipboardActual = clipboard ?: return

                val toSort: MutableList<BlockVector3> =
                        if (ignoreAir) clipboardActual.region.filter { !clipboardActual.getBlock(it).blockType.material.isAir }.toMutableList()
                        else clipboardActual.region.toMutableList()

                val sorted: List<List<BlockVector3>> = buildPattern.sort(toSort, clipboardActual)

                // hop back on sync to place the blocks
                object : BukkitRunnable() {
                    var i: Int = 0
                    override fun run() {
                        if (i >= sorted.size) {
                            this.cancel()
                            return
                        }

                        val toPlace: MutableMap<BlockVector3, BlockVector3> = mutableMapOf()
                        for (blockVector3 in sorted[i]) {
                            val actualLoc: BlockVector3 = blockVector3.add(BukkitAdapter.asBlockVector(origin).subtract(clipboardActual.origin))
                            toPlace[blockVector3] = actualLoc

                            blockPlaceEffect?.onPlace(Location(origin.world, actualLoc.x.toDouble(), actualLoc.y.toDouble(), actualLoc.z.toDouble()))
                        }

                        placeBlocks(toPlace, origin.world!!)
                        i++
                    }
                }.runTaskTimer(Phobos.instance!!, 0, ticksBetweenIterations)
            }
        }.runTaskAsynchronously(Phobos.instance!!)
    }

    private fun placeBlocks(clipboardToActual: Map<BlockVector3, BlockVector3>, world: World) {
        val clipboardActual = this.clipboard ?: return
        WorldEdit.getInstance().editSessionFactory.getEditSession(BukkitAdapter.adapt(world), -1).use {
            for ((clipboardLoc: BlockVector3?, actual: BlockVector3?) in clipboardToActual) {
                it.setBlock(actual, clipboardActual.getBlock(clipboardLoc))
            }
        }
    }
}