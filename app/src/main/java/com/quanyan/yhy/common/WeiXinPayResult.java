package com.quanyan.yhy.common;

import com.tencent.mm.opensdk.modelbase.BaseResp;

/**
 * Created with Android Studio.
 * Title:WeiXinPayResult
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-2-1
 * Time:16:44
 * Version 1.0
 */

public class WeiXinPayResult {
    private BaseResp resp;

    public WeiXinPayResult(BaseResp resp) {
        this.resp = resp;
    }

    public BaseResp getResp() {
        return resp;
    }
}
