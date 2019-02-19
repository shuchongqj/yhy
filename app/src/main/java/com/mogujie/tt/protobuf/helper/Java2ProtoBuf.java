package com.mogujie.tt.protobuf.helper;

import com.mogujie.tt.protobuf.IMBaseDefine;
import com.yhy.common.constants.DBConstant;

/**
 * @author : yingmu on 15-1-6.
 * @email : yingmu@mogujie.com.
 */
public class Java2ProtoBuf {
    /**
     * ----enum 转化接口--
     */
    public static IMBaseDefine.MsgType getProtoMsgType(int msgType) {
        switch (msgType) {
            case DBConstant.MSG_TYPE_GROUP_TEXT:
                return IMBaseDefine.MsgType.MSG_TYPE_GROUP_TEXT;
            case DBConstant.MSG_TYPE_GROUP_AUDIO:
                return IMBaseDefine.MsgType.MSG_TYPE_GROUP_AUDIO;
            case DBConstant.MSG_TYPE_SINGLE_AUDIO:
                return IMBaseDefine.MsgType.MSG_TYPE_SINGLE_AUDIO;
            case DBConstant.MSG_TYPE_SINGLE_TEXT:
                return IMBaseDefine.MsgType.MSG_TYPE_SINGLE_TEXT;
            case DBConstant.MSG_TYPE_SINGLE_PRODUCT_CARD:
                return IMBaseDefine.MsgType.MSG_TYPE_SINGLE_PRODUCT_CARD;
            case DBConstant.MSG_TYPE_CONSULT_TEXT:
                return IMBaseDefine.MsgType.MSG_TYPE_CONSULT_TEXT;
            case DBConstant.MSG_TYPE_CONSULT_AUDIO:
                return IMBaseDefine.MsgType.MSG_TYPE_CONSULT_AUDIO;
            case DBConstant.MSG_TYPE_CONSULT_NOTIFY:
                return IMBaseDefine.MsgType.MSG_TYPE_CONSULT_NOTIFY;
            default:
                return null;
        }
    }


    public static boolean isNewSessionType(int sessionType) {
        switch (sessionType) {
            case DBConstant.SESSION_TYPE_SINGLE:
            case DBConstant.SESSION_TYPE_GROUP:
            case DBConstant.SESSION_TYPE_CONSULT:
                return false;
            default:
                return true;
        }
    }

    public static IMBaseDefine.SessionType getProtoSessionType(int sessionType) {
        switch (sessionType) {
            case DBConstant.SESSION_TYPE_SINGLE:
                return IMBaseDefine.SessionType.SESSION_TYPE_SINGLE;
            case DBConstant.SESSION_TYPE_GROUP:
                return IMBaseDefine.SessionType.SESSION_TYPE_GROUP;
            case DBConstant.SESSION_TYPE_CONSULT:
                return IMBaseDefine.SessionType.SESSION_TYPE_CONSULT;
            default:
                return null;
        }
    }

    public static boolean isNewMsgType(int msgType) {
        switch (msgType) {
            case DBConstant.MSG_TYPE_GROUP_TEXT:
            case DBConstant.MSG_TYPE_GROUP_AUDIO:
            case DBConstant.MSG_TYPE_SINGLE_AUDIO:
            case DBConstant.MSG_TYPE_SINGLE_TEXT:
            case DBConstant.MSG_TYPE_SINGLE_PRODUCT_CARD:
            case DBConstant.MSG_TYPE_CONSULT_TEXT:
            case DBConstant.MSG_TYPE_CONSULT_AUDIO:
            case DBConstant.MSG_TYPE_CONSULT_NOTIFY:
                return false;
            default:
                return true;
        }
    }

    public static int getProtoSessionTypeNew(int sessionType) {

        return 0;
    }

    public static int getProtoMsgTypeNew(int msgType) {
        return 0;
    }

}
