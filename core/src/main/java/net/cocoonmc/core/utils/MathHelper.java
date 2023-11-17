package net.cocoonmc.core.utils;

public class MathHelper {

    public static final float PI = (float) Math.PI;
    public static final float PI_D2 = (float) Math.PI * 0.5f;
    public static final float PI_M2 = (float) Math.PI * 2.0f;

    private static final double FRAC_BIAS = Double.longBitsToDouble(4805340802404319232L);
    private static final double[] ASIN_TAB = new double[257];
    private static final double[] COS_TAB = new double[257];

    private static final float[] SIN = new float[65536];

    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};

    public static int smallestEncompassingPowerOfTwo(int i) {
        int j = i - 1;
        j |= j >> 1;
        j |= j >> 2;
        j |= j >> 4;
        j |= j >> 8;
        j |= j >> 16;
        return j + 1;
    }

    public static boolean isPowerOfTwo(int i) {
        return i != 0 && (i & i - 1) == 0;
    }

    public static float fma(float a, float b, float c) {
//        if (Runtime.HAS_Math_fma)
//            return java.lang.Math.fma(a, b, c);
        return a * b + c;
    }

    public static int ceillog2(int i) {
        i = isPowerOfTwo(i) ? i : smallestEncompassingPowerOfTwo(i);
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int) ((long) i * 125613361L >> 27) & 0x1F];
    }

    public static int log2(int i) {
        return ceillog2(i) - (isPowerOfTwo(i) ? 0 : 1);
    }

    public static int floor(double d) {
        int i = (int) d;
        return d < (double) i ? i - 1 : i;
    }

    public static int ceil(double d) {
        int i = (int) d;
        return d > (double) i ? i + 1 : i;
    }

    public static float fastInvSqrt(float v) {
        float f = 0.5F * v;
        int i = Float.floatToIntBits(v);
        i = 1597463007 - (i >> 1);
        v = Float.intBitsToFloat(i);
        return v * (1.5F - f * v * v);
    }

    public static double fastInvSqrt(double p_181161_0_) {
        double d0 = 0.5D * p_181161_0_;
        long i = Double.doubleToRawLongBits(p_181161_0_);
        i = 6910469410427058090L - (i >> 1);
        p_181161_0_ = Double.longBitsToDouble(i);
        return p_181161_0_ * (1.5D - d0 * p_181161_0_ * p_181161_0_);
    }

    public static float fastInvCubeRoot(float p_226166_0_) {
        int i = Float.floatToIntBits(p_226166_0_);
        i = 1419967116 - i / 3;
        float f = Float.intBitsToFloat(i);
        f = 0.6666667F * f + 1.0F / (3.0F * f * f * p_226166_0_);
        return 0.6666667F * f + 1.0F / (3.0F * f * f * p_226166_0_);
    }

    public static float sqrt(float p_76129_0_) {
        return (float) Math.sqrt(p_76129_0_);
    }

    public static float sqrt(double p_76133_0_) {
        return (float) Math.sqrt(p_76133_0_);
    }

    public static float sin(float f) {
        return SIN[(int) (f * 10430.378F) & '\uffff'];
    }

    public static float cos(float f) {
        return SIN[(int) (f * 10430.378F + 16384.0F) & '\uffff'];
    }

    public static float cosFromSin(float sin, float angle) {
//        if (Options.FASTMATH)
//            return sin(angle + PIHalf_f);
        // sin(x)^2 + cos(x)^2 = 1
        float cos = sqrt(1.0f - sin * sin);
        float a = angle + PI_D2;
        float b = a - (int) (a / PI_M2) * PI_M2;
        if (b < 0.0)
            b = PI_M2 + b;
        if (b >= PI)
            return -cos;
        return cos;
    }

    public static int wrapDegrees(int i) {
        int j = i % 360;
        if (j >= 180) {
            j -= 360;
        }
        if (j < -180) {
            j += 360;
        }
        return j;
    }

    public static float wrapDegrees(float f) {
        float g = f % 360.0f;
        if (g >= 180.0f) {
            g -= 360.0f;
        }
        if (g < -180.0f) {
            g += 360.0f;
        }
        return g;
    }

    public static double wrapDegrees(double d) {
        double e = d % 360.0;
        if (e >= 180.0) {
            e -= 360.0;
        }
        if (e < -180.0) {
            e += 360.0;
        }
        return e;
    }

    public static double toDegrees(double angrad) {
        return Math.toDegrees(angrad);
    }

    public static double toRadians(double value) {
        return Math.toRadians((value + 360) % 360);
    }

    static {
        for (int i = 0; i < 257; ++i) {
            double d = (double) i / 256.0;
            double e = Math.asin(d);
            COS_TAB[i] = Math.cos(e);
            ASIN_TAB[i] = e;
        }
        for (int i = 0; i < SIN.length; ++i) {
            SIN[i] = (float) Math.sin((double) i * Math.PI * 2.0D / 65536.0D);
        }
    }
}
