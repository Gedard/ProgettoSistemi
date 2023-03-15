package utility;

public class OurMath {

    public static final long MOD = (int) Math.pow(2, 63) - 25;

    public static long modPow(long base, long exponent, long modulus) {
        long result = 1;
        base %= modulus;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % modulus;
            }
            exponent >>= 1;
            base = (base * base) % modulus;
        }
        return result;
    }

}
