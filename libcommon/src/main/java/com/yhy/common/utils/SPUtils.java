package com.yhy.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.text.TextUtils;

import com.yhy.common.DirConstants;
import com.yhy.common.beans.city.bean.AddressBean;
import com.yhy.common.beans.hotel.AddHotelSearchBean;
import com.yhy.common.config.ContextHelper;
import com.yhy.common.types.MerchantType;
import com.yhy.common.types.ResourceType;
import com.yhy.common.types.SysConfigType;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;


public class SPUtils {

    private static final String TAG = SPUtils.class.getSimpleName();
    /**
     * 默认的设置保存位置
     */
    public static final String TYPE_DEFAULT = "default";
    //保存登录状态的文件名
    public static final String TYPE_LOG_STATUS = "log_status";
    //保存推送的token
    public static final String TYPE_XG_PUSH_CONFIG = "xg_config";
    //JPush的注册ID
    public static String KEY_REGISTRATION_ID = "registration_id_xg_push_x";

    //当前所在的城市 （高德原生code）
    public static final String EXTRA_CURRENT_AMAP_CITY_CODE = "citycode";
    //当前定位的城市名称
    public static final String EXTRA_CURRENT_CITY_CODE = "city_code";
    //当前定位的城市代码
    public static final String EXTRA_CURRENT_CITY_NAME = "city_name";
    public static final String EXTRA_LOCATION_PROVINCE_NAME = "province_name";
    //历史的城市选择
    public static final String EXTRA_HISTORY_CITY_NAME = "history_city_name";
    //当前的地址
    public static final String EXTRA_CURRENT_ADDRESS = "address";
    //当前的Lat
    public static final String EXTRA_CURRENT_LAT = "lat";
    //当前的LNG
    public static final String EXTRA_CURRENT_LNG = "lng";
    private static final String EXTRA_HOME_CITY_IS_CHANGE = "home_city_change";
    private static final String EXTRA_HOME_CHANGE_CITY_NAME = "home_change_city_name";
    private static final String EXTRA_FREE_TRIP_CITY_NAME = "free_trip_city_name";
    private static final String EXTRA_PACK_TRIP_CITY_NAME = "pack_trip_city_name";
    private static final String EXTRA_LOCAL_CITY_NAME = "extra_local_city_name";
    private static final String EXTRA_ARROUND_CITY_NAME = "extra_arround_city_name";
    private static final String EXTRA_FREE_TRIP_CITY_CODE = "extra_free_trip_city_code";
    private static final String EXTRA_PACK_TRIP_CITY_CODE = "extra_pack_trip_city_code";
    private static final String EXTRA_HOME_CHANGE_CITY_CODE = "extra_home_change_city_code";
    private static final String EXTRA_LOCAL_CITY_CODE = "extra_local_city_code";
    private static final String EXTRA_ARROUND_CITY_CODE = "extra_arround_city_code";
    private static final String EXTRA_FREE_TRIP_CITY_CHANGE = "extra_free_trip_city_change";
    private static final String EXTRA_PACK_TRIP_CITY_CHANGE = "extra_pack_trip_city_change";
    private static final String EXTRA_LOCAL_CITY_CHANGE = "extra_local_city_change";
    private static final String EXTRA_ARROUND_CITY_CHANGE = "extra_arround_city_change";
    private static final String EXTRA_IS_FROM_HOMESEARCH_JUMP = "extra_is_from_homesearch_jump";
    public static final String EXTRA_GONA_DISCOVER_TYPE = "extra_gona_discover_type";
    public static final String EXTRA_TOPIC = "extra_topic";
    public static final String EXTRA_MEDIA = "extra_media";
    public static final String EXTRA_VIDEO = "extra_video";
    public static final String EXTRA_NEWINTENT_TYPE = "extra_newintent_type";
    public static final String EXTRA_CONSULT_SERVICE_STATE = "extra_consult_service_state";
    public static final String EXTRA_TO_WALLET = "toWallet";
    public static String EXTRA_CODE = "code";
    //手机号码
    private static final String KEY_MOBILE_PHONE = "mobile_phone";
    //密码
    private static final String KEY_PWD = "pwd";
    //uid
    private static final String KEY_UID = "uid";
    //页面的来源
    public static final String EXTRA_SOURCE = "source";
    //分享对话框

    public static final String EXTRA_SHARE_CONTENT = "share_content";

    public static final String EXTRA_TYPE_COMMENT = "comment_type";
    public static final String EXTRA_TYPE_PRAISE = "praise_type";
    public static final String REQUEST_CODE = "request_code";
    //类型
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_ITEM_TYPE = "item_type";
    public static final String EXTRA_ADDRESS = "address";
    public static final String EXTRA_SHARE_WAY = "share_way";

    //单日期选择
    public static final String EXTRA_SELECT_DATE = "select_date";
    public static final String EXTRA_INIT_START_DATE = "init_start_date";
    public static final String EXTRA_INIT_END_DATE = "init_end_date";

    public static final String EXTRA_START_DATE = "select_start_date";
    public static final String EXTRA_END_DATE = "select_end_date";
    public static final String EXTRA_PICKSKU = "pick_sku";
    //城市选择
    public static final String EXTRA_SELECT_CITY = "select_city";
    //数据
    public static final String EXTRA_DATA = "data";

    public static final String KEY_USER_NAME = "user_name";
    public static final String BILL_DATA = "bill";
    //应用是否第一次安装启动
    private static String KEY_FIRST_BOOT_APP = "key_first_start_app";

    //昵称
    private static final String KEY_NICK_NAME = "nick_name";
    //头像
    private static final String KEY_USER_ICON = "user_icon";

    public static String EXTRA_AUTH_RESULT = "auth_result";
    public static final String EXTRA_USER_NAME = "user_name";
    public static final String EXTRA_IDCARD = "idcard";
    public static final String EXTRA_VALIDDATE = "valid_date";


    public static String EXTRA_SELECT_CURRENT_VALUE = "select_value";
    public static final String EXTRA_TYPE_LOGIS = "type_logis";
    public static String EXTRA_SELECT_TYPE = "select_type";

