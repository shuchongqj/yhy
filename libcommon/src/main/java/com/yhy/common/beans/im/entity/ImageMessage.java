package com.yhy.common.beans.im.entity;

import android.text.TextUtils;

import com.yhy.common.beans.album.MediaItem;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.MessageConstant;
import com.yhy.common.utils.SequenceNumberMaker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * @author : yingmu on 14-12-31.
 * @email : yingmu@mogujie.com.
 */
public class ImageMessage extends MessageEntity implements Serializable {

    /**
     * 本地保存的path
     */
    private String path = "";
    /**
     * 图片的网络地址
     */
    private String url = "";
    private String thumbnailUrl = "";
    private int loadStatus;

    //存储图片消息
//    private static java.util.HashMap<Long, ImageMessage> imageMessageMap = new java.util.HashMap<Long, ImageMessage>();
//    private static ArrayList<MessageEntity> mediaList = null;

    /**
     * 添加一条图片消息
     *
     * @param msg
     */
//    public static synchronized void addToImageMessageList(ImageMessage msg) {
//        try {
//            if (msg != null && msg.getId() != null) {
//                imageMessageMap.put(msg.getId(), msg);
//            }
//        } catch (Exception e) {
//        }
//    }

    /**
     * 获取图片列表
     *
     * @return
     */
//    public static ArrayList<MessageEntity> getImageMessageList() {
//        mediaList = new ArrayList<>();
//        java.util.Iterator it = imageMessageMap.keySet().iterator();
//        while (it.hasNext()) {
//            mediaList.add(imageMessageMap.get(it.next()));
//        }
//        return mediaList;
//    }
//
//    /**
//     * 清除图片列表
//     */
//    public static synchronized void clearImageMessageList() {
//        imageMessageMap.clear();
//        imageMessageMap.clear();
//    }


    public ImageMessage() {
        msgId = SequenceNumberMaker.getInstance().makelocalUniqueMsgId();
    }

