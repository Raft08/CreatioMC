package be.raft.creatio.plugin;

import be.raft.creatio.CreatioServer;
import be.raft.creatio.api.plugin.Plugin;
import be.raft.creatio.api.plugin.PluginManager;
import be.raft.creatio.plugin.source.PluginDelegate;
import be.raft.creatio.plugin.source.PluginProvider;
import fr.atlasworld.common.logging.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.*;

public class ServerPluginManager implements PluginManager {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<String, Plugin> loadedPlugins;
    private final List<Path> detectedPlugins;

    private final CreatioServer server;

    public ServerPluginManager(CreatioServer server) {
        this.server = server;

        this.loadedPlugins = new HashMap<>();
        this.detectedPlugins = new ArrayList<>();

        this.detectPlugins();
    }

    private void detectPlugins() {
        PluginDelegate delegate = new PluginDelegate(this.detectedPlugins);
        PluginProvider.PROVIDERS.forEach(provider -> provider.registerCandidates(delegate)); // Detecting plugins

        LOGGER.info("Found {} plugins candidates.", loadedPlugins.size());
    }

    @Override
    public boolean pluginLoaded(@NotNull String identifier) {
        return false;
    }

    @Override
    public Optional<Plugin> getPlugin(@NotNull String identifier) {
        return Optional.empty();
    }

    @Override
    public List<Plugin> loadedPlugins() {
        return List.of();
    }
}
