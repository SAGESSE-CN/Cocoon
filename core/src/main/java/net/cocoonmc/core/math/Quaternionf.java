package net.cocoonmc.core.math;

import net.cocoonmc.core.utils.MathHelper;

@SuppressWarnings("unused")
public class Quaternionf {

    public static final Quaternionf ONE = new Quaternionf();

    public float x;
    public float y;
    public float z;
    public float w;

    public Quaternionf() {
        this(0f, 0f, 0f, 1f);
    }

    public Quaternionf(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternionf(Vector3f vec, float f, boolean bl) {
        if (bl) {
            f *= (float) Math.PI / 180;
        }
        float g = MathHelper.sin(f / 2.0f);
        this.x = vec.getX() * g;
        this.y = vec.getY() * g;
        this.z = vec.getZ() * g;
        this.w = MathHelper.cos(f / 2.0f);
    }

    public Quaternionf(float f, float g, float h, boolean bl) {
        if (bl) {
            f *= (float) Math.PI / 180;
            g *= (float) Math.PI / 180;
            h *= (float) Math.PI / 180;
        }
        float i = sin(0.5f * f);
        float j = cos(0.5f * f);
        float k = sin(0.5f * g);
        float l = cos(0.5f * g);
        float m = sin(0.5f * h);
        float n = cos(0.5f * h);
        this.x = i * l * n + j * k * m;
        this.y = j * k * n - i * l * m;
        this.z = i * k * n + j * l * m;
        this.w = j * l * n - i * k * m;
    }

    public Quaternionf(Quaternionf other) {
        this.x = other.x();
        this.y = other.y();
        this.z = other.z();
        this.w = other.w();
    }

    public static Quaternionf fromXYZ(float angleX, float angleY, float angleZ) {
        float sx = MathHelper.sin(angleX * 0.5f);
        float cx = MathHelper.cosFromSin(sx, angleX * 0.5f);
        float sy = MathHelper.sin(angleY * 0.5f);
        float cy = MathHelper.cosFromSin(sy, angleY * 0.5f);
        float sz = MathHelper.sin(angleZ * 0.5f);
        float cz = MathHelper.cosFromSin(sz, angleZ * 0.5f);
        float cycz = cy * cz;
        float sysz = sy * sz;
        float sycz = sy * cz;
        float cysz = cy * sz;
        return new Quaternionf(sx * cycz + cx * sysz, cx * sycz - sx * cysz, cx * cysz + sx * sycz, cx * cycz - sx * sysz);
    }

    public static Quaternionf fromZYX(float angleZ, float angleY, float angleX) {
        float sx = MathHelper.sin(angleX * 0.5f);
        float cx = MathHelper.cosFromSin(sx, angleX * 0.5f);
        float sy = MathHelper.sin(angleY * 0.5f);
        float cy = MathHelper.cosFromSin(sy, angleY * 0.5f);
        float sz = MathHelper.sin(angleZ * 0.5f);
        float cz = MathHelper.cosFromSin(sz, angleZ * 0.5f);
        float cycz = cy * cz;
        float sysz = sy * sz;
        float sycz = sy * cz;
        float cysz = cy * sz;
        return new Quaternionf(sx * cycz - cx * sysz, cx * sycz + sx * cysz, cx * cysz - sx * sycz, cx * cycz + sx * sysz);
    }

    public static Quaternionf fromYXZ(float angleY, float angleX, float angleZ) {
        float sx = MathHelper.sin(angleX * 0.5f);
        float cx = MathHelper.cosFromSin(sx, angleX * 0.5f);
        float sy = MathHelper.sin(angleY * 0.5f);
        float cy = MathHelper.cosFromSin(sy, angleY * 0.5f);
        float sz = MathHelper.sin(angleZ * 0.5f);
        float cz = MathHelper.cosFromSin(sz, angleZ * 0.5f);
        float x = cy * sx;
        float y = sy * cx;
        float z = sy * sx;
        float w = cy * cx;
        return new Quaternionf(x * cz + y * sz, y * cz - x * sz, w * sz - z * cz, w * cz + z * sz);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Quaternionf quaternion = (Quaternionf) object;
        if (Float.compare(quaternion.x, this.x) != 0) {
            return false;
        }
        if (Float.compare(quaternion.y, this.y) != 0) {
            return false;
        }
        if (Float.compare(quaternion.z, this.z) != 0) {
            return false;
        }
        return Float.compare(quaternion.w, this.w) == 0;
    }

    public int hashCode() {
        int i = Float.floatToIntBits(this.x);
        i = 31 * i + Float.floatToIntBits(this.y);
        i = 31 * i + Float.floatToIntBits(this.z);
        i = 31 * i + Float.floatToIntBits(this.w);
        return i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Quaternionf[").append(this.w()).append(" + ");
        stringBuilder.append(this.x()).append("i + ");
        stringBuilder.append(this.y()).append("j + ");
        stringBuilder.append(this.z()).append("k]");
        return stringBuilder.toString();
    }

    public float x() {
        return this.x;
    }

    public float y() {
        return this.y;
    }

    public float z() {
        return this.z;
    }

    public float w() {
        return this.w;
    }

    public void mul(Quaternionf other) {
        float f = x;
        float g = y;
        float h = z;
        float i = w;
        float j = other.x;
        float k = other.y;
        float l = other.z;
        float m = other.w;
        this.x = i * j + f * m + g * l - h * k;
        this.y = i * k - f * l + g * m + h * j;
        this.z = i * l + f * k - g * j + h * m;
        this.w = i * m - f * j - g * k - h * l;
    }

    public void mul(float f) {
        this.x *= f;
        this.y *= f;
        this.z *= f;
        this.w *= f;
    }

    public float dot(Quaternionf other) {
        return x * other.x + y * other.y + z * other.z + w * other.w;
    }

    public Quaternionf conj() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    public Quaternionf inverse() {
        return this.conj();
    }

    public void set(float f, float g, float h, float i) {
        this.x = f;
        this.y = g;
        this.z = h;
        this.w = i;
    }

    public void set(Quaternionf other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        this.w = other.w;
    }

    private static float cos(float f) {
        return (float) Math.cos(f);
    }

    private static float sin(float f) {
        return (float) Math.sin(f);
    }

    public Quaternionf normalize() {
        float f = this.x() * this.x() + this.y() * this.y() + this.z() * this.z() + this.w() * this.w();
        if (f > 1.0E-6f) {
            float g = MathHelper.fastInvSqrt(f);
            this.x *= g;
            this.y *= g;
            this.z *= g;
            this.w *= g;
        } else {
            this.x = 0.0f;
            this.y = 0.0f;
            this.z = 0.0f;
            this.w = 0.0f;
        }
        return this;
    }

    public void slerp(Quaternionf quaternion, float f) {
        throw new UnsupportedOperationException();
    }

    public Quaternionf copy() {
        return new Quaternionf(this);
    }
}
