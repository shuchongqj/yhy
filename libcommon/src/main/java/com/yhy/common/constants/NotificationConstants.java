package com.yhy.common.constants;

/**
 * Created with Android Studio.
 * Title:JsonConstance
 * Description:解析json字符串的常量
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2015/12/15
 * Time:17:07
 * Version 1.0
 */
public class NotificationConstants {

    /**
     * 通知消息 start
     */
    //业务类型
    public static final String KEY_BIZ_TYPE = "bt";
    //业务子类型
    public static final String KEY_BIZ_SUB_TYPE = "bst";
    //push端业务类型
    public static final String KEY_MESSAGE_BIZ_TYPE = "mbt";
    public static final String KEY_MESSAGE_BIZ_SUB_TYPE = "mbst";
    //消息创建时间
    public static final String KEY_CREATE_TIME = "cd";
    public static final String KEY_CREATER_TIME = "createrTime";
    public static final String KEY_NICK_NAME = "nickName";
    public static final String KEY_SUBJECT_IMAGE = "subjectImage";
    public static final String KEY_SUBJECT_CONTENT = "subjectContent";
    public static final String KEY_USER_PHOTO = "userPhoto";
    public static final String KEY_O_ID = "outId";
    public static final String KEY_REPLOY_NAME = "reployName";
    public static final String KEY_BUS_TYPE = "busType";
    //通知栏跳转
    public static final String KEY_NTF_CODE = "noc";
    public static final String KEY_NTF_VAULE = "nov";
    //消息跳转
    public static final String KEY_MSG_CODE = "moc";
    public static final String KEY_MSG_VAULE = "mov";

    //消息列表显示内容和标题
    public static final String KEY_MSG_TITLE = "mt";
    public static final String KEY_MSG_CONTENT = "mc";

    //消息id
    public static final String KEY_MESSAGE_ID = "mid";
    //外部id（跳转页面可使用）
    public static final String KEY_OUT_ID = "oid";
    public static final String KEY_DATA = "data";
    /**
     * 通知消息 end
     */
    public static final int BIZ_TYPE_TRANSACTION = 1;
    public static final int BIZ_TYPE_INTERACTION = 2;
    public static final int BIZ_TYPE_SYSTEM = 3;
    public static final int LOGIN_FIRST_BEGIN = 5;
    public static final String TITLE_TRANSACTION = "订单通知";
    public static final String TITLE_SYSTEM = "系统通知";
    public static final String BIZ_ORDER_ID = "bizOrderId";
    public static final String ORDER_TYPE = "orderType";

    public static final int RECEIVE_COMMENT = 2001;             // 收到评论/回复
    public static final int RECEIVE_PRAISE = 2002;              // 收到点赞
    public static final int ARTICLE_COMMENT = 2003;             // 文章收到评论/回复
    public static final int ARTICLE_PRAISE = 2004;              // 文章收到赞
    public static final int WANDERFULL_VIDEO_COMMENT = 2005;    // 精彩视频评论/回复
    public static final int WANDERFULL_VIDEO_PRAISE = 2006;     // 精彩视频赞
    public static final int MATCH_VIDEO_COMMENT = 2007;         // 赛事视频评论/回复
    public static final int MATCH_VIDEO_PRAISE = 2008;          // 赛事视频赞
    public static final int COMMENT_PRAISE = 2009;

    public static final int LOGIN_FIRST_BEGIN_SUB_TYPE = 5002;


    public static final String KEY_CREATE_TIME_CONTENT = "点赞";

    public static final int BUS_TYPE_USER = 1;

    public static final String KEY_VIDEO_PIC_URL = "videoPicUrl";
    public static final String VERSION = "v";
    public static final String VERSION_2 = "2.0";
}
