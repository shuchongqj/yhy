package com.yhy.router.types;

public enum MainActivityFromType {


    START_FROM_GUIDE(0x1001),
    START_FROM_KICKOUT(0x2001),
    START_FROM_NOTIFICATION(0x3001),
    START_FROM_UNKNOWN(-1);


    private int code;

    MainActivityFromType(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MainActivityFromType form(int code){
        if (code == MainActivityFromType.START_FROM_GUIDE.code){
            return MainActivityFromType.START_FROM_GUIDE;
        }else if (code == MainActivityFromType.START_FROM_KICKOUT.code){
            return MainActivityFromType.START_FROM_KICKOUT;
        }else if (code == MainActivityFromType.START_FROM_NOTIFICATION.code){
            return MainActivityFromType.START_FROM_NOTIFICATION;
        }else{
            return MainActivityFromType.START_FROM_UNKNOWN;
        }
    }
}
