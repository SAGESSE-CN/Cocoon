package net.cocoonmc.runtime.v1_16_R3;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.cocoonmc.core.utils.ReflectUtils;
import net.cocoonmc.runtime.IBlockFactory;
import org.bukkit.block.Skull;

import java.util.UUID;

public class BlockFactory extends TransformFactory implements IBlockFactory {

    @Override
    public void setSkullTexture(Skull skull, String texture) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        Property property = new Property("textures", texture);
        profile.getProperties().put("textures", property);
        ReflectUtils.getMemberField(skull.getClass(), "profile").set(skull, profile);
    }

    @Override
    public String getSkullTexture(Skull skull) {
        GameProfile profile = ReflectUtils.getMemberField(skull.getClass(), "profile").get(skull);
        if (profile != null) {
            return profile.getProperties().get("textures").stream().findFirst().map(Property::getValue).orElse(null);
        }
        return null;
    }

//    public static void setBlock(UseOnContext context, String id, @Nullable BlockState state, @Nullable CompoundTag tag) {
//
//        @Override
//        public int placeItem(org.bukkit.inventory.ItemStack itemStack, IBlockPlaceContext context, EquipmentSlot hand) {


//        try {
////            String skinURL = getSkinURL(id, state, tag);
////
////            // ..
////            Location pos = context.getClickedBlock().getLocation();
////            Location location = context.getClickedBlock().getLocation();
////            CustomBlock.BlockPlaceContext context1;
////            EquipmentSlot hand = EquipmentSlot.HAND;
////            context1 = new CustomBlock.BlockPlaceContext(context.getPlayer(), hand, 2, pos, location, BlockFace.NORTH);
////            CustomBlock.placeItem(NBTEditor.getHead(skinURL), context1, hand);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public static void setBlockAndUpdate(UseOnContext context, String id, @Nullable BlockState state, @Nullable CompoundTag tag) {
//        setBlock(context, id, state, tag);
//        org.bukkit.block.Block target = context.getClickedBlock().getRelative(BlockFace.NORTH);
//        if (!target.getType().isAir()) {
//            target.getState().update(true, false);
//        }
//    }
//
//    public static void updateBlock(org.bukkit.block.Block block, String id, @Nullable BlockState state, @Nullable CompoundTag tag) {
//        try {
//            String skinURL = getSkinURL(id, state, tag);
//            if (block.getType() != Material.PLAYER_HEAD) {
//                block.setType(Material.PLAYER_HEAD, true);
//            }
//            setSkullTexture(block, skinURL);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void updateBlockAndUpdate(org.bukkit.block.Block block, String id, @Nullable BlockState state, @Nullable CompoundTag tag) {
//        updateBlock(block, id, state, tag);
//        block.getState().update(true, false);
//    }
}
