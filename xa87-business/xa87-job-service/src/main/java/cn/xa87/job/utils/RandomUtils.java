package cn.xa87.job.utils;

import java.util.Random;

/**
 * .
 *
 * @author hcqi .
 * @since 2021/4/5
 */

public final class RandomUtils {
    private final static char[] CHAR = {'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    static final Random RANDOM = new Random();

    public static String nextChar(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int idx = RANDOM.nextInt(CHAR.length - 1);
            sb.append(CHAR[idx]);
        }
        return sb.toString();
    }

//    public static int next(int length) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < length; i++) {
//            int n = RANDOM.nextInt(10);
//            sb.append(n);
//        }
//        return Integer.parseInt(sb.toString());
//    }

    public static int next(int min, int max) {
        return (int) (min + Math.random() * (max - min + 1));
    }

    public static long next(long min, long max) {
        return (long) (min + Math.random() * (max - min + 1));
    }

    public static int next(int length) {
        StringBuilder minSb = new StringBuilder();
        StringBuilder maxSb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            minSb.append(i == 0 ? "1" : "0");
            maxSb.append("9");
        }

        int max = Integer.parseInt(maxSb.toString());
        int min = Integer.parseInt(minSb.toString());

        return (int) (min + Math.random() * (max - min + 1));
    }

    public static Double next(Double max, Double min) {
        double result = min + (RANDOM.nextDouble() * (max - min));
        result = (double) Math.round(result * 100) / 100;
        return result;
    }
    public static Double nextF6(Double max, Double min) {
        double result = min + (RANDOM.nextDouble() * (max - min));
        result = (double) Math.round(result * 100000) / 100000;
        return result;
    }

    public static void main(String[] args) {
        System.out.println(next(0.6,0.2));
    }
}
