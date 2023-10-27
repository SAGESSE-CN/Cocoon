package net.cocoonmc.core.world;

import org.bukkit.inventory.EquipmentSlot;

public enum InteractionHand {

    MAIN_HAND, OFF_HAND;


    public static InteractionHand of(org.bukkit.inventory.EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.OFF_HAND) {
            return OFF_HAND;
        }
        return MAIN_HAND;
    }
}