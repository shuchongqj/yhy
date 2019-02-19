package com.yhy.network.req.user;

import com.yhy.network.annotations.Req;
import com.yhy.network.req.BaseRequest;
import com.yhy.network.utils.SecurityType;

@Req(name = "user.logout")
public class LogoutReq extends BaseRequest {


    public LogoutReq() {
        setSecurityType(SecurityType.UserLogin);
    }

}
