package com.yhy.sport.filter;

/**
 * @Description:
 * @Created by zhaolei.yang 2018-07-09 20:32
 */
public class SportType {
    private Type mType;

    public SportType() {
        mType = Type.WALK;
    }

    public void setType(Type type) {
        mType = type;
    }

    public Type getType() {
        return mType;
    }

    public enum Type {
        WALK("walk", 1),
        RUN("run", 2),
        RIDE("ride", 3);

        String msg;
        int code;

        Type(String msg, int code) {
            this.msg = msg;
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public long getCode() {
            return code;
        }
    }

}
