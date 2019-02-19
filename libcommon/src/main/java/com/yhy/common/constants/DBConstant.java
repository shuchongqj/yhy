package com.yhy.common.constants;

/**
 * @author : yingmu on 15-1-5.
 * @email : yingmu@mogujie.com.
 */
public interface DBConstant {

    int READED = 1;
    int UNREAD = 2;

    String COLUMN_BIZ_TYPE = "biz_type";
    String COLUMN_STATUS = "status";
    String COLUMN_ID = "id";
    int LIMIT = 20;
    String TABLE_NOTI_MESSAGE = "noti_message";
    /**
     * 性别
     * 1. 男性 2.女性
     */
    int SEX_MAILE = 1;
    int SEX_FEMALE = 2;

    /**
     * msgType
     */
    int MSG_TYPE_UNKNOW = 0x00;
    int MSG_TYPE_SINGLE_TEXT = 0x01;
    int MSG_TYPE_SINGLE_AUDIO = 0x02;
    int MSG_TYPE_SINGLE_PRODUCT_CARD = 0x03;
    int MSG_TYPE_GROUP_TEXT = 0x11;
    int MSG_TYPE_GROUP_AUDIO = 0x12;
    int MSG_TYPE_GROUP_PRODUCT_CARD = 0x13;
    int MSG_TYPE_CONSULT_TEXT = 0x21;
    int MSG_TYPE_CONSULT_AUDIO = 0x22;
    int MSG_TYPE_CONSULT_NOTIFY = 0x23;
    int MSG_TYPE_CONSULT_KNOWLEDGE = 0x24;

    /**
     * 通知
     */
    int MSG_TYPE_NOTIFICATION = 0x20;
    int MSG_TYPE_INTERACTION = 0x21;

    /**
     * msgDisplayType
     * 保存在DB中，与服务端一致，图文混排也是一条
     * 1. 最基础的文本信息
     * 2. 纯图片信息
     * 3. 语音
     * 4. 图文混排
     */
    int SHOW_UNKNOWN = 0;
    int SHOW_ORIGIN_TEXT_TYPE = 1;
    int SHOW_IMAGE_TYPE = 2;
    int SHOW_AUDIO_TYPE = 3;
    int SHOW_MIX_TEXT = 4;
    int SHOW_GIF_TYPE = 5;
    int SHOW_PRODUCT_CARD_TYPE = 6;
    int SHOW_NOTIFY = 7;
    int SHOW_KNOWLEDGE_TYPE = 8;


    String DISPLAY_FOR_IMAGE = "[图片]";
    String DISPLAY_FOR_MIX = "[图文消息]";
    String DISPLAY_FOR_AUDIO = "[语音]";
    String DISPLAY_FOR_UNKNOWN = "[未知消息]";
    String DISPlAY_FOR_PRODUCT_CARD = "[商品卡片]";
    String DISPLAY_FOR_KNOWLEDGE = "[知识库]";


    /**
     * sessionType
     */
    int SESSION_TYPE_SINGLE = 1;
    int SESSION_TYPE_GROUP = 2;
    int SESSION_TYPE_CONSULT = 3;
    int SESSION_TYPE_NOTIFICATION = 4;

    /**
     * user status
     * 1. 试用期 2. 正式 3. 离职 4.实习
     */
    int USER_STATUS_PROBATION = 1;
    int USER_STATUS_OFFICIAL = 2;
    int USER_STATUS_LEAVE = 3;
    int USER_STATUS_INTERNSHIP = 4;

    /**
     * group type
     */
    int GROUP_TYPE_NORMAL = 1;
    int GROUP_TYPE_TEMP = 2;

    /**
     * group status
     * 1: shield  0: not shield
     */

    int GROUP_STATUS_ONLINE = 0;
    int GROUP_STATUS_SHIELD = 1;

    /**
     * group change Type
     */
    int GROUP_MODIFY_TYPE_ADD = 0;
    int GROUP_MODIFY_TYPE_DEL = 1;

    /**
     * depart status Type
     */
    int DEPT_STATUS_OK = 0;
    int DEPT_STATUS_DELETE = 1;

}
