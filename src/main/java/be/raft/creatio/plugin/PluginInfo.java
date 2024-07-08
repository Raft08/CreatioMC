package be.raft.creatio.plugin;

import be.raft.creatio.api.plugin.PluginMeta;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import fr.atlasworld.common.logging.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;

public final class PluginInfo implements PluginMeta  {
    public static final String FILE_NAME = "plugin.json";
    public static final Gson GSON = new Gson();
    public static final String ID_REGEX = "^[a-z0-9-_]+$";

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String BUKKIT_PLUGIN_FILE = "plugin.yml";
    private static final String PAPER_PLUGIN_FILE = "paper-plugin.yml";

    private final @SerializedName("id") @NotNull String id;
    private final @SerializedName("name") @NotNull String name;
    private final @SerializedName("version") @NotNull String version;
    private final @SerializedName("description") @Nullable String description;
    private final @SerializedName("credits") @Nullable String credits;
    private final @SerializedName("authors") @Nullable List<String> authors;

    // Internal Field
    private final @SerializedName("main") @NotNull String mainClass;

    public PluginInfo(@NotNull String id, @NotNull String name, @NotNull String version, @Nullable String description, @Nullable String credits,
                      @Nullable List<String> authors, @NotNull String mainClass) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.description = description;
        this.credits = credits;
        this.authors = authors;
        this.mainClass = mainClass;

        this.validateInfo();
    }

    public static PluginInfo loadFromCandidate(Path path) throws IOException {
        boolean bukkitPluginFound = false;
        PluginInfo pluginInfo = null;

        try (JarInputStream in = new JarInputStream(
                new BufferedInputStream(Files.newInputStream(path)))) {
            JarEntry entry;
            while ((entry = in.getNextJarEntry()) != null) {
                if (entry.getName().equals(FILE_NAME)) {
                    try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                        pluginInfo = GSON.fromJson(reader, PluginInfo.class);
                        break;
                    }
                }

                if (entry.getName().equals(BUKKIT_PLUGIN_FILE) || entry.getName().equals(PAPER_PLUGIN_FILE)) {
                    bukkitPluginFound = true;
                }
            }
        }

        if (pluginInfo == null && bukkitPluginFound) {
            LOGGER.error("File '{}' is a Bukkit/Spigot/Paper plugin! Creatio does not support these plugins!", path.getFileName());
            return null;
        }

        if (pluginInfo == null) {
            LOGGER.error("File '{}' is not a plugin file!", path.getFileName());
            return null;
        }

        try {
            pluginInfo.validateInfo();
        } catch (NullPointerException | IllegalArgumentException e) {
            LOGGER.error("Plugin '{}' is invalid: {}", path.getFileName(), e.getMessage());
        }

        return pluginInfo;
    }

    private void validateInfo() {
        Preconditions.checkNotNull(this.id, "Plugin id must be specified!");
        Preconditions.checkNotNull(this.name, "Plugin name must be specified!");
        Preconditions.checkNotNull(this.version, "Plugin version must be specified!");
        Preconditions.checkNotNull(this.mainClass, "Plugin main-class must be specified!");

        Preconditions.checkArgument(!this.id.isEmpty(), "Plugin id may not be empty!");
        Preconditions.checkArgument(!this.name.isEmpty(), "Plugin name must not be empty!");
        Preconditions.checkArgument(!this.version.isEmpty(), "Plugin version must not be empty!");
        Preconditions.checkArgument(!this.mainClass.isEmpty(), "Plugin main-class must not be empty!");

        Preconditions.checkArgument(Pattern.matches(ID_REGEX, this.id), "Plugin id must match regex: [" + ID_REGEX + "]");
    }

    @Override
    public @NotNull String id() {
        return this.id;
    }

    @Override
    public @NotNull String name() {
        return this.name;
    }

    @Override
    public @NotNull String version() {
        return this.version;
    }

    @Override
    public @Nullable String description() {
        return this.description;
    }

    @Override
    public @Nullable String credits() {
        return this.credits;
    }

    @Override
    public @NotNull List<String> authors() {
        return List.copyOf(this.authors == null ? List.of() : this.authors);
    }

    public @NotNull String mainClass() {
        return this.mainClass;
    }
}
