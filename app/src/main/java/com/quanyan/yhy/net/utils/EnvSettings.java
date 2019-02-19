package com.quanyan.yhy.net.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.quanyan.yhy.net.NetManager;
import com.smart.sdk.client.ApiConfig;
import com.yhy.common.config.ContextHelper;
import com.yhy.common.types.AppDebug;
import com.yhy.common.utils.SPUtils;

import java.io.IOException;

public class EnvSettings {
    public static EnvConfig read(Context context, AssetManager mAssetManager) {
        EnvConfig mEnvConfig = null;
        ApiConfig.isDebug = AppDebug.NET_DEBUG;
        try {
            String en = SPUtils.getEnv(context);
            if (TextUtils.isEmpty(en)){
                en = EnvConfig.DEFAULT_ENV;
            }
            mEnvConfig = new EnvConfigParser().parse(mAssetManager.open(en));
            if (mEnvConfig.getApiUrl() != null) {
                ContextHelper.setEnvUrl(mEnvConfig.getApiUrl());
            }

            if (mEnvConfig.getHttpsApiUrl() != null) {
                ContextHelper.setHttpsApiUrl(mEnvConfig.getHttpsApiUrl());
            }

            if (mEnvConfig.getLogUrl() != null) {
                ContextHelper.setLogUrl(mEnvConfig.getLogUrl());
            }

            if (mEnvConfig.getImageUrl() != null) {
                ContextHelper.setImageUrl(mEnvConfig.getImageUrl());

            }
            if (mEnvConfig.getUploadUrl() != null) {
                ContextHelper.setUploadImageUrl(mEnvConfig.getUploadUrl());
            }

            if (mEnvConfig.getAppId() != null) {
                ContextHelper.setAppId(mEnvConfig.getAppId());
            }

            if (mEnvConfig.getDomainId() != null) {
                ContextHelper.setDomainId(Integer.parseInt(mEnvConfig.getDomainId()));
            }

            if (mEnvConfig.getPublicKey() != null) {
                NetManager.setRsaHelper(mEnvConfig.getPublicKey());
            }

            if (mEnvConfig.getDefaultDomain() != null) {
                ContextHelper.setDefaultDomain(mEnvConfig.getDefaultDomain());
            }

            if (mEnvConfig.getLiveShareUrl() != null){
                ContextHelper.setLiveShareUrl(mEnvConfig.getLiveShareUrl());
            }

            if (mEnvConfig.getVodUrl() != null) {
                ContextHelper.setVodUrl(mEnvConfig.getVodUrl());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  mEnvConfig;
    }
}
