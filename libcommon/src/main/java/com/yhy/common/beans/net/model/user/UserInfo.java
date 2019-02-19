package com.yhy.common.beans.net.model.user;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:UserInfo
 * Description:
 * Copyright:Copyright (c) 2014
 * Company:中国平安健康保险有限公司
 * Author:Alice
 * Date:15/9/8
 * Time:下午1:57
 * Version 1.0
 */
@Table(name = "user_infos")
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 7054790919159097603L;

    /**
     * id
     */
    @Id
    @NoAutoIncrement
    @Column(column = "id")
    public long id;
    /**
     * 用户ID
     */
    public long userId;
    /**
     * 用户角色 按位与 1:会员 2:大咖 4：部长
     */
    public long options;

    /**
     * 用户头像
     */
    public String avatar;

    /**
     * 昵称
     */
    public String nickname;

    /**
     * 用户姓名
     */
    public String name;

    /**
     * 性别 1-未确认 2-男  3-女
     */
    public String gender;

    /**
     * 生日
     */
    public long birthday;

    /**
     * 常住地 省code
     */
    public int provinceCode;

    /**
     * 常住地 市code
     */
    public int cityCode;

    /**
     * 常住地 区code
     */
    public int areaCode;

    /**
     * 个性签名
     */
    public String signature;

    /**
     * 年龄
     */
    public int age;

    /**
     * 原驻地
     */
    public String liveStation;

    /**
     * 省名称
     */
    public String province;

    /**
     * 市名称
     */
    public String city;

    /**
     * 区名称
     */
    public String area;

    /**
     * 是否是会员
     */
    public boolean vip;
    /**
     * 级别，VERIFIED：加v的,  NORMAL 普通的,DOCTOR 医生,AGENCY 机构，FAMOUS 名人
     */
    public String level;

    /**
     * 描述
     */
    public String introduction;

    /**
     * 当前用户关注类型 UNFOLLOW(不关注), FOLLOW(关注), BOTHFOLLOW(互相关注)
     */
    public String currentUserFollowStatus;

    /**
     * 粉丝数
     */
    public long followerNum;

    /**
     * 用户类型
     */
    public String userType;

//	/**
//	 * 店铺名称
//	 */
//	public String shopName;

    /**
     * 商户类型 EAT:必吃店铺/TALENT:达人店铺/MERCHANT:商家
     */
    public String sellerType;

    /**
     * 店铺Id
     */
    public long merchantId;
    /**
     * 用户头像Url
     */
    public String imgUrl;

    /**
     * 用户二维码
     */
    public String userQRCode;

    /**
     * 客服电话
     */
    @Transient
    public String serviceCall;

    /**
     * 封面
     */
    @Transient
    public String frontCover;

    /**
     * 是否有主页
     */
    public boolean isHasMainPage;

    /**
     * 昵称
     */
    public String nick;

    /**
     * 运动爱好
     */
    public int sportHobby;

    /**
     * 用户在线状态标识 在线：ONLINE / 不在线 NOTONLINE
     */
    public String onlineStatus;

    /**
     * 商品详情客服咨询传的id
     */
    public long serviceUserId;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static UserInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static UserInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            UserInfo result = new UserInfo();
            // 用户ID
            result.userId = json.optLong("userId");
            // id
            result.id = json.optLong("id");
            // 用户角色 按位与 1:会员 2:大咖 4：部长
            result.options = json.optLong("options");
            // 用户头像

            if (!json.isNull("avatar")) {
                result.avatar = json.optString("avatar", null);
            }
            // 昵称

            if (!json.isNull("nickname")) {
                result.nickname = json.optString("nickname", null);
            }

            if (!json.isNull("sportHobby")){
                result.sportHobby = json.optInt("sportHobby");
            }
            // 用户姓名

            if (!json.isNull("name")) {
                result.name = json.optString("name", null);
            }
            // 级别，VERIFIED：加v的,  NORMAL 普通的,DOCTOR 医生,AGENCY 机构，FAMOUS 名人

            if (!json.isNull("level")) {
                result.level = json.optString("level", null);
            }
            // 描述

            if (!json.isNull("introduction")) {
                result.introduction = json.optString("introduction", null);
            }
            // 当前用户关注类型 UNFOLLOW(不关注), FOLLOW(关注), BOTHFOLLOW(互相关注)

            if (!json.isNull("currentUserFollowStatus")) {
                result.currentUserFollowStatus = json.optString("currentUserFollowStatus", null);
            }
            // 粉丝数
            result.followerNum = json.optLong("followerNum");
            // 用户类型

            if (!json.isNull("userType")) {
                result.userType = json.optString("userType", null);
            }
            // 性别 1-未确认 2-男  3-女

            if (!json.isNull("gender")) {
                result.gender = json.optString("gender", null);
            }
            // 生日
            result.birthday = json.optLong("birthday");
            // 常住地 省code
            result.provinceCode = json.optInt("provinceCode");
            // 常住地 市code
            result.cityCode = json.optInt("cityCode");
            //常住地 区code
            result.areaCode = json.optInt("areaCode");
            // 个性签名

            if (!json.isNull("signature")) {
                result.signature = json.optString("signature", null);
            }
            // 年龄
            result.age = json.optInt("age");
            // 原驻地

            if (!json.isNull("liveStation")) {
                result.liveStation = json.optString("liveStation", null);
            }
            // 省名称

            if (!json.isNull("province")) {
                result.province = json.optString("province", null);
            }
            // 市名称

            if (!json.isNull("city")) {
                result.city = json.optString("city", null);
            }
            //区名称
            if (!json.isNull("area")) {
                result.area = json.optString("area", null);
            }
            // 是否是会员
            result.vip = json.optBoolean("vip");

            // 店铺名称
