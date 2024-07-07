package be.raft.creatio.api.plugin;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * Handles the loading of a plugin's classpath.
 */
public interface PluginClassLoader {

    /**
     * Retrieve the meta-data of the plugin getting loaded by this loader.
     *
     * @return plugin meta-data.
     */
    @NotNull
    PluginMeta meta();

    /**
     * Retrieve the plugin's logger.
     *
     * @return logger of the plugin.
     */
    @NotNull
    Logger pluginLogger();

    /**
     * Checks whether the plugin allow external plugins to use it's classpath.
     * <ul>
     * <li><b>True:</b> Other plugins will have access to this loader's classes.</li>
     * <li><b>False:</b> Other plugins won't have access to this loader's classes.</li>
     * </ul>
     * By default, external classpath usage will always be allowed.
     *
     * @return true if externals plugins are allowed to use this loader's classes, false otherwise.
     */
    boolean allowsExternalClasspathUsage();

    /**
     * Sets whether the plugin allow external plugins to use it's classpath.
     * <ul>
     * <li><b>True:</b> Other plugins will have access to this loader's classes.</li>
     * <li><b>False:</b> Other plugins won't have access to this loader's classes.</li>
     * </ul>
     * By default, external classpath usage will always be allowed.
     *
     * @param allow whether to allow externals plugins to use this loader's classpath.
     * @throws IllegalCallerException if the caller of this method is not loaded by this loader.
     */
    void allowExternalClasspathUsage(boolean allow);
}
