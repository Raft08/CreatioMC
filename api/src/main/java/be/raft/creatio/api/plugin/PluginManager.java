package be.raft.creatio.api.plugin;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Plugin manager of the server.
 */
public interface PluginManager {

    /**
     * Checks whether a plugin is loaded or not.
     *
     * @param identifier identifier of the plugin to look for.
     *
     * @return true if the plugin is loaded, false otherwise.
     *
     * @throws NullPointerException if {@code identifier} is {@code null}.
     * @throws IllegalArgumentException if {@code identifier} is an invalid plugin identifier.
     */
    boolean pluginLoaded(@NotNull String identifier);

    /**
     * Retrieve a plugin from its identifier.
     *
     * @param identifier identifier of the plugin to look for.
     *
     * @return Optional possibly containing the instance of the plugin.
     *
     * @throws NullPointerException if {@code identifier} is {@code null}.
     * @throws IllegalArgumentException if {@code identifier} is an invalid plugin identifier.
     */
    Optional<Plugin> getPlugin(@NotNull String identifier);

    /**
     * Retrieve the list of all loaded plugins on the server.
     *
     * @return a list of all loaded plugins on the server.
     */
    List<Plugin> loadedPlugins();
}
