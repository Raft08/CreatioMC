package be.raft.creatio.plugin.loader;

import be.raft.creatio.CreatioServer;
import be.raft.creatio.api.plugin.PluginClassLoader;
import be.raft.creatio.api.plugin.PluginMeta;
import be.raft.creatio.plugin.PluginInfo;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public class ServerPluginClassLoader extends URLClassLoader implements PluginClassLoader {
    private static final GroupClassLoader GROUP_CLASSLOADER = new GroupClassLoader(CreatioServer.class.getClassLoader());

    static {
        registerAsParallelCapable();
    }

    private final PluginInfo info;
    private final Logger logger;
    private final Path sourceFile;

    private boolean allowExternalClasspathUsage;

    public ServerPluginClassLoader(@NotNull Path sourceFile) throws IOException {
        super(new URL[]{sourceFile.toUri().toURL()});

        Preconditions.checkNotNull(sourceFile);
        Preconditions.checkArgument(sourceFile.toFile().isFile(), "Plugin file could not be found!");

        this.info = PluginInfo.loadFromCandidate(sourceFile);
        Preconditions.checkArgument(this.info != null, "Plugin candidate '" + sourceFile.getFileName() + "' is invalid.");

        this.logger = LoggerFactory.getLogger(this.info.id());
        this.sourceFile = sourceFile;

        GROUP_CLASSLOADER.addModuleLoader(this);
    }

    public Class<?> loadClass(String name, boolean resolve, boolean checkRoot, boolean checkModules) throws ClassNotFoundException {
        try {
            return super.loadClass(name, resolve);
        } catch (ClassNotFoundException ignored) {
        }

        if (checkRoot) {
            try {
                return GROUP_CLASSLOADER.loadClass(name, resolve, true, false);
            } catch (ClassNotFoundException ignored) {
            }
        }

        if (checkModules) {
            try {
                return GROUP_CLASSLOADER.loadClass(name, resolve, false, true);
            } catch (ClassNotFoundException ignored) {
            }
        }

        throw new ClassNotFoundException(name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return this.loadClass(name, resolve, true, true);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return this.loadClass(name, false);
    }

    @Override
    public @NotNull PluginInfo meta() {
        return this.info;
    }

    @Override
    public @NotNull Logger pluginLogger() {
        return this.logger;
    }

    public @NotNull Path sourceFile() {
        return this.sourceFile;
    }

    @Override
    public boolean allowsExternalClasspathUsage() {
        return this.allowExternalClasspathUsage;
    }

    @Override
    public void allowExternalClasspathUsage(boolean allow) {
        this.allowExternalClasspathUsage = allow;
    }
}
