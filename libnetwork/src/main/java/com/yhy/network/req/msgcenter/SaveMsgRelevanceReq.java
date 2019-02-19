package com.yhy.network.req.msgcenter;

import com.yhy.common.utils.JSONUtils;
import com.yhy.network.annotations.Param;
import com.yhy.network.annotations.Req;
import com.yhy.network.req.BaseRequest;
import com.yhy.network.utils.SecurityType;

@Req(name = "msgcenter.saveMsgRelevance")
public class SaveMsgRelevanceReq extends BaseRequest {

    private String msgRelevanceInfo;

    public SaveMsgRelevanceReq(String deviceToken, String phoneNum, int phoneBrand) {
        setSecurityType(SecurityType.RegisteredDevice);
        msgRelevanceInfo = JSONUtils.toJson(new Param(deviceToken, phoneNum, phoneBrand));
    }

    public String getMsgRelevanceInfo() {
        return msgRelevanceInfo;
    }

    public void setMsgRelevanceInfo(String msgRelevanceInfo) {
        this.msgRelevanceInfo = msgRelevanceInfo;
    }

    static class Param{
        private String	deviceToken;	// 设备token
        private String	phoneNum;	// 手机号
        private int phoneBrand; //手机品牌

        public Param(String deviceToken, String phoneNum, int phoneBrand) {
            this.deviceToken = deviceToken;
            this.phoneNum = phoneNum;
            this.phoneBrand = phoneBrand;
        }

        public int getPhoneBrand() {
            return phoneBrand;
        }

        public void setPhoneBrand(int phoneBrand) {
            this.phoneBrand = phoneBrand;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }


    }
}