    public static String EXTRA_MEDIAOBJECT = "mMediaObject";
    public static String EXTRA_TITLE = "title";
    public static String EXTRA_CONTENT = "content";

    public static String EXTRA_ID = "id";
    public static String EXTRA_OPEN_ID = "open_id";
    public static String EXTRA_UNION_ID = "union_id";
    public static String EXTRA_USER_TYPE = "extra_user_type";
    public static String EXTRA_NAME = "name";
    public static String EXTRA_ISHASNEXT = "isHasNext";
    public static String EXTRA_USER_INFO = "user_info";
    public static String KEY_VIP = "is_vip";

    public static String KEY_ROLE_TYPE = "role_type";
    public static final String EXTRA_TAG_ID = "tag_id";
    public static final String EXTRA_TAG_NAME = "tag_name";
    public static final String DISC_CODE = "DiscCode";

    /*--------------------------------------发现-----------------------------------*/
    public static final String EXTRA_ADD_LIVE_LABEL = "add_live_label";
    public static final String EXTRA_ADD_LIVE_LOCATION = "add_live_location";

    /**
     * 保存草稿   直播  动态
     **/
    public static final String KEY_LIVE_DRAFT_CONTENT = "live_draft";
    public static final String EXTRA_SHOW_GIF = "show_gif";
    public final static String EXTRA_MODE = "mode";
    //平台客服电话
    public final static String KEY_SEVICE_PHONE = "service_phone";
    //平台客服UID
    public final static String KEY_SEVICE_UID = "service_uid";
    //重要条款
    public final static String KEY_AGREENMENT_URL = "agreementUrl";
    //信息认证
    public final static String KEY_PROBATE = "master_probate";

    //语音播放输出模式
    public static String KEY_AUDIO_MUSIC_STREAM_TYPE = "audio_stream_type";
    //TOKEN
    public static final String KEY_EXTRA_ACCESSTOKEN = "access_token";

    //第一次删除咨询消息列表
    public static final String KEY_FIRST_DELETE_CONSULT_SESSION = "KEY_FIRST_DELETE_CONSULT_SESSION";
    /**
     * 直播分享选中的位置
     */
    public static final String EXTRA_LIVE_SHARE_POSITION = "KEY_EXTRA_LIVE_SHARE_POSITION";
    /**
     * 直播分辨率设置
     */
    public static final String EXTRA_LIVE_DEFINTION_INDEX = "EXTRA_LIVE_DEFINTION_INDEX";

    public static final String USER_SPORT_HABIT = "USER_SPORT_HABIT";

    public static void save(Context c, String key, String value) {
        save(TYPE_LOG_STATUS, c, key, value);
    }

    public static void save(Context c, String key, boolean value) {
        save(TYPE_LOG_STATUS, c, key, value);
    }

    public static void save(Context c, String key, int value) {
        save(TYPE_LOG_STATUS, c, key, value);
    }

    public static void save(Context c, String key, long value) {
        save(TYPE_LOG_STATUS, c, key, value);
    }

    public static void save(Context c, String key, float value) {
        save(TYPE_LOG_STATUS, c, key, value);
    }

