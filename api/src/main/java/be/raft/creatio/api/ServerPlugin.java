package be.raft.creatio.api;

import be.raft.creatio.api.plugin.Plugin;
import be.raft.creatio.api.plugin.PluginClassLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.InputStream;
import java.util.List;

public abstract class ServerPlugin implements Plugin {
    protected PluginClassLoader classloader;

    protected ServerPlugin() {
        if (!(this.getClass().getClassLoader() instanceof PluginClassLoader loader))
            throw new IllegalCallerException("Server Plugin must be loaded using a PluginClassLoader");

        this.classloader = loader;
    }

    protected Logger pluginLogger() {
        return this.classloader.pluginLogger();
    }

    @Override
    public @NotNull String id() {
        return this.classloader.meta().id();
    }

    @Override
    public @NotNull String name() {
        return this.classloader.meta().name();
    }

    @Override
    public @NotNull String version() {
        return this.classloader.meta().version();
    }

    @Override
    public @Nullable String description() {
        return this.classloader.meta().description();
    }

    @Override
    public @Nullable String credits() {
        return this.classloader.meta().credits();
    }

    @Override
    public @NotNull List<String> authors() {
        return this.classloader.meta().authors();
    }
}
