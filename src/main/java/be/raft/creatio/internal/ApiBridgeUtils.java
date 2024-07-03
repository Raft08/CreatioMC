package be.raft.creatio.internal;

import net.minestom.server.world.Difficulty;

public class ApiBridgeUtils {
    public static Difficulty parseApiDifficulty(be.raft.creatio.api.settings.Difficulty difficulty) {
        return Difficulty.valueOf(difficulty.name());
    }

    public static be.raft.creatio.api.settings.Difficulty parseInternalDifficulty(Difficulty difficulty) {
        return be.raft.creatio.api.settings.Difficulty.valueOf(difficulty.name());
    }
}