    public static void save(String type, Context c, String key, String value) {
        try {

            if (key != null) {
                SharedPreferences mPreferences = c
                        .getSharedPreferences(type, 0);
                SharedPreferences.Editor mEditor = mPreferences.edit();
                if (value != null) {
                    mEditor.putString(key, value);
                } else {
                    mEditor.remove(key);
                }
                mEditor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(String type, Context c, String key, int value) {
        try {
            SharedPreferences mPreferences = c.getSharedPreferences(type, 0);
            SharedPreferences.Editor mEditor = mPreferences.edit();
            if (key != null) {
                mEditor.putInt(key, value);
            }
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(String type, Context c, String key, boolean value) {
        try {
            SharedPreferences mPreferences = c.getSharedPreferences(type, 0);
            SharedPreferences.Editor mEditor = mPreferences.edit();
            if (key != null) {
                mEditor.putBoolean(key, value);
            }
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(String type, Context c, String key, long value) {
        try {
            SharedPreferences mPreferences = c.getSharedPreferences(type, 0);
            SharedPreferences.Editor mEditor = mPreferences.edit();
            if (key != null) {
                mEditor.putLong(key, value);
            }
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(String type, Context c, String key, float value) {
        try {
            SharedPreferences mPreferences = c.getSharedPreferences(type, 0);
            SharedPreferences.Editor mEditor = mPreferences.edit();
            if (key != null) {
                mEditor.putFloat(key, value);

            }
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getString(Context c, String key) {
        return getString(TYPE_LOG_STATUS, c, key);
    }

    public static String getString(Context c, String key, String defStr) {
        return getString(TYPE_LOG_STATUS, c, key, defStr);
    }

    public static String getString(String type, Context c, String key) {
        String value = "";
        SharedPreferences preferences = c.getSharedPreferences(type, 0);
        if (preferences == null) {
            return value;
        }
        value = preferences.getString(key, "");
        return value;
    }

    public static String getString(String type, Context c, String key, String defaultStr) {
        String value = null;
        SharedPreferences preferences = c.getSharedPreferences(type, 0);
        if (preferences == null) {
            return value;
        }
        value = preferences.getString(key, defaultStr);
        return value;
    }

    public static long getLong(Context c, String key) {
        return getLong(TYPE_LOG_STATUS, c, key);
    }

    public static long getLong(String type, Context c, String key) {
        long value = 0;
        SharedPreferences preferences = c.getSharedPreferences(type, 0);
        if (preferences == null) {
            return value;
        }

        value = preferences.getLong(key, 0);
        return value;
    }

    public static long getLong(Context c, String key, long defaultValue) {
        return getLong(TYPE_LOG_STATUS, c, key, defaultValue);
    }

    public static int getInt(Context c, String key) {
        return getInt(TYPE_LOG_STATUS, c, key, 0);
    }

    public static int getInt(Context c, String key, int defaultVaule) {
        return getInt(TYPE_LOG_STATUS, c, key, defaultVaule);
    }

    public static long getLong(String type, Context c, String key, long defaultValue) {
        long value = 0;
        SharedPreferences preferences = c.getSharedPreferences(type, 0);
        if (preferences == null) {
            return value;
        }

        value = preferences.getLong(key, defaultValue);
        return value;
    }

    public static int getInt(String type, Context c, String key, int defaultVaule) {
        int value = 0;
        SharedPreferences preferences = c.getSharedPreferences(type, 0);
        if (preferences == null) {
            return value;
        }

        value = preferences.getInt(key, defaultVaule);
        return value;
    }

    public static boolean getBoolean(Context c, String key) {
        return getBoolean(TYPE_LOG_STATUS, c, key);
    }

    public static boolean getBoolean(String type, Context c, String key) {
        Boolean value = false;
        SharedPreferences preferences = c.getSharedPreferences(type, 0);
        if (preferences == null) {
            return false;
        }
        value = preferences.getBoolean(key, false);
        return value;
    }

    public static boolean getBoolean(Context c, String key, boolean defaultValue) {
        return getBoolean(TYPE_LOG_STATUS, c, key, defaultValue);
    }

    public static boolean getBoolean(String type, Context c, String key,
                                     boolean defaultValue) {
        Boolean value = false;
        SharedPreferences preferences = c.getSharedPreferences(type, 0);
        if (preferences == null) {
            return false;
        }
        value = preferences.getBoolean(key, defaultValue);
        return value;
    }

    public static float getFloat(Context c, String key) {
        return getFloat(TYPE_LOG_STATUS, c, key);
    }

    public static float getFloat(String type, Context c, String key) {
        float value = 0f;
        SharedPreferences preferences = c.getSharedPreferences(type, 0);
        if (preferences == null) {
            return value;
        }
        value = preferences.getFloat(key, 0f);
        return value;
    }

    public static boolean contains(Context context, String type, String key) {
        SharedPreferences preferences = context.getSharedPreferences(type, 0);
        return preferences.contains(key);
    }

    public static boolean contains(Context context, String key) {
        return contains(context, TYPE_LOG_STATUS, key);
    }

    public static void remove(Context context, String key) {
        try {
            SharedPreferences mPreferences = context.getSharedPreferences(
                    TYPE_LOG_STATUS, 0);
            SharedPreferences.Editor mEditor = mPreferences.edit();
            if (key != null) {
                mEditor.remove(key);
            }
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void remove(String type, Context c, String key) {
        try {
            SharedPreferences mPreferences = c.getSharedPreferences(type, 0);
            SharedPreferences.Editor mEditor = mPreferences.edit();
            if (key != null) {
                mEditor.remove(key);
            }
            mEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空某一类别的数据
     *
     * @param type
     * @param context
     */
    public static void clearPreference(String type, Context context) {
        try {
            SharedPreferences preference = context
                    .getSharedPreferences(type, 0);
            SharedPreferences.Editor editor = preference.edit();
            editor.clear();
            editor.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void clearLogStatus(Context context) {
        clearPreference(TYPE_LOG_STATUS, context);
    }

    //保存选择的历史城市
    public static void setHistoryCity(Context context, String historyCity) {
        if (historyCity != null) {
            save(TYPE_DEFAULT, context, EXTRA_HISTORY_CITY_NAME, historyCity);
        }
    }

    //获取历史选择的城市
    public static String getExtraHistoryCityName(Context context) {
        return getString(TYPE_DEFAULT, context, EXTRA_HISTORY_CITY_NAME);
    }

    //获取当前的城市名称
    public static String getExtraCurrentCityName(Context context) {
        return getString(TYPE_DEFAULT, context, EXTRA_CURRENT_CITY_NAME);
    }

    //首页是否改变了当前定位的城市
    public static boolean getHomeCityIsChange(Context context) {
        return getBoolean(context, EXTRA_HOME_CITY_IS_CHANGE);
    }

    //同城周边  选择的城市
    public static String getLocalCityName(Context context) {
        return getString(context, EXTRA_LOCAL_CITY_NAME);
    }

    public static void setLocalCityName(Context context, String cityname) {
        save(context, EXTRA_LOCAL_CITY_NAME, cityname);
    }

    ///同城周边  选择的城市Code
    public static String getLocalCityCode(Context context) {
        return getString(context, EXTRA_LOCAL_CITY_CODE);
    }

    public static void setLocalCityCode(Context context, String cityCode) {
        save(context, EXTRA_LOCAL_CITY_CODE, cityCode);
    }

    //同城周边  选择的城市
    public static String getArroundCityName(Context context) {
        return getString(context, EXTRA_ARROUND_CITY_NAME);
    }

    public static void setArroundCityName(Context context, String cityName) {
        save(context, EXTRA_ARROUND_CITY_NAME, cityName);
    }

    public static String getArroundCityCode(Context context) {
        return getString(context, EXTRA_ARROUND_CITY_CODE);
    }

    public static void setArroundCityCode(Context context, String cityCode) {
        save(context, EXTRA_ARROUND_CITY_CODE, cityCode);
    }

    public static void setFreeTripCityChange(Context context, boolean isChange) {
        save(context, EXTRA_FREE_TRIP_CITY_CHANGE, isChange);
    }

    public static void setPackTripCityChange(Context context, boolean isChange) {
        save(context, EXTRA_PACK_TRIP_CITY_CHANGE, isChange);
    }
    public static void setLocalCityChange(Context context, boolean isChange) {
        save(context, EXTRA_LOCAL_CITY_CHANGE, isChange);
    }

    public static void setArroundCityChange(Context context, boolean isChange) {
        save(context, EXTRA_ARROUND_CITY_CHANGE, isChange);
    }

    //跟团 自由行 选择的出发地
    public static String getFreeTripCityName(Context context) {
        return getString(context, EXTRA_FREE_TRIP_CITY_NAME);
    }

    //跟团 自由行 选择的出发地城市Code
    public static String getFreeTripCityCode(Context context) {
        return getString(context, EXTRA_FREE_TRIP_CITY_CODE);
    }

    //跟团 自由行 选择的出发地
    public static String getPackTripCityName(Context context) {
        return getString(context, EXTRA_PACK_TRIP_CITY_NAME);
    }

    //跟团 自由行 选择的出发地城市Code
    public static String getPackTripCityCode(Context context) {
        return getString(context, EXTRA_PACK_TRIP_CITY_CODE);
    }

    //保存跟团 自由行 中选择的目的地
    public static void setExtraPackTripCityName(Context context, String cityName) {
        save(context, EXTRA_PACK_TRIP_CITY_NAME, cityName);
    }

    //保存跟团 自由行 中选择的目的地Code
    public static void setExtraPackTripCityCode(Context context, String cityCode) {
        save(context, EXTRA_PACK_TRIP_CITY_CODE, cityCode);
    }

    //保存跟团 自由行 中选择的目的地
    public static void setExtraFreeTripCityName(Context context, String cityName) {
        save(context, EXTRA_FREE_TRIP_CITY_NAME, cityName);
    }

    //保存跟团 自由行 中选择的目的地Code
    public static void setExtraFreeTripCityCode(Context context, String cityCode) {
        save(context, EXTRA_FREE_TRIP_CITY_CODE, cityCode);
    }

    //获取首页改变的定位城市名称
    public static String getHomeChangeCityName(Context context) {
        return getString(context, EXTRA_HOME_CHANGE_CITY_NAME);
    }

    //获取首页改变的定位城市名称Code
    public static String getHomeChangeCityCode(Context context) {
        return getString(context, EXTRA_HOME_CHANGE_CITY_CODE, "");
    }

    //获取当前的城市信息(高德原生CODE)
    public static String getExtraCurrentAmapCity(Context context) {
        return getString(TYPE_DEFAULT, context, EXTRA_CURRENT_AMAP_CITY_CODE);
    }


    //保存当前城市代码
    public static String getExtraCurrentCityCode(Context context) {
        return getString(TYPE_DEFAULT, context, EXTRA_CURRENT_CITY_CODE);
    }

    //获取当前的位置
    public static String getExtraCurrentAddress(Context context) {
        return getString(TYPE_DEFAULT, context, EXTRA_CURRENT_ADDRESS);
    }

    //获取当前的Lat
    public static String getExtraCurrentLat(Context context) {
        return getString(TYPE_DEFAULT, context, EXTRA_CURRENT_LAT);
//        return "31.18912500000000000";
    }

    //获取当前的LNG
    public static String getExtraCurrentLon(Context context) {
        return getString(TYPE_DEFAULT, context, EXTRA_CURRENT_LNG);
    }

    //设置手机号码
    public static void setMobilePhone(Context context, String phone) {
        if (!TextUtils.isEmpty(phone)) {
            save(TYPE_LOG_STATUS, context, KEY_MOBILE_PHONE, phone);
        }
    }

    //获取手机号码
    public static String getMobilePhone(Context context) {
        return getString(TYPE_LOG_STATUS, context, KEY_MOBILE_PHONE);
    }

    //是否第一次启动
    public static boolean isAppFirstStart(Context context) {
        return getBoolean(TYPE_DEFAULT, context, KEY_FIRST_BOOT_APP, true);
    }

    //设置第一次启动的标志位
    public static void setAppFirstStart(Context context) {
        save(TYPE_DEFAULT, context, KEY_FIRST_BOOT_APP, false);
    }


    /**
     * 是否开通会员
     *
     * @return
     */
    public static boolean isVip(Context context) {
        return getBoolean(context, KEY_VIP) || ((getRoleType(context) & MerchantType.VIP_USER) == MerchantType.VIP_USER);
    }

    /**
     * 大V商家用户
     *
     * @return
     */
    public static boolean isMerchantUser(long options) {
        return (options & MerchantType.MERCHANT_USER) == MerchantType.MERCHANT_USER;
    }

    //设置是否会员
    public static void setVip(Context context, boolean isVip) {
        save(context, KEY_VIP, isVip);
    }

    //设置角色
    public static void setRoleType(Context context, long roleType) {
        save(context, KEY_ROLE_TYPE, roleType);
    }

    //保存UID
    public static void setUid(Context context, long uid) {
        if (uid != -1) {
            save(context, KEY_UID, uid);
        }
    }

    public static long getRoleType(Context context) {
        return getLong(context, KEY_ROLE_TYPE, 0);
    }

    //设置昵称
    public static void setNickName(Context context, String nickName) {
        if (nickName != null) {
            save(context, KEY_NICK_NAME, nickName);
        }
    }

    //获取昵称
    public static String getNickName(Context context) {
        return getString(context, KEY_NICK_NAME);
    }

    //设置用户头像
    public static void setUserIcon(Context context, String nickName) {
        if (nickName != null) {
            save(context, KEY_USER_ICON, nickName);
        }
    }

    //获取用户头像
    public static String getUserIcon(Context context) {
        return getString(context, KEY_USER_ICON);
    }

    private static final String KEY_USER_COVER = "user_cover";

    //设置用户封面
    public static void setUserCover(Context context, String cover) {
        if (cover != null) {
            save(context, KEY_USER_COVER, cover);
        }
    }

    //获取用户封面
    public static String getUserCover(Context context) {
        return getString(context, KEY_USER_COVER);
    }


    private static final String KEY_USER_HOME_PAGE = "user_home_page";

    //设置用户主页
    public static void setUserHomePage(Context context, Boolean cover) {
        if (cover != null) {
            save(context, KEY_USER_HOME_PAGE, cover);
        }
    }

    //设置用户运动爱好
    public static void setUserSportHabit(Context context, int habit) {
        save(context, USER_SPORT_HABIT, habit);
    }

    //获取用户运动爱好
    public static int getUserSportHabit(Context context) {
        return getInt(context, USER_SPORT_HABIT);

    }


    //获取用户主页
    public static Boolean getUserHomePage(Context context) {
        return getBoolean(context, KEY_USER_HOME_PAGE);
    }


    public static void logout(Context context) {
        remove(context, KEY_UID);
    }

    /**
     * 保存直播草稿
     *
     * @param context
     * @param content
     */
    public static void saveLiveDraft(Context context, String content) {
        save(context, SPUtils.KEY_LIVE_DRAFT_CONTENT, content);
    }

    /**
     * 获取直播草稿
     *
     * @param context
     * @return
     */
    public static String getLiveDraft(Context context) {
        return getString(context, KEY_LIVE_DRAFT_CONTENT);
    }

    /**
     * 保存客服电话
     *
     * @param context
     * @param phone
     */
    public static void saveServicePhone(Context context, String phone) {
        save(TYPE_DEFAULT, context, KEY_SEVICE_PHONE, phone);
    }


    /**
     * 获取客服电话
     *
     * @param context
     * @return
     */
    public static String getServicePhone(Context context) {
        return getString(TYPE_DEFAULT, context, KEY_SEVICE_PHONE, "");
    }

    /**
     * 保存客服UID
     *
     * @param context
     * @param uid
     */
    public static void saveServiceUID(Context context, long uid) {
        save(TYPE_DEFAULT, context, KEY_SEVICE_UID, uid);
    }

    /**
     * 获取客服UID
     *
     * @param context
     * @return
     */
    public static long getServiceUID(Context context) {
        return getLong(TYPE_DEFAULT, context, KEY_SEVICE_UID, -1);
    }

    /**
     * 保存服务条款
     *
     * @param context
     * @param phone
     */
    public static void saveServiceProtocol(Context context, String phone) {
        save(TYPE_DEFAULT, context, KEY_AGREENMENT_URL, phone);
    }

    /**
     * 获取服务条款
     *
     * @param context
     * @return
     */
    public static String getServiceProtocol(Context context) {
        return getString(TYPE_DEFAULT, context, KEY_AGREENMENT_URL, null);
    }

    /**
     * 保存所有图标
     *
     * @param context
     * @param icons
     */
    private final static String KEY_SYSTEM_ICONS = "system_icons";

    public static void saveComIcons(Context context, String icons) {
        save(TYPE_DEFAULT, context, KEY_SYSTEM_ICONS, icons);
    }

    /**
     * 获取所有图标
     *
     * @param context
     * @return
     */
    public static String getComIcons(Context context) {
        return getString(TYPE_DEFAULT, context, KEY_SYSTEM_ICONS, null);
    }

    public static void saveDestCities(Context context, String type, String cities) {
        save(TYPE_DEFAULT, context, type, cities);
    }

    /**
     * 获取当前的环境
     *
     * @param context
     * @return
     */
    private final static String KEY_ENV = "env";

    public static String getEnv(Context context) {
        return getString(TYPE_DEFAULT, context, KEY_ENV, null);
    }

    /**
     * 保存选择的环境
     *
     * @param context
     * @param env
     */
    public static void setEnv(Context context, String env) {
        save(TYPE_DEFAULT, context, KEY_ENV, env);
    }

    /**
     * 保存分享URL的前缀
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveShareDefaultUrl(Context context, String key, String value) {
        save(TYPE_DEFAULT, context, key, value);
    }

    /**
     * 获取分享URL的前缀
     *
     * @param context
     * @param key
     * @return
     */
    public static String getShareDefaultUrl(Context context, String key) {
        return getString(TYPE_DEFAULT, context, key);
    }

    /**
     * 判断是否时从首页跳转到搜索结果页
     *
     * @param applicationContext
     * @param b
     */
    public static void setIsJumpFromHomeSearch(Context applicationContext, boolean b) {
        save(applicationContext, EXTRA_IS_FROM_HOMESEARCH_JUMP, b);
    }

    /**
     * 判断是否时从首页跳转到搜索结果页
     *
     * @param context
     */
    public static boolean isJumpFromHomeSearch(Context context) {
        return getBoolean(context, EXTRA_IS_FROM_HOMESEARCH_JUMP);
    }

    public static String getExtraLocationProvinceName(Context context) {
        return getString(context, EXTRA_LOCATION_PROVINCE_NAME);
    }

    /**
     * 获取出发地历史城市集合
     *
     * @param context
     * @return
     */
    public static ArrayList<AddressBean> getCityHistoryAddress(Context context) {
        ArrayList<AddressBean> historyCitys = new ArrayList<>();
        String extraHistoryCityName = getExtraHistoryCityName(context);
        if (!TextUtils.isEmpty(extraHistoryCityName)) {
            historyCitys = JSONUtils.convertToArrayList(extraHistoryCityName, AddressBean.class);
        }
        return historyCitys;
    }

    /**
     * 获取当前语音输出模式
     *
     * @param ctx
     * @return
     */
    public static int getAudioStreamType(Context ctx) {
        return getInt(TYPE_DEFAULT, ctx, KEY_AUDIO_MUSIC_STREAM_TYPE, AudioManager.STREAM_MUSIC);
    }

    public static void saveAudioStreamType(Context ctx, int streamType) {
        save(TYPE_DEFAULT, ctx, KEY_AUDIO_MUSIC_STREAM_TYPE, streamType);
    }

    /**
     * Web用User Token
     */
    public static final String KEY_WEB_USER_TOKEN = "web_user_token";
    public static final String KEY_WEB_TOKEN_EXPIRE_TIME = "web_token_expire_time";

    /**
     * 获取Web UserToken
     *
     * @param context
     * @return
     */
    public static String getWebUserToken(Context context) {
        return getString(context, KEY_WEB_USER_TOKEN);
    }

    /**
     * 保存Web UserToken
     *
     * @param context
     * @return
     */
    public static void setWebUserToken(Context context, String wUserToken, long expiredTime) {

        if (!TextUtils.isEmpty(wUserToken)) {
            save(context, KEY_WEB_USER_TOKEN, wUserToken);

            if (expiredTime != -1) {
                save(context, KEY_WEB_TOKEN_EXPIRE_TIME, expiredTime);
            }
        }
    }

    /**
     * 获取打点上传最大值
     *
     * @param context
     * @return
     */
    private static final long DEFAULT_DCLOG_LENGTH = 50 * 1024;

    public static long getDCMaxLength(Context context) {
        String length = getString(TYPE_DEFAULT, context, SysConfigType.DOT_UP_LENGTH);
        if (TextUtils.isEmpty(length)) {
            return DEFAULT_DCLOG_LENGTH;
        }
        try {
            long l = Long.parseLong(length);
            if (l > 0) {
                return l;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DEFAULT_DCLOG_LENGTH;
    }

    /**
     * 判断是否需要升级系统配置信息
     *
     * @param context
     * @return
     */
    private static final long MIN_SC_UPDATE_INTERVAL = 24 * 60 * 60 * 1000;
    private static final String KEY_LAST_UPDATE_SC_TIME = "last_update_sc_time";

    /**
     * 保存最后一次更新系统配置的时间
     *
     * @param context
     * @param time
     */
    public static void setLastUpdateScTime(Context context, long time) {
        save(TYPE_DEFAULT, context, KEY_LAST_UPDATE_SC_TIME, time);
    }

    /**
     * 获取积分
     */
    private static final String KEY_SCORE = "score";

    public static long getScore(Context context) {
        return getLong(TYPE_LOG_STATUS, context, KEY_SCORE);
    }

    /**
     * 保存积分
     *
     * @param context
     * @param score
     */
    public static void setScore(Context context, long score) {
        save(TYPE_LOG_STATUS, context, KEY_SCORE, score);
    }

    /**
     * 获取优惠券
     */
    private static final String KEY_COUPON_COUNT = "coupon_count";

    public static long getCouponCount(Context context) {
        return getLong(TYPE_LOG_STATUS, context, KEY_COUPON_COUNT);
    }

    /**
     * 保存优惠券
     *
     * @param context
     * @param coupons
     */
    public static void setCouponCount(Context context, long coupons) {
        save(TYPE_LOG_STATUS, context, KEY_COUPON_COUNT, coupons);
    }

    /**
     * 获取我的粉丝数
     */
    private static final String KEY_MY_ATTENTIONS = "my_attention";

    public static long getMyFans(Context context) {
        return getLong(TYPE_LOG_STATUS, context, KEY_MY_FANS);
    }

    /**
     * 保存我的粉丝数
     *
     * @param context
     * @param attentions
     */
    public static void setMyFans(Context context, long attentions) {
        save(TYPE_LOG_STATUS, context, KEY_MY_FANS, attentions);
    }

    /**
     * 获取我的关注数
     */
    private static final String KEY_MY_FANS = "my_fans";

    /**
     * 保存我的关注数
     *
     * @param context
     * @param score
     */
    public static void setMyAttentions(Context context, long score) {
        save(TYPE_LOG_STATUS, context, KEY_MY_ATTENTIONS, score);
    }

    /**
     * 获取是否是白名单
     */
    private static final String KEY_IS_IN_WHITE_LIST = "is_white_list";

    public static int isInWhiteList(Context context) {
        return getInt(TYPE_LOG_STATUS, context, KEY_IS_IN_WHITE_LIST, -1);
    }

    /**
     * 获取积分商城更新文案
     */
    private static final String KEY_IS_INT_TEXT_CHANGED = "is_int_text_changed";

    public static boolean getIntTextChanged(Context context) {
        return getBoolean(TYPE_DEFAULT, context, KEY_IS_INT_TEXT_CHANGED);
    }


    /**
     * 保存发布咨询的信息
     *
     * @param context
     * @param text
     */
    private static final String KEY_RELEASE_CONSULT_TEXT = "release_consult_text";

    public static void setReleaseConsultText(Context context, String text) {
        save(TYPE_LOG_STATUS, context, KEY_RELEASE_CONSULT_TEXT, text);
    }

    /**
     * 获取发布咨询的信息
     *
     * @param context
     */
    public static String getReleaseConsultText(Context context) {
        return getString(TYPE_LOG_STATUS, context, KEY_RELEASE_CONSULT_TEXT);
    }

    /**
     * 判断是否需要升级系统配置信息
     *
     * @param context
     * @return
     */
    //定义一个半小时的间隔
    private static final long MIN_WTK_UPDATE_INTERVAL = 60 * 60 * 1000 + 30 * 60 * 1000;
    private static final String KEY_LAST_UPDATE_WTK_TIME = "last_update_wtk_time";

    public static boolean isNeedUpdateWtk(Context context) {
        return System.currentTimeMillis() - getLong(TYPE_LOG_STATUS, context, KEY_LAST_UPDATE_WTK_TIME) >= MIN_WTK_UPDATE_INTERVAL;
    }

    /**
     * 保存最后一次更新系统配置的时间
     *
     * @param context
     * @param time
     */
    public static void setLastUpdateWtkTime(Context context, long time) {
        save(TYPE_LOG_STATUS, context, KEY_LAST_UPDATE_WTK_TIME, time);
    }
    /**
     * 获取签到的URL
     */
    public static String getCheckInUrl(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_CHECK_IN);
    }

    /**
     * 获取积分商城首页H5
     *
     * @param context
     * @return
     */
    public static String getIntegralHomeUrl(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_INTEGRAL_MALL);
    }

    /**
     * 邀请有礼
     *
     * @param context
     * @return
     */
    public static String getInviteGiftUrl(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_INVITE_GIFT);
    }

    /**
     * 获取计步器首页的URL
     */
    public static String getPedometerUrl(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_HOME_PEDOMETER);
    }

    private static final String KEY_SHARE_OK = "key_steps_share_status";

    /**
     * 设置步数分享状态
     */
    public static void setShareOK(Context context, boolean isShareOk) {
        save(TYPE_DEFAULT, context, KEY_SHARE_OK, isShareOk);
    }

    /**
     * 是否手动更新状态
     *
     * @param context
     * @param isManual
     * @return
     */
    private static final String KEY_MANUAL_UPDATE_SERVICE_STATUS = "key_manual_update_service_status";

    public static boolean isManualUpdateServiceStatus(Context context) {
        return getBoolean(TYPE_DEFAULT, context, KEY_MANUAL_UPDATE_SERVICE_STATUS, false);
    }

    /**
     * 设置手动状态
     *
     * @param context
     * @param isManual
     */
    public static void setManualUpdateServiceStatus(Context context, boolean isManual) {
        save(TYPE_DEFAULT, context, KEY_MANUAL_UPDATE_SERVICE_STATUS, isManual);
    }

    /**
     * 获取上次打开弹框的时间
     *
     * @param context
     * @return
     */
    private static final String KEY_BOMBOX_OPEN_TIME = "key_bombox_open_time";

    public static long getLastBomboxOpenTime(Context context) {
        return getLong(TYPE_DEFAULT, context, KEY_BOMBOX_OPEN_TIME, -1);
    }

    /**
     * 设置打开弹框的时间
     *
     * @param context
     * @param time
     */
    public static void setLastBomboxOpenTime(Context context, long time) {
        save(TYPE_DEFAULT, context, KEY_BOMBOX_OPEN_TIME, time);
    }

    /**
     * 判断是否需要打开首页弹框
     *
     * @param context
     * @return
     */
    public static boolean isNeedOpenBombox(Context context) {
        boolean a = ((System.currentTimeMillis() - getLastBomboxOpenTime(context)) > 24 * 60 * 60 * 1000);
        boolean b = !TextUtils.isEmpty(getString(TYPE_DEFAULT, context, ResourceType.QUANYAN_BOMB_BOX_LIST));
        LogUtils.i(TAG, "a = " + String.valueOf(a) + ",b = " + String.valueOf(b) + ", value = " + getString(TYPE_DEFAULT, context, ResourceType.QUANYAN_BOMB_BOX_LIST));
        return a && b;
    }

    /**
     * 获取dsig
     */
    public static final String KEY_DSIG = "key_dsig";

    public static String getDSig(Context context) {
        return getString(TYPE_DEFAULT, context, KEY_DSIG);
    }

    /**
     * 保存dsig
     *
     * @param context
     * @param dsig
     */
    public static void saveDSig(Context context, String dsig) {
        save(TYPE_DEFAULT, context, KEY_DSIG, dsig);
    }

    public static final String KEY_WALLET_NAME = "wallet_name";

    /**
     * 保存电子钱包用户姓名
     *
     * @param context
     * @param name
     */
    public static void saveWalletName(Context context, String name) {
        save(TYPE_DEFAULT, context, KEY_WALLET_NAME, name);
    }

    /**
     * 得到电子钱包用户姓名
     *
     * @param context
     * @return
     */
    public static String getWalletName(Context context) {
        return getString(TYPE_DEFAULT, context, KEY_WALLET_NAME);
    }


    public static String getWalletDeal(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_ADD_BANK_CARD_PROTOCAL);
    }

    public static void saveMemberCode(Context context, String memberCode) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_MEMBER_CODE, memberCode);
    }

    public static String getMemberCode(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_MEMBER_CODE);
    }

    public static void saveRecharge(Context context, String charge) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_RECHARGE, charge);
    }

    public static String getRecharge(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_RECHARGE);
    }

    public static void saveCardBag(Context context, String cardBag) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_CARD_BAG, cardBag);
    }

    public static String getCardBag(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_CARD_BAG);
    }

    public static void saveFastBooking(Context context, String cardBag) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_FAST_BOOKING, cardBag);
    }

    public static String getFaskBooking(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_FAST_BOOKING);
    }

    public static void saveRecommend(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_RECOMMEND_VENUE, url);
    }

    public static String getRecommend(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_RECOMMEND_VENUE);
    }

