package sh.sagan.phobos.builder.placeeffect.impl;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.bukkit.Location;
import org.bukkit.Particle;
import sh.sagan.phobos.builder.placeeffect.PlaceEffect;

import java.util.concurrent.ThreadLocalRandom;

public class GreenSparkleEffect implements PlaceEffect {

    private final ThreadLocalRandom rand = ThreadLocalRandom.current();

    @Override
    public void onPlace(Location blockLocation, Clipboard clipboard) {
        blockLocation.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, blockLocation.clone().add(
                rand.nextDouble(-1, 1),
                rand.nextDouble(-1, 1),
                rand.nextDouble(-1, 1)
        ), 1);
    }
}
