package me.sagan.phobos.builder.effect.impl

import me.sagan.phobos.builder.effect.BlockPlaceEffect
import org.bukkit.Location
import org.bukkit.Particle
import kotlin.random.Random

class GreenSparklePlaceEffect : BlockPlaceEffect() {

    override fun onPlace(location: Location) {
        for (i in 0..2) {
            location.world?.spawnParticle(Particle.VILLAGER_HAPPY, location.clone().add(0.5, 0.5, 0.5).add(
                    Random.nextDouble(-1.2, 1.2),
                    Random.nextDouble(-1.2, 1.2),
                    Random.nextDouble(-1.2, 1.2)), 1)
        }
    }
}