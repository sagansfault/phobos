package me.sagan.phobos.builder.effect.impl

import me.sagan.phobos.builder.effect.BlockPlaceEffect
import org.bukkit.Location
import org.bukkit.Particle
import kotlin.random.Random

class GreenSparklePlaceEffect : BlockPlaceEffect() {

    override fun onPlace(location: Location) {
        for (i in 0..3) {
            location.world?.spawnParticle(Particle.VILLAGER_HAPPY, location.clone().add(0.5, 0.5, 0.5).add(
                    Random.nextDouble(-1.3, 1.3),
                    Random.nextDouble(-1.3, 1.3),
                    Random.nextDouble(-1.3, 1.3)), 1)
        }
    }
}