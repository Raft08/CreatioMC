package be.raft.creatio.plugin;

import be.raft.creatio.CreatioServer;
import be.raft.creatio.api.ServerPlugin;
import be.raft.creatio.api.plugin.Plugin;
import be.raft.creatio.api.plugin.PluginManager;
import be.raft.creatio.plugin.loader.PluginInitializationModule;
import be.raft.creatio.plugin.loader.ServerPluginClassLoader;
import be.raft.creatio.plugin.source.PluginDelegate;
import be.raft.creatio.plugin.source.PluginProvider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import fr.atlasworld.common.logging.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.*;

public class ServerPluginManager implements PluginManager {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<String, Plugin> loadedPlugins;
    private final List<Path> detectedPlugins;

    private final PluginInitializationModule injectionModule;

    public ServerPluginManager(CreatioServer server) {
        this.loadedPlugins = new HashMap<>();
        this.detectedPlugins = new ArrayList<>();

        this.injectionModule = new PluginInitializationModule(server, this);

        this.detectPlugins();
    }

    private void detectPlugins() {
        PluginDelegate delegate = new PluginDelegate(this.detectedPlugins);
        PluginProvider.PROVIDERS.forEach(provider -> provider.registerCandidates(delegate)); // Detecting plugins

        LOGGER.info("Found {} plugins candidates.", this.detectedPlugins.size());
    }

    public void loadPlugins() {
        Injector injector = Guice.createInjector(this.injectionModule);

        this.detectedPlugins.forEach(candidate -> this.loadPlugin(candidate, injector));
    }

    @SuppressWarnings("unchecked")
    private void loadPlugin(Path file, Injector injector) {
        try {
            ServerPluginClassLoader loader = new ServerPluginClassLoader(file);

            if (this.loadedPlugins.containsKey(loader.meta().id()))
                throw new IllegalArgumentException("Plugin '" + loader.meta().id() + "' already loaded!");

            Class<?> foundMain = loader.loadClass(loader.meta().mainClass(), false, false, false);
            if (!ServerPlugin.class.isAssignableFrom(foundMain))
                throw new IllegalArgumentException("Main class must extend " + ServerPlugin.class.getSimpleName() + "!");

            Class<? extends ServerPlugin> moduleClass = (Class<? extends ServerPlugin>) foundMain;
            ServerPlugin module = injector.getInstance(moduleClass);

            if (module == null)
                throw new IllegalArgumentException("Injector did not return plugin!");

            LOGGER.info("Loaded plugin '{}'.", module.id());
            this.loadedPlugins.put(loader.meta().id(), module);
        } catch (MalformedURLException e) {
            LOGGER.error("Invalid plugin file: ", e);
        } catch (IllegalArgumentException | NullPointerException e) {
            LOGGER.error("Invalid plugin: {}", e.getMessage());
        } catch (ClassNotFoundException e) {
            LOGGER.error("Could not find main-class of '{}'!", file.getFileName());
        } catch (Throwable throwable) {
            LOGGER.error("Could not create plugin '{}':", file.getFileName(), throwable);
        }
    }

    @Override
    public boolean pluginLoaded(@NotNull String identifier) {
        return this.loadedPlugins.containsKey(identifier);
    }

    @Override
    public Optional<Plugin> getPlugin(@NotNull String identifier) {
        return Optional.ofNullable(this.loadedPlugins.get(identifier));
    }

    @Override
    public List<Plugin> loadedPlugins() {
        return List.copyOf(this.loadedPlugins.values());
    }
}
