package be.raft.creatio.server;

import be.raft.creatio.api.settings.Difficulty;
import be.raft.creatio.api.settings.ServerSettings;
import be.raft.creatio.bootstrap.Main;
import be.raft.creatio.internal.ApiBridgeUtils;
import com.google.common.base.Preconditions;
import com.moandjiezana.toml.Toml;
import fr.atlasworld.common.logging.LogUtils;
import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.*;

public class ServerConfiguration implements ServerSettings {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String LOCATION = "server.toml";
    private static final String DEFAULT_LOCATION = "server-default.toml";

    private static final int BUFFER_SIZE = 4096;

    private final Toml loadedConfig;

    public ServerConfiguration() {
        File configurationFile = new File(LOCATION);

        if (!configurationFile.exists() || !configurationFile.isFile())
            unpackDefaultConfiguration(configurationFile);

        this.loadedConfig = this.loadConfig(configurationFile);
    }

    private void unpackDefaultConfiguration(File destination) {
        LOGGER.info("Generating default 'server.toml' file..");

        try {
            if (destination.isFile())
                destination.delete();

            destination.createNewFile();

            try (InputStream input = ServerConfiguration.class.getClassLoader().getResourceAsStream(DEFAULT_LOCATION);
                 FileOutputStream output = new FileOutputStream(destination)) {

                byte[] buffer = new byte[BUFFER_SIZE];
                int read;
                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
            }
        } catch (IOException e) {
            Main.crash("Failed to create 'server.toml' file! (Are your permissions properly configured ?)", e, true);
        }
    }

    private Toml loadConfig(File configurationFile) {
        Toml defaults = new Toml();

        try (InputStream input = ServerConfiguration.class.getClassLoader().getResourceAsStream(DEFAULT_LOCATION)) {
            defaults.read(input);
        } catch (IOException ignored) {
        }

        Toml loaded = new Toml(defaults).read(configurationFile);

        double defaultVersion = defaults.getLong("internal.version");
        double loadedVersion = loaded.getLong("internal.version");

        if (loadedVersion != defaultVersion) {
            LOGGER.info("'server.toml' version mismatch, unpacking default configuration..");

            this.unpackDefaultConfiguration(configurationFile);
            return this.loadConfig(configurationFile);
        }

        return loaded;
    }

    public void applyConfig() {
        Difficulty difficulty = Difficulty.parse(this.settingsDifficulty());
        if (difficulty == null) {
            LOGGER.warn("Unknown difficulty '{}', Difficulty NORMAL will be used.", this.settingsDifficulty());
            difficulty = Difficulty.NORMAL;
        }

        MinecraftServer.setCompressionThreshold(this.networkCompressionThreshold());
        MinecraftServer.setDifficulty(ApiBridgeUtils.parseApiDifficulty(difficulty));

        // Minestom Server Flag Settings
        System.setProperty("minestom.chunk-view-distance", String.valueOf(this.renderDistance()));
        System.setProperty("minestom.world-border-size", String.valueOf(this.worldSize()));
    }

    @Override
    public void difficulty(@NotNull Difficulty difficulty) {
        Preconditions.checkNotNull(difficulty, difficulty);
        MinecraftServer.setDifficulty(ApiBridgeUtils.parseApiDifficulty(difficulty));
    }

    @Override
    public @NotNull Difficulty difficulty() {
        return ApiBridgeUtils.parseInternalDifficulty(MinecraftServer.getDifficulty());
    }

    public boolean acceptedEULA() {
        return this.loadedConfig.getBoolean("eula");
    }

    public String serverAddress() {
        return this.loadedConfig.getString("online.address");
    }

    public int serverPort() {
        return this.loadedConfig.getDouble("online.port").intValue();
    }

    public String serverMOTD() {
        return this.loadedConfig.getString("online.motd");
    }

    public String authentication() {
        return this.loadedConfig.getString("online.authentication");
    }

    public String proxySecret() {
        return this.loadedConfig.getString("online.proxy-secret");
    }

    public boolean hideOnlinePlayers() {
        return this.loadedConfig.getBoolean("online.hide-online-players");
    }

    public boolean whitelist() {
        return this.loadedConfig.getBoolean("online.whitelist");
    }

    public int maximumPlayers() {
        return this.loadedConfig.getDouble("online.max-players").intValue();
    }

    public boolean logIpAddresses() {
        return this.loadedConfig.getBoolean("online.log-ips");
    }

    public int networkCompressionThreshold() {
        return this.loadedConfig.getDouble("online.network-compression-threshold").intValue();
    }

    public int rateLimit() {
        return this.loadedConfig.getDouble("online.rate-limit").intValue();
    }

    public String gameMode() {
        return this.loadedConfig.getString("gameplay.gamemode");
    }

    public boolean forceGameMode() {
        return this.loadedConfig.getBoolean("gameplay.force-gamemode");
    }

    public @NotNull String settingsDifficulty() {
        return this.loadedConfig.getString("gameplay.difficulty");
    }

    public boolean hardcode() {
        return this.loadedConfig.getBoolean("gameplay.hardcode");
    }

    public boolean enablePvP() {
        return this.loadedConfig.getBoolean("gameplay.pvp");
    }

    public int renderDistance() {
        return this.loadedConfig.getDouble("world.render-distance").intValue();
    }

    public int simulationDistance() {
        return this.loadedConfig.getDouble("world.simulation-distance").intValue();
    }

    public String worldType() {
        return this.loadedConfig.getString("world.world-type");
    }

    public String worldSeed() {
        return this.loadedConfig.getString("world.world-seed");
    }

    public String worldName() {
        return this.loadedConfig.getString("world.world-name");
    }

    public int worldSize() {
        return this.loadedConfig.getDouble("world.max-world-size").intValue();
    }

    public boolean spawnAnimals() {
        return this.loadedConfig.getBoolean("world.spawn-animals");
    }

    public boolean spawnMonsters() {
        return this.loadedConfig.getBoolean("world.spawn-monsters");
    }

    public boolean spawnNpc() {
        return this.loadedConfig.getBoolean("world.spawn-npcs");
    }

    public boolean generateStructures() {
        return this.loadedConfig.getBoolean("world.generate-structures");
    }

    public boolean allowFlight() {
        return this.loadedConfig.getBoolean("misc.allow-flight");
    }

    public boolean broadcastConsoleToOps() {
        return this.loadedConfig.getBoolean("misc.broadcast-console-to-ops");
    }

    public boolean enableCommandBlocks() {
        return this.loadedConfig.getBoolean("misc.enable-command-blocks");
    }

    public int operatorPermissionLevel() {
        return this.loadedConfig.getDouble("misc.op-permission-level").intValue();
    }
}
