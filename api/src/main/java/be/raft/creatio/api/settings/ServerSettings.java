package be.raft.creatio.api.settings;

import org.jetbrains.annotations.NotNull;

/**
 * Holds all the server settings.
 * Most of the options showed here are immutable, but some can be changed.
 * Remember that the settings changed here won't be saved after server restart.
 */
public interface ServerSettings {

    /**
     * Retrieve the current difficulty of the server.
     *
     * @return server difficulty.
     */
    @NotNull
    Difficulty difficulty();

    /**
     * Change the current server difficulty.
     *
     * @param difficulty new difficulty to set.
     * @throws NullPointerException if {@code difficulty} is null.
     */
    void difficulty(@NotNull Difficulty difficulty);
}
