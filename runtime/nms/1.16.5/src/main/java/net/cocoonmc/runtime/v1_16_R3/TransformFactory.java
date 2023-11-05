package net.cocoonmc.runtime.v1_16_R3;

import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EnumDirection;
import net.minecraft.server.v1_16_R3.EnumHand;
import net.minecraft.server.v1_16_R3.IInventory;
import net.minecraft.server.v1_16_R3.ItemActionContext;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.MovingObjectPositionBlock;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.Vec3D;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventory;

public class TransformFactory {

    private static final EnumDirection[] DIRECTIONS_TO_VANILLA = EnumDirection.values();
    private static final EnumHand[] HANDS_TO_VANILLA = EnumHand.values();

    public static net.cocoonmc.core.item.ItemStack convertToCocoon(ItemStack itemStack) {
        return ItemFactory.ITEM_TRANSFORMER.convertToCocoon(itemStack);
    }

    public static ItemStack convertToVanilla(net.cocoonmc.core.item.ItemStack itemStack) {
        return ItemFactory.ITEM_TRANSFORMER.convertToVanilla(itemStack);
    }


    public static net.cocoonmc.core.world.entity.Player convertToCocoon(EntityHuman player) {
        return net.cocoonmc.core.world.entity.Player.of((org.bukkit.entity.Player) player.getBukkitEntity());
    }

    public static net.cocoonmc.core.BlockPos convertToCocoon(BlockPosition pos) {
        return new net.cocoonmc.core.BlockPos(pos.getX(), pos.getY(), pos.getZ());
    }

    public static net.cocoonmc.core.resources.ResourceLocation convertToCocoon(MinecraftKey rl) {
        return new net.cocoonmc.core.resources.ResourceLocation(rl.getNamespace(), rl.getKey());
    }

    public static net.cocoonmc.core.network.FriendlyByteBuf convertToCocoon(PacketDataSerializer buf) {
        return new net.cocoonmc.core.network.FriendlyByteBuf(buf);
    }

    public static EntityPlayer convertToVanilla(net.cocoonmc.core.world.entity.Player player) {
        if (player != null) {
            return ((CraftPlayer) player.asBukkit()).getHandle();
        }
        return null;
    }

    public static WorldServer convertToVanilla(net.cocoonmc.core.world.Level level) {
        if (level != null) {
            return ((CraftWorld) level.asBukkit()).getHandle();
        }
        return null;
    }

    public static IInventory convertToVanilla(org.bukkit.inventory.Inventory inventory) {
        return ((CraftInventory) inventory).getInventory();
    }

    public static BlockPosition convertToVanilla(net.cocoonmc.core.BlockPos pos) {
        return new BlockPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    public static MinecraftKey convertToVanilla(net.cocoonmc.core.resources.ResourceLocation rl) {
        return new MinecraftKey(rl.getNamespace(), rl.getPath());
    }

    public static PacketDataSerializer convertToVanilla(net.cocoonmc.core.network.FriendlyByteBuf buf) {
        return new PacketDataSerializer(buf);
    }

    public static ItemActionContext convertToVanilla(net.cocoonmc.core.item.context.UseOnContext context) {
        WorldServer serverLevel = convertToVanilla(context.getLevel());
        EntityPlayer serverPlayer = convertToVanilla(context.getPlayer());
        EnumHand useItemHand = convertToVanilla(context.getHand());
        MovingObjectPositionBlock hitResult = convertToVanilla(context.getHitResult());
        ItemStack itemStack = convertToVanilla(context.getItemInHand());
        return new ItemActionContext(serverLevel, serverPlayer, useItemHand, itemStack, hitResult);
    }

    public static MovingObjectPositionBlock convertToVanilla(net.cocoonmc.core.item.context.BlockHitResult hitResult) {
        Vec3D loc = convertToVanilla(hitResult.getLocation());
        BlockPosition pos = convertToVanilla(hitResult.getBlockPos());
        EnumDirection dir = convertToVanilla(hitResult.getDirection());
        switch (hitResult.getType()) {
            case ENTITY:
            case BLOCK:
                return new MovingObjectPositionBlock(loc, dir, pos, hitResult.isInside());

            default:
                return MovingObjectPositionBlock.a(loc, dir, pos);
        }
    }

    public static Vec3D convertToVanilla(net.cocoonmc.core.math.Vector3f vector) {
        return new Vec3D(vector.getX(), vector.getY(), vector.getZ());
    }

    public static EnumHand convertToVanilla(net.cocoonmc.core.world.InteractionHand hand) {
        return HANDS_TO_VANILLA[hand.ordinal()];
    }

    public static EnumDirection convertToVanilla(net.cocoonmc.core.Direction dir) {
        return DIRECTIONS_TO_VANILLA[dir.ordinal()];
    }
}
