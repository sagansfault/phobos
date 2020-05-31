package me.sagan.phobos

import me.sagan.phobos.builder.SchemBuilder
import me.sagan.phobos.builder.buildpattern.impl.SimpleSpiralBuildPattern
import me.sagan.phobos.builder.effect.impl.GreenSparklePlaceEffect
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Phobos : JavaPlugin(), Listener {

    companion object {
        var instance: Phobos? = null
    }

    override fun onEnable() {

        instance = this

        // Plugin startup logic
        Bukkit.getServer().pluginManager.registerEvents(this, this)
    }

    override fun onDisable() {

    }

    @EventHandler
    fun interact(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK || !event.player.isOp || event.hand != EquipmentSlot.HAND || event.item != null) return

        val block = event.clickedBlock ?: return
        val loc = block.location.clone().add(0.0, 1.0, 0.0)
        val file = File("D:\\Games\\MC Servers\\Testing\\plugins\\WorldEdit\\schematics\\house.schem")

        SchemBuilder(file)
                .buildPattern(SimpleSpiralBuildPattern())
                .ticksBetweenIterations(2)
                .ignoreAir(true)
                .blockPlaceEffect(GreenSparklePlaceEffect())
                .buildAt(loc)
    }
}