package com.quanyan.yhy.net;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.harwkin.nb.camera.FileUtil;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ComplainType;
import com.quanyan.yhy.common.CountType;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.MimeType;
import com.quanyan.yhy.common.QueryType;
import com.quanyan.yhy.common.ResourceType;
import com.quanyan.yhy.data.MainSquareHome;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.smart.sdk.api.request.ApiCode;
import com.smart.sdk.api.request.Comcenter_AddPictureText;
import com.smart.sdk.api.request.Comcenter_GetIcon;
import com.smart.sdk.api.request.Comcenter_GetPictureTextInfo;
import com.smart.sdk.api.request.Comcenter_UpdatePictureText;
import com.smart.sdk.api.request.Items_GetDataCounts;
import com.smart.sdk.api.request.Items_GetItemListByCode;
import com.smart.sdk.api.request.Items_GetNearbyGuideList;
import com.smart.sdk.api.request.Items_GetScenicList;
import com.smart.sdk.api.request.Items_GetSugGuideList;
import com.smart.sdk.api.request.Live_GetLiveList;
import com.smart.sdk.api.request.Msgcenter_GetPushRecord;
import com.smart.sdk.api.request.Msgcenter_SaveMsgRelevance;
import com.smart.sdk.api.request.Place_AllActiveCitys;
import com.smart.sdk.api.request.Resourcecenter_AddCrash;
import com.smart.sdk.api.request.Resourcecenter_Feedback;
import com.smart.sdk.api.request.Resourcecenter_GetArticleListByBoothCode;
import com.smart.sdk.api.request.Resourcecenter_GetBooth;
import com.smart.sdk.api.request.Resourcecenter_GetCategoryTags;
import com.smart.sdk.api.request.Resourcecenter_GetComplaintOptions;
import com.smart.sdk.api.request.Resourcecenter_GetDestList;
import com.smart.sdk.api.request.Resourcecenter_GetDestListNew;
import com.smart.sdk.api.request.Resourcecenter_GetDestinationByCode;
import com.smart.sdk.api.request.Resourcecenter_GetMainPublishBootInfo;
import com.smart.sdk.api.request.Resourcecenter_GetMainSquareHomeInfo;
import com.smart.sdk.api.request.Resourcecenter_GetMainSquareSportInfo;
import com.smart.sdk.api.request.Resourcecenter_GetMainSquareUserInfo;
import com.smart.sdk.api.request.Resourcecenter_GetMainSquareUserWalletInfo;
import com.smart.sdk.api.request.Resourcecenter_GetMianUserToolsInfo;
import com.smart.sdk.api.request.Resourcecenter_GetMianUserToolsInfoWithoutLogin;
import com.smart.sdk.api.request.Resourcecenter_GetMultiBooths;
import com.smart.sdk.api.request.Resourcecenter_GetOnlineUpgrade;
import com.smart.sdk.api.request.Resourcecenter_GetPageBooth;
import com.smart.sdk.api.request.Resourcecenter_GetQueryTermList;
import com.smart.sdk.api.request.Resourcecenter_GetSystemConfig;
import com.smart.sdk.api.request.Resourcecenter_QueryDestinationList;
import com.smart.sdk.api.request.Resourcecenter_QueryDestinationTree;
import com.smart.sdk.api.request.Resourcecenter_SubmitComplaint;
import com.smart.sdk.api.request.Snscenter_GetShortVideoDetail;
import com.smart.sdk.api.request.Snscenter_GetUGCPageListForMainSquare;
import com.smart.sdk.api.request.Snscenter_GetUgcNewAddCountByTime;
import com.smart.sdk.api.request.Snscenter_SaveUserCorrelation;
import com.smart.sdk.api.request.Track_GetCurrentPointByStep;
import com.smart.sdk.api.request.Track_GetHistoryPedometerInfo;
import com.smart.sdk.api.request.Track_GetInviteShareInfo;
import com.smart.sdk.api.request.Track_GetPedometerUserInfo;
import com.smart.sdk.api.request.Track_GetStartPoint;
import com.smart.sdk.api.request.Track_GetYesterdayPoint;
import com.smart.sdk.api.request.Track_UploadSteps;
import com.smart.sdk.api.request.Trademanager_QueryUserRecentSportsOrder;
import com.smart.sdk.api.request.User_AddAddress;
import com.smart.sdk.api.request.User_AddCertificate;
import com.smart.sdk.api.request.User_AddUserContact;
import com.smart.sdk.api.request.User_DeleteAddress;
import com.smart.sdk.api.request.User_DeleteCertificate;
import com.smart.sdk.api.request.User_DeleteUserContact;
import com.smart.sdk.api.request.User_EditUserContact;
import com.smart.sdk.api.request.User_EditUserInfo;
import com.smart.sdk.api.request.User_EdtiAddress;
import com.smart.sdk.api.request.User_GetUserAddressById;
import com.smart.sdk.api.request.User_GetUserContactsById;
import com.smart.sdk.api.request.User_GetUserInfoByUserId;
import com.smart.sdk.api.request.User_GetWapLoginToken;
import com.smart.sdk.api.request.User_UpdateCertificate;
import com.smart.sdk.api.resp.Api_BoolResp;
import com.smart.sdk.api.resp.Api_COMCENTER_PictureTextListQuery;
import com.smart.sdk.api.resp.Api_ITEMS_CodeQueryDTO;
import com.smart.sdk.api.resp.Api_ITEMS_DataCountVO;
import com.smart.sdk.api.resp.Api_ITEMS_NearGuideInfo;
import com.smart.sdk.api.resp.Api_ITEMS_QueryDataCountDTO;
import com.smart.sdk.api.resp.Api_ITEMS_QueryTerm;
import com.smart.sdk.api.resp.Api_ITEMS_QueryTermsDTO;
import com.smart.sdk.api.resp.Api_LIVE_LiveRecordAPIPageQuery;
import com.smart.sdk.api.resp.Api_MSGCENTER_MsgRelevanceInfo;
import com.smart.sdk.api.resp.Api_PLACE_CityListResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_ComplaintInfo;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_CrashList;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_DestinationCodeName;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_DestinationQuery;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_HomeInfoResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_MainSquareHomeQuery;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_MainSquareSportQuery;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_MainSquareUserInfoQuery;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_MainSquareUserWalletQuery;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_PageInfo;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_PublishBootResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_SportInfoResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_UserInfoResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_UserToolsResult;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_UserWalletResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_MainSquarePageListQuery;
import com.smart.sdk.api.resp.Api_SNSCENTER_NewUgcAddCountQuery;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsShortVideoDetailQuery;
import com.smart.sdk.api.resp.Api_SNSCENTER_UgcCountResultList;
import com.smart.sdk.api.resp.Api_TRACK_StepParam;
import com.smart.sdk.api.resp.Api_TRACK_SyncParamList;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_DetailOrder;
import com.smart.sdk.api.resp.Api_USER_MyAddressContentInfo;
import com.smart.sdk.api.resp.Api_USER_UserContact;
import com.smart.sdk.api.resp.Api_USER_UserInfo;
import com.smart.sdk.client.ApiContext;
import com.smart.sdk.client.BaseRequest;
import com.yhy.common.beans.net.model.AppDefaultConfig;
import com.yhy.common.beans.net.model.AppHomeData;
import com.yhy.common.beans.net.model.HomeTourGuide;
import com.yhy.common.beans.net.model.TalentHomeData;
import com.yhy.common.beans.net.model.club.PageInfo;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.common.BoothList;
import com.yhy.common.beans.net.model.common.ComIconList;
import com.yhy.common.beans.net.model.common.CrashInfoList;
import com.yhy.common.beans.net.model.common.PictureTextListQuery;
import com.yhy.common.beans.net.model.common.address.MyAddressContentInfo;
import com.yhy.common.beans.net.model.common.address.MyAddress_ArrayResp;
import com.yhy.common.beans.net.model.common.person.PictureTextListResult;
import com.yhy.common.beans.net.model.common.person.UserContact;
import com.yhy.common.beans.net.model.common.person.UserContact_ArrayResp;
import com.yhy.common.beans.net.model.discover.UgcInfoResultList;
import com.yhy.common.beans.net.model.guide.GuideScenicInfoList;
import com.yhy.common.beans.net.model.master.QueryTerm;
import com.yhy.common.beans.net.model.member.MemberDetail;
import com.yhy.common.beans.net.model.member.MemberPurchauseDetail;
import com.yhy.common.beans.net.model.member.PrivilegeInfoPageList;
import com.yhy.common.beans.net.model.msg.LiveRecordAPIPageResult;
import com.yhy.common.beans.net.model.msg.PushRecordResult;
import com.yhy.common.beans.net.model.paycore.AliBatchPayParam;
import com.yhy.common.beans.net.model.paycore.AliPayInfo;
import com.yhy.common.beans.net.model.paycore.BankCard;
import com.yhy.common.beans.net.model.paycore.BankCardList;
import com.yhy.common.beans.net.model.paycore.BankNameList;
import com.yhy.common.beans.net.model.paycore.BaseResult;
import com.yhy.common.beans.net.model.paycore.BillList;
import com.yhy.common.beans.net.model.paycore.CebCloudBatchPayParam;
import com.yhy.common.beans.net.model.paycore.CebCloudPayInfo;
import com.yhy.common.beans.net.model.paycore.CebCloudPayParam;
import com.yhy.common.beans.net.model.paycore.EleAccountInfo;
import com.yhy.common.beans.net.model.paycore.ElePurseBatchPayParam;
import com.yhy.common.beans.net.model.paycore.ElePursePayParam;
import com.yhy.common.beans.net.model.paycore.PayCoreBaseResult;
import com.yhy.common.beans.net.model.paycore.PcPayResult;
import com.yhy.common.beans.net.model.paycore.PcPayStatusInfo;
import com.yhy.common.beans.net.model.paycore.RechargeParam;
import com.yhy.common.beans.net.model.paycore.RechargeResult;
import com.yhy.common.beans.net.model.paycore.SettlementList;
import com.yhy.common.beans.net.model.paycore.SetupPayPwdParam;
import com.yhy.common.beans.net.model.paycore.SubmitIdCardPhotoParam;
import com.yhy.common.beans.net.model.paycore.SubmitIdCardPhotoResult;
import com.yhy.common.beans.net.model.paycore.UpdatePayPwdParam;
import com.yhy.common.beans.net.model.paycore.VerifyIdCardPhotoParam;
import com.yhy.common.beans.net.model.paycore.VerifyIdentityParam;
import com.yhy.common.beans.net.model.paycore.VerifyIdentityResult;
import com.yhy.common.beans.net.model.paycore.WithdrawParam;
import com.yhy.common.beans.net.model.paycore.WxBatchPayParam;
import com.yhy.common.beans.net.model.paycore.WxPayInfo;
import com.yhy.common.beans.net.model.pedometer.InviteShareInfo;
import com.yhy.common.beans.net.model.pedometer.PedometerHistoryResultList;
import com.yhy.common.beans.net.model.pedometer.PedometerUserInfo;
import com.yhy.common.beans.net.model.pedometer.StepParam;
import com.yhy.common.beans.net.model.pedometer.StepResult;
import com.yhy.common.beans.net.model.pedometer.SyncParamList;
import com.yhy.common.beans.net.model.pedometer.SyncResult;
import com.yhy.common.beans.net.model.rc.ArticleRecommendInfo;
import com.yhy.common.beans.net.model.rc.CategoryTagList;
import com.yhy.common.beans.net.model.rc.CityList;
import com.yhy.common.beans.net.model.rc.ComplaintInfo;
import com.yhy.common.beans.net.model.rc.ComplaintOptionInfoList;
import com.yhy.common.beans.net.model.rc.DestinationQuery;
import com.yhy.common.beans.net.model.rc.OnlineUpgrade;
import com.yhy.common.beans.net.model.rc.SystemConfig;
import com.yhy.common.beans.net.model.track.ReceivePointResult;
import com.yhy.common.beans.net.model.trip.ArroundScenicInitListResult;
import com.yhy.common.beans.net.model.trip.ScenicInfoResult;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.beans.net.model.user.Destination;
import com.yhy.common.beans.net.model.user.DestinationList;
import com.yhy.common.beans.net.model.user.UserInfo;
import com.yhy.common.beans.user.User;
import com.yhy.common.config.ContextHelper;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.JSONUtils;
import com.yhy.common.utils.SPUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 地址、游客、个人中心、升级、配置、广告位等通用接口的聚集地
 */
