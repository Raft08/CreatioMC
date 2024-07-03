package be.raft.creatio;

import be.raft.creatio.api.Server;
import be.raft.creatio.api.settings.ServerSettings;
import be.raft.creatio.server.ServerConfiguration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

public class CreatioServer implements Server {
    private static final String SERVER_BRAND = "CreatioMC";

    private final ServerConfiguration configuration;
    private final MinecraftServer server;

    public CreatioServer() {
        this.configuration = new ServerConfiguration();
        this.server = MinecraftServer.init();
        MinecraftServer.setBrandName(SERVER_BRAND);
    }

    public void initialize() {
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer container = instanceManager.createInstanceContainer();

        container.setChunkSupplier(LightingChunk::new);
        container.setGenerator(unit -> {
            unit.modifier().fillHeight(0, 40, Block.STONE);

            if (unit.absoluteStart().blockY() < 40 && unit.absoluteEnd().blockY() > 40) {
                unit.modifier().setBlock(unit.absoluteStart().blockX(), 40, unit.absoluteStart().blockZ(), Block.TORCH);
            }
        });

        GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();
        handler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            event.getPlayer().setGameMode(GameMode.CREATIVE);

            event.getPlayer().setRespawnPoint(new Pos(0, 42, 0));
            event.setSpawningInstance(container);
        });
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