    public static void saveNewRecommend(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_NEW_RECOMMEND_VENUE, url);
    }

    public static String getNewRecommend(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_NEW_RECOMMEND_VENUE);
    }

    public static void saveVendeDetail(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_VENUE_DETAIL, url);
    }

    public static String getVendeDetail(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_VENUE_DETAIL);
    }

    public static void saveClubActivityList(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_CLUB_ACTIVITY_LIST, url);
    }
    public static void saveClubActivityDetail(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_CLUB_ACTIVITY_DETAIL, url);
    }

    public static String getClubActivityDetail(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_CLUB_ACTIVITY_DETAIL);
    }

    public static void saveClubList(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_CLUB_LIST, url);
    }

    public static String getClubList(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_CLUB_LIST);
    }

    public static void saveClubDetail(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_CLUB_DETAIL, url);
    }

    public static String getClubDetail(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_CLUB_DETAIL);
    }

    public static void saveFootballBooking(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_FOOTBALL_BOOKING, url);
    }

    public static void saveBasketballBooking(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_BASKETBALL_BOOKING, url);
    }

    public static String getBasketballBooking(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_BASKETBALL_BOOKING);
    }

    public static void saveBadmintonBooking(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_BADMINTON_BOOKING, url);
    }

    public static void saveTennisBooking(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_TENNIS_BOOKING, url);
    }

    public static void saveClubHome(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_CLUB_HOME, url);
    }

    public static void saveAddClub(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_CLUB_ADD, url);
    }

    public static String addClub(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_CLUB_ADD);
    }

    public static void saveAddClubAct(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_CLUB_ADD_ACT, url);
    }

    public static String getAddClubAct(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_CLUB_ADD_ACT);
    }

    public static String getAboutUs(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_ABOUT_US);
    }

    public static void saveMemberLevel(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_MEMBER_LEVEL, url);
    }

    public static String getMemberLevel(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_MEMBER_LEVEL);
    }

    public static void saveMyorder(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_MY_ORDER, url);
    }

    public static String getMyorder(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_MY_ORDER);
    }

    public static void saveWallet(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_WALLET, url);
    }

    public static void saveBClubHome(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_B_CLUB_HOME, url);
    }

    public static void saveVenueManageHomr(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_VENUE_MANAGE_HOMR, url);
    }

    public static void saveRankingList(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_RANKING_LIST, url);
    }

    public static void saveVenueDataHomr(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_VENUE_DATA_HOMR, url);
    }

    public static void saveVideoList(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_VIDEO_LIST, url);
    }

    public static void saveAddMyCard(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_ADD_MY_CARD, url);
    }

    public static String getAddMyCard(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_ADD_MY_CARD);
    }

    public static void saveShowAllTool(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_SHOW_ALL_TOOL, url);
    }

    public static String getShowAllTool(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_SHOW_ALL_TOOL);
    }

    public static void savePointDetail(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_POINT_DETAIL, url);
    }

    public static String getPointDetail(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_POINT_DETAIL);
    }

    public static void saveRechargeDetail(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_RECHARGE_DETAIL, url);
    }

    public static String getRechargeDetail(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_RECHARGE_DETAIL);
    }

    public static void saveCardVoucher(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_CARD_VOUCHER, url);
    }

    public static void saveAboutUs(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_ABOUT_US, url);
    }

    public static String getCardVoucher(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_CARD_VOUCHER);
    }

    public static void savePonitPayUrl(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_PONIT_PAY_URL, url);
    }

    public static String getPonitPayUrl(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_PONIT_PAY_URL);
    }

    public static void saveVenueActivityList(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_VENUE_ACTIVITY_LIST, url);
    }

    public static String getVenueActivityList(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_VENUE_ACTIVITY_LIST);
    }


    public static void saveURLLIVELIST(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_LIVE_LIST, url);
    }

    public static void saveURL_VENUE_ORDER_DETAIL(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_VENUE_ORDER_DETAIL, url);
    }

    public static String getURL_VENUE_ORDER_DETAIL(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_VENUE_ORDER_DETAIL);
    }

    public static void saveURL_POINT_ITEM_DETAIL(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_POINT_ITEM_DETAIL, url);
    }

    public static String getURL_POINT_ITEM_DETAIL(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_POINT_ITEM_DETAIL);
    }

    public static void saveURL_SCAN_HEXIAO(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_SCAN_HEXIAO, url);
    }

    public static String getURL_SCAN_HEXIAO(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_SCAN_HEXIAO);
    }

    public static void saveURL_RECEIVE_ADDRESS(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_RECEIVE_ADDRESS, url);
    }

    public static void saveOPEN_NEW_H5_MALL(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.OPEN_NEW_H5_MALL, url);
    }

    public static String getOPEN_NEW_H5_MALL(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.OPEN_NEW_H5_MALL);
    }

    public static void saveURL_SPORT_YYW_MORE(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_SPORT_YYW_MORE, url);
    }

    public static String getURL_SPORT_YYW_MORE(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_SPORT_YYW_MORE);
    }

    public static void saveURL_TRAIN_COURSE_LIST(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_TRAIN_COURSE_LIST, url);
    }

    public static String getURL_TRAIN_COURSE_LIST(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_TRAIN_COURSE_LIST);
    }

    public static void saveURL_TRAIN_COURSE_DETAIL(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_TRAIN_COURSE_DETAIL, url);
    }

    public static String getURL_TRAIN_COURSE_DETAIL(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_TRAIN_COURSE_DETAIL);
    }

    public static void saveURL_ORG_DETAIL(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_ORG_DETAIL, url);
    }

    public static String getURL_ORG_DETAIL(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_ORG_DETAIL);
    }

    public static void saveURL_ORG_LIST(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_ORG_LIST, url);
    }

    public static String getURL_ORG_LIST(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_ORG_LIST);
    }

    public static void saveURL_WAR_SQUARE(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_WAR_SQUARE, url);
    }

    public static String getURL_WAR_SQUARE(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_WAR_SQUARE);
    }

    public static void saveURL_WAR_ID(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_WAR_ID, url);
    }

    public static String getURL_WAR_ID(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_WAR_ID);
    }

    public static void saveURL_CONFIRM_WAR(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_CONFIRM_WAR, url);
    }

    public static String getURL_CONFIRM_WAR(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_CONFIRM_WAR);
    }

    public static void saveURL_TRAIN_HOME(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_TRAIN_HOME, url);
    }

    public static String getURL_TRAIN_HOME(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_TRAIN_HOME);
    }

    public static String getURL_RECEIVE_ADDRESS(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_RECEIVE_ADDRESS);
    }

    public static void setDiscCode(Context context, String discCode) {
        if (discCode != null) {
            save(TYPE_DEFAULT, context, DISC_CODE, discCode);
        }
    }

    public static String getDiscCode(Context context) {

        return getString(TYPE_DEFAULT, context, DISC_CODE);
    }

    //<<<<<<< HEAD
    public static void saveSHOPCART_LIST(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.SHOPCART_LIST, url);
    }

    public static void saveVIEW_INTEGRAL_DETAIL(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.VIEW_INTEGRAL_DETAIL, url);
    }

    public static void saveVIEW_INTEGRAL_TASK_LIST(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.VIEW_INTEGRAL_TASK_LIST, url);
    }

    public static void savePOINT_ORDER_DETAIL(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.POINT_ORDER_DETAIL, url);
    }

    public static void saveURL_QUANZI_ARTICLE(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_QUANZI_ARTICLE, url);
    }

    public static void saveURL_SHORT_VIDEO(Context context, String url) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_SHORT_VIDEO, url);
    }

    public static String getURL_QUANZI_ARTICLE(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_QUANZI_ARTICLE);
    }

    public static String getURL_SHORT_VIDEO(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_SHORT_VIDEO);
    }

    public static String getSHOPCART_LIST(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.SHOPCART_LIST);
    }

    public static String getVIEW_INTEGRAL_DETAIL(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.VIEW_INTEGRAL_DETAIL);
    }

    public static String getVIEW_INTEGRAL_TASK_LIST(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.VIEW_INTEGRAL_TASK_LIST);
    }

    public static String getPOINT_ORDER_DETAIL(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.POINT_ORDER_DETAIL);
    }

    public static String getRegisterApnsToken(Context context) {
        return getString(TYPE_XG_PUSH_CONFIG, context, KEY_REGISTRATION_ID, null);
    }

    public static void setRegisterApnsToken(Context context, String id) {
        save(TYPE_XG_PUSH_CONFIG, context, KEY_REGISTRATION_ID, id);
    }

    public static void saveOutVenueDetail(Context context, String cardBag) {
        save(TYPE_DEFAULT, context, SysConfigType.URL_OUT_VENUE_DETAIL, cardBag);
    }

    public static String getOutVenueDetail(Context context) {
        return getString(TYPE_DEFAULT, context, SysConfigType.URL_OUT_VENUE_DETAIL);
    }
}
