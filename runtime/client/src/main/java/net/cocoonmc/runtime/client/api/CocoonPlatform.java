package net.cocoonmc.runtime.client.api;

public enum CocoonPlatform {
    FORGE, FABRIC, UNKNOWN;

    public static CocoonPlatform get() {
        String name = CocoonPlatform.class.getPackage().getName();
        if (name.contains("forge")) {
            return FORGE;
        }
        if (name.contains("fabric")) {
            return FABRIC;
        }
        return UNKNOWN;
    }
}
