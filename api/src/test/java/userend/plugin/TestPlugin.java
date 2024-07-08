package userend.plugin;

import be.raft.creatio.api.Server;
import be.raft.creatio.api.ServerPlugin;
import jakarta.inject.Inject;
import org.slf4j.Logger;

public class TestPlugin extends ServerPlugin {
    public static Logger LOGGER;

    private final Server server;

    @Inject
    public TestPlugin(Server server) {
        LOGGER = this.pluginLogger();
        this.server = server;

        LOGGER.info("TestPlugin initialized.");
    }
}
