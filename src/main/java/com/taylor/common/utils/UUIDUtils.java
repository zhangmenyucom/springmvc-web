package com.taylor.common.utils;

import java.util.UUID;

/**
 * Created by Kowalski on 2017/5/11
 * Updated by Kowalski on 2017/5/11
 */
public final class UUIDUtils {

    private static final char[] digit = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};

    /**22位UUID*/
    public static String generate22UUID() {

        UUID uid = UUID.randomUUID();
        long most = uid.getMostSignificantBits();

        char[] buf = new char[22];
        int charPos = 22;
        int radix = 1 << 6;
        long mask = radix - 1;
        do {
            charPos--;
            buf[charPos] = digit[(int)(most & mask)];
            most >>>= 6;
        } while (most != 0);

        long least = uid.getLeastSignificantBits();
        do {
            charPos--;
            buf[charPos] = digit[(int)(least & mask)];
            least >>>= 6;
        } while (least != 0);
        return new String(buf, charPos, (22 - charPos));
    }

    /**无 - UUID*/
    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        long most = uuid.getMostSignificantBits();

        long least = uuid.getLeastSignificantBits();

        return (digits(most >> 32, 8) +
                digits(most >> 16, 4) +
                digits(most, 4) +
                digits(least >> 48, 4) +
                digits(least, 12));
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits << 2);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }


//    public static void main(String... args) {
//        for(int i=0;i<100;i++){
//            System.out.println(generate22UUID());
//            System.out.println(generateUUID());
//        }
//    }

}
