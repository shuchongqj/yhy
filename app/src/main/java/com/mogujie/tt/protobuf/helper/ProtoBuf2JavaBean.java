package com.mogujie.tt.protobuf.helper;

import com.google.protobuf.ByteString;
import com.harwkin.nb.camera.ImageUtils;
import com.mogujie.tt.imservice.entity.MsgAnalyzeEngine;
import com.mogujie.tt.imservice.manager.IMLoginManager;
import com.mogujie.tt.protobuf.IMBaseDefine;
import com.mogujie.tt.protobuf.IMMessage;
import com.smart.sdk.api.resp.Api_USER_UserInfo;
import com.yhy.common.beans.im.entity.AudioMessage;
import com.yhy.common.beans.im.entity.KnowLedgeMessage;
import com.yhy.common.beans.im.entity.MessageEntity;
import com.yhy.common.beans.im.entity.NotifyMessage;
import com.yhy.common.beans.im.entity.ProductCardMessage;
import com.yhy.common.beans.im.entity.SessionEntity;
import com.yhy.common.beans.im.entity.UnknownMessage;
import com.yhy.common.beans.im.entity.UnreadEntity;
import com.yhy.common.beans.im.entity.UserEntity;
import com.yhy.common.beans.net.model.user.UserInfo;
import com.yhy.common.beans.user.User;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.MessageConstant;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.FileUtil;
import com.yhy.common.utils.pinyin.PinYin;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * @author : yingmu on 15-1-5.
 * @email : yingmu@mogujie.com.
 */
public class ProtoBuf2JavaBean {


//    public static UserEntity getUserEntity(IMBaseDefine.UserInfo userInfo){
//        UserEntity userEntity = new UserEntity();
//        int timeNow = (int) (System.currentTimeMillis()/1000);
//
//        userEntity.setStatus(userInfo.getStatus());
//        userEntity.setAvatar(userInfo.getAvatarUrl());
//        userEntity.setCreated(timeNow);
//        userEntity.setDepartmentId(userInfo.getDepartmentId());
//        userEntity.setEmail(userInfo.getEmail());
//        userEntity.setGender(userInfo.getUserGender());
//        userEntity.setMainName(userInfo.getUserNickName());
//        userEntity.setPhone(userInfo.getUserTel());
//        userEntity.setPinyinName(userInfo.getUserDomain());
//        userEntity.setRealName(userInfo.getUserRealName());
//        userEntity.setUpdated(timeNow);
//        userEntity.setPeerId(userInfo.getUserId());
//
//        PinYin.getPinYin(userEntity.getMainName(), userEntity.getPinyinElement());
//        return userEntity;
//    }

    public static UserEntity getUserEntity(Api_USER_UserInfo userInfo) {
        UserEntity userEntity = new UserEntity();
        int timeNow = (int) (System.currentTimeMillis() / 1000);

        userEntity.setAvatar(ImageUtils.getImageFullUrl(userInfo.avatar));
        userEntity.setCreated(timeNow);
        userEntity.setPinyinName(userInfo.nickname);
        userEntity.setGender(userInfo.gender != null ? Integer.parseInt(userInfo.gender) : 1);
        userEntity.setMainName(userInfo.nickname);
        userEntity.setRealName(userInfo.name);
        userEntity.setUpdated(timeNow);
        userEntity.setPeerId(userInfo.id);
        userEntity.setOptions((int) userInfo.options);
        userEntity.setIsVip(userInfo.vip);
        PinYin.getPinYin(userEntity.getMainName(), userEntity.getPinyinElement());
        return userEntity;
    }

    public static UserEntity getUserEntity(UserInfo userInfo) {
        UserEntity userEntity = new UserEntity();
        int timeNow = (int) (System.currentTimeMillis() / 1000);

        userEntity.setAvatar(ImageUtils.getImageFullUrl(userInfo.avatar));
        userEntity.setCreated(timeNow);
        userEntity.setPinyinName(userInfo.nickname);
        userEntity.setGender(userInfo.gender != null ? Integer.parseInt(userInfo.gender) : 1);
        userEntity.setMainName(userInfo.nickname);
        userEntity.setRealName(userInfo.name);
        userEntity.setUpdated(timeNow);
        userEntity.setPeerId(userInfo.id);
        userEntity.setOptions((int) userInfo.options);
        userEntity.setIsVip(userInfo.vip);

        PinYin.getPinYin(userEntity.getMainName(), userEntity.getPinyinElement());
        return userEntity;
    }

