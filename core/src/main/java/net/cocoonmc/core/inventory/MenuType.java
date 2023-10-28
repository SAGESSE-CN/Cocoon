package net.cocoonmc.core.inventory;

import net.cocoonmc.core.resources.ResourceLocation;
import org.bukkit.entity.Player;

public class MenuType<T extends Menu> {

    private final ResourceLocation registryName;
    private final Factory<T, ?> factory;

    public MenuType(ResourceLocation registryName, Factory<T, ?> factory) {
        this.registryName = registryName;
        this.factory = factory;
    }

    public <V> T createMenu(Player player, V hostObject) {
        // noinspection unchecked
        Factory<T, V> factory1 = (Factory<T, V>) factory;
        return factory1.createMenu(this, player, hostObject);
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public interface Factory<T, V> {

        T createMenu(MenuType<?> menuType, Player player, V hostObject);
    }
}