public class CommonNetManager extends BaseNetManager {
    private static CommonNetManager mInstance;

    public CommonNetManager(Context context, ApiContext apiContext, Handler handler) {
        mContext = context;
        mApiContext = apiContext;
        mHandler = handler;
    }


    public synchronized static CommonNetManager getInstance(Context context, ApiContext apiContext, Handler handler) {
        if (mInstance == null) {
            mInstance = new CommonNetManager(context, apiContext, handler);
        }

        return mInstance;
    }

    /**
     * 释放单例
     */
    public synchronized void release() {
        if (mInstance != null) {
            mInstance = null;
        }
    }
    //****************************************消息中心 Begin ********************************//

    /**
     * 异步上传图片
     *
     * @param filePaths 图片本地的地址
     * @param lsn
     */
    public void doUpdateImages(final List<String> filePaths,
                               final OnResponseListener<List<String>> lsn) {
        doUploadFiles(filePaths, lsn, MimeType.IMAGES);
    }

    /**
     * 异步上传视频
     *
     * @param filePaths 视频本地的地址
     * @param lsn
     */
    public void doUpdateVideos(final List<String> filePaths,
                               final OnResponseListener<List<String>> lsn) {
        doUploadFiles(filePaths, lsn, MimeType.VIDEOS);
    }

    /**
     * 异步上传日志文件
     *
     * @param filePaths 日志路径
     * @param lsn
     */
    public void doUploadLogs(final List<String> filePaths,
                             final OnResponseListener<List<String>> lsn) {
        doUploadFiles(filePaths, lsn, MimeType.TEXT);
    }

