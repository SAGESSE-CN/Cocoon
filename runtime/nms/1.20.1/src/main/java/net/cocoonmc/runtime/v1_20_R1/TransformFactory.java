package net.cocoonmc.runtime.v1_20_R1;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventory;

public class TransformFactory {

    private static final Direction[] DIRECTIONS_TO_VANILLA = Direction.values();
    private static final InteractionHand[] HANDS_TO_VANILLA = InteractionHand.values();

    public static net.cocoonmc.core.item.ItemStack convertToCocoon(ItemStack itemStack) {
        return ItemFactory.ITEM_TRANSFORMER.convertToCocoon(itemStack);
    }

    public static ItemStack convertToVanilla(net.cocoonmc.core.item.ItemStack itemStack) {
        return ItemFactory.ITEM_TRANSFORMER.convertToVanilla(itemStack);
    }


    public static net.cocoonmc.core.world.entity.Player convertToCocoon(Player player) {
        return net.cocoonmc.core.world.entity.Player.of((org.bukkit.entity.Player) player.getBukkitEntity());
    }

    public static net.cocoonmc.core.BlockPos convertToCocoon(BlockPos pos) {
        return new net.cocoonmc.core.BlockPos(pos.getX(), pos.getY(), pos.getZ());
    }

    public static net.cocoonmc.core.resources.ResourceLocation convertToCocoon(ResourceLocation rl) {
        return new net.cocoonmc.core.resources.ResourceLocation(rl.getNamespace(), rl.getPath());
    }

    public static net.cocoonmc.core.network.FriendlyByteBuf convertToCocoon(FriendlyByteBuf buf) {
        return new net.cocoonmc.core.network.FriendlyByteBuf(buf);
    }

    public static ServerPlayer convertToVanilla(net.cocoonmc.core.world.entity.Player player) {
        if (player != null) {
            return ((CraftPlayer) player.asBukkit()).getHandle();
        }
        return null;
    }

    public static ServerLevel convertToVanilla(net.cocoonmc.core.world.Level level) {
        if (level != null) {
            return ((CraftWorld) level.asBukkit()).getHandle();
        }
        return null;
    }

    public static Container convertToVanilla(org.bukkit.inventory.Inventory inventory) {
        return ((CraftInventory) inventory).getInventory();
    }

    public static BlockPos convertToVanilla(net.cocoonmc.core.BlockPos pos) {
        return new BlockPos(pos.getX(), pos.getY(), pos.getZ());
    }

    public static ResourceLocation convertToVanilla(net.cocoonmc.core.resources.ResourceLocation rl) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath());
    }

    public static FriendlyByteBuf convertToVanilla(net.cocoonmc.core.network.FriendlyByteBuf buf) {
        return new FriendlyByteBuf(buf);
    }

    public static UseOnContext convertToVanilla(net.cocoonmc.core.item.context.UseOnContext context) {
        ServerLevel serverLevel = convertToVanilla(context.getLevel());
        ServerPlayer serverPlayer = convertToVanilla(context.getPlayer());
        InteractionHand useItemHand = convertToVanilla(context.getHand());
        BlockHitResult hitResult = convertToVanilla(context.getHitResult());
        ItemStack itemStack = convertToVanilla(context.getItemInHand());
        return new UseOnContext(serverLevel, serverPlayer, useItemHand, itemStack, hitResult);
    }

    public static BlockHitResult convertToVanilla(net.cocoonmc.core.item.context.BlockHitResult hitResult) {
        Vec3 loc = convertToVanilla(hitResult.getLocation());
        BlockPos pos = convertToVanilla(hitResult.getBlockPos());
        Direction dir = convertToVanilla(hitResult.getDirection());
        switch (hitResult.getType()) {
            case ENTITY:
            case BLOCK:
                return new BlockHitResult(loc, dir, pos, hitResult.isInside());

            default:
                return BlockHitResult.miss(loc, dir, pos);
        }
    }

    public static Vec3 convertToVanilla(net.cocoonmc.core.math.Vector3f vector) {
        return new Vec3(vector.getX(), vector.getY(), vector.getZ());
    }

    public static InteractionHand convertToVanilla(net.cocoonmc.core.world.InteractionHand hand) {
        return HANDS_TO_VANILLA[hand.ordinal()];
    }

    public static Direction convertToVanilla(net.cocoonmc.core.Direction dir) {
        return DIRECTIONS_TO_VANILLA[dir.ordinal()];
    }
}
