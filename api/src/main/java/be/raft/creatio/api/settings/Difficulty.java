package be.raft.creatio.api.settings;

import javax.annotation.Nullable;

public enum Difficulty {
    PEACEFUL, EASY, NORMAL, HARD;

    @Nullable
    public static Difficulty parse(String string) {
        if (string == null)
            return null;

        try {
            return valueOf(string.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // No Match
        }
    }
}
