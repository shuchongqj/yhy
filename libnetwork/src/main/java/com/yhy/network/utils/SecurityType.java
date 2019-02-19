package com.yhy.network.utils;

public class SecurityType {
    public static final int None = 0;
    public static final int RegisteredDevice = 256;
    public static final int UserTrustedDevice = 1024;
    public static final int MobileOwner = 2048;
    public static final int MobileOwnerTrustedDevice = 4096;
    public static final int UserLogin = 8192;
    public static final int UserLoginAndMobileOwner = 10240;
}
