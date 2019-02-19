package com.yhy.user;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.beans.user.User;
import com.yhy.common.beans.user.UserFriendShipInfo;
import com.yhy.common.beans.user.UserLevelInfo;
import com.yhy.common.cache.ACache;
import com.yhy.common.eventbus.event.EvBusGetUserExInfo;
import com.yhy.common.eventbus.event.EvBusGetUserInfo;
import com.yhy.common.utils.JSONUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCaller;
import com.yhy.network.YhyCode;
import com.yhy.network.api.resourcecenter.ResourceCenterApi;
import com.yhy.network.api.user.UserApi;
import com.yhy.network.manager.AccountManager;
import com.yhy.network.req.resourcecenter.GetMainSquareUserInfoReq;
import com.yhy.network.req.user.GetUserInfoByUserIdReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.resourcecenter.GetMainSquareUserInfoResp;
import com.yhy.network.resp.user.GetUserInfoByUserIdResp;
import com.yhy.network.utils.RequestHandlerKt;
import com.yhy.service.IUserService;

import java.util.concurrent.ConcurrentHashMap;

import de.greenrobot.event.EventBus;

import static com.yhy.router.RouterPath.SERVICE_USER;

@Route(path = SERVICE_USER, name = "userService")
public class UserService implements IUserService {

    //用去去除上次重复请求
    private ConcurrentHashMap<String, YhyCaller> getUserInfoCallMap;
    private ConcurrentHashMap<String, YhyCaller> getUserExInfoCallMap;

    @Override
    public void init(Context context) {
        getUserInfoCallMap = new ConcurrentHashMap<>();
        getUserExInfoCallMap = new ConcurrentHashMap<>();
    }

    @Override
    public void saveUserInfo(User user) {
        if (user == null) {
            return;
        }
        SPUtils.setNickName(YHYBaseApplication.getInstance(), user.getNickname());
        SPUtils.setUserIcon(YHYBaseApplication.getInstance(), user.getAvatar());
        SPUtils.setUserCover(YHYBaseApplication.getInstance(), user.getFrontCover());
        SPUtils.setUserHomePage(YHYBaseApplication.getInstance(), user.isHasMainPage());
        SPUtils.setUserSportHabit(YHYBaseApplication.getInstance(), user.getSportHobby());
        getCache().put(String.valueOf(user.getUserId()), JSONUtils.toJson(user));
        EventBus.getDefault().post(new EvBusGetUserInfo(user));
    }

    @Override
    public boolean isLogin() {
        return AccountManager.Companion.getAccountManager().getUserId() > 0;
    }

    @Override
    public boolean isLoginUser(long userId) {
        long uid = AccountManager.Companion.getAccountManager().getUserId();
        return isLogin() && uid == userId;
    }

    @Override
    public long getLoginUserId() {
        return AccountManager.Companion.getAccountManager().getUserId();
    }

