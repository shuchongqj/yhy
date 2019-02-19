package com.mogujie.tt.imservice.event;

/**
 * Created with Android Studio.
 * Title:SendCarInfoResponse
 * Description:发送车车位置返回情况
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/11/9
 * Time:11:14
 * Version 1.0
 */
public enum SendCarInfoResponseEvent {
    //发送成功
    OK,
    //发送失败
    ERROR,
    //发送成功服务器返回解析错误
    IO_ERROR
}
