package com.quanyan.yhy.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.common.CommonUrl;
import com.quanyan.yhy.common.DirConstants;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.SysConfigType;
import com.yhy.common.beans.im.NotificationMessageEntity;
import com.yhy.common.beans.net.model.VideoInfo;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.common.BoothList;
import com.yhy.common.beans.net.model.common.ComIconList;
import com.yhy.common.beans.net.model.rc.SysConfig;
import com.yhy.common.beans.net.model.user.DestinationList;
import com.yhy.common.beans.net.model.user.UserInfo;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.eventbus.event.NotificationEvent;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


public class DBManager {
    public static final int VERSION = 2;
    public static DBManager mDBManager;
    private DbUtils mDbUtils;
    private Context mContext;

    @Autowired
    IUserService userService;

    public DBManager(Context context) {
        mContext = context;
        YhyRouter.getInstance().inject(this);
        init();
    }

    public DbUtils getDbUtils() {
        return mDbUtils;
    }

    private String getDBName() {
        String dbName = "quanyan_" + userService.getLoginUserId() + ".db";
        return dbName;
    }

    private void init() {
//        mDbUtils = DbUtils.create(mContext, DirConstants.DIR_DB, getDBName(), VERSION, new DbUtils.DbUpgradeListener(){
//
//            @Override
//            public void onUpgrade(DbUtils dbUtils, int i, int i1) {
//
//            }
//        });

        mDbUtils = DbUtils.create(mContext, getDBName(), VERSION, new DbUtils.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbUtils dbUtils, int oldVersion, int newVersion) {
                //更新通知表
                updateTable(dbUtils, NotificationMessageEntity.class);
            }
        });
    }

    public static void updateTable(DbUtils dbUtils, Class<?> tClass) {
        try {
            if (dbUtils.tableIsExist(tClass)) {
                String tableName = tClass.getName();
                tableName = tableName.replace(".", "_");
                String sql = "select * from " + tableName;
                //声名一个map用来保存原有表中的字段
                Map<String, String> filedMap = new HashMap<>();
                //执行自定义的sql语句
                Cursor cursor = dbUtils.execQuery(sql);
                int count = cursor.getColumnCount();
                for (int i = 0; i < count; i++) {
                    filedMap.put(cursor.getColumnName(i), cursor.getColumnName(i));
                }
                //cursor在用完之后一定要close
                cursor.close();
                //下面用到了一些反射知识，下面是获取实体类的所有私有属性（即我们更改表结构后的所有字段名）
                Field[] fields = UserInfo.class.getDeclaredFields();

                for (int i = 0; i < fields.length; i++) {
                    if (filedMap.containsKey(fields[i].getName())) {
                        //假如字段名已存在就进行下次循环
                        continue;
                    } else {
                        //不存在，就放到map中，并且给表添加字段
                        filedMap.put(fields[i].getName(), fields[i].getName());
                        //根据属性的类型给表增加字段
                        if (fields[i].getType().toString().equals("class java.lang.String")) {

                            dbUtils.execNonQuery("alter table " + tableName + " add " + fields[i].getName() + " TEXT ");
                        } else if (fields[i].getType().equals("int") || fields[i].getType().equals("long") || fields[i].getType().equals("boolean")) {
                            dbUtils.execNonQuery("alter table " + tableName + " add " + fields[i].getName() + " INTEGER ");
                        }
                    }
                }


            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    public static DBManager getInstance(Context context) {
        if (mDBManager == null) {
            mDBManager = new DBManager(context);
        }

        return mDBManager;
    }

    /**
     * 保存对象到数据库
     *
     * @param obj
     */
    public void saveOrUpdate(Object obj) {
        try {
            mDbUtils.saveOrUpdate(obj);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存服务器的配置项
     *
     * @param list
     */
    public void updateSysConfigs(List<SysConfig> list) {
        try {
            if (list == null || list.size() == 0) {
                return;
            }

            for (SysConfig sysConfig : list) {

                if (SysConfigType.URL_MEMBER_CODE.equals(sysConfig.title)) {
                    SPUtils.saveMemberCode(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_RECHARGE.equals(sysConfig.title)) {
                    SPUtils.saveRecharge(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_CARD_BAG.equals(sysConfig.title)) {
                    SPUtils.saveCardBag(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_FAST_BOOKING.equals(sysConfig.title)) {
                    SPUtils.saveFastBooking(mContext, sysConfig.content);
                    continue;
                }
               if (SysConfigType.URL_OUT_VENUE_DETAIL.equals(sysConfig.title)) {
                    SPUtils.saveOutVenueDetail(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_RECOMMEND_VENUE.equals(sysConfig.title)) {
                    SPUtils.saveRecommend(mContext, sysConfig.content);
                    continue;
                }
               if (SysConfigType.URL_NEW_RECOMMEND_VENUE.equals(sysConfig.title)) {
                    SPUtils.saveNewRecommend(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_VENUE_DETAIL.equals(sysConfig.title)) {
                    SPUtils.saveVendeDetail(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_CLUB_ACTIVITY_LIST.equals(sysConfig.title)) {
                    SPUtils.saveClubActivityList(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_CLUB_ACTIVITY_DETAIL.equals(sysConfig.title)) {
                    SPUtils.saveClubActivityDetail(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_CLUB_LIST.equals(sysConfig.title)) {
                    SPUtils.saveClubList(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_CLUB_DETAIL.equals(sysConfig.title)) {
                    SPUtils.saveClubDetail(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_FOOTBALL_BOOKING.equals(sysConfig.title)) {
                    SPUtils.saveFootballBooking(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_BASKETBALL_BOOKING.equals(sysConfig.title)) {
                    SPUtils.saveBasketballBooking(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_BADMINTON_BOOKING.equals(sysConfig.title)) {
                    SPUtils.saveBadmintonBooking(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_TENNIS_BOOKING.equals(sysConfig.title)) {
                    SPUtils.saveTennisBooking(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_CLUB_HOME.equals(sysConfig.title)) {
                    SPUtils.saveClubHome(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_MEMBER_LEVEL.equals(sysConfig.title)) {
                    SPUtils.saveMemberLevel(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_MY_ORDER.equals(sysConfig.title)) {
                    SPUtils.saveMyorder(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_WALLET.equals(sysConfig.title)) {
                    SPUtils.saveWallet(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_B_CLUB_HOME.equals(sysConfig.title)) {
                    SPUtils.saveBClubHome(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_VENUE_MANAGE_HOMR.equals(sysConfig.title)) {
                    SPUtils.saveVenueManageHomr(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_RANKING_LIST.equals(sysConfig.title)) {
                    SPUtils.saveRankingList(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_VENUE_DATA_HOMR.equals(sysConfig.title)) {
                    SPUtils.saveVenueDataHomr(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_VIDEO_LIST.equals(sysConfig.title)) {
                    SPUtils.saveVideoList(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_CLUB_ADD.equals(sysConfig.title)) {
                    SPUtils.saveAddClub(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_CLUB_ADD_ACT.equals(sysConfig.title)) {
                    SPUtils.saveAddClubAct(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.SERVICE_TEL.equals(sysConfig.title)) {
                    SPUtils.saveServicePhone(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.SERVICE_PROVISION.equals(sysConfig.title)) {
                    SPUtils.saveServiceProtocol(mContext, sysConfig.content);
                    continue;
                }

                if (SysConfigType.URL_ADD_MY_CARD.equals(sysConfig.title)) {
                    SPUtils.saveAddMyCard(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_SHOW_ALL_TOOL.equals(sysConfig.title)) {
                    SPUtils.saveShowAllTool(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_RECHARGE_DETAIL.equals(sysConfig.title)) {
                    SPUtils.saveRechargeDetail(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_POINT_DETAIL.equals(sysConfig.title)) {
                    SPUtils.savePointDetail(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_CARD_VOUCHER.equals(sysConfig.title)) {
                    SPUtils.saveCardVoucher(mContext, sysConfig.content);
                    continue;
                }

                if (SysConfigType.URL_ABOUT_US.equals(sysConfig.title)) {
                    SPUtils.saveAboutUs(mContext, sysConfig.content);
                    continue;
                }

                if (SysConfigType.URL_PONIT_PAY_URL.equals(sysConfig.title)) {
                    SPUtils.savePonitPayUrl(mContext, sysConfig.content);
                    continue;
                }

                if (SysConfigType.URL_VENUE_ACTIVITY_LIST.equals(sysConfig.title)) {
                    SPUtils.saveVenueActivityList(mContext, sysConfig.content);
                    continue;
                }

                if (SysConfigType.URL_VENUE_ORDER_DETAIL.equals(sysConfig.title)) {
                    SPUtils.saveURL_VENUE_ORDER_DETAIL(mContext, sysConfig.content);
                    continue;
                }

                if (SysConfigType.URL_POINT_ITEM_DETAIL.equals(sysConfig.title)) {
                    SPUtils.saveURL_POINT_ITEM_DETAIL(mContext, sysConfig.content);
                    continue;
                }

                if (SysConfigType.URL_LIVE_LIST.equals(sysConfig.title)) {
                    SPUtils.saveURLLIVELIST(mContext, sysConfig.content);
                    continue;
                }

                if (SysConfigType.CUSTOMER_SERVICE_UID.equals(sysConfig.title) && !StringUtil.isEmpty(sysConfig.content)) {
                    SPUtils.saveServiceUID(mContext, Long.parseLong(sysConfig.content));
                    continue;
                }

                //TODO 打点上传的阀值
                if (SysConfigType.DOT_UP_LENGTH.equals(sysConfig.title) && !StringUtil.isEmpty(sysConfig.content)) {
                    SPUtils.save(SPUtils.TYPE_DEFAULT, mContext, SysConfigType.DOT_UP_LENGTH, sysConfig.content);
                    continue;
                }

                if (SysConfigType.URL_HOME_PEDOMETER.equals(sysConfig.title) && !StringUtil.isEmpty(sysConfig.content)) {
                    SPUtils.save(SPUtils.TYPE_DEFAULT, mContext.getApplicationContext(), SysConfigType.URL_HOME_PEDOMETER, sysConfig.content);
                    continue;
                }

                if (CommonUrl.KEY_EXPERT.equals(sysConfig.title) ||
                        CommonUrl.KEY_ITEM.equals(sysConfig.title) ||
                        CommonUrl.KEY_SHARE_FREETRAVEL.equals(sysConfig.title) ||
                        CommonUrl.KEY_SHARE_LOCALPLOY.equals(sysConfig.title) ||
                        CommonUrl.KEY_SHARE_MUSTBUY.equals(sysConfig.title) ||
                        CommonUrl.KEY_SHARE_TEAMTRAVEL.equals(sysConfig.title) ||
                        CommonUrl.KEY_SHARE_CONSULTING_SERVICE.equals(sysConfig.title) ||
                        CommonUrl.KEY_CONSULTING_SERVICE.equals(sysConfig.title) ||
                        SysConfigType.URL_CHECK_IN.equals(sysConfig.title) ||
                        SysConfigType.URL_HOME_PEDOMETER.equals(sysConfig.title) ||
                        SysConfigType.URL_TALENT_STORY.equals(sysConfig.title) ||
                        SysConfigType.POINT_MALL_HOME_PIC.equals(sysConfig.title) ||
                        SysConfigType.URL_MASTER_CONSULTING_SERVICE_PROTOCOL.equals(sysConfig.title) ||
                        SysConfigType.URL_MASTER_CONSULTING_INTRODUCTION.equals(sysConfig.title) ||
                        SysConfigType.URL_TOUR_GUIDE_PRACTICAL_TIPS.equals(sysConfig.title) ||
                        SysConfigType.URL_TOUR_GUIDE_AUDIO_SUFFIX.equals(sysConfig.title) ||
                        SysConfigType.URL_SHOP_HOMPAGE_AUDIO_SUFFIX.equals(sysConfig.title) ||
                        ItemType.KEY_FOOD.equals(sysConfig.title) ||
                        SysConfigType.URL_INTEGRAL_MALL.equals(sysConfig.title) ||
                        SysConfigType.URL_INVITE_GIFT.equals(sysConfig.title) ||
                        SysConfigType.URL_GUIDE_SCENIC_INTRODUCE.equals(sysConfig.title) ||
                        SysConfigType.URL_ADD_BANK_CARD_PROTOCAL.equals(sysConfig.title) ||
                        SysConfigType.URL_ADD_QR_HEAD.equals(sysConfig.title) ||
                        SysConfigType.URL_LIVE_SHARE_LINK.equals(sysConfig.title)
                        ) {
                    SPUtils.saveShareDefaultUrl(mContext, sysConfig.title, sysConfig.content);
                    continue;
                }
                if (CommonUrl.KEY_SCENIC.equals(sysConfig.title)) {
                    SPUtils.saveShareDefaultUrl(mContext, CommonUrl.KEY_SCENIC_PICTURE, sysConfig.content);
                    continue;
                }

                //酒店图文
                if (CommonUrl.KEY_HOTEL.equals(sysConfig.title)) {
                    SPUtils.saveShareDefaultUrl(mContext, CommonUrl.KEY_HOTEL_PICTURE, sysConfig.content);
                    continue;
                }

                if (SysConfigType.URL_SCAN_HEXIAO.equals(sysConfig.title)) {
                    SPUtils.saveURL_SCAN_HEXIAO(mContext, sysConfig.content);
                    continue;
                }

                if (SysConfigType.URL_RECEIVE_ADDRESS.equals(sysConfig.title)) {
                    SPUtils.saveURL_RECEIVE_ADDRESS(mContext, sysConfig.content);
                    continue;
                }

                if (SysConfigType.OPEN_NEW_H5_MALL.equals(sysConfig.title)) {
                    SPUtils.saveOPEN_NEW_H5_MALL(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_SPORT_YYW_MORE.equals(sysConfig.title)) {
                    SPUtils.saveURL_SPORT_YYW_MORE(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_TRAIN_HOME.equals(sysConfig.title)) {
                    SPUtils.saveURL_TRAIN_HOME(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_TRAIN_COURSE_DETAIL.equals(sysConfig.title)) {
                    SPUtils.saveURL_TRAIN_COURSE_DETAIL(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_TRAIN_COURSE_LIST.equals(sysConfig.title)) {
                    SPUtils.saveURL_TRAIN_COURSE_LIST(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_ORG_DETAIL.equals(sysConfig.title)) {
                    SPUtils.saveURL_ORG_DETAIL(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_ORG_LIST.equals(sysConfig.title)) {
                    SPUtils.saveURL_ORG_LIST(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_WAR_SQUARE.equals(sysConfig.title)) {
                    SPUtils.saveURL_WAR_SQUARE(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_WAR_ID.equals(sysConfig.title)) {
                    SPUtils.saveURL_WAR_ID(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_CONFIRM_WAR.equals(sysConfig.title)) {
                    SPUtils.saveURL_CONFIRM_WAR(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_SPORT_YYW_MORE.equals(sysConfig.title)) {
                    SPUtils.saveURL_SPORT_YYW_MORE(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.SHOPCART_LIST.equals(sysConfig.title)) {
                    SPUtils.saveSHOPCART_LIST(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.VIEW_INTEGRAL_DETAIL.equals(sysConfig.title)) {
                    SPUtils.saveVIEW_INTEGRAL_DETAIL(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.VIEW_INTEGRAL_TASK_LIST.equals(sysConfig.title)) {
                    SPUtils.saveVIEW_INTEGRAL_TASK_LIST(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.POINT_ORDER_DETAIL.equals(sysConfig.title)) {
                    SPUtils.savePOINT_ORDER_DETAIL(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_QUANZI_ARTICLE.equals(sysConfig.title)) {
                    SPUtils.saveURL_QUANZI_ARTICLE(mContext, sysConfig.content);
                    continue;
                }
                if (SysConfigType.URL_SHORT_VIDEO.equals(sysConfig.title)) {
                    SPUtils.saveURL_SHORT_VIDEO(mContext, sysConfig.content);
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图标信息
     *
     * @param list
     */
    public void updateComIcons(ComIconList list) {
        if (list == null) {
            return;
        }
        try {
            SPUtils.saveComIcons(mContext, list.serialize().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存目的地城市
     *
     * @param list
     */
    public void updateDestCities(String type, DestinationList list) {
        if (list == null) {
            return;
        }
        try {
            SPUtils.saveDestCities(mContext, type, list.serialize().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存闪页信息
     *
     * @param booth
     */
    public void updateADInfo(Booth booth) {
        if (booth == null || booth.showcases == null || booth.showcases.size() == 0) {
            return;
        }
        try {
            SPUtils.save(mContext, SysConfigType.AD, booth.showcases.get(0).serialize().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存s首页弹窗广告信息
     *
     * @param boothlist
     */
    public void updateADMultiInfo(BoothList boothlist) {
        if (boothlist == null || boothlist.value == null || boothlist.value.size() == 0 || boothlist.value.get(0) == null || boothlist.value.get(0).showcases == null || boothlist.value.get(0).showcases.size() == 0) {
            return;
        }
        try {
            SPUtils.save(mContext, SysConfigType.MULTI_AD, boothlist.value.get(0).showcases.get(0).serialize().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存达人TAB标题
     *
     * @param booth
     */
    public void updateMasterTabInfo(Booth booth) {
        if (booth == null || booth.showcases == null || booth.showcases.size() == 0) {
            return;
        }
        try {
            SPUtils.save(mContext, SysConfigType.MASTER_TAB, booth.serialize().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param mixId
     * @return
     */
    public List<NotificationMessageEntity> getNotificationMessageEntity(int bizType, long mixId) {
        Selector selector = Selector.from(NotificationMessageEntity.class).where(DBConstant.COLUMN_BIZ_TYPE, bizType != 2 ? "!=" : "=", 2);
        if (mixId > 0) {
            selector = selector.and(DBConstant.COLUMN_ID, "<", mixId);
        }
        selector = selector.orderBy(DBConstant.COLUMN_ID, true).limit(DBConstant.LIMIT);
        try {
            return mDbUtils.findAll(selector);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public NotificationMessageEntity getLastNotification(int bizType) {
        Selector selector = Selector.from(NotificationMessageEntity.class).where(DBConstant.COLUMN_BIZ_TYPE, bizType == 2 ? "=" : "!=", "2").orderBy(DBConstant.COLUMN_ID, true).limit(1);
        try {
            List<NotificationMessageEntity> entities = mDbUtils.findAll(selector);
            if (entities != null && entities.size() > 0) return entities.get(0);
            return null;
        } catch (DbException e) {
            return null;
        }
    }

    public long getNotificationUnReadCount(int bizType) {
        try {
            return mDbUtils.count(Selector.from(NotificationMessageEntity.class).where(DBConstant.COLUMN_STATUS, "=", DBConstant.UNREAD).and(DBConstant.COLUMN_BIZ_TYPE, bizType == 2 ? "=" : "!=", 2));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 保存用户信息
     *
     * @param userLoginInfo
     */
    public void saveUserInfo(UserInfo userLoginInfo) {
        try {
            if (userLoginInfo == null) {
                return;
            }
            SPUtils.setNickName(mContext, userLoginInfo.nickname);
            SPUtils.setUserIcon(mContext, userLoginInfo.avatar);
            SPUtils.setRoleType(mContext, userLoginInfo.options);
            SPUtils.setUserCover(mContext, userLoginInfo.frontCover);
            SPUtils.setUserHomePage(mContext, userLoginInfo.isHasMainPage);
            SPUtils.setVip(mContext, userLoginInfo.vip);
            SPUtils.setUserSportHabit(mContext, userLoginInfo.sportHobby);
            //saveHeaderIcon(userLoginInfo.avatar);//放在PersonFragment中做保存头像Icon
            mDbUtils.saveOrUpdate(userLoginInfo);

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存用户运动爱好
     *
     * @param userLoginInfo
     */
    public void saveUserSportHabit(UserInfo userLoginInfo) {
        try {
            if (userLoginInfo == null) {
                return;
            }
            SPUtils.setUserSportHabit(mContext, userLoginInfo.sportHobby);
            mDbUtils.saveOrUpdate(userLoginInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("CheckResult")
    private void saveHeaderIcon(String avatar) {
//            ImageLoadManager.loadCircleImage(avatar, );
//            DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
//                    .cacheOnDisk(true).cacheInMemory(true).build();
//
//            ImageLoader.getInstance().loadImage(ImageUtils.getImageFullUrl(avatar), displayImageOptions, new ImageLoadingListener() {
//                @Override
//                public void onLoadingStarted(String imageUri, View view) {
//
//                }
//
//                @Override
//                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//                }
//
//                @Override
//                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                    if (loadedImage != null) {
//                        try {
//                            saveFile(loadedImage);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//                @Override
//                public void onLoadingCancelled(String imageUri, View view) {
//
//                }
//            });

    }

    private void saveFile(Bitmap bitmap) throws IOException {
        File file = new File(DirConstants.DIR_PIC_ORIGIN);
        if (!file.exists()) {
            file.mkdir();
        }
        File save = new File(DirConstants.DIR_PIC_ORIGIN + DirConstants.USER_HEAD_ICON);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(save));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    /**
     * 删除用户登录信息
     */
    public void deleteUserInfo() {
        try {
            mDbUtils.dropTable(UserInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mDBManager != null) {
            mDBManager = null;
        }
    }

    /**
     * 将未读通知消息 清空
     */
    public void clearNotiMsgUnRead(int bizType) {

        String sql = "UPDATE " + DBConstant.TABLE_NOTI_MESSAGE + " SET " + DBConstant.COLUMN_STATUS + " = " + DBConstant.READED + " WHERE " + DBConstant.COLUMN_STATUS + " = " + DBConstant.UNREAD + " AND " + DBConstant.COLUMN_BIZ_TYPE + (bizType == 2 ? " = " : " != ") + 2;
        try {
            if (mDbUtils.tableIsExist(NotificationMessageEntity.class)) {
                mDbUtils.execNonQuery(sql);
                EventBus.getDefault().post(new NotificationEvent(NotificationEvent.Event.UNREAD_CLEAR, bizType));
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void deleteMsg(NotificationMessageEntity entity) {
        try {
            mDbUtils.delete(NotificationMessageEntity.class, WhereBuilder.b(DBConstant.COLUMN_ID, "=", entity.getId()));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void deleteMsgByBizType(int bizType) {
        try {
            mDbUtils.delete(NotificationMessageEntity.class, WhereBuilder.b(DBConstant.COLUMN_BIZ_TYPE, bizType == 2 ? "!=" : "=", String.valueOf(bizType)));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本地缓存的视频列表
     */
    public List<VideoInfo> doGetVideoList() {
        List<VideoInfo> videos = new ArrayList<>();
        if (mDbUtils == null) {
            return videos;
        }
        try {
            Selector selector = Selector.from(VideoInfo.class);
            selector.orderBy("create_date", true);
            videos = mDbUtils.findAll(selector);
            if (videos == null) {
                return new ArrayList<>();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return videos;
    }

    /**
     * 删除视频文件
     *
     * @param videoInfo
     * @return
     */
    public boolean doDeleteVideo(VideoInfo videoInfo) {
        if (videoInfo == null || mDbUtils == null) {
            return false;
        }
        try {
            mDbUtils.deleteById(VideoInfo.class, videoInfo.id);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除视频缓存的表
     */
    public void deleteVideoTable() {
        try {
            mDbUtils.dropTable(VideoInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除视频列表
     *
     * @param videoInfos
     * @return
     */
    public boolean doDeleteVideos(List<VideoInfo> videoInfos) {
        if (videoInfos == null || mDbUtils == null) {
            return false;
        }
        for (VideoInfo videoInfo : videoInfos) {
            doDeleteVideo(videoInfo);
        }
        return true;
    }

    /**
     * 新增或者更新视频信息
     *
     * @param videoInfo
     * @return
     */
    public void addOrUpdateVideo(VideoInfo videoInfo) {
        if (videoInfo == null || mDbUtils == null) {
            return;
        }
        try {
            mDbUtils.saveOrUpdate(videoInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否缓存过
     */
    public boolean isCached(VideoInfo videoInfo) {
        if (videoInfo == null || mDbUtils == null) {
            return false;
        }
        try {
            return mDbUtils.findById(VideoInfo.class, videoInfo.id) != null;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * @param
     * @return
     */
    public void save(Object entity) {
        if (entity == null || mDbUtils == null) {
            return;
        }
        try {
            mDbUtils.save(entity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
