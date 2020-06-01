package me.sagan.phobos

import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class Phobos : JavaPlugin(), Listener {

    companion object {
        var instance: Phobos? = null
    }

    override fun onEnable() {
        instance = this
    }

    override fun onDisable() {
    }
}