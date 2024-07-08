package be.raft.creatio.plugin.loader;

import be.raft.creatio.api.Server;
import be.raft.creatio.api.plugin.PluginManager;
import com.google.inject.AbstractModule;

public class PluginInitializationModule extends AbstractModule {
    private final Server server;
    private final PluginManager pluginManager;

    public PluginInitializationModule(Server server, PluginManager pluginManager) {
        this.server = server;
        this.pluginManager = pluginManager;
    }

    @Override
    protected void configure() {
        this.bind(Server.class).toInstance(this.server);
        this.bind(PluginManager.class).toInstance(this.pluginManager);
    }
}
