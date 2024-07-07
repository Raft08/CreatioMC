package be.raft.creatio.plugin.source;

import be.raft.creatio.bootstrap.LaunchArgs;

import java.util.List;

public interface PluginProvider {
    List<PluginProvider> PROVIDERS = List.of(
            // new ArgumentPluginProvider(LaunchArgs.get()),
            new DirectoryPluginProvider(LaunchArgs.get().pluginDirectory())
    );

    void registerCandidates(PluginDelegate delegate);
}
