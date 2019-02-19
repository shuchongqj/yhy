package com.yhy.common.beans.im.entity;

import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.MessageConstant;
import com.yhy.common.utils.SequenceNumberMaker;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * @author : yingmu on 14-12-31.
 * @email : yingmu@mogujie.com.
 */
public class TextMessage extends MessageEntity implements Serializable {

    public TextMessage() {
        msgId = SequenceNumberMaker.getInstance().makelocalUniqueMsgId();
    }

    private TextMessage(MessageEntity entity) {
        /**父类的id*/
        id = entity.getId();
        msgId = entity.getMsgId();
        fromId = entity.getFromId();
        toId = entity.getToId();
        sessionKey = entity.getSessionKey();
        content = entity.getContent();
        msgType = entity.getMsgType();
        displayType = entity.getDisplayType();
        status = entity.getStatus();
        created = entity.getCreated();
        updated = entity.getUpdated();
        serviceId = entity.getServiceId();
    }

    public static TextMessage parseFromNet(MessageEntity entity) {
        TextMessage textMessage = new TextMessage(entity);
        textMessage.setStatus(MessageConstant.MSG_SUCCESS);
        textMessage.setDisplayType(DBConstant.SHOW_ORIGIN_TEXT_TYPE);
        return textMessage;
    }

    public static TextMessage parseFromDB(MessageEntity entity) {
        if (entity.getDisplayType() != DBConstant.SHOW_ORIGIN_TEXT_TYPE) {
            throw new RuntimeException("#TextMessage# parseFromDB,not SHOW_ORIGIN_TEXT_TYPE");
        }
        TextMessage textMessage = new TextMessage(entity);
        return textMessage;
    }

    public static TextMessage buildForSend(String content, UserEntity fromUser, PeerEntity peerEntity,int sessionType,long serviceId) {
        TextMessage textMessage = new TextMessage();
        int nowTime = (int) (System.currentTimeMillis() / 1000);
        textMessage.setFromId(fromUser.getPeerId());
        textMessage.setToId(peerEntity.getPeerId());
        textMessage.setUpdated(nowTime);
        textMessage.setCreated(nowTime);
        textMessage.setDisplayType(DBConstant.SHOW_ORIGIN_TEXT_TYPE);
        textMessage.setGIfEmo(true);
        if (sessionType == DBConstant.SESSION_TYPE_SINGLE){
            textMessage.setMsgType(DBConstant.MSG_TYPE_SINGLE_TEXT);
        }else if (sessionType == DBConstant.SESSION_TYPE_CONSULT){
            textMessage.setMsgType(DBConstant.MSG_TYPE_CONSULT_TEXT);
        }
        textMessage.setServiceId(serviceId);

        textMessage.setStatus(MessageConstant.MSG_SENDING);
        // 内容的设定
        textMessage.setContent(content);
        textMessage.buildSessionKey(true);
        return textMessage;
    }


    /**
     * Not-null value.
     * DB的时候需要
     */
    @Override
    public String getContent() {
        return content;
    }

    @Override
    public byte[] getSendContent() {
        try {
            /** 加密*/
//            String sendContent =new String(com.mogujie.tt.Security.getInstance().EncryptMsg(content));
            String sendContent = content;
            return sendContent.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
