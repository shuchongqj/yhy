package com.yhy.network.req.resourcecenter;

import com.yhy.network.annotations.Req;
import com.yhy.network.req.BaseRequest;
import com.yhy.network.utils.SecurityType;

@Req(name = "resourcecenter.getMainSquareUserInfo")
public class GetMainSquareUserInfoReq extends BaseRequest {

    private P mainSquareUserInfoQuery;

    public GetMainSquareUserInfoReq(P mainSquareUserInfoQuery) {
        setSecurityType(SecurityType.UserLogin);
        this.mainSquareUserInfoQuery = mainSquareUserInfoQuery;
    }

    public P getMainSquareUserInfoQuery() {
        return mainSquareUserInfoQuery;
    }

    public void setMainSquareUserInfoQuery(P mainSquareUserInfoQuery) {
        this.mainSquareUserInfoQuery = mainSquareUserInfoQuery;
    }

    public static class P {
        private long uid;

        public P(long uid) {
            this.uid = uid;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }
    }
}