//			if(!json.isNull("shopName")){
//				result.shopName = json.optString("shopName", null);
//			}

            // 商户类型 具体待定
            if (!json.isNull("sellerType")) {
                result.sellerType = json.optString("sellerType", null);
            }

            // 店铺Id
            result.merchantId = json.optLong("merchantId");

            // 用户头像Url

            if (!json.isNull("imgUrl")) {
                result.imgUrl = json.optString("imgUrl", null);
            }
            // 用户二维码

            if (!json.isNull("userQRCode")) {
                result.userQRCode = json.optString("userQRCode", null);
            }
            // 客服电话

            if (!json.isNull("serviceCall")) {
                result.serviceCall = json.optString("serviceCall", null);
            }
            // 封面
            if (!json.isNull("frontCover")) {
                result.frontCover = json.optString("frontCover", null);
            }
            // 是否有主页
            result.isHasMainPage = json.optBoolean("isHasMainPage");

            if (!json.isNull("nick")) {
                result.nick = json.optString("nick", null);
            }

            if (!json.isNull("onlineStatus")) {
                result.onlineStatus = json.optString("onlineStatus", null);
            }

            // 客服Id
            if (!json.isNull("serviceUserId")) {
                result.serviceUserId = json.optLong("serviceUserId");
            }
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
// 用户ID
        json.put("userId", this.userId);
        // id
        json.put("id", this.id);

        // 用户角色 按位与 1:会员 2:大咖 4：部长
        json.put("options", this.options);

        // 用户头像
        if (this.avatar != null) {
            json.put("avatar", this.avatar);
        }

        // 昵称
        if (this.nickname != null) {
            json.put("nickname", this.nickname);
        }

        json.put("sportHobby",this.sportHobby);

        // 用户姓名
        if (this.name != null) {
            json.put("name", this.name);
        }

        // 性别 1-未确认 2-男  3-女
        if (this.gender != null) {
            json.put("gender", this.gender);
        }

        // 生日
        json.put("birthday", this.birthday);

        // 常住地 省code
        json.put("provinceCode", this.provinceCode);

        // 常住地 市code
        json.put("cityCode", this.cityCode);

        //常住地 区code
        json.put("areaCode", this.areaCode);

        // 个性签名
        if (this.signature != null) {
            json.put("signature", this.signature);
        }

        // 年龄
        json.put("age", this.age);

        // 原驻地
        if (this.liveStation != null) {
            json.put("liveStation", this.liveStation);
        }

        // 省名称
        if (this.province != null) {
            json.put("province", this.province);
        }

        // 市名称
        if (this.city != null) {
            json.put("city", this.city);
        }

        // 市名称
        if (this.area != null) {
            json.put("area", this.area);
        }

        // 是否是会员
        json.put("vip", this.vip);
        // 级别，VERIFIED：加v的,  NORMAL 普通的,DOCTOR 医生,AGENCY 机构，FAMOUS 名人
        if (this.level != null) {
            json.put("level", this.level);
        }

        // 描述
        if (this.introduction != null) {
            json.put("introduction", this.introduction);
        }

        // 当前用户关注类型 UNFOLLOW(不关注), FOLLOW(关注), BOTHFOLLOW(互相关注)
        if (this.currentUserFollowStatus != null) {
            json.put("currentUserFollowStatus", this.currentUserFollowStatus);
        }

        // 粉丝数
        json.put("followerNum", this.followerNum);

        // 用户类型
        if (this.userType != null) {
            json.put("userType", this.userType);
        }

        // 店铺名称
//		if(this.shopName != null) { json.put("shopName", this.shopName); }

        // 商户类型 具体待定
        if (this.sellerType != null) {
            json.put("sellerType", this.sellerType);
        }

        // 店铺Id
        json.put("merchantId", this.merchantId);

        // 用户头像Url
        if (this.imgUrl != null) {
            json.put("imgUrl", this.imgUrl);
        }

        // 用户二维码
        if (this.userQRCode != null) {
            json.put("userQRCode", this.userQRCode);
        }
        // 客服电话
        if (this.serviceCall != null) {
            json.put("serviceCall", this.serviceCall);
        }
        // 封面
        if (this.frontCover != null) {
            json.put("frontCover", this.frontCover);
        }

        // 是否有主页
        json.put("isHasMainPage", this.isHasMainPage);

        if (this.nick != null) {
            json.put("nick", this.nick);
        }
        if (this.onlineStatus != null) {
            json.put("onlineStatus", this.onlineStatus);
        }
        return json;
    }
}
