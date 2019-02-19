package com.quanyan.yhy.ui.circles;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.CacheManager;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.discover.TopicInfoResultList;

/**
 * Created with Android Studio.
 * Title:CirclesController
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/6/21
 * Time:09:29
 * Version 1.1.0
 */
public class CirclesController extends BaseController {
    public static final int MSG_TOPIC_LIST_OK = 0x80001;
    public static final int MSG_TOPIC_LIST_ERROR = 0x80002;

    public CirclesController(Context context, Handler handler) {
        super(context, handler);
    }

    /**
     *
     * @param context
     * @param pageIndex
     * @param pageSize
     * @param type 1.推荐 2.全部
     * @param startNum
     */
    public void getCirclesList(final Context context, final int pageIndex, final int pageSize, final int type, final long startNum){
        NetManager.getInstance(context).doGetTopicPageList(pageIndex, pageSize, type, startNum, new OnResponseListener<TopicInfoResultList>() {
            @Override
            public void onComplete(boolean isOK, TopicInfoResultList result, int errorCode, String errorMsg) {
                if(isOK){
                    if(result == null){
                        result = new TopicInfoResultList();
                    }else{
                        new CacheManager(context,mUiHandler).saveTopicListData(result);
                    }
                    sendMessage(MSG_TOPIC_LIST_OK, result);
                    return;
                }
                sendMessage(MSG_TOPIC_LIST_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(MSG_TOPIC_LIST_ERROR, errorCode, 0, errorMessage);
            }
        });
    }
}
