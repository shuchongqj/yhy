package com.yhy.common.base;

import com.yhy.common.types.EnvType;

public class YhyEnvironment {

    private int envType;
    private String channel;

    public boolean isOnline(){
        boolean isOnline;
        switch (envType) {
            case EnvType.DEVELOP:
            case EnvType.TEST:
                isOnline = false;
                break;
            case EnvType.RELEASE:
                isOnline = true;
                break;
            default:
                isOnline = false;
                break;
        }
        return isOnline;
    }

    public YhyEnvironment(int envType, String channel) {
        this.envType = envType;
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }

    public int getEnvType() {
        return envType;
    }
}
