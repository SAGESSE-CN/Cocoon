package net.cocoonmc.core.math;

import net.cocoonmc.core.nbt.FloatTag;
import net.cocoonmc.core.nbt.ListTag;
import net.cocoonmc.core.utils.MathHelper;

public class Rotations {

    protected final float x;
    protected final float y;
    protected final float z;

    public Rotations(float f, float g, float h) {
        this.x = Float.isInfinite(f) || Float.isNaN(f) ? 0.0f : f % 360.0f;
        this.y = Float.isInfinite(g) || Float.isNaN(g) ? 0.0f : g % 360.0f;
        this.z = Float.isInfinite(h) || Float.isNaN(h) ? 0.0f : h % 360.0f;
    }

    public Rotations(ListTag listTag) {
        this(listTag.getFloat(0), listTag.getFloat(1), listTag.getFloat(2));
    }

    public ListTag save() {
        ListTag listTag = ListTag.newInstance();
        listTag.add(FloatTag.valueOf(this.x));
        listTag.add(FloatTag.valueOf(this.y));
        listTag.add(FloatTag.valueOf(this.z));
        return listTag;
    }


    public static Rotations of(org.bukkit.util.EulerAngle angle) {
        float x = (float) MathHelper.toDegrees(angle.getX());
        float y = (float) MathHelper.toDegrees(angle.getY());
        float z = (float) MathHelper.toDegrees(angle.getZ());
        return new Rotations(x, y, z);
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }


    public float getWrappedX() {
        return MathHelper.wrapDegrees(this.x);
    }

    public float getWrappedY() {
        return MathHelper.wrapDegrees(this.y);
    }

    public float getWrappedZ() {
        return MathHelper.wrapDegrees(this.z);
    }

    public org.bukkit.util.EulerAngle asBukkit() {
        return new org.bukkit.util.EulerAngle(MathHelper.toRadians(x), MathHelper.toRadians(y), MathHelper.toRadians(z));
    }
}
