package sh.sagan.phobos.builder.palceeffect.impl;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.bukkit.Location;
import org.bukkit.Particle;
import sh.sagan.phobos.builder.palceeffect.PlaceEffect;

import java.util.concurrent.ThreadLocalRandom;

public class GreenSparkleEffect implements PlaceEffect {

    @Override
    public void onPlace(Location blockLocation, Clipboard clipboard) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        blockLocation.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, blockLocation.clone().add(
                rand.nextDouble(-1, 1),
                rand.nextDouble(-1, 1),
                rand.nextDouble(-1, 1)
        ), 1);
    }
}