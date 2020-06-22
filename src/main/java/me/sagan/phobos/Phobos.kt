package me.sagan.phobos

import org.bukkit.plugin.java.JavaPlugin

class Phobos : JavaPlugin() {

    companion object {
        lateinit var instance: Phobos
    }

    override fun onEnable() {
        instance = this
    }

    override fun onDisable() {
    }
}