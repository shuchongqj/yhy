package com.yhy.common.beans.im.entity;

import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.MessageConstant;
import com.yhy.common.utils.SequenceNumberMaker;

import java.io.Serializable;

/**
 * @author : yingmu on 14-12-31.
 * @email : yingmu@mogujie.com.
 */
public class UnknownMessage extends MessageEntity implements Serializable {
    public static final String CONTENT = "当前客户端版本过低，无法显示此消息，请更新至最新版本。";

    public UnknownMessage() {
        msgId = SequenceNumberMaker.getInstance().makelocalUniqueMsgId();
    }

    private UnknownMessage(MessageEntity entity) {
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

    public static UnknownMessage parseFromNet(MessageEntity entity) {
        UnknownMessage textMessage = new UnknownMessage(entity);
        textMessage.setStatus(MessageConstant.MSG_SUCCESS);
        textMessage.setDisplayType(DBConstant.SHOW_UNKNOWN);
        return textMessage;
    }

    public static UnknownMessage parseFromDB(MessageEntity entity) {
        if (entity.getDisplayType() != DBConstant.SHOW_UNKNOWN) {
            throw new RuntimeException("#TextMessage# parseFromDB,not SHOW_ORIGIN_TEXT_TYPE");
        }
        UnknownMessage textMessage = new UnknownMessage(entity);
        return textMessage;
    }

    /**
     * Not-null value.
     * DB的时候需要
     */
    @Override
    public String getContent() {
        return CONTENT;
    }

    @Override
    public byte[] getSendContent() {
        return null;
    }
}