    public static UserEntity getUserEntity(User userInfo) {
        UserEntity userEntity = new UserEntity();
        int timeNow = (int) (System.currentTimeMillis() / 1000);

        userEntity.setAvatar(ImageUtils.getImageFullUrl(userInfo.getAvatar()));
        userEntity.setCreated(timeNow);
        userEntity.setPinyinName(userInfo.getNickname());
        userEntity.setGender(userInfo.getGender() != null ? Integer.parseInt(userInfo.getGender()) : 1);
        userEntity.setMainName(userInfo.getNickname());
        userEntity.setRealName(userInfo.getName());
        userEntity.setUpdated(timeNow);
        userEntity.setPeerId(userInfo.getUserId());
        userEntity.setOptions((int) userInfo.getOptions());
        userEntity.setIsVip(userInfo.isVip());

        PinYin.getPinYin(userEntity.getMainName(), userEntity.getPinyinElement());
        return userEntity;
    }

    public static SessionEntity getSessionEntity(IMBaseDefine.ContactSessionInfo sessionInfo) {

        int msgType;
        int sessionType;
        String content;
        if (sessionInfo.hasExtInfo()) {
            IMBaseDefine.ExtInfo extInfo = sessionInfo.getExtInfo();
            msgType = getJavaMsgTypeNew(extInfo.getMsgType());
            sessionType = getJavaSessionTypeNew(extInfo.getSessionType());
            content = extInfo.getMsgData().toStringUtf8();
        } else {
            msgType = getJavaMsgType(sessionInfo.getLatestMsgType());
            sessionType = getJavaSessionType(sessionInfo.getSessionType());
            content = sessionInfo.getLatestMsgData().toStringUtf8();
        }

        if (sessionType == -1) {
            return null;
        }
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setLatestMsgType(msgType);
        sessionEntity.setPeerType(sessionType);
        sessionEntity.setPeerId(sessionInfo.getSessionId());
        sessionEntity.setServiceId(sessionInfo.getMsgItem());
        sessionEntity.buildSessionKey();
        sessionEntity.setTalkId(sessionInfo.getLatestMsgFromUserId());
        sessionEntity.setLatestMsgId(sessionInfo.getLatestMsgId());
        sessionEntity.setCreated(sessionInfo.getUpdatedTime());
//        String desMessage = new String(com.mogujie.tt.Security.getInstance().DecryptMsg(content));
        String desMessage = content;
        // 判断具体的类型是什么
        if (msgType == DBConstant.MSG_TYPE_GROUP_TEXT ||
                msgType == DBConstant.MSG_TYPE_SINGLE_TEXT || msgType == DBConstant.MSG_TYPE_CONSULT_TEXT) {
            desMessage = MsgAnalyzeEngine.analyzeMessageDisplay(desMessage);
        } else if (msgType == DBConstant.MSG_TYPE_SINGLE_PRODUCT_CARD) {
            desMessage = DBConstant.DISPlAY_FOR_PRODUCT_CARD;
        } else if (msgType == DBConstant.MSG_TYPE_SINGLE_AUDIO || msgType == DBConstant.MSG_TYPE_CONSULT_AUDIO) {
            desMessage = DBConstant.DISPLAY_FOR_AUDIO;
        } else if (msgType == DBConstant.MSG_TYPE_UNKNOW) {
            desMessage = DBConstant.DISPLAY_FOR_UNKNOWN;
        } else if (msgType == DBConstant.MSG_TYPE_CONSULT_KNOWLEDGE) {
            desMessage = DBConstant.DISPLAY_FOR_KNOWLEDGE;
        } else if (msgType == DBConstant.MSG_TYPE_CONSULT_NOTIFY) {
            try {
                JSONObject object = new JSONObject(desMessage);
                desMessage = object.optString(String.valueOf(IMLoginManager.instance().getLoginId()));
            } catch (JSONException e) {
                return null;
            }
        }

        sessionEntity.setLatestMsgData(desMessage);
        sessionEntity.setUpdated(sessionInfo.getUpdatedTime());
        sessionEntity.setStatus(MessageConstant.MSG_SUCCESS);
        return sessionEntity;
    }

