package net.cocoonmc.core;

import net.cocoonmc.core.math.Vector3i;
import net.cocoonmc.core.utils.BukkitHelper;
import net.cocoonmc.core.world.entity.Entity;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public enum Direction {

    DOWN(0, 1, -1, "down", AxisDirection.NEGATIVE, Axis.Y, new Vector3i(0, -1, 0)),
    UP(1, 0, -1, "up", AxisDirection.POSITIVE, Axis.Y, new Vector3i(0, 1, 0)),
    NORTH(2, 3, 2, "north", AxisDirection.NEGATIVE, Axis.Z, new Vector3i(0, 0, -1)),
    SOUTH(3, 2, 0, "south", AxisDirection.POSITIVE, Axis.Z, new Vector3i(0, 0, 1)),
    WEST(4, 5, 1, "west", AxisDirection.NEGATIVE, Axis.X, new Vector3i(-1, 0, 0)),
    EAST(5, 4, 3, "east", AxisDirection.POSITIVE, Axis.X, new Vector3i(1, 0, 0));

    private final int data3d;
    private final int oppositeIndex;
    private final int data2d;
    private final String name;
    private final Axis axis;
    private final AxisDirection axisDirection;
    private final Vector3i normal;

    Direction(int data3d, int oppositeIndex, int data2d, String name, AxisDirection axisDirection, Axis axis, Vector3i normal) {
        this.data3d = data3d;
        this.data2d = data2d;
        this.oppositeIndex = oppositeIndex;
        this.name = name;
        this.axis = axis;
        this.axisDirection = axisDirection;
        this.normal = normal;
    }

    public static Direction by(BlockFace face) {
        return BukkitHelper.convertToCocoon(face);
    }

    public static Direction[] orderedByNearest(Entity entity) {
        float f = entity.getViewXRot(1.0f) * ((float) Math.PI / 180);
        float g = -entity.getViewYRot(1.0f) * ((float) Math.PI / 180);
        double h = Math.sin(f);
        double i = Math.cos(f);
        double j = Math.sin(g);
        double k = Math.cos(g);
        boolean bl = j > 0.0f;
        boolean bl2 = h < 0.0f;
        boolean bl3 = k > 0.0f;
        double l = bl ? j : -j;
        double m = bl2 ? -h : h;
        double n = bl3 ? k : -k;
        double o = l * i;
        double p = n * i;
        Direction direction = bl ? Direction.EAST : Direction.WEST;
        Direction direction2 = bl2 ? Direction.UP : Direction.DOWN;
        Direction direction3 = bl3 ? Direction.SOUTH : Direction.NORTH;
        if (l > n) {
            if (m > o) {
                return makeDirectionArray(direction2, direction, direction3);
            }
            if (p > m) {
                return makeDirectionArray(direction, direction3, direction2);
            }
            return makeDirectionArray(direction, direction2, direction3);
        }
        if (m > p) {
            return makeDirectionArray(direction2, direction3, direction);
        }
        if (o > m) {
            return makeDirectionArray(direction3, direction, direction2);
        }
        return makeDirectionArray(direction3, direction2, direction);
    }

    private static Direction[] makeDirectionArray(Direction var0, Direction var1, Direction var2) {
        return new Direction[]{var0, var1, var2, var2.getOpposite(), var1.getOpposite(), var0.getOpposite()};
    }
//
//    public static Direction rotate(Matrix4f var0, Direction var1) {
//        Vec3i var2 = var1.getNormal();
//        Vector4f var3 = var0.transform(new Vector4f((float) var2.getX(), (float) var2.getY(), (float) var2.getZ(), 0.0F));
//        return getNearest(var3.x(), var3.y(), var3.z());
//    }
//
//    public static Collection<Direction> allShuffled(RandomSource var0) {
//        return Util.shuffledCopy(values(), var0);
//    }
//
//    public static Stream<Direction> stream() {
//        return Stream.of(VALUES);
//    }
//
//    public Quaternionf getRotation() {
//        Quaternionf var10000;
//        switch (this) {
//            case DOWN:
//                var10000 = (new Quaternionf()).rotationX(3.1415927F);
//                break;
//            case UP:
//                var10000 = new Quaternionf();
//                break;
//            case NORTH:
//                var10000 = (new Quaternionf()).rotationXYZ(1.5707964F, 0.0F, 3.1415927F);
//                break;
//            case SOUTH:
//                var10000 = (new Quaternionf()).rotationX(1.5707964F);
//                break;
//            case WEST:
//                var10000 = (new Quaternionf()).rotationXYZ(1.5707964F, 0.0F, 1.5707964F);
//                break;
//            case EAST:
//                var10000 = (new Quaternionf()).rotationXYZ(1.5707964F, 0.0F, -1.5707964F);
//                break;
//            default:
//                throw new IncompatibleClassChangeError();
//        }
//
//        return var10000;
//    }

    public int get3DDataValue() {
        return this.data3d;
    }

    public int get2DDataValue() {
        return this.data2d;
    }

    public AxisDirection getAxisDirection() {
        return this.axisDirection;
    }

    public static Direction getFacingAxis(Entity var0, Axis var1) {
//        switch (var1) {
//            case X:
//                var10000 = EAST.isFacingAngle(var0.getViewYRot(1.0F)) ? EAST : WEST;
//                break;
//            case Z:
//                var10000 = SOUTH.isFacingAngle(var0.getViewYRot(1.0F)) ? SOUTH : NORTH;
//                break;
//            case Y:
//                var10000 = var0.getViewXRot(1.0F) < 0.0F ? UP : DOWN;
//                break;
//            default:
//                throw new IncompatibleClassChangeError();
//        }
        return null;
    }

    public Direction getOpposite() {
        return Direction.values()[oppositeIndex];
    }
//
//    public Direction getClockWise(Axis var0) {
//        Direction var10000;
//        switch (var0) {
//            case X:
//                var10000 = this != WEST && this != EAST ? this.getClockWiseX() : this;
//                break;
//            case Z:
//                var10000 = this != NORTH && this != SOUTH ? this.getClockWiseZ() : this;
//                break;
//            case Y:
//                var10000 = this != UP && this != DOWN ? this.getClockWise() : this;
//                break;
//            default:
//                throw new IncompatibleClassChangeError();
//        }
//
//        return var10000;
//    }
//
//    public Direction getCounterClockWise(Axis var0) {
//        Direction var10000;
//        switch (var0) {
//            case X:
//                var10000 = this != WEST && this != EAST ? this.getCounterClockWiseX() : this;
//                break;
//            case Z:
//                var10000 = this != NORTH && this != SOUTH ? this.getCounterClockWiseZ() : this;
//                break;
//            case Y:
//                var10000 = this != UP && this != DOWN ? this.getCounterClockWise() : this;
//                break;
//            default:
//                throw new IncompatibleClassChangeError();
//        }
//
//        return var10000;
//    }

    public Direction getClockWise() {
        switch (this) {
            case NORTH:
                return EAST;
            case SOUTH:
                return WEST;
            case WEST:
                return NORTH;
            case EAST:
                return SOUTH;
            default:
                throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
        }
    }

    private Direction getClockWiseX() {
        switch (this) {
            case DOWN:
                return SOUTH;
            case UP:
                return NORTH;
            case NORTH:
                return DOWN;
            case SOUTH:
                return UP;
            default:
                throw new IllegalStateException("Unable to get X-rotated facing of " + this);
        }
    }

    private Direction getCounterClockWiseX() {
        Direction var10000;
        switch (this) {
            case DOWN:
                var10000 = NORTH;
                break;
            case UP:
                var10000 = SOUTH;
                break;
            case NORTH:
                var10000 = UP;
                break;
            case SOUTH:
                var10000 = DOWN;
                break;
            default:
                throw new IllegalStateException("Unable to get X-rotated facing of " + this);
        }

        return var10000;
    }

    private Direction getClockWiseZ() {
        switch (this) {
            case DOWN:
                return WEST;
            case UP:
                return EAST;
            case WEST:
                return UP;
            case EAST:
                return DOWN;
            default:
                throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
        }
    }

    private Direction getCounterClockWiseZ() {
        switch (this) {
            case DOWN:
                return EAST;
            case UP:
                return WEST;
            case WEST:
                return DOWN;
            case EAST:
                return UP;
            default:
                throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
        }
    }

    public Direction getCounterClockWise() {
        switch (this) {
            case NORTH:
                return WEST;
            case SOUTH:
                return EAST;
            case WEST:
                return SOUTH;
            case EAST:
                return NORTH;
            default:
                throw new IllegalStateException("Unable to get CCW facing of " + this);
        }
    }

    public int getStepX() {
        return this.normal.getX();
    }

    public int getStepY() {
        return this.normal.getY();
    }

    public int getStepZ() {
        return this.normal.getZ();
    }

    public String getName() {
        return this.name;
    }

    public Axis getAxis() {
        return this.axis;
    }

//    @Nullable
//    public static Direction byName(@Nullable String var0) {
//        return (Direction) CODEC.byName(var0);
//    }
//
//    public static Direction from3DDataValue(int var0) {
//        return BY_3D_DATA[Mth.abs(var0 % BY_3D_DATA.length)];
//    }
//
//    public static Direction from2DDataValue(int var0) {
//        return BY_2D_DATA[Mth.abs(var0 % BY_2D_DATA.length)];
//    }
//
//    @Nullable
//    public static Direction fromDelta(int var0, int var1, int var2) {
//        if (var0 == 0) {
//            if (var1 == 0) {
//                if (var2 > 0) {
//                    return SOUTH;
//                }
//
//                if (var2 < 0) {
//                    return NORTH;
//                }
//            } else if (var2 == 0) {
//                if (var1 > 0) {
//                    return UP;
//                }
//
//                return DOWN;
//            }
//        } else if (var1 == 0 && var2 == 0) {
//            if (var0 > 0) {
//                return EAST;
//            }
//
//            return WEST;
//        }
//
//        return null;
//    }
//
//    public static Direction fromYRot(double var0) {
//        return from2DDataValue(Mth.floor(var0 / 90.0 + 0.5) & 3);
//    }
//
//    public static Direction fromAxisAndDirection(Axis var0, AxisDirection var1) {
//        Direction var10000;
//        switch (var0) {
//            case X:
//                var10000 = var1 == AxisDirection.POSITIVE ? EAST : WEST;
//                break;
//            case Z:
//                var10000 = var1 == AxisDirection.POSITIVE ? SOUTH : NORTH;
//                break;
//            case Y:
//                var10000 = var1 == AxisDirection.POSITIVE ? UP : DOWN;
//                break;
//            default:
//                throw new IncompatibleClassChangeError();
//        }
//
//        return var10000;
//    }
//
//    public float toYRot() {
//        return (float) ((this.data2d & 3) * 90);
//    }
//
//    public static Direction getRandom(RandomSource var0) {
//        return (Direction) Util.getRandom(VALUES, var0);
//    }

    public static Direction getNearest(double var0, double var2, double var4) {
        return getNearest((float) var0, (float) var2, (float) var4);
    }

    public static Direction getNearest(float var0, float var1, float var2) {
        Direction var3 = NORTH;
        float var4 = Float.MIN_VALUE;
        Direction[] var5 = Direction.values();
        int var6 = var5.length;

        for (int var7 = 0; var7 < var6; ++var7) {
            Direction var8 = var5[var7];
            float var9 = var0 * (float) var8.normal.getX() + var1 * (float) var8.normal.getY() + var2 * (float) var8.normal.getZ();
            if (var9 > var4) {
                var4 = var9;
                var3 = var8;
            }
        }

        return var3;
    }

    public BlockFace asBukkit() {
        return BukkitHelper.convertToBukkit(this);
    }

    @Override
    public String toString() {
        return this.name;
    }

//    public static Direction get(AxisDirection var0, Axis var1) {
//        Direction[] var2 = VALUES;
//        int var3 = var2.length;
//
//        for (int var4 = 0; var4 < var3; ++var4) {
//            Direction var5 = var2[var4];
//            if (var5.getAxisDirection() == var0 && var5.getAxis() == var1) {
//                return var5;
//            }
//        }
//
//        throw new IllegalArgumentException("No such direction: " + var0 + " " + var1);
//    }

    public Vector3i getNormal() {
        return this.normal;
    }

//    public boolean isFacingAngle(float var0) {
//        float var1 = var0 * 0.017453292F;
//        float var2 = -Mth.sin(var1);
//        float var3 = Mth.cos(var1);
//        return (float) this.normal.getX() * var2 + (float) this.normal.getZ() * var3 > 0.0F;
//    }

    public enum Axis implements Predicate<Direction> {
        X("x") {
            public int choose(int var0, int var1, int var2) {
                return var0;
            }

            public double choose(double var0, double var2, double var4) {
                return var0;
            }
        }, Y("y") {
            public int choose(int var0, int var1, int var2) {
                return var1;
            }

            public double choose(double var0, double var2, double var4) {
                return var2;
            }
        }, Z("z") {
            public int choose(int var0, int var1, int var2) {
                return var2;
            }

            public double choose(double var0, double var2, double var4) {
                return var4;
            }
        };

        public static final Axis[] VALUES = values();
        private final String name;

        Axis(String var2) {
            this.name = var2;
        }

        public String getName() {
            return this.name;
        }

        public boolean isVertical() {
            return this == Y;
        }

        public boolean isHorizontal() {
            return this == X || this == Z;
        }

        public String toString() {
            return this.name;
        }

        public boolean test(@Nullable Direction var0) {
            return var0 != null && var0.getAxis() == this;
        }

        public abstract int choose(int var1, int var2, int var3);

        public abstract double choose(double var1, double var3, double var5);
    }

    public enum AxisDirection {
        POSITIVE(1, "Towards positive"), NEGATIVE(-1, "Towards negative");

        private final int step;
        private final String name;

        AxisDirection(int var2, String var3) {
            this.step = var2;
            this.name = var3;
        }

        public int getStep() {
            return this.step;
        }

        public String getName() {
            return this.name;
        }

        public String toString() {
            return this.name;
        }

        public AxisDirection opposite() {
            return this == POSITIVE ? NEGATIVE : POSITIVE;
        }
    }
}
