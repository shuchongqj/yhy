package com.newyhy.manager;


import com.newyhy.model.ErrorInfo;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.yhy.R;

import java.util.EnumMap;

/**
 * 错误码初始化管理类
 */
public class ErrorManager {
    public static String TAG = ErrorManager.class.getSimpleName();
    private static ErrorManager instance;

    private ErrorManager(){
        init();
    }

    /**
     * 网络错误类型枚举
     */
    public EnumMap<IActionTitleBar.ErrorType, ErrorInfo> mErrorDefaultMap;

    public static ErrorManager getInstance() {
        if (instance == null)
            instance = new ErrorManager();
        return instance;
    }

    private void init() {
        mErrorDefaultMap = new EnumMap<>(
                IActionTitleBar.ErrorType.class);
        // 网络错误时头部的提示
        mErrorDefaultMap.put(IActionTitleBar.ErrorType.ERRORTOP, new ErrorInfo(
                R.mipmap.ic_send_fail, R.string.sm_error_type_top, R.string.sm_error_type_top));
        // 列表为空时的显示
        mErrorDefaultMap.put(IActionTitleBar.ErrorType.EMPTYVIEW, new ErrorInfo(
                R.mipmap.error_empty_icon, R.string.error_view_nodata_title, R.string.error_view_nodata_content));
        mErrorDefaultMap.put(IActionTitleBar.ErrorType.EMPTYVIEWSEARCH, new ErrorInfo(
                R.mipmap.error_empty_search, R.string.error_view_nodata_title, R.string.error_view_nodata_content));
        // 网络加载数据错误
        mErrorDefaultMap.put(IActionTitleBar.ErrorType.ERRORNET, new ErrorInfo(
                R.mipmap.network_error, R.string.error_view_network_loaderror_title, R.string.error_view_network_loaderror_content));
        // 本地数据加载错误
        mErrorDefaultMap.put(IActionTitleBar.ErrorType.ERRORRES, new ErrorInfo(
                R.mipmap.error_empty_icon, R.string.sm_error_type_res, R.string.sm_error_type_res));
        // 无网络
        mErrorDefaultMap.put(IActionTitleBar.ErrorType.NETUNAVAILABLE, new ErrorInfo(R.mipmap.error_network_unavailable, R.string.error_view_network_unavailable, R.string.error_view_network_unavailable_notice));
        mErrorDefaultMap.put(IActionTitleBar.ErrorType.LOADING, new ErrorInfo(R.drawable.ic_loading, R.string.loading_text, R.string.loading_text_sub_title));
    }

    /**
     * 返回错误类型信息
     *
     * @return
     */
    public EnumMap<IActionTitleBar.ErrorType, ErrorInfo> getErrorDefaultMap() {
        return mErrorDefaultMap;
    }
}
