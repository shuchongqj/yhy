package com.yhy.network.req.device;

import com.yhy.network.annotations.Param;
import com.yhy.network.annotations.Req;
import com.yhy.network.req.BaseRequest;
import com.yhy.network.utils.SecurityType;

@Req(name = "device.register")
public class RegisterReq extends BaseRequest {

    @Param(name = "info")
    private String info;
    private String channel;
    private String alg;

    public RegisterReq() {
        setSecurityType(SecurityType.None);
    }

    public RegisterReq(String info, String channel, String alg) {
        this();
        this.info = info;
        this.channel = channel;
        this.alg = alg;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }
}
