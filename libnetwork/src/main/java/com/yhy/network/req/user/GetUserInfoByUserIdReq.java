package com.yhy.network.req.user;

import com.yhy.network.annotations.Req;
import com.yhy.network.req.BaseRequest;
import com.yhy.network.utils.SecurityType;

@Req(name = "user.getUserInfoByUserId")
public class GetUserInfoByUserIdReq extends BaseRequest {

    private long theUserId;

    public GetUserInfoByUserIdReq(long theUserId) {
        setSecurityType(SecurityType.None);
        this.theUserId = theUserId;
    }

    public long getTheUserId() {
        return theUserId;
    }

    public void setTheUserId(long theUserId) {
        this.theUserId = theUserId;
    }
}
