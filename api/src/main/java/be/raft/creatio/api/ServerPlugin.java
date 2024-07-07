package be.raft.creatio.api;

import be.raft.creatio.api.plugin.Plugin;
import be.raft.creatio.api.plugin.PluginClassLoader;

public abstract class ServerPlugin implements Plugin {
    protected PluginClassLoader classloader;

    protected ServerPlugin() {
        if (!(this.getClass().getClassLoader() instanceof PluginClassLoader loader))
            throw new IllegalCallerException("Server Plugin must be loaded using a PluginClassLoader");

        this.classloader = loader;
    }


}
