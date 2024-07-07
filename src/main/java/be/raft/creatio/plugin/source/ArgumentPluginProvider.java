package be.raft.creatio.plugin.source;

import be.raft.creatio.bootstrap.LaunchArgs;
import com.google.common.base.Preconditions;
import fr.atlasworld.common.logging.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class ArgumentPluginProvider implements PluginProvider {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final LaunchArgs args;

    public ArgumentPluginProvider(@NotNull LaunchArgs args) {
        Preconditions.checkNotNull(args);

        this.args = args;
    }

    @Override
    public void registerCandidates(PluginDelegate delegate) {
        List<Path> files = this.args.addedPluginFiles();

        if (files.isEmpty())
            return;

        for (Path file : files) {
            if (fileValid(file))
                delegate.registerCandidate(file);
        }
    }

    private boolean fileValid(Path path) {
        File file = path.toFile();

        if (!file.getName().endsWith(".jar")) {
            LOGGER.error("'{}' is not a valid plugin, plugin file is not a Java Archive!", path.getFileName());
            return false;
        };

        if (!file.exists() || !file.isFile()) {
            LOGGER.error("'{}' is not a valid plugin, no file could be found!", path.getFileName());
            return false;
        }

        return true;
    }
}