    @Override
    public User getUserInfo(final long userId) {
        YhyCaller lastGetUserInfoCaller = new UserApi().getUserInfo(new GetUserInfoByUserIdReq(userId), new YhyCallback<Response<GetUserInfoByUserIdResp>>() {
            @Override
            public void onSuccess(Response<GetUserInfoByUserIdResp> data) {
                getUserInfoCallMap.remove(String.valueOf(userId));
                User user = new User();
                user.setUserId(userId);
                user.setAvatar(data.getContent().getAvatar());
                user.setNickname(data.getContent().getNickname());
                user.setName(data.getContent().getName());
                user.setNickname(data.getContent().getNickname());
                user.setSignature(data.getContent().getSignature());
                user.setProvinceCode(data.getContent().getProvinceCode());
                user.setProvince(data.getContent().getProvince());
                user.setCityCode(data.getContent().getCityCode());
                user.setCity(data.getContent().getCity());
                user.setAreaCode(0);
                user.setArea("");
                user.setBirthday(data.getContent().getBirthday());
                user.setHasMainPage(data.getContent().isHasMainPage());
                user.setFrontCover(data.getContent().getFrontCover());
                user.setGender(data.getContent().getGender());
                user.setSportHobby(data.getContent().getSportHobby());
                user.setUserId(data.getContent().getId());
                user.setAvatar(data.getContent().getAvatar());
                user.setAge(data.getContent().getAge());
                user.setLiveStation(data.getContent().getLiveStation());
                user.setOptions(data.getContent().getOptions());
                user.setVip(data.getContent().isVip());

                saveUserInfo(user);
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {
                getUserInfoCallMap.remove(String.valueOf(userId));
            }
        }).execAsync();
        lastGetUserInfoCaller = getUserInfoCallMap.putIfAbsent(String.valueOf(userId), lastGetUserInfoCaller);
        if (lastGetUserInfoCaller != null) {
            lastGetUserInfoCaller.cancel();
        }
        return JSONUtils.convertToObject(getCache().getAsString(String.valueOf(userId)), User.class);
    }

    @Override
    public User getUserExtInfo(long userId) {
        YhyCaller lastGetUserInfoCaller = new ResourceCenterApi().getMainSquareUserInfo(new GetMainSquareUserInfoReq(new GetMainSquareUserInfoReq.P(userId)), new YhyCallback<Response<GetMainSquareUserInfoResp>>() {
            @Override
            public void onSuccess(Response<GetMainSquareUserInfoResp> data) {
                getUserExInfoCallMap.remove(String.valueOf(userId));
                User user = new User();
                user.setUserId(userId);

                UserFriendShipInfo userFriendShipInfo = new UserFriendShipInfo();
                userFriendShipInfo.setFansCount(data.getContent().getFansCount());
                userFriendShipInfo.setFollowedCount(data.getContent().getFollowedCount());
                userFriendShipInfo.setUgcCount(data.getContent().getUgcCount());

                user.setUserFriendShipInfo(userFriendShipInfo);

                UserLevelInfo userLevelInfo = new UserLevelInfo();
                if (data.getContent().getMemberLevel() != null) {
                    userLevelInfo.setExpireTime(data.getContent().getMemberLevel().getExpireTime());
                    userLevelInfo.setLevelId(data.getContent().getMemberLevel().getLevelId());
                    userLevelInfo.setLevelMaxPoint(data.getContent().getMemberLevel().getLevelMaxPoint());
                    userLevelInfo.setLevelMinPoint(data.getContent().getMemberLevel().getLevelMinPoint());
                    userLevelInfo.setLevelName(data.getContent().getMemberLevel().getLevelName());
                    userLevelInfo.setLevelUrl(data.getContent().getMemberLevel().getLevelUrl());
                    userLevelInfo.setMemberLevelPoint(data.getContent().getMemberLevel().getMemberLevelPoint());
                    userLevelInfo.setPointNumId(data.getContent().getMemberLevel().getPointNumId());
                    userLevelInfo.setStartTime(data.getContent().getMemberLevel().getStartTime());

                } else {
                    // 为什么这个服务器会返回null  也是醉了  我就塞一遍默认值吧
                    userLevelInfo.setExpireTime(0);
                    userLevelInfo.setLevelId(0);
                    userLevelInfo.setLevelMaxPoint(0);
                    userLevelInfo.setLevelMinPoint(0);
                    userLevelInfo.setLevelName("");
                    userLevelInfo.setLevelUrl("");
                    userLevelInfo.setMemberLevelPoint(0);
                    userLevelInfo.setPointNumId(0);
                    userLevelInfo.setStartTime(0);

                }
                user.setUserLevelInfo(userLevelInfo);
                getCache().put(String.valueOf(userId) + "_ex_info", JSONUtils.toJson(user));
                EventBus.getDefault().post(new EvBusGetUserExInfo(user));
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {
                getUserExInfoCallMap.remove(String.valueOf(userId));
            }
        }).execAsync();
        lastGetUserInfoCaller = getUserExInfoCallMap.putIfAbsent(String.valueOf(userId) + "_ex_info", lastGetUserInfoCaller);
        if (lastGetUserInfoCaller != null) {
            lastGetUserInfoCaller.cancel();
        }
        return JSONUtils.convertToObject(getCache().getAsString(String.valueOf(userId) + "_ex_info"), User.class);

    }


    private ACache getCache() {
        return ACache.get(YHYBaseApplication.getInstance(), "user");
    }
}
