package be.raft.creatio.api.registry;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public final class RegistryKey {
    public static final String REGEX = "^[a-z0-9_-]+$";

    private final String namespace;
    private final String key;

    public static RegistryKey fromString(@NotNull String string) {
        Preconditions.checkNotNull(string);

        String[] parts = string.split(":", 3);
        if (parts.length != 2)
            throw new IllegalArgumentException("Invalid registry key: " + string);

        String namespace = parts[0];
        String key = parts[1];
        return new RegistryKey(namespace, key);
    }

    public RegistryKey(@NotNull String namespace, @NotNull String key) {
        Preconditions.checkNotNull(namespace, "Invalid Namespace");
        Preconditions.checkNotNull(key, "Invalid Key");

        Preconditions.checkArgument(namespace.matches(REGEX), "Invalid Namespace");
        Preconditions.checkArgument(key.matches(REGEX), "Invalid Key");

        this.namespace = namespace;
        this.key = key;
    }

    public String namespace() {
        return this.namespace;
    }

    public String key() {
        return this.key;
    }

    @Override
    public String toString() {
        return this.namespace + ":" + this.key;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!(obj instanceof RegistryKey other))
            return false;

        return this.key.equals(other.key) && this.namespace.equals(other.namespace);
    }
}
