package be.raft.creatio.server.world;

import be.raft.creatio.api.registry.RegistryKey;
import be.raft.creatio.api.world.World;
import net.minestom.server.instance.InstanceContainer;

public class InstanceWorld implements World {
    private final RegistryKey key;
    private final InstanceContainer instance;



    public InstanceWorld(RegistryKey key, InstanceContainer instance) {
        this.key = key;
        this.instance = instance;


    }

    @Override
    public RegistryKey key() {
        return this.key;
    }

    public InstanceContainer instance() {
        return this.instance;
    }
}
