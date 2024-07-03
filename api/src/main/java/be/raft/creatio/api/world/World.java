package be.raft.creatio.api.world;

import be.raft.creatio.api.registry.RegistryKey;

/**
 * Represents a world or a dimension running on the server.
 * <p>
 * Creatio's World internally uses Minestom's Instance system.
 * <b>Warning: </b> Remember that world are multithreaded!
 */
public interface World {

    /**
     * Retrieve the unique registry key of the world.
     *
     * @return world registry key.
     */
    RegistryKey key();


}
