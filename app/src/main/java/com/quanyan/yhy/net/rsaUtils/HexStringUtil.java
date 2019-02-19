package com.quanyan.yhy.net.rsaUtils;

public class HexStringUtil {
    private static final char[] hexArray = "0123456789abcdef".toCharArray();

    public static final String toHexString(byte[] bs) {
        if (bs == null) return null;
        char[] hexChars = new char[bs.length * 2];
        for (int i = 0; i < bs.length; i++) {
            int v = bs[i] & 0xff;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0f];
        }
        return new String(hexChars);
    }

}
