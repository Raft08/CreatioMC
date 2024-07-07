package be.raft.creatio.plugin.source;

import java.nio.file.Path;
import java.util.List;

public class PluginDelegate {
    private final List<Path> registeredCandidate;

    public PluginDelegate(List<Path> registeredCandidate) {
        this.registeredCandidate = registeredCandidate;
    }

    public void registerCandidate(Path candidate) {
        this.registeredCandidate.add(candidate);
    }
}
