package net.cocoonmc.core.block;

import net.cocoonmc.core.Direction;
import net.cocoonmc.core.block.state.properties.AttachFace;
import net.cocoonmc.core.block.state.properties.BedPart;
import net.cocoonmc.core.block.state.properties.BooleanProperty;
import net.cocoonmc.core.block.state.properties.EnumProperty;

public class BlockStateProperties {

    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public static final BooleanProperty OCCUPIED = BooleanProperty.create("occupied");

    //public static final EnumProperty<BlockFace> FACING = DirectionProperty.create("facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);

    public static final EnumProperty<AttachFace> ATTACH_FACE = EnumProperty.create("face", AttachFace.class);

    public static final EnumProperty<Direction> HORIZONTAL_FACING = EnumProperty.create("facing", Direction.class, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

    public static final EnumProperty<BedPart> BED_PART = EnumProperty.create("part", BedPart.class);
}
