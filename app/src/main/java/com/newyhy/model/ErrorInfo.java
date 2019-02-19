package com.newyhy.model;

/**
 * 网络错误类型信息类
 */
public class ErrorInfo {
    public int errorIconRes;
    public int errorTextRes;
    public int errorMessageRes;

    /**
     * 网络错误信息的设置
     *
     * @param icon            错误类型图标
     * @param text            错误文本标题
     * @param errorMessageRes 错误文本内容子标题
     */
    public ErrorInfo(int icon, int text, int errorMessageRes) {
        errorIconRes = icon;
        errorTextRes = text;
        this.errorMessageRes = errorMessageRes;
    }
}
