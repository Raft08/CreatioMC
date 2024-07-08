package be.raft.creatio.bootstrap;

import be.raft.creatio.CreatioServer;
import fr.atlasworld.common.logging.LogUtils;
import org.slf4j.Logger;

public class Main {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static CreatioServer server;

    public static void main(String[] args) {
        LaunchArgs.parse(args);

        LOGGER.info("Starting CreatioMC {} (Commit: {} | Branch: {})", Version.getInstance().getVersion(), Version.getInstance().getCommit().substring(0, 7), Version.getInstance().getBranch());

        server = new CreatioServer();

        server.initialize();
        server.start();
    }

    public static void shutdown() {
        server.stop(false);

        System.exit(0);
    }

    public static void crash(String message, boolean causedByUser) {
        if (!causedByUser)
            LOGGER.error("FATAL: UNHANDLED EXCEPTION, Please report this to the developers!");

        LOGGER.error("FATAL: {}", message);

        try {
            server.stop(true);
        } catch (Throwable e) {
            LOGGER.warn("Abrupt stopping caused some internal errors to occur.");
            LOGGER.trace("Failed to gracefully stopped the program: ", e);
        }

        System.exit(-1);
    }

    public static void crash(String message, Throwable cause, boolean causedByUser) {
        if (!causedByUser)
            LOGGER.error("FATAL: UNHANDLED EXCEPTION, Please report this to the developers!");

        LOGGER.error("FATAL: {}", message, cause);

        try {
            server.stop(true);
        } catch (Throwable e) {
            LOGGER.warn("Abrupt stopping caused some internal errors to occur.");
            LOGGER.trace("Failed to gracefully stopped the program: ", e);
        }

        System.exit(-1);
    }
}
