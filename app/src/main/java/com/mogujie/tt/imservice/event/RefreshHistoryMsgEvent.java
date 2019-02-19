package com.mogujie.tt.imservice.event;

import com.yhy.common.beans.im.entity.MessageEntity;

import java.util.List;

/**
 * @author : yingmu on 15-3-26.
 * @email : yingmu@mogujie.com.
 *
 * 异步刷新历史消息
 */
public class RefreshHistoryMsgEvent {
   public int pullTimes;
   public int lastMsgId;
   public int count;
   public List<MessageEntity> listMsg;
   public long peerId;
   public int peerType;
   public String sessionKey;
   public long serviceId;

   public RefreshHistoryMsgEvent(){}

}