    /**
     * 异步上传文件
     *
     * @param filePaths
     * @param lsn
     * @param mimeType
     */
    private void doUploadFiles(final List<String> filePaths, final OnResponseListener<List<String>> lsn, final String mimeType) {
        if (!checkSubmitStatus(lsn) || filePaths == null) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final List<String> uploadFileNames = new ArrayList<String>();
                    for (int j = 0; j < filePaths.size(); j++) {
                        String strPath = filePaths.get(j);
                        String uploadResult = uploadFileInner(strPath, mimeType);
                        if (uploadResult != null) {
                            uploadFileNames.add(uploadResult);
                        }
                    }

                    if (uploadFileNames != null && uploadFileNames.size() > 0) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(true, uploadFileNames,
                                            ErrorCode.STATUS_OK, null);
                                }
                            }
                        });

                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(false, null,
                                            ErrorCode.UPLOAD_PICTURE_FAILED,
                                            mContext.getString(R.string.picture_upload_failed));
                                }
                            }
                        });

                    }
                } catch (final Exception e) {
                    handlerRequestException(e, lsn);
                    e.printStackTrace();
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * @param filePath
     * @param mimeType
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private String uploadFileInner(String filePath, String mimeType) throws IOException, JSONException {
        HttpClient httpclient = new DefaultHttpClient();
        // 请求处理页面
        String uploadUrl = ContextHelper.getUploadImageUrl();
        HttpPost httppost = new HttpPost(uploadUrl);
        MultipartEntity reqEntity = new MultipartEntity();
        // 创建待处理的文件
        FileBody file = new FileBody(new File(filePath), mimeType);
        // 对请求的表单域进行填充
        reqEntity.addPart(FileUtil.getFileName(filePath), file);
        // 设置请求
        httppost.setEntity(reqEntity);
        // 执行
        HttpResponse response = httpclient.execute(httppost);
        if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
            HttpEntity entity = response.getEntity();
            // 显示内容
            String uploadFileJson = null;
            if (entity != null) {
                String json = EntityUtils.toString(entity);
                HarwkinLogUtil.info("image upload json = " + json);

                JSONObject obj = new JSONObject(json);
                if (obj.has("data")) {
                    uploadFileJson = obj.getString("data");
                    if (uploadFileJson.equals("null")) {
                        return null;
                    }
                }
            }
            if (entity != null) {
                entity.consumeContent();
            }

            return uploadFileJson;
        } else {
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int length = -1;
            while ((length = is.read(buf)) != -1) {
                baos.write(buf, 0, length);
            }
            final String errorMsg = baos.toString();
            HarwkinLogUtil.info("upload failed ======" + errorMsg);
        }
        return null;
    }

    /**
     * 获取用户的信息
     *
     * @param lsn
     */
    public void doGetUserProfile(long userId, final OnResponseListener<UserInfo> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final User_GetUserInfoByUserId req = new User_GetUserInfoByUserId(userId);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                UserInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = UserInfo.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }

            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 更新用户信息
     *
     * @param userInfo
     * @param lsn
     */
    public void doUpdateUserProfile(final User userInfo,
                                    final OnResponseListener<Boolean> lsn) throws Exception {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        if (userInfo == null) {
            return;
        }
        final User_EditUserInfo req = new User_EditUserInfo(Api_USER_UserInfo.deserialize(JSONUtils.toJson(userInfo)));

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }

            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 反馈
     *
     * @param content
     * @param contact
     * @param lsn
     */
    public void doFeedback(final String content,
                           final String contact,
                           final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Resourcecenter_Feedback req = new Resourcecenter_Feedback(content);
        if (!StringUtil.isEmpty(contact)) {
            req.setContract(contact);
        }
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }

            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取广告位信息
     *
     * @param type
     * @param lsn
     */
    public void doGetBooth(final String type,
                           final OnResponseListener<Booth> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Resourcecenter_GetBooth req = new Resourcecenter_GetBooth(type);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Booth value = null;
                if (req.getResponse() != null) {
                    try {
                        value = Booth.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


    /**
     * 获取目录标签
     *
     * @param type
     * @param lsn
     */
    public void doGetCategoryTags(final String type,
                                  final OnResponseListener<CategoryTagList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Resourcecenter_GetCategoryTags req = new Resourcecenter_GetCategoryTags(type);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                CategoryTagList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = CategoryTagList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }

            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }
    //TODO
    //****************************************消息中心 END ********************************//

    /**
     * 获取游客列表
     *
     * @param lsn
     */
    public void doGetVisitorList(final Long userId,
                                 final OnResponseListener<UserContact_ArrayResp> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final User_GetUserContactsById req = new User_GetUserContactsById(userId);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                UserContact_ArrayResp value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = UserContact_ArrayResp.deserialize(req.getResponse().serialize());
                                    } catch (JSONException e) {
                                        if (lsn != null) {
                                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                                        }
                                        e.printStackTrace();
                                    }
                                }
                                if (lsn != null) {
                                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                                }
                            }
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }


    /**
     * 新增或更新游客
     *
     * @param userContact
     */
    public void doAddOrUpdateVisitorInfo(final UserContact userContact, final OnResponseListener<UserContact> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final User_AddUserContact req = new User_AddUserContact(Api_USER_UserContact.deserialize(userContact.serialize().toString()));
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                UserContact value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = UserContact.deserialize(req.getResponse().serialize());
                                    } catch (JSONException e) {
                                        if (lsn != null) {
                                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                                        }
                                        e.printStackTrace();
                                    }
                                }
                                if (lsn != null) {
                                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                                }
                            }
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 更新游客
     *
     * @param userContact
     * @param lsn
     */
    public void doUpdateVisitorInfo(final UserContact userContact, final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final User_EditUserContact req = new User_EditUserContact(Api_USER_UserContact.deserialize(userContact.serialize().toString()));
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                                }
                            }
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 增加证件
     *
     * @param userContact
     * @param lsn
     */
    public void doAddCertificate(final UserContact userContact, final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn) || userContact == null) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final User_AddCertificate req = new User_AddCertificate(Api_USER_UserContact.deserialize(userContact.serialize().toString()));
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                                }
                            }
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 更新证件
     *
     * @param userContact
     * @param lsn
     */
    public void doUpdateCertificate(final UserContact userContact, final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn) || userContact == null) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final User_UpdateCertificate req = new User_UpdateCertificate(Api_USER_UserContact.deserialize(userContact.serialize().toString()));
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                                }
                            }
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }


    /**
     * 删除游客
     *
     * @param userContact
     */
    public void doDeleteVisitor(final UserContact userContact, final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final User_DeleteUserContact req = new User_DeleteUserContact(Api_USER_UserContact.deserialize(userContact.serialize().toString()));
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(true, req.getResponse().value == 1, ErrorCode.STATUS_OK, req.getReturnMessage());
                                }
                            }
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 删除证件
     *
     * @param contactId
     * @param type      @Description("证件类型 1-身份证 2-护照 3-军人证 4-港澳通行证")
     * @param lsn
     */
    public void doDeleteCertificate(final long contactId, final String type, final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final User_DeleteCertificate req = new User_DeleteCertificate(contactId, type);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                                }
                            }
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 删除联系人
     *
     * @param contactId
     *//*
    public void doDeleteContact(int contactId, OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {

            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }*/

    /**
     * 获取地址列表
     *
     * @param userId
     * @param lsn
     */
    public void doGetAddressList(final Long userId,
                                 final OnResponseListener<MyAddress_ArrayResp> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final User_GetUserAddressById req = new User_GetUserAddressById(userId);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                MyAddress_ArrayResp value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = MyAddress_ArrayResp.deserialize(req.getResponse().serialize());
                                    } catch (JSONException e) {
                                        if (lsn != null) {
                                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                                        }
                                        e.printStackTrace();
                                    }
                                }
                                if (lsn != null) {
                                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                                }
                            }
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 新增地址信息
     *
     * @param addressContentInfo
     */
    public void doAddOrUpdateAddressInfo(final MyAddressContentInfo addressContentInfo, final OnResponseListener<MyAddressContentInfo> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final User_AddAddress req = new User_AddAddress(Api_USER_MyAddressContentInfo.deserialize(addressContentInfo.serialize().toString()));
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                MyAddressContentInfo value = null;
                                if (req.getResponse() != null) {
                                    try {
                                        value = MyAddressContentInfo.deserialize(req.getResponse().serialize());
                                    } catch (JSONException e) {
                                        if (lsn != null) {
                                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                                        }
                                        e.printStackTrace();
                                    }
                                }
                                if (lsn != null) {
                                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                                }
                            }
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (Exception e) {
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 删除地址
     *
     * @param addressId
     */
    public void doDeleteAddress(final Long addressId, final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final User_DeleteAddress req = new User_DeleteAddress(addressId);
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(true, req.getResponse().value == 1, ErrorCode.STATUS_OK, req.getReturnMessage());
                                }
                            }
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 更新地址信息
     *
     * @param addressContentInfo
     * @param lsn
     */
    public void doUpdateAddressInfo(final MyAddressContentInfo addressContentInfo, final OnResponseListener<Boolean> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                try {
                    final User_EdtiAddress req = new User_EdtiAddress(Api_USER_MyAddressContentInfo.deserialize(addressContentInfo.serialize().toString()));
                    sendRequest(mContext, mApiContext, req);
                    if (req.getReturnCode() == ApiCode.SUCCESS) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (lsn != null) {
                                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                                }
                            }
                        });
                    } else {
                        handlerException(req.getReturnCode(), req.getReturnMessage(), lsn);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 获取目的地所有省市列表
     *
     * @param lsn
     */
    public void doGetDestList(final OnResponseListener<CityList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Resourcecenter_GetDestList req = new Resourcecenter_GetDestList();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                CityList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = CityList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 升级
     *
     * @param lsn
     */
    public void doGetOnlineUpgrade(final OnResponseListener<OnlineUpgrade> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        OnlineUpgrade onlineUpgrade = new OnlineUpgrade();
//        onlineUpgrade.forceUpgrade = false;
//        onlineUpgrade.version = "21100";
//        onlineUpgrade.versionName = "0.11.25";
//        onlineUpgrade.desc = "测试升级";
//        onlineUpgrade.forceDesc = "测试强制升级";
//        onlineUpgrade.downloadUrl = "http://static.qyer.com/upload/mobile/plan.apk";
//        if (lsn != null) {
//            lsn.onComplete(true, onlineUpgrade, ErrorCode.STATUS_OK, null);
//        }
        final Resourcecenter_GetOnlineUpgrade req = new Resourcecenter_GetOnlineUpgrade();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                OnlineUpgrade value = null;
                if (req.getResponse() != null) {
                    try {
                        value = OnlineUpgrade.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 返回会员特权列表
     *
     * @param lsn
     */
    public void doGetPrivilegeInfoPageList(int pageIndex, int pageSize, final OnResponseListener<PrivilegeInfoPageList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

//        Api_MEMBERCENTER_PageInfo pageInfo = new Api_MEMBERCENTER_PageInfo();
//        pageInfo.pageNo = pageIndex;
//        pageInfo.pageSize = pageSize;
//
//        final Membercenter_GetPrivilegeInfoPageList req = new Membercenter_GetPrivilegeInfoPageList(pageInfo);
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                PrivilegeInfoPageList value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = PrivilegeInfoPageList.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 获取会员详情
     *
     * @param lsn
     */
    public void doGetMemberDetail(final OnResponseListener<MemberDetail> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Membercenter_GetMemberDetail req = new Membercenter_GetMemberDetail();
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                MemberDetail value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = MemberDetail.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 获取会员购买详情页信息
     *
     * @param lsn
     */
    public void doGetMemberPurchuseDetail(final OnResponseListener<MemberPurchauseDetail> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Membercenter_GetMemberPurchuseDetail req = new Membercenter_GetMemberPurchuseDetail();
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                MemberPurchauseDetail value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = MemberPurchauseDetail.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 获取系统配置
     *
     * @param lsn
     */
    public void doGetSystemConfig(final OnResponseListener<SystemConfig> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Resourcecenter_GetSystemConfig req = new Resourcecenter_GetSystemConfig();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                SystemConfig value = null;
                if (req.getResponse() != null) {
                    try {
                        value = SystemConfig.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取多个资源位信息
     *
     * @param codes
     * @param lsn
     */
    public void doGetMultiBooths(final List<String> codes,
                                 final OnResponseListener<BoothList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Resourcecenter_GetMultiBooths req = new Resourcecenter_GetMultiBooths(codes);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                BoothList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = BoothList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 查询图标
     *
     * @param lsn
     */
    public void doGetIcon(final OnResponseListener<ComIconList> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Comcenter_GetIcon req = new Comcenter_GetIcon();

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ComIconList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ComIconList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 专题列表
     *
     * @param code
     * @param lsn
     */
    public void doGetPageBooth(String code,
                               PageInfo pageInfo,
                               final OnResponseListener<Booth> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Resourcecenter_GetPageBooth req = new Resourcecenter_GetPageBooth(code, Api_RESOURCECENTER_PageInfo.deserialize(pageInfo.serialize().toString()));

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Booth value = null;
                if (req.getResponse() != null) {
                    try {
                        value = Booth.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 目的地列表
     *
     * @param lsn
     */
    public void doGetDestListNew(final OnResponseListener<CityList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Resourcecenter_GetDestListNew req = new Resourcecenter_GetDestListNew();

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                CityList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = CityList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取筛选条件列表
     *
     * @param code
     * @param lsn
     * @throws JSONException
     */
    public void doGetQueryFilter(String code, final OnResponseListener<QueryTerm> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Resourcecenter_GetQueryTermList req = new Resourcecenter_GetQueryTermList(code);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                QueryTerm value = null;
                if (req.getResponse() != null) {
                    try {
                        value = QueryTerm.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 初始化APP
     *
     * @param lsn
     * @throws JSONException
     */
    public void doInitApp(final OnResponseListener<AppDefaultConfig> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                Resourcecenter_GetSystemConfig configReq = new Resourcecenter_GetSystemConfig();
                Comcenter_GetIcon iconConfigReq = new Comcenter_GetIcon();
                Resourcecenter_GetBooth adBooth = new Resourcecenter_GetBooth(ResourceType.JX_HOME_AD);
                Resourcecenter_GetBooth masterTabText = new Resourcecenter_GetBooth(ResourceType.QUANYAN_TALENT_TAG_LIST);
                AppDefaultConfig configResp = new AppDefaultConfig();
                try {
                    sendRequest(mContext, mApiContext, new BaseRequest[]{configReq,
                            adBooth,
                            masterTabText});

                    if (configReq.getReturnCode() == ApiCode.SUCCESS && configReq.getResponse() != null) {
                        SystemConfig value = SystemConfig.deserialize(configReq.getResponse().serialize());
                        if (value != null) {
                            configResp.setSystemConfig(value);
                        }
                    }

                    if (iconConfigReq.getReturnCode() == ApiCode.SUCCESS) {
                        ComIconList value = ComIconList.deserialize(iconConfigReq.getResponse().serialize());
                        if (value != null) {
                            configResp.setComIconList(value);
                        }
                    }

                    // 闪屏广告
                    if (adBooth.getReturnCode() == ApiCode.SUCCESS) {
                        Booth value = Booth.deserialize(adBooth.getResponse().serialize());
                        if (value != null) {
                            configResp.setAdBooth(value);
                        }
                    }

                    if (lsn != null) {
                        lsn.onComplete(true, configResp, ErrorCode.STATUS_OK, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 获取投诉选项
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetComplaintOptions(final OnResponseListener<ComplaintOptionInfoList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.pageNo = 1;
        pageInfo.pageSize = 100;
        final Resourcecenter_GetComplaintOptions req = new Resourcecenter_GetComplaintOptions(
                ComplainType.SUBJECT,
                Api_RESOURCECENTER_PageInfo.deserialize(pageInfo.serialize()));

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ComplaintOptionInfoList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ComplaintOptionInfoList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 投诉
     *
     * @param complaintInfo
     * @param lsn
     * @throws JSONException
     */
    public void doSubmitComplaint(ComplaintInfo complaintInfo, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Resourcecenter_SubmitComplaint req = new Resourcecenter_SubmitComplaint(
                Api_RESOURCECENTER_ComplaintInfo.deserialize(complaintInfo.serialize()));

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 条件查询目的列表
     *
     * @param param
     * @param lsn
     * @throws JSONException
     */
    public void doQueryDestinationList(DestinationQuery param, final OnResponseListener<DestinationList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || param == null) {
            return;
        }

        final Resourcecenter_QueryDestinationList req = new Resourcecenter_QueryDestinationList(
                Api_RESOURCECENTER_DestinationQuery.deserialize(param.serialize()));

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                DestinationList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = DestinationList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 条件查询目的列表
     *
     * @param type
     * @param lsn
     * @throws JSONException
     */
    public void doQueryDestinationTree(String type, final OnResponseListener<DestinationList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || type == null) {
            return;
        }

        final Resourcecenter_QueryDestinationTree req = new Resourcecenter_QueryDestinationTree(type);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                DestinationList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = DestinationList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 根据code获取目的地信息
     *
     * @param cityCode
     * @param type
     * @param lsn
     * @throws JSONException
     */
    public void doGetDestinationByCode(int cityCode,
                                       String type,
                                       final OnResponseListener<Destination> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || cityCode < 1 || StringUtil.isEmpty(type)) {
            return;
        }

        final Resourcecenter_GetDestinationByCode req = new Resourcecenter_GetDestinationByCode(cityCode, type);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Destination value = null;
                if (req.getResponse() != null) {
                    try {
                        value = Destination.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 初始化周边景点页面数据
     *
     * @param lsn
     * @throws JSONException
     */
    public void doInitArroundScenicList(final String cityCode, final OnResponseListener<ArroundScenicInitListResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                ArroundScenicInitListResult result = new ArroundScenicInitListResult();

                try {
                    Resourcecenter_GetQueryTermList reqFilter = new Resourcecenter_GetQueryTermList(ResourceType.JX_SCENIC_LIST);
                    sendRequest(mContext, mApiContext, reqFilter);
                    if (reqFilter.getReturnCode() == ApiCode.SUCCESS && reqFilter.getResponse() != null) {
                        QueryTerm queryTerm = QueryTerm.deserialize(reqFilter.getResponse().serialize());
                        Api_ITEMS_QueryTermsDTO params = new Api_ITEMS_QueryTermsDTO();
                        List<Api_ITEMS_QueryTerm> terms = new ArrayList<>();
                        if (queryTerm != null) {
                            result.filtes = queryTerm;
                            for (int num = 0; num < queryTerm.queryTermList.size(); num++) {
                                QueryTerm t = queryTerm.queryTermList.get(num);
                                if (t.queryTermList != null && t.queryTermList.size() > 0) {
                                    if (!StringUtil.isEmpty(t.queryTermList.get(0).type) && !StringUtil.isEmpty(t.queryTermList.get(0).value)) {
                                        Api_ITEMS_QueryTerm term = new Api_ITEMS_QueryTerm();
                                        term.type = t.queryTermList.get(0).type;
                                        term.value = t.queryTermList.get(0).value;
                                        terms.add(term);
                                    }
                                }
                            }
                        }

                        Resourcecenter_GetDestinationByCode destReq = new Resourcecenter_GetDestinationByCode(Integer.parseInt(cityCode), ItemType.SCENIC);
                        sendRequest(mContext, mApiContext, destReq);

                        if (destReq.getReturnCode() == ApiCode.SUCCESS && destReq.getResponse() != null && destReq.getResponse().circumCities != null) {
                            Destination destination = Destination.deserialize(destReq.getResponse().serialize());
                            if (destination != null) {
                                result.destination = destination;
                            }

                            StringBuffer sb = new StringBuffer();
                            for (Api_RESOURCECENTER_DestinationCodeName value : destReq.getResponse().circumCities) {
                                sb.append(value.code).append(",");
                            }
                            if (sb.toString().length() > 0) {
                                Api_ITEMS_QueryTerm term = new Api_ITEMS_QueryTerm();
                                term.type = QueryType.DEST_CITY;
                                term.value = sb.toString().substring(0, sb.toString().length() - 1);
                                terms.add(term);

                                params.queryTerms = terms;
                                params.pageNo = 1;
                                params.pageSize = ValueConstants.PAGESIZE;

                                if (!StringUtil.isEmpty(SPUtils.getExtraCurrentLat(mContext))) {
                                    params.latitude = Double.parseDouble(SPUtils.getExtraCurrentLat(mContext));
                                }
                                if (!StringUtil.isEmpty(SPUtils.getExtraCurrentLon(mContext))) {
                                    params.longitude = Double.parseDouble(SPUtils.getExtraCurrentLon(mContext));
                                }

                                Items_GetScenicList scenicReq = new Items_GetScenicList(params);
                                sendRequest(mContext, mApiContext, scenicReq);

                                ScenicInfoResult value = null;
                                if (scenicReq.getReturnCode() == ApiCode.SUCCESS && scenicReq.getResponse() != null) {
                                    try {
                                        value = ScenicInfoResult.deserialize(scenicReq.getResponse().serialize());

                                        if (value != null) {
                                            result.result = value;
                                        }

                                        if (lsn != null) {
                                            lsn.onComplete(true, result, ErrorCode.STATUS_OK, scenicReq.getReturnMessage());
                                        }
                                    } catch (JSONException e) {
                                        if (lsn != null) {
                                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                                        }
                                        e.printStackTrace();
                                    }
                                } else {
                                    lsn.onComplete(false, null, ErrorCode.STATUS_NETWORK_EXCEPTION, scenicReq.getReturnMessage());
                                }
                            } else {
                                if (lsn != null) {
                                    lsn.onComplete(false, null, ErrorCode.STATUS_NETWORK_EXCEPTION, destReq.getReturnMessage());
                                }
                            }
                        } else {
                            if (lsn != null) {
                                lsn.onComplete(false, null, ErrorCode.STATUS_NETWORK_EXCEPTION, destReq.getReturnMessage());
                            }
                        }
                    } else {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.STATUS_NETWORK_EXCEPTION, reqFilter.getReturnMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 获取WebToken
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetWapLoginToken(final OnResponseListener<String> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final User_GetWapLoginToken req = new User_GetWapLoginToken();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取历史步数
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetHistoryPedometerInfo(final OnResponseListener<PedometerHistoryResultList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Track_GetHistoryPedometerInfo req = new Track_GetHistoryPedometerInfo();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                PedometerHistoryResultList value = null;
                if (req.getResponse() != null) {
                    try {
                        value = PedometerHistoryResultList.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取计步器用户信息
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetPedometerUserInfo(final OnResponseListener<PedometerUserInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Track_GetPedometerUserInfo req = new Track_GetPedometerUserInfo();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                PedometerUserInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = PedometerUserInfo.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 历史步数同步
     *
     * @param syncParamList
     * @param lsn
     * @throws JSONException
     */
    public void doSyncHistoryData(SyncParamList syncParamList, final OnResponseListener<SyncResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || syncParamList == null) {
            return;
        }

        final Track_UploadSteps req = new Track_UploadSteps(Api_TRACK_SyncParamList.deserialize(syncParamList.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                SyncResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = SyncResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 通过当前步数获得积分
     *
     * @param stepParam
     * @param lsn
     * @throws JSONException
     */
    public void doGetCurrentPointByStep(StepParam stepParam, final OnResponseListener<StepResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn) || stepParam == null) {
            return;
        }
        final Track_GetCurrentPointByStep req = new Track_GetCurrentPointByStep(Api_TRACK_StepParam.deserialize(stepParam.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                StepResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = StepResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取起步基金
     *
     * @param lsn
     */
    public void doGetStepStartPoint(final OnResponseListener<ReceivePointResult> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Track_GetStartPoint req = new Track_GetStartPoint();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ReceivePointResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ReceivePointResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 计步器——获取昨日积分
     *
     * @param lsn
     */
    public void doGetYesterdayPoint(final OnResponseListener<ReceivePointResult> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Track_GetYesterdayPoint req = new Track_GetYesterdayPoint();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                ReceivePointResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = ReceivePointResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取邀请配置信息
     *
     * @param lsn
     * @throws JSONException
     */
    public void doInviteShareInfo(final OnResponseListener<InviteShareInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Track_GetInviteShareInfo req = new Track_GetInviteShareInfo();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                InviteShareInfo value = null;
                if (req.getResponse() != null) {
                    try {
                        value = InviteShareInfo.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


    /**
     * 增加图文
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doAddPictureText(PictureTextListQuery params, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Comcenter_AddPictureText req = new Comcenter_AddPictureText(Api_COMCENTER_PictureTextListQuery.deserialize(params.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


    /**
     * 查询图文详情(APP统一返回属性)
     *
     * @param outId
     * @param outType
     * @param lsn
     * @throws JSONException
     */
    public void doGetPictureTextInfo(long outId, String outType, final OnResponseListener<PictureTextListResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Comcenter_GetPictureTextInfo req = new Comcenter_GetPictureTextInfo(outId, outType);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                PictureTextListResult value = null;
                if (req.getResponse() != null) {
                    try {
                        value = PictureTextListResult.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


    /**
     * 修改图文
     *
     * @param params
     * @param lsn
     * @throws JSONException
     */
    public void doUpdatePictureText(PictureTextListQuery params, final OnResponseListener<Boolean> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Comcenter_UpdatePictureText req = new Comcenter_UpdatePictureText(Api_COMCENTER_PictureTextListQuery.deserialize(params.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 获取APP首页数据
     *
     * @param lsn
     * @throws JSONException
     */
    public void doInitHomeData(final OnResponseListener<AppHomeData> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                List<String> codes = new ArrayList<>();
                codes.add(ResourceType.QUANYAN_PAGE_AD_ROTATION);
                codes.add(ResourceType.RESOURCE_CODE_A);
//                codes.add(ResourceType.RESOURCE_CODE_B);
                codes.add(ResourceType.RESOURCE_CODE_C1);
                codes.add(ResourceType.QUANYAN_TRAVEL_COMMUNITY);
//                codes.add(ResourceType.QUANYAN_CURRENT_RECOMMEND);
                codes.add(ResourceType.QUANYAN_RECOMMEND);
                codes.add(ResourceType.QUANYAN_HOME_LIVE);
                codes.add(ResourceType.QUANYAN_HOME_LIVE_MORE);
                codes.add(ResourceType.QUANYAN_HOME_CONSULTING_TALENT_MORE);
                codes.add(ResourceType.QUANYAN_SIX_BLOCKS_BANNER);

                Resourcecenter_GetMultiBooths boothsReq = new Resourcecenter_GetMultiBooths(codes);

                Api_ITEMS_CodeQueryDTO talentParams = new Api_ITEMS_CodeQueryDTO();
                talentParams.boothCode = ResourceType.QUANYAN_HOME_CONSULTING_TALENT;
                Items_GetItemListByCode consultingReq = new Items_GetItemListByCode(talentParams);

                Api_ITEMS_CodeQueryDTO expParams = new Api_ITEMS_CodeQueryDTO();
                expParams.boothCode = ResourceType.QUANYAN_HOME_PREEMPTIVE_EXPERIENCE;
                Items_GetItemListByCode expReq = new Items_GetItemListByCode(expParams);

                Resourcecenter_GetArticleListByBoothCode artReq = new Resourcecenter_GetArticleListByBoothCode(ResourceType.QUANYAN_TRAVEL_INFORMATION);

                AppHomeData configResp = new AppHomeData();
                try {
                    sendRequest(mContext, mApiContext, new BaseRequest[]{boothsReq,
                            consultingReq,
                            expReq,
                            artReq});

                    if (boothsReq.getReturnCode() == ApiCode.SUCCESS && boothsReq.getResponse() != null) {
                        BoothList value = BoothList.deserialize(boothsReq.getResponse().serialize());
                        if (value != null && value.value != null && value.value.size() > 0) {
                            for (Booth b : value.value) {
                                if (ResourceType.QUANYAN_PAGE_AD_ROTATION.equals(b.code)) {
                                    configResp.mBanner = b;
                                } else if (ResourceType.RESOURCE_CODE_A.equals(b.code)) {
                                    configResp.mBall = b;
                                }/*else if(ResourceType.RESOURCE_CODE_B.equals(b.code)){
                                    configResp.mQuanYanHeader = b;
                                }*/ else if (ResourceType.RESOURCE_CODE_C1.equals(b.code)) {
                                    configResp.mFiveBlocks = b;
                                } else if (ResourceType.QUANYAN_TRAVEL_COMMUNITY.equals(b.code)) {
                                    configResp.mThreeBlocks = b;
                                }/*else if(ResourceType.QUANYAN_CURRENT_RECOMMEND.equals(b.code)){
                                    configResp.mWeekRecommand = b;
                                }*/ else if (ResourceType.QUANYAN_RECOMMEND.equals(b.code)) {
                                    configResp.mQuanYanRecommand = b;
                                } else if (ResourceType.QUANYAN_HOME_LIVE.equals(b.code)) {
                                    configResp.mQuanYanLive = b;
                                } else if (ResourceType.QUANYAN_HOME_LIVE_MORE.equals(b.code)) {
                                    configResp.mQuanYanLiveMoreClick = b;
                                } else if (ResourceType.QUANYAN_HOME_CONSULTING_TALENT_MORE.equals(b.code)) {
                                    configResp.mMasterConsultMoreClick = b;
                                } else if (ResourceType.QUANYAN_SIX_BLOCKS_BANNER.equals(b.code)) {
                                    configResp.mSixBlocks = b;
                                }
                            }
                        }
                    }

                    if (consultingReq.getReturnCode() == ApiCode.SUCCESS && consultingReq.getResponse() != null) {
                        ShortItemsResult value = ShortItemsResult.deserialize(consultingReq.getResponse().serialize());
                        if (value != null) {
                            configResp.mConsultingGoods = value;
                        }
                    }

                    if (expReq.getReturnCode() == ApiCode.SUCCESS) {
                        ShortItemsResult value = ShortItemsResult.deserialize(expReq.getResponse().serialize());
                        if (value != null) {
                            configResp.mExperienceGoods = value;
                        }
                    }

                    if (artReq.getReturnCode() == ApiCode.SUCCESS && artReq.getResponse() != null) {
                        ArticleRecommendInfo value = ArticleRecommendInfo.deserialize(artReq.getResponse().serialize());
                        if (value != null) {
                            configResp.mArticles = value;
                        }
                    }

                    if (lsn != null) {
                        lsn.onComplete(true, configResp, ErrorCode.STATUS_OK, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 获取达人咨询数据
     *
     * @param lsn
     * @throws JSONException
     */
    public void doInitTalentHomeData(final OnResponseListener<TalentHomeData> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                List<String> codes = new ArrayList<>();
                codes.add(ResourceType.QUANYAN_TALENT_HOME_STORY);
                codes.add(ResourceType.QUANYAN_TALENT_HOME_AD_1);
                codes.add(ResourceType.QUANYAN_TALENT_HOME_AD_2);

                Resourcecenter_GetMultiBooths boothsReq = new Resourcecenter_GetMultiBooths(codes);

                Api_LIVE_LiveRecordAPIPageQuery liveRecordAPIPageQuery = new Api_LIVE_LiveRecordAPIPageQuery();
                liveRecordAPIPageQuery.liveStatus = null;
                liveRecordAPIPageQuery.locationCityCode = "-1";
                liveRecordAPIPageQuery.pageNo = 1;
                liveRecordAPIPageQuery.pageSize = 2;
                Live_GetLiveList live_getLiveList = new Live_GetLiveList(liveRecordAPIPageQuery);

                Api_ITEMS_CodeQueryDTO talentParams = new Api_ITEMS_CodeQueryDTO();
                talentParams.boothCode = ResourceType.QUANYAN_TALENT_HOME_CONSULTING;
                Items_GetItemListByCode consultingReq = new Items_GetItemListByCode(talentParams);

                Api_ITEMS_CodeQueryDTO expParams = new Api_ITEMS_CodeQueryDTO();
                expParams.boothCode = ResourceType.QUANYAN_TALENT_HOME_TRAVEL;
                Items_GetItemListByCode expReq = new Items_GetItemListByCode(expParams);

                Api_ITEMS_QueryDataCountDTO countParams = new Api_ITEMS_QueryDataCountDTO();
                List<String> types = new ArrayList<>();
                types.add(CountType.INTERESTED_CONSULT);
                types.add(CountType.INTERESTED_LINE);
                countParams.countType = types;
                Items_GetDataCounts countReq = new Items_GetDataCounts(countParams);
                TalentHomeData configResp = new TalentHomeData();
                try {
                    sendRequest(mContext, mApiContext, new BaseRequest[]{boothsReq,
                            consultingReq,
                            expReq,
                            countReq,
                            live_getLiveList});

                    if (boothsReq.getReturnCode() == ApiCode.SUCCESS && boothsReq.getResponse() != null) {
                        BoothList value = BoothList.deserialize(boothsReq.getResponse().serialize());
                        if (value != null && value.value != null && value.value.size() > 0) {
                            for (Booth b : value.value) {
                                if (ResourceType.QUANYAN_TALENT_HOME_STORY.equals(b.code)) {
                                    configResp.mTalenStory = b;
                                } else if (ResourceType.QUANYAN_TALENT_HOME_AD_1.equals(b.code)) {
                                    configResp.mRecommandBooth1 = b;
                                } else if (ResourceType.QUANYAN_TALENT_HOME_AD_2.equals(b.code)) {
                                    configResp.mRecommandBooth2 = b;
                                }
                            }
                        }
                    }

                    if (consultingReq.getReturnCode() == ApiCode.SUCCESS && consultingReq.getResponse() != null) {
                        ShortItemsResult value = ShortItemsResult.deserialize(consultingReq.getResponse().serialize());
                        if (value != null) {
                            configResp.mConsultingGoods = value;
                        }
                    }

                    if (expReq.getReturnCode() == ApiCode.SUCCESS && expReq.getResponse() != null) {
                        ShortItemsResult value = ShortItemsResult.deserialize(expReq.getResponse().serialize());
                        if (value != null) {
                            configResp.mMasterLineGoods = value;
                        }
                    }

                    if (countReq.getReturnCode() == ApiCode.SUCCESS && countReq.getResponse() != null) {
                        for (Api_ITEMS_DataCountVO data : countReq.getResponse().dataCountVOList) {
                            if (CountType.INTERESTED_CONSULT.equals(data.countType)) {
                                configResp.mConsultedCount = (int) data.count;
                            } else if (CountType.INTERESTED_LINE.equals(data.countType)) {
                                configResp.mLineReadCount = (int) data.count;
                            }
                        }
                    }

                    if (live_getLiveList.getReturnCode() == ApiCode.SUCCESS && live_getLiveList.getResponse() != null) {
                        LiveRecordAPIPageResult liveRecordAPIPageResult = LiveRecordAPIPageResult.deserialize(
                                live_getLiveList.getResponse().serialize()
                        );
                        if (liveRecordAPIPageQuery != null) {
                            configResp.mLiveRecordAPIPageResult = liveRecordAPIPageResult;
                        }
                    }

                    if (lsn != null) {
                        lsn.onComplete(true, configResp, ErrorCode.STATUS_OK, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }

    /**
     * 钱包检验验证码
     *
     * @param verifyCodeType
     * @param verifyCode
     * @param lsn
     * @throws JSONException
     */
    public void doCheckVerifyCode(String verifyCodeType, String verifyCode, String verifyIdentityCode, final OnResponseListener<PayCoreBaseResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_CheckVerifyCode req = new Paycore_CheckVerifyCode(verifyCodeType, verifyCode, verifyIdentityCode);
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                PayCoreBaseResult value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = PayCoreBaseResult.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 根据卡号得到银行卡信息
     *
     * @param bankCardNo
     * @param lsn
     * @throws JSONException
     */
    public void doGetBankCardByCardNo(String bankCardNo, final OnResponseListener<BankCard> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_GetBankCardByCardNo req = new Paycore_GetBankCardByCardNo(bankCardNo);
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                BankCard value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = BankCard.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 获取光大快捷支付的信息
     *
     * @param cebCloudPayParam
     * @param lsn
     * @throws JSONException
     */
    public void doGetCebCloudPayInfo(CebCloudPayParam cebCloudPayParam, final OnResponseListener<CebCloudPayInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_GetCebCloudPayInfo req = new Paycore_GetCebCloudPayInfo(Api_PAYCORE_CebCloudPayParam.deserialize(cebCloudPayParam.serialize()));
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                CebCloudPayInfo value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = CebCloudPayInfo.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 得到用户电子钱包的信息
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetEleAccountInfo(final OnResponseListener<EleAccountInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_GetEleAccountInfo req = new Paycore_GetEleAccountInfo();
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                EleAccountInfo value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = EleAccountInfo.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 获取钱包订单支付状态
     *
     * @param bizOrderId
     * @param lsn
     * @throws JSONException
     */
    public void doGetPcPayStatusInfo(long bizOrderId, final OnResponseListener<PcPayStatusInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_GetPayStatusInfo req = new Paycore_GetPayStatusInfo(bizOrderId);
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                PcPayStatusInfo value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = PcPayStatusInfo.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 查询用户收支明细
     *
     * @param lsn
     * @throws JSONException
     */
    public void doPageQueryUserBill(int pageNo, int pageSize, final OnResponseListener<BillList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_PageQueryUserBill req = new Paycore_PageQueryUserBill(pageNo, pageSize);
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                BillList value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = BillList.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 查询用户绑定的银行卡列表
     *
     * @param lsn
     * @throws JSONException
     */
    public void doPageQueryUserBindBankCard(int pageNo, int pageSize, final OnResponseListener<BankCardList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_PageQueryUserBindBankCard req = new Paycore_PageQueryUserBindBankCard(pageNo, pageSize);
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                BankCardList value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = BankCardList.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 查询已结算信息详情
     *
     * @param settlementId
     * @param pageNo
     * @param pageSize
     * @param lsn
     * @throws JSONException
     */
    public void doQuerySettlementDetails(long settlementId, int pageNo, int pageSize, final OnResponseListener<SettlementList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_QuerySettlementDetails req = new Paycore_QuerySettlementDetails(settlementId);
//        if(pageNo != 0 && pageSize != 0){
//            req.setPageNo(pageNo);
//            req.setPageSize(pageSize);
//        }
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                SettlementList value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = SettlementList.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 查询已结算信息
     *
     * @param pageNo
     * @param pageSize
     * @param lsn
     * @throws JSONException
     */
    public void doQuerySettlements(int pageNo, int pageSize, final OnResponseListener<SettlementList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_QuerySettlements req = new Paycore_QuerySettlements();
//        if(pageNo != 0 && pageSize != 0){
//            req.setPageNo(pageNo);
//            req.setPageSize(pageSize);
//        }
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                SettlementList value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = SettlementList.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 得到交易状态
     *
     * @param payOrderId
     * @param lsn
     * @throws JSONException
     */
    public void doQueryTransStatus(long payOrderId, final OnResponseListener<PcPayResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_QueryTransStatus req = new Paycore_QueryTransStatus(payOrderId);
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                PcPayResult value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = PcPayResult.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 查询未结算信息详情
     *
     * @param pageNo
     * @param pageSize
     * @param lsn
     * @throws JSONException
     */
    public void doQueryUnsettlements(int pageNo, int pageSize, final OnResponseListener<SettlementList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_QueryUnsettlements req = new Paycore_QueryUnsettlements();
//        if(pageNo != 0 && pageSize != 0){
//            req.setPageNo(pageNo);
//            req.setPageSize(pageSize);
//        }
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                SettlementList value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = SettlementList.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 钱包充值
     *
     * @param rechargeParam
     * @param lsn
     * @throws JSONException
     */
    public void doRecharge(RechargeParam rechargeParam, final OnResponseListener<RechargeResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_Recharge req = new Paycore_Recharge(Api_PAYCORE_RechargeParam.deserialize(rechargeParam.serialize()));
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                RechargeResult value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = RechargeResult.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 钱包发送验证码
     *
     * @param verifyCodeType
     * @param mobilePhone
     * @param lsn
     * @throws JSONException
     */
    public void doSendVerifyCode(String verifyCodeType, String mobilePhone, final OnResponseListener<PayCoreBaseResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_SendVerifyCode req = new Paycore_SendVerifyCode(verifyCodeType);
//        if(!StringUtil.isEmpty(mobilePhone)){
//            req.setMobilePhone(mobilePhone);
//        }
//
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                PayCoreBaseResult value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = PayCoreBaseResult.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 设置支付密码
     *
     * @param setupPayPwdParam
     * @param lsn
     * @throws JSONException
     */
    public void doSetupPayPwd(SetupPayPwdParam setupPayPwdParam, final OnResponseListener<PayCoreBaseResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_SetupPayPwd req = new Paycore_SetupPayPwd(Api_PAYCORE_SetupPayPwdParam.deserialize(setupPayPwdParam.serialize()));
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                PayCoreBaseResult value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = PayCoreBaseResult.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 更新支付密码
     *
     * @param updatePayPwdParam
     * @param lsn
     * @throws JSONException
     */
    public void doUpdatePayPwd(UpdatePayPwdParam updatePayPwdParam, final OnResponseListener<PayCoreBaseResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_UpdatePayPwd req = new Paycore_UpdatePayPwd(Api_PAYCORE_UpdatePayPwdParam.deserialize(updatePayPwdParam.serialize()));
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                PayCoreBaseResult value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = PayCoreBaseResult.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 验证身份
     *
     * @param verifyIdentityParam
     * @param lsn
     * @throws JSONException
     */
    public void doVerifyIdentity(VerifyIdentityParam verifyIdentityParam, final OnResponseListener<VerifyIdentityResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_VerifyIdentity req = new Paycore_VerifyIdentity(Api_PAYCORE_VerifyIdentityParam.deserialize(verifyIdentityParam.serialize()));
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                VerifyIdentityResult value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = VerifyIdentityResult.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 验证支付密码
     *
     * @param payPwd
     * @param lsn
     * @throws JSONException
     */
    public void doVerifyPayPwd(String payPwd, final OnResponseListener<PayCoreBaseResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_VerifyPayPwd req = new Paycore_VerifyPayPwd(payPwd);
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                PayCoreBaseResult value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = PayCoreBaseResult.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 提现
     *
     * @param withdrawParam
     * @param lsn
     * @throws JSONException
     */
    public void doWithdraw(WithdrawParam withdrawParam, final OnResponseListener<PayCoreBaseResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_Withdraw req = new Paycore_Withdraw(Api_PAYCORE_WithdrawParam.deserialize(withdrawParam.serialize()));
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                PayCoreBaseResult value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = PayCoreBaseResult.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }


    /**
     * 钱包批量支付
     *
     * @param elePurseBatchPayParam
     * @param lsn
     * @throws JSONException
     */
    public void doElePurseBatchPay(ElePurseBatchPayParam elePurseBatchPayParam, final OnResponseListener<PayCoreBaseResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_ElePurseBatchPay req = new Paycore_ElePurseBatchPay(Api_PAYCORE_ElePurseBatchPayParam.deserialize(elePurseBatchPayParam.serialize()));
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                PayCoreBaseResult value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = PayCoreBaseResult.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 支付宝批量支付的参数
     *
     * @param aliBatchPayParam
     * @param lsn
     * @throws JSONException
     */
    public void doGetAliBatchPayInfo(AliBatchPayParam aliBatchPayParam, final OnResponseListener<AliPayInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_GetAliBatchPayInfo req = new Paycore_GetAliBatchPayInfo(Api_PAYCORE_AliBatchPayParam.deserialize(aliBatchPayParam.serialize()));
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                AliPayInfo value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = AliPayInfo.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 获取光大快捷批量支付的信息
     *
     * @param cebCloudBatchPayParam
     * @param lsn
     * @throws JSONException
     */
    public void doGetCebCloudBatchPayInfo(CebCloudBatchPayParam cebCloudBatchPayParam, final OnResponseListener<CebCloudPayInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_GetCebCloudBatchPayInfo req = new Paycore_GetCebCloudBatchPayInfo(Api_PAYCORE_CebCloudBatchPayParam.deserialize(cebCloudBatchPayParam.serialize()));
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                CebCloudPayInfo value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = CebCloudPayInfo.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }


    /**
     * 钱包支付
     *
     * @param elePursePayParam
     * @param lsn
     * @throws JSONException
     */
    public void doElePursePay(ElePursePayParam elePursePayParam, final OnResponseListener<PayCoreBaseResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_ElePursePay req = new Paycore_ElePursePay(Api_PAYCORE_ElePursePayParam.deserialize(elePursePayParam.serialize()));
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                PayCoreBaseResult value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = PayCoreBaseResult.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 得到银行名列表
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetBankNameList(final OnResponseListener<BankNameList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_GetBankNameList req = new Paycore_GetBankNameList();
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                BankNameList value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = BankNameList.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 加载景区导览首页数据
     *
     * @param lsn
     * @throws JSONException
     */
    public void doLoadHomeTourGuideData(final OnResponseListener<HomeTourGuide> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Runnable requestThread = new Runnable() {
            @Override
            public void run() {
                Resourcecenter_GetBooth bannersReq = new Resourcecenter_GetBooth(ResourceType.QUANYAN_HOME_TOUR_GUIDE_BANNERS);
                Resourcecenter_GetBooth bannerReq = new Resourcecenter_GetBooth(ResourceType.QUANYAN_HOME_TOUR_GUIDE_ONE_BANNER);

                Api_ITEMS_NearGuideInfo params = new Api_ITEMS_NearGuideInfo();
                if (!StringUtil.isEmpty(SPUtils.getExtraCurrentCityCode(mContext))) {
                    params.cityCode = SPUtils.getExtraCurrentCityCode(mContext);
                }

                if (!StringUtil.isEmpty(SPUtils.getExtraCurrentLat(mContext))) {

                    params.latitude = Double.valueOf(SPUtils.getExtraCurrentLat(mContext));
                    ;
                }
                if (!StringUtil.isEmpty(SPUtils.getExtraCurrentLon(mContext))) {
                    params.longitude = Double.valueOf(SPUtils.getExtraCurrentLon(mContext));
                    ;
                }
                Items_GetNearbyGuideList nearbyReq = new Items_GetNearbyGuideList(params);
                Items_GetSugGuideList sugReq = new Items_GetSugGuideList();

                HomeTourGuide tourGuide = new HomeTourGuide();
                try {
                    sendRequest(mContext, mApiContext, new BaseRequest[]{
                            bannersReq, bannerReq, nearbyReq, sugReq});

                    if (bannersReq.getReturnCode() == ApiCode.SUCCESS && bannersReq.getResponse() != null) {
                        tourGuide.banners = Booth.deserialize(bannersReq.getResponse().serialize());
                    }

                    if (bannerReq.getReturnCode() == ApiCode.SUCCESS && bannerReq.getResponse() != null) {
                        tourGuide.singleBanner = Booth.deserialize(bannerReq.getResponse().serialize());
                    }

                    if (nearbyReq.getReturnCode() == ApiCode.SUCCESS && nearbyReq.getResponse() != null) {
                        tourGuide.nearByGuideScenicInfoList = GuideScenicInfoList.deserialize(nearbyReq.getResponse().serialize());
                    }

                    if (sugReq.getReturnCode() == ApiCode.SUCCESS && sugReq.getResponse() != null) {
                        tourGuide.recommandGuideScenicInfoList = GuideScenicInfoList.deserialize(sugReq.getResponse().serialize());
                    }

                    if (lsn != null) {
                        lsn.onComplete(true, tourGuide, ErrorCode.STATUS_OK, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handlerRequestException(e, lsn);
                }
            }
        };
        NetThreadManager.getInstance().execute(requestThread);
    }


    /**
     * 验证照片是否为身份证
     *
     * @param verifyIdCardPhotoParam
     * @param lsn
     * @throws JSONException
     */
    public void doVerifyIdCardPhoto(VerifyIdCardPhotoParam verifyIdCardPhotoParam, final OnResponseListener<BaseResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_VerifyIdCardPhoto req = new Paycore_VerifyIdCardPhoto(Api_PAYCORE_VerifyIdCardPhotoParam.deserialize(verifyIdCardPhotoParam.serialize()));
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                BaseResult value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = BaseResult.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }


    /**
     * 提交身份证照片
     *
     * @param submitIdCardPhotoParam
     * @param lsn
     * @throws JSONException
     */
    public void doSubmitIdCardPhoto(SubmitIdCardPhotoParam submitIdCardPhotoParam, final OnResponseListener<SubmitIdCardPhotoResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_SubmitIdCardPhoto req = new Paycore_SubmitIdCardPhoto(Api_PAYCORE_SubmitIdCardPhotoParam.deserialize(submitIdCardPhotoParam.serialize()));
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                SubmitIdCardPhotoResult value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = SubmitIdCardPhotoResult.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 获取微信批量支付的信息
     *
     * @param wxBatchPayParam
     * @param lsn
     * @throws JSONException
     */
    public void doGetWxBatchPayInfo(WxBatchPayParam wxBatchPayParam, final OnResponseListener<WxPayInfo> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        final Paycore_GetWxBatchPayInfo req = new Paycore_GetWxBatchPayInfo(Api_PAYCORE_WxBatchPayParam.deserialize(wxBatchPayParam.serialize()));
//        new RequestExecutor() {
//
//            @Override
//            protected boolean prepare() {
//                return checkSubmitStatus(lsn);
//            }
//
//            @Override
//            protected void postExecute(BaseRequest<?> request) {
//                WxPayInfo value = null;
//                if (req.getResponse() != null) {
//                    try {
//                        value = WxPayInfo.deserialize(req.getResponse().serialize());
//                    } catch (JSONException e) {
//                        if (lsn != null) {
//                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                }
//                if (lsn != null) {
//                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }
//            }
//
//            @Override
//            protected void handleException(BaseRequest<?> request) {
//                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
//            }
//
//            @Override
//            protected void handleRequestException(Exception exception) {
//                handlerRequestException(exception, lsn);
//            }
//        }.execute(req);
    }

    /**
     * 添加crash信息
     *
     * @param crashInfoList
     * @param lsn
     * @throws JSONException
     */
    public void doAddCrash(CrashInfoList crashInfoList, final OnResponseListener<Boolean> lsn) throws JSONException {

        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Resourcecenter_AddCrash req = new Resourcecenter_AddCrash(Api_RESOURCECENTER_CrashList.deserialize(crashInfoList.serialize()));
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                if (lsn != null) {
                    lsn.onComplete(true, req.getResponse().value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }

            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


    public void doGetHomeCourtData(String detail, double latitude, double longtitude,
                                   String citycode, int interestType,
                                   final OnResponseListener<Api_RESOURCECENTER_HomeInfoResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        QuanPoiInfo quanPoiInfo=  new QuanPoiInfo();
//        quanPoiInfo.detail=detail;
//        quanPoiInfo.latitude=latitude;
//        quanPoiInfo.longitude=longtitude;
        Gson gson = new Gson();
//       String poiJson= gson.toJson(quanPoiInfo);
//        Api_RESOURCECENTER_POIInfo poiInfo =  Api_RESOURCECENTER_POIInfo.deserialize(poiJson);
        MainSquareHome mainSquareHome = new MainSquareHome();
        mainSquareHome.cityCode = citycode;
        mainSquareHome.interestType = interestType;
        mainSquareHome.latitude = latitude;
        mainSquareHome.longitude = longtitude;

        String queryJson = gson.toJson(mainSquareHome);
        Api_RESOURCECENTER_MainSquareHomeQuery query = Api_RESOURCECENTER_MainSquareHomeQuery.deserialize(queryJson);
        final Resourcecenter_GetMainSquareHomeInfo req = new Resourcecenter_GetMainSquareHomeInfo(query);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Api_RESOURCECENTER_HomeInfoResult value = null;
                if (req.getResponse() != null) {
                    try {
//                        value = String.deserialize(req.getResponse().serialize());
                        Gson g = new Gson();
                        value = g.fromJson(req.getResponse().serialize().toString(), Api_RESOURCECENTER_HomeInfoResult.class);
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


    public void doGetHomeCourtUGCData(int pageNo, int pageSize, int type, String cityCode, double latitude, double longitude,
                                      final OnResponseListener<UgcInfoResultList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        PageInfo pgInfo = new PageInfo();
//        pgInfo.pageNo=pageNo;
//        pgInfo.pageSize=pageSize;
//        Gson gson =new Gson();
//        String pageInfoJson= gson.toJson(pgInfo);
//        Api_SNSCENTER_PageInfo pageInfo =  Api_SNSCENTER_PageInfo.deserialize(pageInfoJson);
//        UgcData ugcData = new UgcData();
//
//        ugcData.type=type;
//        ugcData.pageInfo = pageInfo;
//
//        String queryJson= gson.toJson(ugcData);
//        Api_SNSCENTER_TopicPageListQuery query = Api_SNSCENTER_TopicPageListQuery.deserialize(queryJson);
//         Snscenter_GetUGCPageList req = new Snscenter_GetUGCPageList(query);
        Api_SNSCENTER_MainSquarePageListQuery query = new Api_SNSCENTER_MainSquarePageListQuery();
        query.cityCode = cityCode;
        query.interestType = type;
        query.latitude = latitude;
        query.longitude = longitude;
        query.pageNo = pageNo;
        query.pageSize = pageSize;
        final Snscenter_GetUGCPageListForMainSquare req = new Snscenter_GetUGCPageListForMainSquare(query);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                UgcInfoResultList value = null;
                if (req.getResponse() != null) {
                    try {
//                      value = String.deserialize(req.getResponse().serialize());
                        Gson g = new Gson();
                        value = g.fromJson(req.getResponse().serialize().toString(), UgcInfoResultList.class);

//                        value =HomeCourtUgcReturn.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


    public void doGetMineInfo(long userId,
                              final OnResponseListener<Api_RESOURCECENTER_UserInfoResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;

        }
//        Long userIdInteger = new Long(userId);
//        Gson g = new Gson();
//        String value = g.toJson(userIdInteger);
        String value = "{uid:" + userId + "}";
        Api_RESOURCECENTER_MainSquareUserInfoQuery query = Api_RESOURCECENTER_MainSquareUserInfoQuery.deserialize(value);
        final Resourcecenter_GetMainSquareUserInfo req = new Resourcecenter_GetMainSquareUserInfo(query);


        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Api_RESOURCECENTER_UserInfoResult value = null;
                if (request.getResponse() != null) {
                    try {
//                        value = String.deserialize(req.getResponse().serialize());
                        Gson g = new Gson();
                        value = g.fromJson(req.getResponse().serialize().toString(), Api_RESOURCECENTER_UserInfoResult.class);

//                        value =HomeCourtUgcReturn.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    public void doGetUserWallet(long userId, final OnResponseListener<Api_RESOURCECENTER_UserWalletResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
//        Long userIdInteger = new Long(userId);
//        Gson g = new Gson();
        String value = "{uid:" + userId + "}";

        Api_RESOURCECENTER_MainSquareUserWalletQuery query = Api_RESOURCECENTER_MainSquareUserWalletQuery.deserialize(value);

        final Resourcecenter_GetMainSquareUserWalletInfo req = new Resourcecenter_GetMainSquareUserWalletInfo(query);


        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {

                Api_RESOURCECENTER_UserWalletResult value = null;
                if (request.getResponse() != null) {
                    try {
                        Gson g = new Gson();
                        value = g.fromJson(req.getResponse().serialize().toString(), Api_RESOURCECENTER_UserWalletResult.class);
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 根据用户获取工具信息
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetUserToolsInfo(final OnResponseListener<Api_RESOURCECENTER_UserToolsResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Resourcecenter_GetMianUserToolsInfo req = new Resourcecenter_GetMianUserToolsInfo();

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {

                Api_RESOURCECENTER_UserToolsResult value = null;
                if (request.getResponse() != null) {
                    try {
                        Gson g = new Gson();
                        value = g.fromJson(req.getResponse().serialize().toString(), Api_RESOURCECENTER_UserToolsResult.class);
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 未登录根据获取工具信息
     *
     * @param lsn
     * @throws JSONException
     */
    public void doGetToolsInfoWithoutLogin(final OnResponseListener<Api_RESOURCECENTER_UserToolsResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Resourcecenter_GetMianUserToolsInfoWithoutLogin req = new Resourcecenter_GetMianUserToolsInfoWithoutLogin();
        new RequestExecutor() {
            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {

                Api_RESOURCECENTER_UserToolsResult value = null;
                if (request.getResponse() != null) {
                    try {
                        Gson g = new Gson();
                        value = g.fromJson(req.getResponse().serialize().toString(), Api_RESOURCECENTER_UserToolsResult.class);
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    public void doGetSportInfo(long latitude, long longtitude,
                               String cityCode,
                               final OnResponseListener<Api_RESOURCECENTER_SportInfoResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        MainSquareHome home = new MainSquareHome();
        home.cityCode = cityCode;
        home.latitude = latitude;
        home.longitude = longtitude;
        Gson g = new Gson();
        String value = g.toJson(home);
        Api_RESOURCECENTER_MainSquareSportQuery query = Api_RESOURCECENTER_MainSquareSportQuery.deserialize(value);
        final Resourcecenter_GetMainSquareSportInfo req = new Resourcecenter_GetMainSquareSportInfo(query);


        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Api_RESOURCECENTER_SportInfoResult value = null;
                if (request.getResponse() != null) {
                    try {
//                        value = String.deserialize(req.getResponse().serialize());
                        Gson g = new Gson();
                        value = g.fromJson(req.getResponse().serialize().toString(), Api_RESOURCECENTER_SportInfoResult.class);

//                        value =HomeCourtUgcReturn.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    public void doGetMainPublishBootInfo(String cityName, String cityCode, final OnResponseListener<Api_RESOURCECENTER_PublishBootResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Resourcecenter_GetMainPublishBootInfo req = new Resourcecenter_GetMainPublishBootInfo(cityName);
        req.setCityCode(cityCode);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Api_RESOURCECENTER_PublishBootResult value = null;
                if (request.getResponse() != null) {
                    try {
//                        value = String.deserialize(req.getResponse().serialize());
                        Gson g = new Gson();
                        value = g.fromJson(req.getResponse().serialize().toString(), Api_RESOURCECENTER_PublishBootResult.class);

//                        value =HomeCourtUgcReturn.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


    public void doGetCityInfo(boolean containDistrictFlg,
                              final OnResponseListener<Api_PLACE_CityListResult> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Place_AllActiveCitys req = new Place_AllActiveCitys(containDistrictFlg);


        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Api_PLACE_CityListResult value = null;
                if (request.getResponse() != null) {
                    try {
//                        value = String.deserialize(req.getResponse().serialize());
                        Gson g = new Gson();
                        value = g.fromJson(req.getResponse().serialize().toString(), Api_PLACE_CityListResult.class);

//                        value =HomeCourtUgcReturn.deserialize(req.getResponse().serialize());
                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                }
                if (lsn != null) {
                    lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    public void doGetUgcNewAddCountByTime(double mLat, double mLng, String mCityCode, int mType, long lastRequestTime,
                                          final OnResponseListener<Api_SNSCENTER_UgcCountResultList> lsn) throws JSONException {

        if (!checkSubmitStatus(lsn)) {
            return;
        }

        Api_SNSCENTER_NewUgcAddCountQuery query = new Api_SNSCENTER_NewUgcAddCountQuery();
        query.latitude = mLat;
        query.longitude = mLng;
        query.cityCode = mCityCode;
        query.interestType = mType;
        query.openTime = lastRequestTime;

        final Snscenter_GetUgcNewAddCountByTime req = new Snscenter_GetUgcNewAddCountByTime(query);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Api_SNSCENTER_UgcCountResultList list = null;
                if (req.getResponse() != null) {
                    try {
                        Gson g = new Gson();
                        list = g.fromJson(req.getResponse().serialize().toString(), Api_SNSCENTER_UgcCountResultList.class);

                    } catch (JSONException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                    if (lsn != null) {
                        lsn.onComplete(true, list, ErrorCode.STATUS_OK, req.getReturnMessage());
                    }
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);
            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 我的模块 获取最接近开始的活动或运动子订单
     */
    public void doGetUserRecentSportsOrder(final OnResponseListener<Api_TRADEMANAGER_DetailOrder> lsn) {
        if (!checkSubmitStatus(lsn)) {
            return;
        }

        final Trademanager_QueryUserRecentSportsOrder req = new Trademanager_QueryUserRecentSportsOrder();
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
                Api_TRADEMANAGER_DetailOrder order = null;
                if (lsn != null) {
                    try {
                        Gson g = new Gson();
                        if (req.getResponse() != null) {
                            order = g.fromJson(req.getResponse().serialize().toString(), Api_TRADEMANAGER_DetailOrder.class);
                        } else {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR,  req.getReturnMessage());
                            return;
                        }
                    } catch (Exception e) {
                        if (lsn != null) {
                            lsn.onComplete(false, null, ErrorCode.DATA_FORMAT_ERROR, e.getMessage());
                        }
                        e.printStackTrace();
                    }
                    lsn.onComplete(true, order, ErrorCode.STATUS_OK, req.getReturnMessage());
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }

    /**
     * 首页弹窗广告
     */
    public void getHomePopup(final OnResponseListener<BoothList> lsn) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        List<String> codes = new ArrayList<>();
        codes.add(ResourceType.JX_SQUARE_AD);
        final Resourcecenter_GetMultiBooths req = new Resourcecenter_GetMultiBooths(codes);

        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> request) {
//                Api_TRADEMANAGER_DetailOrder order = null;
//                if (lsn != null) {
//                    try {
//                        Gson g = new Gson();
//                        if (req.getResponse() == null) {
//                            return;
//                        }
//                        order = g.fromJson(req.getResponse().serialize().toString(),Api_TRADEMANAGER_DetailOrder.class);
//                    } catch (Exception e) {
//                        if (lsn != null){
//                            lsn.onComplete(false,null,ErrorCode.DATA_FORMAT_ERROR,e.getMessage());
//                        }
//                        e.printStackTrace();
//                    }
//                    lsn.onComplete(true,order, ErrorCode.STATUS_OK, req.getReturnMessage());
//                }

                // 首页弹窗广告
                try {
                    if (req.getReturnCode() == ApiCode.SUCCESS && req.getResponse() != null) {
                        BoothList value = BoothList.deserialize(req.getResponse().serialize());
                        if (value != null) {
                            lsn.onComplete(true, value, ErrorCode.STATUS_OK, req.getReturnMessage());
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


    /**
     * 小视频播放次数加1
     *
     * @param lsn
     * @throws JSONException
     */
    public void getShortVideoDetail(final OnResponseListener<Long> lsn, long ugcId) throws JSONException {
        if (!checkSubmitStatus(lsn)) {
            return;
        }
        final Api_SNSCENTER_SnsShortVideoDetailQuery query = new Api_SNSCENTER_SnsShortVideoDetailQuery();
        query.ugcId = ugcId;
        Snscenter_GetShortVideoDetail req = new Snscenter_GetShortVideoDetail(query);
        new RequestExecutor() {

            @Override
            protected boolean prepare() {
                return checkSubmitStatus(lsn);
            }

            @Override
            protected void postExecute(BaseRequest<?> req) {
                try {
                    //回调中不用做处理
                } catch (Exception e) {

                }
            }

            @Override
            protected void handleException(BaseRequest<?> request) {
                handlerException(request.getReturnCode(), request.getReturnMessage(), lsn);

            }

            @Override
            protected void handleRequestException(Exception exception) {
                handlerRequestException(exception, lsn);
            }
        }.execute(req);
    }


}

