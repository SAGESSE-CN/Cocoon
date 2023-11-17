package net.cocoonmc.core.network.syncher;

import net.cocoonmc.core.BlockPos;
import net.cocoonmc.core.Direction;
import net.cocoonmc.core.math.Rotations;
import net.cocoonmc.core.nbt.CompoundTag;
import net.cocoonmc.core.network.Component;
import net.cocoonmc.core.network.FriendlyByteBuf;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class EntityDataSerializers {

    public static final EntityDataSerializer<Byte> BYTE = EntityDataSerializer.simple((it, value) -> it.writeByte(value), FriendlyByteBuf::readByte);
    public static final EntityDataSerializer<Integer> INT = EntityDataSerializer.simple(FriendlyByteBuf::writeVarInt, FriendlyByteBuf::readVarInt);
    public static final EntityDataSerializer<Long> LONG = EntityDataSerializer.simple(FriendlyByteBuf::writeVarLong, FriendlyByteBuf::readVarLong);
    public static final EntityDataSerializer<Float> FLOAT = EntityDataSerializer.simple(FriendlyByteBuf::writeFloat, FriendlyByteBuf::readFloat);
    public static final EntityDataSerializer<String> STRING = EntityDataSerializer.simple(FriendlyByteBuf::writeUtf, FriendlyByteBuf::readUtf);
    public static final EntityDataSerializer<Component> COMPONENT = EntityDataSerializer.simple(FriendlyByteBuf::writeComponent, FriendlyByteBuf::readComponent);
    public static final EntityDataSerializer<Optional<Component>> OPTIONAL_COMPONENT = EntityDataSerializer.optional(FriendlyByteBuf::writeComponent, FriendlyByteBuf::readComponent);
    //public static final EntityDataSerializer<ItemStack> ITEM_STACK = EntityDataSerializer.simple(FriendlyByteBuf::writeItem, FriendlyByteBuf::readItem);
    //public static final EntityDataSerializer<BlockState> BLOCK_STATE = EntityDataSerializer.simpleId(Block.BLOCK_STATE_REGISTRY);
    //public static final EntityDataSerializer<Optional<BlockState>> OPTIONAL_BLOCK_STATE =
    public static final EntityDataSerializer<Boolean> BOOLEAN = EntityDataSerializer.simple(FriendlyByteBuf::writeBoolean, FriendlyByteBuf::readBoolean);
    //public static final EntityDataSerializer<ParticleOptions> PARTICLE =
    public static final EntityDataSerializer<BlockPos> BLOCK_POS = EntityDataSerializer.simple(FriendlyByteBuf::writeBlockPos, FriendlyByteBuf::readBlockPos);
    public static final EntityDataSerializer<Optional<BlockPos>> OPTIONAL_BLOCK_POS = EntityDataSerializer.optional(FriendlyByteBuf::writeBlockPos, FriendlyByteBuf::readBlockPos);
    public static final EntityDataSerializer<Direction> DIRECTION = EntityDataSerializer.simpleEnum(Direction.class);
    public static final EntityDataSerializer<Optional<UUID>> OPTIONAL_UUID = EntityDataSerializer.optional(FriendlyByteBuf::writeUUID, FriendlyByteBuf::readUUID);
    //public static final EntityDataSerializer<Optional<GlobalPos>> OPTIONAL_GLOBAL_POS = EntityDataSerializer.optional(FriendlyByteBuf::writeGlobalPos, FriendlyByteBuf::readGlobalPos);
    public static final EntityDataSerializer<CompoundTag> COMPOUND_TAG = EntityDataSerializer.simple(FriendlyByteBuf::writeNbt, FriendlyByteBuf::readNbt);
    //public static final EntityDataSerializer<VillagerData> VILLAGER_DATA =
    //public static final EntityDataSerializer<OptionalInt> OPTIONAL_UNSIGNED_INT =
    //public static final EntityDataSerializer<Pose> POSE = EntityDataSerializer.simpleEnum(Pose.class);
    //public static final EntityDataSerializer<CatVariant> CAT_VARIANT = EntityDataSerializer.simpleId(BuiltInRegistries.CAT_VARIANT);
    //public static final EntityDataSerializer<FrogVariant> FROG_VARIANT = EntityDataSerializer.simpleId(BuiltInRegistries.FROG_VARIANT);
    //public static final EntityDataSerializer<Holder<PaintingVariant>> PAINTING_VARIANT = EntityDataSerializer.simpleId(BuiltInRegistries.PAINTING_VARIANT.asHolderIdMap());
    //public static final EntityDataSerializer<Sniffer.State> SNIFFER_STATE = EntityDataSerializer.simpleEnum(Sniffer.State.class);
    //public static final EntityDataSerializer<Vector3f> VECTOR3 = EntityDataSerializer.simple(FriendlyByteBuf::writeVector3f, FriendlyByteBuf::readVector3f);
    //public static final EntityDataSerializer<Quaternionf> QUATERNION = EntityDataSerializer.simple(FriendlyByteBuf::writeQuaternion, FriendlyByteBuf::readQuaternion);


    public static final EntityDataSerializer<Rotations> ROTATIONS = new EntityDataSerializer<Rotations>() {
        @Override
        public void write(FriendlyByteBuf buf, Rotations value) {
            buf.writeFloat(value.getX());
            buf.writeFloat(value.getY());
            buf.writeFloat(value.getZ());
        }

        @Override
        public Rotations read(FriendlyByteBuf buf) {
            return new Rotations(buf.readFloat(), buf.readFloat(), buf.readFloat());
        }
    };
}
