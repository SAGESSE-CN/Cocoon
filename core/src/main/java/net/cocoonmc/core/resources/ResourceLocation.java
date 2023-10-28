package net.cocoonmc.core.resources;

import org.bukkit.NamespacedKey;

import java.util.Objects;

public class ResourceLocation {

    protected final String namespace;
    protected final String path;

    public ResourceLocation(String id) {
        String[] parts = decompose(id, ':');
        this.namespace = parts[0];
        this.path = parts[1];
    }

    public ResourceLocation(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
    }

    public static ResourceLocation of(org.bukkit.NamespacedKey key) {
        return new ResourceLocation(key.getNamespace(), key.getKey());
    }

    public static org.bukkit.NamespacedKey of(ResourceLocation key) {
        return new NamespacedKey(key.getNamespace(), key.getPath());
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResourceLocation)) return false;
        ResourceLocation that = (ResourceLocation) o;
        return Objects.equals(namespace, that.namespace) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, path);
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }

    private static String[] decompose(String id, char delimiter) {
        String[] results = new String[]{"minecraft", id};
        int var3 = id.indexOf(delimiter);
        if (var3 >= 0) {
            results[1] = id.substring(var3 + 1);
            if (var3 >= 1) {
                results[0] = id.substring(0, var3);
            }
        }
        return results;
    }
}
