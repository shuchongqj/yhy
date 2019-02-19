package com.yhy.network.req.user;

import com.yhy.network.annotations.Req;
import com.yhy.network.req.BaseRequest;
import com.yhy.network.utils.SecurityType;

@Req(name = "user.login")
public class LoginReq extends BaseRequest {

    private String mobile;
    private String password;

    public LoginReq() {
        setSecurityType(SecurityType.RegisteredDevice);
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
