package be.raft.creatio.api.settings;

import javax.annotation.Nullable;

public enum GameMode {
    SURVIVAL, CREATIVE, SPECTATOR, ADVENTURE;

    @Nullable
    public static GameMode parse(String string) {
        if (string == null)
            return null;

        try {
            return valueOf(string.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // No Match
        }
    }
}
