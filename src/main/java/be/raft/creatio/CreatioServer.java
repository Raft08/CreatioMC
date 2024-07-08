package be.raft.creatio;

import be.raft.creatio.api.Server;
import be.raft.creatio.plugin.ServerPluginManager;
import be.raft.creatio.server.ServerConfiguration;
import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public class CreatioServer implements Server {
    private static final String SERVER_BRAND = "CreatioMC";

    private final ServerConfiguration configuration;
    private final ServerPluginManager pluginManager;

    private final MinecraftServer server;

    public CreatioServer() {
        this.configuration = new ServerConfiguration();
        this.pluginManager = new ServerPluginManager(this);

        this.server = MinecraftServer.init();
        MinecraftServer.setBrandName(SERVER_BRAND);
    }

    public void initialize() {
        this.pluginManager.loadPlugins();
    }

    public void start() {
        this.server.start("0.0.0.0", 7777);
    }

    public void stop(boolean interrupt) {
        if (this.server == null) // Server crashed before even initializing the actual server.
            MinecraftServer.stopCleanly();
    }

    @Override
    public @NotNull ServerConfiguration settings() {
        return this.configuration;
    }
}