    /**
     * 拆分消息在上层做掉 图文混排
     * 在这判断
     */
    public static MessageEntity getMessageEntity(IMBaseDefine.MsgInfo msgInfo) {
        MessageEntity messageEntity;
        if (!msgInfo.hasExtInfo()) {
            IMBaseDefine.MsgType msgType = msgInfo.getMsgType();
            switch (msgType) {
                case MSG_TYPE_SINGLE_AUDIO:
                case MSG_TYPE_GROUP_AUDIO:
                case MSG_TYPE_CONSULT_AUDIO:
                    try {
                        /**语音的解析不能转自 string再返回来*/
                        messageEntity = analyzeAudio(msgInfo);
                    } catch (JSONException e) {
                        return null;
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                    break;
                case MSG_TYPE_GROUP_TEXT:
                case MSG_TYPE_SINGLE_TEXT:
                case MSG_TYPE_CONSULT_TEXT:
                    messageEntity = analyzeText(msgInfo);
                    break;
//            case MSG_TYPE_GROUP_PRODUCT_CARD:
                case MSG_TYPE_SINGLE_PRODUCT_CARD:
                    messageEntity = analyzeProductCard(msgInfo);
                    break;
                case MSG_TYPE_CONSULT_NOTIFY:
                    messageEntity = analyzeNotify(msgInfo);
                    break;
                default:
                    messageEntity = analyzeUnKnownMessage(msgInfo);
                    break;

            }
        } else {
            IMBaseDefine.ExtInfo extInfo = msgInfo.getExtInfo();
            int msgType = extInfo.getMsgType();
            switch (msgType) {
                case IMBaseDefine.ExtMsgType.MSG_TYPE_SINGLE_AUDIO_COMPAT_VALUE:
                case IMBaseDefine.ExtMsgType.MSG_TYPE_GROUP_AUDIO_COMPAT_VALUE:
                case IMBaseDefine.ExtMsgType.MSG_TYPE_CONSULT_AUDIO_COMPAT_VALUE:
                    try {
                        /**语音的解析不能转自 string再返回来*/
                        messageEntity = analyzeAudio(msgInfo);
                    } catch (JSONException e) {
                        return null;
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                    break;

                case IMBaseDefine.ExtMsgType.MSG_TYPE_GROUP_TEXT_COMPAT_VALUE:
                case IMBaseDefine.ExtMsgType.MSG_TYPE_SINGLE_TEXT_COMPAT_VALUE:
                case IMBaseDefine.ExtMsgType.MSG_TYPE_CONSULT_TEXT_COMPAT_VALUE:
                    messageEntity = analyzeText(msgInfo);
                    break;
//            case MSG_TYPE_GROUP_PRODUCT_CARD:
                case IMBaseDefine.ExtMsgType.MSG_TYPE_SINGLE_PRODUCT_CARD_COMPAT_VALUE:
                    messageEntity = analyzeProductCard(msgInfo);
                    break;
                case IMBaseDefine.ExtMsgType.MSG_TYPE_CONSULT_NOTIFY_COMPAT_VALUE:
                    messageEntity = analyzeNotify(msgInfo);
                    break;
                case IMBaseDefine.ExtMsgType.MSG_TYPE_CONSULT_KNOWLEDGE_BASE_VALUE:
                    messageEntity = analyzeKnowledge(msgInfo);
                    break;
                default:
                    messageEntity = analyzeUnKnownMessage(msgInfo);
                    break;

            }
        }
        return messageEntity;
    }

    private static MessageEntity analyzeKnowledge(IMBaseDefine.MsgInfo msgInfo) {
        int msgType;
        String content;
        if (!msgInfo.hasExtInfo()) {
            msgType = getJavaMsgType(msgInfo.getMsgType());
            content = msgInfo.getMsgData().toStringUtf8();
        } else {
            IMBaseDefine.ExtInfo extInfo = msgInfo.getExtInfo();
            msgType = getJavaMsgTypeNew(extInfo.getMsgType());
            content = extInfo.getMsgData().toStringUtf8();
        }

        KnowLedgeMessage knowLedgeMessage = new KnowLedgeMessage();
        knowLedgeMessage.setFromId(msgInfo.getFromSessionId());
        knowLedgeMessage.setMsgId(msgInfo.getMsgId());
        knowLedgeMessage.setMsgType(msgType);
        knowLedgeMessage.setStatus(MessageConstant.MSG_SUCCESS);
        knowLedgeMessage.setDisplayType(DBConstant.SHOW_KNOWLEDGE_TYPE);
        knowLedgeMessage.setCreated(msgInfo.getCreateTime());
        knowLedgeMessage.setUpdated(msgInfo.getCreateTime());
        knowLedgeMessage.setContent(content);
        knowLedgeMessage.setServiceId(msgInfo.getMsgItem());

        return knowLedgeMessage;

    }

    public static MessageEntity analyzeUnKnownMessage(IMBaseDefine.MsgInfo msgInfo) {

        int msgType;
        String content;
        if (!msgInfo.hasExtInfo()) {
            msgType = getJavaMsgType(msgInfo.getMsgType());
            content = msgInfo.getMsgData().toStringUtf8();
        } else {
            IMBaseDefine.ExtInfo extInfo = msgInfo.getExtInfo();
            msgType = getJavaMsgTypeNew(extInfo.getMsgType());
            content = extInfo.getMsgData().toStringUtf8();
        }

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setCreated(msgInfo.getCreateTime());
        messageEntity.setUpdated(msgInfo.getCreateTime());
        messageEntity.setFromId(msgInfo.getFromSessionId());
        messageEntity.setMsgId(msgInfo.getMsgId());
        messageEntity.setMsgType(msgType);
        messageEntity.setStatus(MessageConstant.MSG_SUCCESS);
        messageEntity.setContent(content);
        return UnknownMessage.parseFromNet(messageEntity);
    }


    private static MessageEntity analyzeProductCard(IMBaseDefine.MsgInfo msgInfo) {

        int msgType;
        String content;
        if (!msgInfo.hasExtInfo()) {
            msgType = getJavaMsgType(msgInfo.getMsgType());
            content = msgInfo.getMsgData().toStringUtf8();
        } else {
            IMBaseDefine.ExtInfo extInfo = msgInfo.getExtInfo();
            msgType = getJavaMsgTypeNew(extInfo.getMsgType());
            content = extInfo.getMsgData().toStringUtf8();
        }

        ProductCardMessage productCardMessage = new ProductCardMessage();
        productCardMessage.setFromId(msgInfo.getFromSessionId());
        productCardMessage.setMsgId(msgInfo.getMsgId());
        productCardMessage.setMsgType(msgType);
        productCardMessage.setStatus(MessageConstant.MSG_SUCCESS);
        productCardMessage.setDisplayType(DBConstant.SHOW_PRODUCT_CARD_TYPE);
        productCardMessage.setCreated(msgInfo.getCreateTime());
        productCardMessage.setUpdated(msgInfo.getCreateTime());
        productCardMessage.setContent(content);
        productCardMessage.setServiceId(msgInfo.getMsgItem());

        try {
            JSONObject jsonObject = new JSONObject(content);
            productCardMessage.setType(jsonObject.getInt("TYPE"));
            JSONObject extraObject = jsonObject.getJSONObject("EXTRA");
            productCardMessage.setSubType(extraObject.getString("SUB_TYPE"));
            JSONObject dataObject = extraObject.getJSONObject("DATA");
            productCardMessage.setTitle(dataObject.getString("title"));
            productCardMessage.setProductId(dataObject.getInt("id"));
            productCardMessage.setImgUrl(dataObject.getString("img_url"));
            if (dataObject.has("summary")) {
                productCardMessage.setSummary(dataObject.getString("summary"));
            }
            productCardMessage.setPrice(dataObject.getLong("price"));

        } catch (JSONException e) {
            return null;
        }
        return productCardMessage;
    }

    private static MessageEntity analyzeNotify(IMBaseDefine.MsgInfo msgInfo) {

        int msgType;
        String content;
        if (!msgInfo.hasExtInfo()) {
            msgType = getJavaMsgType(msgInfo.getMsgType());
            content = msgInfo.getMsgData().toStringUtf8();
        } else {
            IMBaseDefine.ExtInfo extInfo = msgInfo.getExtInfo();
            msgType = getJavaMsgTypeNew(extInfo.getMsgType());
            content = extInfo.getMsgData().toStringUtf8();
        }

        NotifyMessage NotifyMessage = new NotifyMessage();
        NotifyMessage.setFromId(msgInfo.getFromSessionId());
        NotifyMessage.setMsgId(msgInfo.getMsgId());
        NotifyMessage.setMsgType(msgType);
        NotifyMessage.setStatus(MessageConstant.MSG_SUCCESS);
        NotifyMessage.setDisplayType(DBConstant.SHOW_NOTIFY);
        NotifyMessage.setCreated(msgInfo.getCreateTime());
        NotifyMessage.setUpdated(msgInfo.getCreateTime());
        String desMessage = content;
        try {
            JSONObject object = new JSONObject(desMessage);
            desMessage = object.optString(String.valueOf(IMLoginManager.instance().getLoginId()));
        } catch (JSONException e) {
            return null;
        }

        NotifyMessage.setContent(desMessage);
        NotifyMessage.setServiceId(msgInfo.getMsgItem());
        return NotifyMessage;
    }

    public static MessageEntity analyzeText(IMBaseDefine.MsgInfo msgInfo) {
        return MsgAnalyzeEngine.analyzeMessage(msgInfo);
    }

    public static AudioMessage analyzeAudio(IMBaseDefine.MsgInfo msgInfo) throws JSONException, UnsupportedEncodingException {

        int msgType;
        ByteString bytes;
        if (!msgInfo.hasExtInfo()) {
            msgType = getJavaMsgType(msgInfo.getMsgType());
            bytes = msgInfo.getMsgData();
        } else {
            IMBaseDefine.ExtInfo extInfo = msgInfo.getExtInfo();
            msgType = getJavaMsgTypeNew(extInfo.getMsgType());
            bytes = extInfo.getMsgData();
        }
        AudioMessage audioMessage = new AudioMessage();
        audioMessage.setFromId(msgInfo.getFromSessionId());
        audioMessage.setMsgId(msgInfo.getMsgId());
        audioMessage.setMsgType(msgType);
        audioMessage.setStatus(MessageConstant.MSG_SUCCESS);
        audioMessage.setReadStatus(MessageConstant.AUDIO_UNREAD);
        audioMessage.setDisplayType(DBConstant.SHOW_AUDIO_TYPE);
        audioMessage.setCreated(msgInfo.getCreateTime());
        audioMessage.setUpdated(msgInfo.getCreateTime());
        audioMessage.setServiceId(msgInfo.getMsgItem());

        byte[] audioStream = bytes.toByteArray();
        if (audioStream.length < 4) {
            audioMessage.setReadStatus(MessageConstant.AUDIO_READED);
            audioMessage.setAudioPath("");
            audioMessage.setAudiolength(0);
        } else {
            int msgLen = audioStream.length;
            byte[] playTimeByte = new byte[4];
            byte[] audioContent = new byte[msgLen - 4];

            System.arraycopy(audioStream, 0, playTimeByte, 0, 4);
            System.arraycopy(audioStream, 4, audioContent, 0, msgLen - 4);
            int playTime = CommonUtil.byteArray2int(playTimeByte);
            String audioSavePath = FileUtil.saveAudioResourceToFile(audioContent, audioMessage.getFromId());
            audioMessage.setAudiolength(playTime);
            audioMessage.setAudioPath(audioSavePath);
        }

        /**抽离出来 或者用gson*/
        JSONObject extraContent = new JSONObject();
        extraContent.put("audioPath", audioMessage.getAudioPath());
        extraContent.put("audiolength", audioMessage.getAudiolength());
        extraContent.put("readStatus", audioMessage.getReadStatus());
        String audioContent = extraContent.toString();
        audioMessage.setContent(audioContent);

        return audioMessage;
    }


    public static MessageEntity getMessageEntity(IMMessage.IMMsgData msgData) {
        IMBaseDefine.MsgInfo msgInfo;
        IMBaseDefine.MsgInfo.Builder builder = IMBaseDefine.MsgInfo.newBuilder()

                .setMsgData(msgData.getMsgData())
                .setMsgId(msgData.getMsgId())
                .setMsgType(msgData.getMsgType())
                .setCreateTime(msgData.getCreateTime())
                .setFromSessionId(msgData.getFromUserId()).setMsgItem(msgData.getMsgItem());

        if (!msgData.hasExtInfo()) {
            msgInfo = builder.build();
        } else {
            IMBaseDefine.ExtInfo extInfo = msgData.getExtInfo();
            msgInfo = builder.setExtInfo(extInfo).build();
        }
        MessageEntity messageEntity = getMessageEntity(msgInfo);
        if (messageEntity != null) {
            messageEntity.setToId(msgData.getToSessionId());
        }
        return messageEntity;
    }

    public static UnreadEntity getUnreadEntity(IMBaseDefine.UnreadInfo pbInfo) {
        UnreadEntity unreadEntity = new UnreadEntity();
        int sessionType;
        String msgData;
        if (!pbInfo.hasExtInfo()) {
            sessionType = getJavaSessionType(pbInfo.getSessionType());
            msgData = pbInfo.getLatestMsgData().toString();
        } else {
            IMBaseDefine.ExtInfo extInfo = pbInfo.getExtInfo();
            sessionType = getJavaSessionTypeNew(extInfo.getSessionType());
            msgData = extInfo.getMsgData().toStringUtf8();
        }
        if (sessionType == -1) return null;
        unreadEntity.setSessionType(sessionType);
        unreadEntity.setLatestMsgData(msgData);
        unreadEntity.setPeerId(pbInfo.getSessionId());
        unreadEntity.setLaststMsgId(pbInfo.getLatestMsgId());
        unreadEntity.setUnReadCnt(pbInfo.getUnreadCnt());
        unreadEntity.setServiceId(pbInfo.getMsgItem());
        unreadEntity.buildSessionKey();
        return unreadEntity;
    }

    /**
     * ----enum 转化接口--
     */
    public static int getJavaMsgType(IMBaseDefine.MsgType msgType) {
        switch (msgType) {
            case MSG_TYPE_GROUP_TEXT:
                return DBConstant.MSG_TYPE_GROUP_TEXT;
            case MSG_TYPE_GROUP_AUDIO:
                return DBConstant.MSG_TYPE_GROUP_AUDIO;
            case MSG_TYPE_SINGLE_AUDIO:
                return DBConstant.MSG_TYPE_SINGLE_AUDIO;
            case MSG_TYPE_SINGLE_TEXT:
                return DBConstant.MSG_TYPE_SINGLE_TEXT;
            case MSG_TYPE_SINGLE_PRODUCT_CARD:
                return DBConstant.MSG_TYPE_SINGLE_PRODUCT_CARD;
            case MSG_TYPE_CONSULT_NOTIFY:
                return DBConstant.MSG_TYPE_CONSULT_NOTIFY;
            case MSG_TYPE_CONSULT_AUDIO:
                return DBConstant.MSG_TYPE_CONSULT_AUDIO;
            case MSG_TYPE_CONSULT_TEXT:
                return DBConstant.MSG_TYPE_CONSULT_TEXT;
//            case MSG_TYPE_GROUP_PRODUCT_CARD:
//                return DBConstant.MSG_TYPE_GROUP_PRODUCT_CARD;
            default:
                return DBConstant.MSG_TYPE_UNKNOW;
        }
    }

    public static int getJavaMsgTypeNew(int msgType) {
        switch (msgType) {
            case IMBaseDefine.ExtMsgType.MSG_TYPE_GROUP_TEXT_COMPAT_VALUE:
                return DBConstant.MSG_TYPE_GROUP_TEXT;
            case IMBaseDefine.ExtMsgType.MSG_TYPE_GROUP_AUDIO_COMPAT_VALUE:
                return DBConstant.MSG_TYPE_GROUP_AUDIO;
            case IMBaseDefine.ExtMsgType.MSG_TYPE_SINGLE_AUDIO_COMPAT_VALUE:
                return DBConstant.MSG_TYPE_SINGLE_AUDIO;
            case IMBaseDefine.ExtMsgType.MSG_TYPE_SINGLE_TEXT_COMPAT_VALUE:
                return DBConstant.MSG_TYPE_SINGLE_TEXT;
            case IMBaseDefine.ExtMsgType.MSG_TYPE_SINGLE_PRODUCT_CARD_COMPAT_VALUE:
                return DBConstant.MSG_TYPE_SINGLE_PRODUCT_CARD;
            case IMBaseDefine.ExtMsgType.MSG_TYPE_CONSULT_NOTIFY_COMPAT_VALUE:
                return DBConstant.MSG_TYPE_CONSULT_NOTIFY;
            case IMBaseDefine.ExtMsgType.MSG_TYPE_CONSULT_AUDIO_COMPAT_VALUE:
                return DBConstant.MSG_TYPE_CONSULT_AUDIO;
            case IMBaseDefine.ExtMsgType.MSG_TYPE_CONSULT_TEXT_COMPAT_VALUE:
                return DBConstant.MSG_TYPE_CONSULT_TEXT;
            case IMBaseDefine.ExtMsgType.MSG_TYPE_CONSULT_KNOWLEDGE_BASE_VALUE:
                return DBConstant.MSG_TYPE_CONSULT_KNOWLEDGE;
            default:
                return DBConstant.MSG_TYPE_UNKNOW;

        }
    }


    public static int getJavaSessionType(IMBaseDefine.SessionType sessionType) {
        switch (sessionType) {
            case SESSION_TYPE_SINGLE:
                return DBConstant.SESSION_TYPE_SINGLE;
            case SESSION_TYPE_GROUP:
                return DBConstant.SESSION_TYPE_GROUP;
            case SESSION_TYPE_CONSULT:
                return DBConstant.SESSION_TYPE_CONSULT;
            default:
                return -1;
        }
    }

    public static int getJavaSessionTypeNew(int sessionType) {
        switch (sessionType) {
            case IMBaseDefine.ExtSessionType.SESSION_TYPE_SINGLE_COMPAT_VALUE:
                return DBConstant.SESSION_TYPE_SINGLE;
            case IMBaseDefine.ExtSessionType.SESSION_TYPE_GROUP_COMPAT_VALUE:
                return DBConstant.SESSION_TYPE_GROUP;
            case IMBaseDefine.ExtSessionType.SESSION_TYPE_CONSULT_COMPAT_VALUE:
                return DBConstant.SESSION_TYPE_CONSULT;
            default:
                return -1;
        }
    }

    public static int getJavaGroupType(IMBaseDefine.GroupType groupType) {
        switch (groupType) {
            case GROUP_TYPE_NORMAL:
                return DBConstant.GROUP_TYPE_NORMAL;
            case GROUP_TYPE_TMP:
                return DBConstant.GROUP_TYPE_TEMP;
            default:
                throw new IllegalArgumentException("sessionType is illegal,cause by #getProtoSessionType#" + groupType);
        }
    }

    public static int getGroupChangeType(IMBaseDefine.GroupModifyType modifyType) {
        switch (modifyType) {
            case GROUP_MODIFY_TYPE_ADD:
                return DBConstant.GROUP_MODIFY_TYPE_ADD;
            case GROUP_MODIFY_TYPE_DEL:
                return DBConstant.GROUP_MODIFY_TYPE_DEL;
            default:
                throw new IllegalArgumentException("GroupModifyType is illegal,cause by " + modifyType);
        }
    }

    public static int getDepartStatus(IMBaseDefine.DepartmentStatusType statusType) {
        switch (statusType) {
            case DEPT_STATUS_OK:
                return DBConstant.DEPT_STATUS_OK;
            case DEPT_STATUS_DELETE:
                return DBConstant.DEPT_STATUS_DELETE;
            default:
                throw new IllegalArgumentException("getDepartStatus is illegal,cause by " + statusType);
        }

    }

}
