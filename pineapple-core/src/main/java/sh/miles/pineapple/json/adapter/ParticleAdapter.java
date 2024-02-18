package sh.miles.pineapple.json.adapter;

import org.bukkit.Particle;
import sh.miles.pineapple.json.adapter.base.BukkitRegistryJsonAdapter;

public class ParticleAdapter implements BukkitRegistryJsonAdapter<Particle> {
    @Override
    public Class<Particle> getAdapterType() {
        return Particle.class;
    }

    @Override
    public boolean isHierarchy() {
        return true;
    }
}
