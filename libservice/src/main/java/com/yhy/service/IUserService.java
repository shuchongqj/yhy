package com.yhy.service;


import com.alibaba.android.arouter.facade.template.IProvider;
import com.yhy.common.beans.user.User;

/**
 * 用户相关的service
 *
 */
public interface IUserService extends IProvider{

    void saveUserInfo(User user);

    boolean isLogin();

    boolean isLoginUser(long userId);

    long getLoginUserId();

    /**
     * 获取用户信息，会先返回缓存中的信息，并从网络去获取<br>
     *     当最新的用户信息获取到后会发送event EvBusGetUserInfo - 用户信息获取事件，获取失败不通知
     * @param userId
     * @return 缓存中的用户信息，可能为null
     */
    User getUserInfo(long userId);

    /**
     * 获取用户额外信息，包括粉丝数之类的、会员等级。
     *      当最新的用户信息获取到后会发送event EvBugGetUserExInfo - 用户额外信息获取事件，获取失败不通知
     * @param userId
     * @return 缓存中的用户信息，可能为null
     */
    User getUserExtInfo(long userId);
}
