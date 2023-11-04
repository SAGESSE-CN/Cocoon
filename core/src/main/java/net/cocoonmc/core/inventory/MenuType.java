package net.cocoonmc.core.inventory;

import net.cocoonmc.core.resources.ResourceLocation;
import net.cocoonmc.core.world.entity.Player;

import java.util.HashMap;
import java.util.Objects;

public class MenuType<T extends Menu> {

    private static final HashMap<ResourceLocation, MenuType<?>> KEYED_MENU_TYPES = new HashMap<>();

    private ResourceLocation registryName;
    private final Factory<T, ?> factory;

    public MenuType(Factory<T, ?> factory) {
        this.factory = factory;
    }

    public <V> T createMenu(Player player, V hostObject) {
        // noinspection unchecked
        Factory<T, V> factory1 = (Factory<T, V>) factory;
        return factory1.create(this, player, hostObject);
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuType)) return false;
        MenuType<?> menuType = (MenuType<?>) o;
        return Objects.equals(registryName, menuType.registryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registryName);
    }

    public static MenuType<?> byKey(ResourceLocation registryName) {
        return KEYED_MENU_TYPES.get(registryName);
    }

    public static <T extends Menu> MenuType<T> register(ResourceLocation registryName, MenuType<T> menuType) {
        menuType.registryName = registryName;
        KEYED_MENU_TYPES.put(registryName, menuType);
        return menuType;
    }

    public interface Factory<T, V> {

        T create(MenuType<?> menuType, Player player, V hostObject);
    }
}