    /**
     * 消息拆分的时候需要
     */
    private ImageMessage(MessageEntity entity) {
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

    /**
     * 接受到网络包，解析成本地的数据
     */
    public static ImageMessage parseFromNet(MessageEntity entity) throws JSONException {
        String strContent = entity.getContent();
        // 判断开头与结尾
        if (strContent.startsWith(MessageConstant.IMAGE_MSG_START)
                && strContent.endsWith(MessageConstant.IMAGE_MSG_END)) {
            // image message todo 字符串处理下
            ImageMessage imageMessage = new ImageMessage(entity);
            imageMessage.setDisplayType(DBConstant.SHOW_IMAGE_TYPE);
            String imageUrl = strContent.substring(MessageConstant.IMAGE_MSG_START.length());
            imageUrl = imageUrl.substring(0, imageUrl.indexOf(MessageConstant.IMAGE_MSG_END));

            /**抽离出来 或者用gson*/
            JSONObject extraContent = new JSONObject();
            extraContent.put("path", "");
            extraContent.put("url", imageUrl);
            extraContent.put("loadStatus", MessageConstant.IMAGE_UNLOAD);
            String imageContent = extraContent.toString();
            imageMessage.setContent(imageContent);

            imageMessage.setUrl(imageUrl.isEmpty() ? null : imageUrl);
            imageMessage.setContent(strContent);
            imageMessage.setLoadStatus(MessageConstant.IMAGE_UNLOAD);
            imageMessage.setStatus(MessageConstant.MSG_SUCCESS);
            return imageMessage;
        } else {
            throw new RuntimeException("no image type,cause by [start,end] is wrong!");
        }
    }


    public static ImageMessage parseFromDB(MessageEntity entity) {
        if (entity.getDisplayType() != DBConstant.SHOW_IMAGE_TYPE) {
            throw new RuntimeException("#ImageMessage# parseFromDB,not SHOW_IMAGE_TYPE");
        }
        ImageMessage imageMessage = new ImageMessage(entity);
        String originContent = entity.getContent();
        JSONObject extraContent;
        try {
            extraContent = new JSONObject(originContent);
            imageMessage.setPath(extraContent.getString("path"));
            imageMessage.setUrl(extraContent.getString("url"));
            int loadStatus = extraContent.getInt("loadStatus");

            //todo temp solution
            if (loadStatus == MessageConstant.IMAGE_LOADING) {
                loadStatus = MessageConstant.IMAGE_UNLOAD;
            }
            imageMessage.setLoadStatus(loadStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return imageMessage;
    }

    // 消息页面，发送图片消息
    public static ImageMessage buildForSend(MediaItem item, UserEntity fromUser, PeerEntity peerEntity, int sessionType, long serviceId) {
        ImageMessage msg = new ImageMessage();
        if (!TextUtils.isEmpty(item.getMediaPath()) && new File(item.getMediaPath()).exists()) {
            msg.setPath(item.getMediaPath());
        } else if (!TextUtils.isEmpty(item.getThumbnailPath()) && new File(item.getThumbnailPath()).exists()) {
            msg.setPath(item.getThumbnailPath());
        } else {
            msg.setPath(null);
        }
        // 将图片发送至服务器
        int nowTime = (int) (System.currentTimeMillis() / 1000);

        msg.setFromId(fromUser.getPeerId());
        msg.setToId(peerEntity.getPeerId());
        msg.setCreated(nowTime);
        msg.setUpdated(nowTime);
        msg.setDisplayType(DBConstant.SHOW_IMAGE_TYPE);
        // content 自动生成的
        if (sessionType == DBConstant.SESSION_TYPE_SINGLE){
            msg.setMsgType(DBConstant.MSG_TYPE_SINGLE_TEXT);
        }else if (sessionType == DBConstant.SESSION_TYPE_CONSULT){
            msg.setMsgType(DBConstant.MSG_TYPE_CONSULT_TEXT);
        }
        msg.setServiceId(serviceId);

        msg.setStatus(MessageConstant.MSG_SENDING);
        msg.setLoadStatus(MessageConstant.IMAGE_UNLOAD);
        msg.buildSessionKey(true);
        return msg;
    }

    public static ImageMessage buildForSend(String takePhotoSavePath, UserEntity fromUser, PeerEntity peerEntity,int sessionType,long serviceId) {
        ImageMessage imageMessage = new ImageMessage();
        int nowTime = (int) (System.currentTimeMillis() / 1000);
        imageMessage.setFromId(fromUser.getPeerId());
        imageMessage.setToId(peerEntity.getPeerId());
        imageMessage.setUpdated(nowTime);
        imageMessage.setCreated(nowTime);
        imageMessage.setDisplayType(DBConstant.SHOW_IMAGE_TYPE);
        imageMessage.setPath(takePhotoSavePath);
        if (sessionType == DBConstant.SESSION_TYPE_SINGLE){
            imageMessage.setMsgType(DBConstant.MSG_TYPE_SINGLE_TEXT);
        }else if (sessionType == DBConstant.SESSION_TYPE_CONSULT){
            imageMessage.setMsgType(DBConstant.MSG_TYPE_CONSULT_TEXT);
        }
        imageMessage.setServiceId(serviceId);
        imageMessage.setStatus(MessageConstant.MSG_SENDING);
        imageMessage.setLoadStatus(MessageConstant.IMAGE_UNLOAD);
        imageMessage.buildSessionKey(true);
        return imageMessage;
    }

    /**
     * Not-null value.
     */
    @Override
    public String getContent() {
        JSONObject extraContent = new JSONObject();
        try {
            extraContent.put("path", path);
            extraContent.put("url", url);
            extraContent.put("loadStatus", loadStatus);
            String imageContent = extraContent.toString();
            return imageContent;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] getSendContent() {
        // 发送的时候非常关键
        String sendContent = MessageConstant.IMAGE_MSG_START
                + url + MessageConstant.IMAGE_MSG_END;
        /**
         * 加密
         */
//       String  encrySendContent =new String(com.mogujie.tt.Security.getInstance().EncryptMsg(sendContent));
        String encrySendContent = sendContent;
        try {
            return encrySendContent.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * -----------------------set/get------------------------
     */
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        thumbnailUrl = url;
        if (TextUtils.isEmpty(thumbnailUrl)) {
            return;
        }
        String format = "";
        int index = thumbnailUrl.lastIndexOf(".");
        if (index > 0) {
            format = thumbnailUrl.substring(index, thumbnailUrl.length());
            thumbnailUrl = thumbnailUrl.substring(0, index);
//            thumbnailUrl = thumbnailUrl + "_" + size + "x" + size + format;
            thumbnailUrl = thumbnailUrl  + format;
        }
    }

    public int getLoadStatus() {
        return loadStatus;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setLoadStatus(int loadStatus) {
        this.loadStatus = loadStatus;
    }
}
