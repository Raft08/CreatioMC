package be.raft.creatio.api;

import be.raft.creatio.api.settings.ServerSettings;
import org.jetbrains.annotations.NotNull;

/**
 * Server API Layer
 */
public interface Server {

    /**
     * Retrieve the server settings.
     *
     * @return server settings
     */
    @NotNull
    ServerSettings settings();
}
