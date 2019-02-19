package com.yhy.sport.model;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * @Description:
 * @Created by zhaolei.yang 2018-07-09 17:09
 */
@RealmClass
public class YHYLocation implements RealmModel, Serializable {
    /**
     * 用户ID，未登录用户该字段可以用DeviceUtils.obtainIdentification()替代
     */
    private String userId;

    private RealmList<RawLocation> locations;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public RealmList<RawLocation> getLocations() {
        return locations;
    }

    public void setLocations(RealmList<RawLocation> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "YHYLocation{" +
                "userId='" + userId + '\'' +
                ", locations=" + locations +
                '}';
    }
}
