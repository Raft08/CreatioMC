package be.raft.creatio.api.plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PluginMeta {

    /**
     * Retrieve the identifier of the plugin.
     *
     * @return the unique identifier of the plugin.
     */
    @NotNull
    String id();

    /**
     * Retrieve the name of the plugin.
     *
     * @return the name of the plugin.
     */
    @NotNull
    String name();
    /**
     * Retrieve the version of the plugin.
     *
     * @return the version of the plugin.
     */
    @NotNull
    String version();

    /**
     * Retrieve the description of the plugin.
     *
     * @return the description of the plugin, or {@code null} if none was provided.
     */
    @Nullable
    String description();

    /**
     * Retrieve the credits of the plugin.
     *
     * @return the credits of the plugin, or {@code null} if none was provided.
     */
    @Nullable
    String credits();

    /**
     * Retrieve the authors of the plugin.
     *
     * @return the authors of the plugin.
     *         If no authors we're provided this returns an empty list.
     */
    @NotNull
    List<String> authors();
}
