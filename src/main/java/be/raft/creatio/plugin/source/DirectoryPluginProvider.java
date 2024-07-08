package be.raft.creatio.plugin.source;

import com.google.common.base.Preconditions;
import fr.atlasworld.common.logging.LogUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class DirectoryPluginProvider implements PluginProvider {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int MAX_PLUGIN_SEARCH_DEPTH = 1;

    private final File sourceDirectory;

    public DirectoryPluginProvider(@NotNull File sourceDirectory) {
        Preconditions.checkNotNull(sourceDirectory);
        Preconditions.checkArgument(sourceDirectory.isDirectory(), "File is not a directory!");

        this.sourceDirectory = sourceDirectory;
    }

    @Override
    public void registerCandidates(PluginDelegate delegate) {
        List<Path> foundCandidates = new ArrayList<>();

        try {
            Files.walkFileTree(this.sourceDirectory.toPath(), EnumSet.of(FileVisitOption.FOLLOW_LINKS),
                    MAX_PLUGIN_SEARCH_DEPTH, new PluginFileWalker(foundCandidates));
        } catch (IOException e) {
            LOGGER.error("Failed to scan for plugins: ", e);
        }

        foundCandidates.forEach(delegate::registerCandidate);
    }

    private static final class PluginFileWalker implements FileVisitor<Path> {
        private final List<Path> foundCandidates;

        public PluginFileWalker(List<Path> foundCandidates) {
            this.foundCandidates = foundCandidates;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            LOGGER.debug("Scanning directory '{}' for plugins..", dir);

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            File target = file.toFile();

            if (target.isDirectory())
                return FileVisitResult.CONTINUE;

            if (!target.getName().endsWith(".jar"))
                return FileVisitResult.CONTINUE;

            this.foundCandidates.add(file);
            LOGGER.info("Found plugin '{}'.", file.getFileName());

            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            LOGGER.error("Unable to scan file '{}': {}", file, exc);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            return FileVisitResult.CONTINUE;
        }
    }
}
