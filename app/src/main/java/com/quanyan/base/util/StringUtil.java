package com.quanyan.base.util;

import android.content.Context;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.smart.sdk.api.request.ApiCode;
import com.yhy.common.beans.net.model.tm.ServiceArea;
import com.yhy.common.utils.SPUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with Android Studio.
 * Title:StringUtil
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/10/30
 * Time:下午7:22
 * Version 1.0
 */
public class StringUtil {
//    public static  String FALG_RMB = "￥";

    /**
     * 统一的人民币符号
     *
     * @param context
     * @return
     */
    public static String getFlagRmb(Context context) {
        return context.getString(R.string.money_symbol);
    }

    /**
     * 把人民币分转换成圆
     *
     * @param rmbFen
     * @return
     */
    public static double converRMb2Yun(long rmbFen) {
        return (double) rmbFen / 100;
    }

    public static String converRMb2YunStr(long rmbFen) {
        if (rmbFen % 100 == 0) {
            return new DecimalFormat("#########0")
                    .format(converRMb2Yun(rmbFen));
        } else if (rmbFen % 10 == 0) {
            return new DecimalFormat("#########0.0")
                    .format(converRMb2Yun(rmbFen));
        } else if (rmbFen == 0) {
            return "0";
        } else {
            return new DecimalFormat("#########0.##").format(converRMb2Yun(rmbFen));
        }
    }

    /**
     * 价格当字符串处理(带小数点)
     *
     * @param price
     * @return
     */
    public static String convertPriceNoSymbol(long price) {
        String priceString = String.valueOf(price);
        if (priceString.length() > 2) {
            String prefix = priceString.substring(0, priceString.length() - 2);
            String lastfix = priceString.substring(priceString.length() - 2);
            return prefix + "." + lastfix;
        } else if (priceString.length() == 2) {
            return "0." + priceString;
        } else {
            return "0.0" + priceString;
        }
    }

    /**
     * 从小数点后非0位开始显示包含人民币符号
     *
     * @param price
     * @return
     */
    public static String convertPriceNoSymbolExceptLastZeroWithFlag(Context context, long price) {
        return getFlagRmb(context) + convertPriceNoSymbolExceptLastZero(price);
    }

    /**
     * 从小数点后非0位开始显示
     *
     * @param price
     * @return
     */
    public static String convertPriceNoSymbolExceptLastZero(long price) {
        String priceString = String.valueOf(price);
        if (priceString.length() > 2) {
            String prefix = priceString.substring(0, priceString.length() - 2);
            String lastfix = priceString.substring(priceString.length() - 2);
            int zero = getExceptLastZeroIndex(lastfix);
            switch (zero) {
                case 0:
                    return prefix + "." + lastfix.substring(0, 1);
                case 1:
                    return prefix + "." + lastfix;
                default:
                    return prefix;
            }
        } else if (priceString.length() == 2) {
            int zero = getExceptLastZeroIndex(priceString);
            switch (zero) {
                case 0:
                    return "0." + priceString.substring(0, 1);
                case 1:
                    return "0." + priceString;
                default:
                    return "0";
            }
        } else {
            if ("0".equals(priceString)) {
                return "0";
            } else {
                return "0.0" + priceString;
            }
        }

    }

    /**
     * 10积分抵1元, 最多抵扣不超过 maxPoint
     *
     * @param maxPoint
     * @return
     */
    public static String convertScoreToDiscount(Context context, long maxPoint) {
        if (maxPoint > 0) {
            long score = SPUtils.getScore(context);
            if (maxPoint > score){
                return pointToYuanOne(score*10);
            }else {
                return pointToYuanOne(maxPoint*10);

            }

        } else {
            return "0";
        }

    }

    /**
     *   传值的时候统一将point*10   和后台传的价格级别一致
     * @param point
     * @return
     */
    public static String pointToYuan(long point) {
//        if (point >= 10){
//            if (point % 10 == 0) {
//                return String.valueOf(point/10);
//            }else {
//                return new DecimalFormat("#########0.0")
//                        .format(point/10f);
//            }
//        }else {
//            return new DecimalFormat("#########0.0")
//                    .format(point/10f);
//        }
        if (point >= 100){
            if (point % 100 == 0) {
                return String.valueOf(point/100);
            }else {
                return new DecimalFormat("#########0.00")
                        .format(point/100f);
            }
        }else {
            return new DecimalFormat("#########0.00")
                    .format(point/100f);
        }
    }

    public static String pointToYuanOne(long point) {
//        if (point >= 10){
//            if (point % 10 == 0) {
//                return String.valueOf(point/10);
//            }else {
//                return new DecimalFormat("#########0.0")
//                        .format(point/10f);
//            }
//        }else {
//            return new DecimalFormat("#########0.0")
//                    .format(point/10f);
//        }
        if (point >= 100){
            if (point % 100 == 0) {
                return String.valueOf(point/100);
            }else {
                return new DecimalFormat("#########0.0")
                        .format(point/100f);
            }
        }else {
            return new DecimalFormat("#########0.0")
                    .format(point/100f);
        }
    }

    private static int getExceptLastZeroIndex(String pointPrice) {
        for (int index = pointPrice.length() - 1; index >= 0; index--) {
            if (pointPrice.charAt(index) == '0') {
                continue;
            }
            return index;
        }
        return -1;
    }

    /**
     * 价格当字符串处理(不带小数点)
     *
     * @param price
     * @return
     */
    public static String convertPriceNoSymbolDot(long price) {
        String priceString = String.valueOf(price);
        if (priceString.length() > 2) {
            String prefix = priceString.substring(0, priceString.length() - 2);
            return prefix;
        } else {
            return "0";
        }
    }

    /**
     * 无四舍五入取整数
     *
     * @param rmbFen
     * @return
     */
    public static String converRMb2YunStrNoDot(long rmbFen) {
        String strRmbFen = String.valueOf(rmbFen);
        if (strRmbFen.length() > 2) {
            return strRmbFen.substring(0, strRmbFen.length() - 2);
        } else {
            return "0";
        }
    }

    /**
     * 四舍五入取整数
     *
     * @param rmbFen
     * @return
     */
    public static String converRMb2YunStrNoDotHalfAdjust(long rmbFen) {
        return new DecimalFormat("#########")
                .format(converRMb2Yun(rmbFen));
    }

    /**
     * 人民币转换成圆无符号
     *
     * @param rmbFen
     * @return
     */
    public static String converRMb2YunNoFlag(long rmbFen) {
        String result = converRMb2YunStr(rmbFen);
        return result;
    }

    /**
     * 包含小数点
     *
     * @param rmbFen
     * @return
     */
    public static String converRMb2YunWithFlag(Context context, long rmbFen) {
        String result = getFlagRmb(context) + converRMb2YunStr(rmbFen);
        return result;
    }


    public static String converRMb2YunWithFlagNoDot(Context context, long rmbFen) {
        String result = getFlagRmb(context) + converRMb2YunStrNoDot(rmbFen);
        return result;
    }

    // 设置删除线
    public static void setDeleteLine4TextView(TextView textView, String txt) {
        SpannableString msp = new SpannableString(txt);
        msp.setSpan(new StrikethroughSpan(), 0, txt.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(msp);
    }

    /**
     * 判断是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0 || str.equalsIgnoreCase("null");
    }

    /**
     * String 转 List
     *
     * @param items
     * @return
     */
    public static List<String> stringsToList(String[] items) {
        if (items == null) {
            return null;
        }
        List<String> itemList = new ArrayList<String>();
        for (String str : items) {
            itemList.add(str);
        }
        String s = "";
        s.getBytes();
        return itemList;
    }

    public static String nullToEmpty(String str) {
        if (null == str) {
            return "";
        }

        return str;
    }

    public static String handlerErrorCode(Context context, int errorCode) {
        if (context == null) {
            return null;
        }
        String errorMsg = "";
        switch (errorCode) {
            case ApiCode.NO_ACTIVE_DEVICE:
                errorMsg = context.getString(R.string.server_error_NO_ACTIVE_DEVICE);
                break;
            case ApiCode.NO_TRUSTED_DEVICE:
//                errorMsg = context.getString(R.string.server_error_NO_TRUSTED_DEVICE);
                break;
            case ApiCode.RISK_USER_LOCKED:
                errorMsg = context.getString(R.string.server_error_RISK_CONTROL_FORBIDDEN_1007000);
                break;
            case ApiCode.TOKEN_EXPIRE:
                errorMsg = context.getString(R.string.server_error_TOKEN_EXPIRE);
                break;
            case ApiCode.USER_LOCKED:
                errorMsg = context.getString(R.string.server_error_USER_LOCKED);
                break;
            case ApiCode.APPID_NOT_EXIST:
                errorMsg = context.getString(R.string.server_error_APPID_NOT_EXIST);
                break;
            case ApiCode.DYNAMIC_CODE_ERROR:
                errorMsg = context.getString(R.string.server_error_DYNAMIC_CODE_ERROR);
                break;
            case ApiCode.REQUEST_PARSE_ERROR:
                errorMsg = context.getString(R.string.server_error_REQUEST_PARSE_ERROR);
                break;
            case ApiCode.SIGNATURE_ERROR:
                errorMsg = context.getString(R.string.server_error_SIGNATURE_ERROR);
                break;
            case ApiCode.ACCESS_DENIED:
                errorMsg = context.getString(R.string.server_error_ACCESS_DENIED);
                break;
            case ApiCode.PARAMETER_ERROR:
                errorMsg = context.getString(R.string.server_error_PARAMETER_ERROR);
                break;
            case ApiCode.UNKNOWN_METHOD:
                errorMsg = context.getString(R.string.server_error_UNKNOWN_METHOD);
                break;
            case ApiCode.UNKNOWN_ERROR:
                errorMsg = context.getString(R.string.server_error_UNKNOWN_ERROR);
                break;
            case ApiCode.USERNAME_OR_PASSWORD_ERROR_1003010:
                errorMsg = context.getString(R.string.server_error_USERNAME_OR_PASSWORD_ERROR_1003010);
                break;
            case ApiCode.USER_NOT_FOUND_1003020:
                errorMsg = context.getString(R.string.server_error_USER_NOT_FOUND_1003020);
                break;
            case ApiCode.LOGIN_FAILED_1003030:
                errorMsg = context.getString(R.string.server_error_LOGIN_FAILED_1003030);
                break;
            case ApiCode.USER_EXIST_1003040:
                errorMsg = context.getString(R.string.server_error_USER_EXIST_1003040);
                break;
            case ApiCode.UNSUPPORTED_PHONE_NUM_1003050:
                errorMsg = context.getString(R.string.server_error_DEVICE_PHONE_NUM_ERROR_1003050);
                break;
            case ApiCode.MOBILE_REGISTED_1015003:
                errorMsg = context.getString(R.string.server_error_MOBILE_REGISTED_1015003);
                break;
            case ApiCode.MOBILE_NOT_REGIST:
                errorMsg = context.getString(R.string.server_error_MOBILE_NOT_REGIST);
                break;
            case ApiCode.SMS_SEND_ING_1015005:
                errorMsg = context.getString(R.string.server_error_SMS_SEND_ING_1015005);
                break;
            case ApiCode.ADDRESS_MAX_NUM_ERROR_1009000:
                errorMsg = context.getString(R.string.server_error_ADDRESS_MAX_NUM_ERROR_1009000);
                break;
            case ApiCode.NICKNAME_EXISTED_1015009:
                errorMsg = context.getString(R.string.server_error_NICKNAME_EXISTED_1015009);
                break;
            case ApiCode.PASSWORD_ERROR_1003160:
                errorMsg = context.getString(R.string.server_error_PASSWORD_ERROR_1003160);
                break;
            case ApiCode.SAME_PASSWORD_ERROR_1003060:
                errorMsg = context.getString(R.string.server_error_SAME_PASSWORD_ERROR_1003060);
                break;
//            case ApiCode.SMS_PASSWORD_ERROR_1003070:
//                errorMsg = context.getString(R.string.server_error_SMS_PASSWORD_ERROR_1003070);
//                break;
            case ApiCode.STATIC_PASSWORD_NOT_SET_1003130:
                errorMsg = context.getString(R.string.server_error_STATIC_PASSWORD_NOT_SET_1003130);
                break;
            case ApiCode.NO_PERMISSION_TO_VIEW_6000000:
                errorMsg = context.getString(R.string.server_error_NO_PERMISSION_TO_VIEW_6000000);
                break;
            case ApiCode.ORDER_CANNOT_CLOSE_6000023:
                errorMsg = context.getString(R.string.server_error_ORDER_CANNOT_CLOSE_6000023);
                break;
//            case ApiCode.ORDER_STATUS_ERROR_6002000:
            case ApiCode.PAY_STATUS_IS_NOT_WAIT_PAY_6000202:
                errorMsg = context.getString(R.string.server_error_PAY_STATUS_IS_NOT_WAIT_PAY_6000202);
                break;
            case ApiCode.BUYER_CONFIRM_GOODS_DELIVERIED_ERROR_6000021:
                errorMsg = context.getString(R.string.server_error_BUYER_CONFIRM_GOODS_DELIVERIED_ERROR_6000021);
                break;
            case ApiCode.ITEM_NOT_AVAILABLE_6000022:
                errorMsg = context.getString(R.string.server_error_ITEM_NOT_AVAILABLE_6000022);
                break;
            case ApiCode.ORDER_CAN_NOT_CLOSE_6000203:
                errorMsg = context.getString(R.string.server_error_ORDER_CAN_NOT_CLOSE_6000203);
                break;
            case ApiCode.NEED_ADDRESS_6000040:
                errorMsg = context.getString(R.string.server_error_NEED_ADDRESS_6000040);
                break;
            case ApiCode.INVOICE_INFO_INCOMPLETE_6000041:
                errorMsg = context.getString(R.string.server_error_INVOICE_INFO_INCOMPLETE_6000041);
                break;
            case ApiCode.ITEM_NOT_FOUND_6000100:
                errorMsg = context.getString(R.string.server_error_ITEM_NOT_FOUND_6000100);
                break;
            case ApiCode.SOLD_OUT_6000101:
                errorMsg = context.getString(R.string.server_error_SOLD_OUT_6000101);
                break;
            case ApiCode.BIZ_ORDER_NOT_FOUND_6000300:
                errorMsg = context.getString(R.string.server_error_BIZ_ORDER_NOT_FOUND_6000300);
                break;
            case ApiCode.MISSING_PARAM_6999999:
                errorMsg = context.getString(R.string.server_error_MISSING_PARAM_6999999);
                break;
            case ApiCode.PAGE_QUERY_BIZ_ORDER_FOR_BUYER_ERROR_6000017:
                errorMsg = context.getString(R.string.server_error_PAGE_QUERY_BIZ_ORDER_FOR_BUYER_ERROR_6000017);
                break;
            case ApiCode.ENTER_DATE_EXPIRED_4000004:
                errorMsg = context.getString(R.string.server_error_ENTER_DATE_EXPIRED_4000004);
                break;
            case ApiCode.BIND_VOUCHER_ERROR_11000002:
                errorMsg = context.getString(R.string.server_error_BIND_VOUCHER_ERROR_11000002);
                break;
            case ApiCode.HAS_BIND_VOUCHER_11000003:
                errorMsg = context.getString(R.string.server_error_HAS_BIND_VOUCHER_11000003);
                break;
            // TODO: 2018/5/3   更换jar包后解开 
//            case ApiCode.EXCEED_MAX_FOLLOW_SIZE_3000020:
//                errorMsg = context.getString(R.string.server_error_EXCEED_MAX_FOLLOW_SIZE_3000020);
//                break;
//            case ApiCode.WRONG_VOUCHER_6000050:
//                errorMsg = context.getString(R.string.server_error_DISCOUNT_COUPON_6000050);
//                break;
            case ApiCode.CREATE_ORDER_PARAM_ERROR_6000073:
                errorMsg = context.getString(R.string.server_error_CREATE_ORDER_PARAM_ERROR_6000073);
                break;
            case ApiCode.CONSULTION_PROCESS_ORDER_QUERY_ERROR_6003005:
                errorMsg = context.getString(R.string.server_error_CONSULTION_PROCESS_ORDER_QUERY_ERROR_6003005);
                break;

            case ErrorCode.DEVICE_TOKEN_MISSING:
                errorMsg = context.getString(R.string.local_error_device_token_missing);
                break;
            case ErrorCode.NETWORK_UNAVAILABLE:
                errorMsg = context.getString(R.string.network_unavailable);
                break;
            case ErrorCode.STATUS_IO_EXCEPTION:
            case ErrorCode.STATUS_NETWORK_EXCEPTION:
                errorMsg = context.getString(R.string.local_error_network_access_failed);
                break;
            case ErrorCode.NOT_LOGIN:
                errorMsg = context.getString(R.string.local_error_not_login);
                break;
            case ErrorCode.UPLOAD_PICTURE_FAILED:
                errorMsg = context.getString(R.string.picture_upload_failed);
                break;
            case ErrorCode.CONNECTTION_TIME_OUT:
                errorMsg = context.getString(R.string.local_error_network_access_failed);
                break;
            case ErrorCode.SDCARD_UNAVAILABLE:
                errorMsg = context.getString(R.string.local_error_sdcard_unavailable);
                break;
            case ApiCode.SYSTEM_ERROR_6999998:
                errorMsg = context.getString(R.string.cancel_fail);
                break;
            default:
                errorMsg = context.getString(R.string.server_error_DEFAULT);
                break;
        }
        return errorMsg;
    }

    //空格不能输入
    public static InputFilter getSpaceStopInputFilter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
                if (source.equals(" ")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
    }

    //限制字符数
    public static InputFilter limitInputFilter(int max) {
        return new InputFilter.LengthFilter(max);
    }

    /**
     * 获取价格
     *
     * @param memberPrice
     * @param price
     * @param originPrice
     * @param oldPrice
     * @return
     */
    public static long getPrice(long memberPrice, long price, long originPrice, long oldPrice) {
        if (memberPrice > 0) {
            return memberPrice;
        }

        if (price > 0) {
            return price;
        }

        if (originPrice > 0) {
            return originPrice;
        }

        if (oldPrice > 0) {
            return oldPrice;
        }

        return 0;
    }


    /***
     * 去掉首尾指定字符串
     *
     * @param stream  要处理的字符串
     * @param trimstr 要去掉的字符串
     * @return
     */
    public static String sideTrim(String stream, String trimstr) {
        // null或者空字符串的时候不处理
        if (stream == null || stream.length() == 0 || trimstr == null || trimstr.length() == 0) {
            return "";
        }
        // 结束位置
        int epos = 0;
        String removetrimstr = stream.trim();
        String regpattern = "[" + trimstr + "]*+";
        Pattern pattern = Pattern.compile(regpattern, Pattern.CASE_INSENSITIVE);
        // 去掉结尾的指定字符
        StringBuffer buffer = new StringBuffer(removetrimstr).reverse();
        Matcher matcher = pattern.matcher(buffer);
        if (matcher.lookingAt()) {
            epos = matcher.end();
            removetrimstr = new StringBuffer(buffer.substring(epos)).reverse().toString();
        }
        // 去掉开头的指定字符
        matcher = pattern.matcher(removetrimstr);
        if (matcher.lookingAt()) {
            epos = matcher.end();
            removetrimstr = removetrimstr.substring(epos);
        }
        return removeEnter(removetrimstr);
    }

    /***
     * 去掉字符串中重复的回车
     *
     * @return
     */
    public static String removeEnter(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        String removeTrimStr = str.trim();
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < removeTrimStr.length(); i++) {
            String s = removeTrimStr.substring(i, i + 1);
            if (s.equals("\n")) {
                if (data.size() > 1) {
                    if (data.get(data.size() - 2).equals("\n") && data.get(data.size() - 1).equals("\n")) {
                    } else {
                        data.add(s);
                    }
                } else {
                    data.add(s);
                }
            } else {
                data.add(s);
            }
        }
        String result = "";
        for (String s : data) {
            result += s;
        }

        return result;
    }

    //求字符串的实际长度（汉字2个字节，英文字符1个字节）
    public static int Length(String str) {
        int len = 0;
        char[] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            //汉字
            if (RegexUtil.isChineseCharacter(String.valueOf(arr[i]))) {
                len += 2;
            } else {
                len += 1;
            }
        }

        return len;
    }

    /**
     * 格式化景区距离
     *
     * @param context
     * @param distance
     * @return
     */
    public static String formatDistance(Context context, long distance) {
        if (distance <= 100) {
            return String.format(context.getString(R.string.label_scenic_distance), context.getString(R.string.label_min_distance));
        }
        if (distance > 1000 * 1000) {
            return context.getString(R.string.label_max_distance);
        }

        String strDis = new DecimalFormat("##########0.0")
                .format((double) distance / 1000) + context.getString(R.string.label_unit_km);
        return String.format(context.getString(R.string.label_scenic_distance), strDis);
    }

    /**
     * 格式化点评数据
     *
     * @param context
     * @param rateCount
     * @return
     */
    public static String formatCommentInfo(Context context, int rateCount) {
        return String.format(context.getString(R.string.label_format_scenic_comment), rateCount);
    }

    /**
     * 格式化已售
     *
     * @param context
     * @param num
     * @return
     */
    public static String formatSales(Context context, int num) {
        String saleNum = String.valueOf(num);
        if (num > 9999) {
            saleNum = context.getString(R.string.label_max_sales);
        }

        return String.format(context.getString(R.string.label_sales), saleNum);
    }

    /**
     * 格式化服务次数
     *
     * @param context
     * @param num
     * @return
     */
    public static String formatServiceCount(Context context, int num) {
        if (context == null) {
            return "";
        }
        String saleNum = String.valueOf(num);
        if (num > 9999) {
            saleNum = context.getString(R.string.label_max_sales);
        }

        return String.format(context.getString(R.string.label_master_service_count), saleNum);
    }

    public static List<Long> longsToList(long[] longs) {
        if (longs == null) {
            return null;
        }
        List<Long> itemList = new ArrayList<>();
        for (Long str : longs) {
            itemList.add(str);
        }
        return itemList;
    }

    /**
     * list 转 String
     *
     * @param list
     * @return
     */
    public static String[] listToStrings(List<String> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        String[] ss = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ss[i] = list.get(i);
        }
        return ss;
    }

    /**
     * 格式化优惠券显示(仅供店铺优惠券列表使用)
     *
     * @param value
     * @return
     */
    public static String formatCouponPrice(long value) {
        if (value == 0) {
            return "";
        } else if (value > 0 && value < 10) {
            return ".0" + String.valueOf(value);
        } else if (value >= 10 && value < 100) {
            if (value % 10 == 0) {
                return "." + String.valueOf(value / 10);
            } else {
                return "." + String.valueOf(value);
            }
        }
        return "";
    }

    public static String convertPriceNoSymbolWithFlag(Context context, long price) {
        return getFlagRmb(context) + convertPriceNoSymbol(price);
    }

    /**
     * 格式化话题详情的阅读数和参与数量
     *
     * @param num
     * @return
     */
    public static String formatTopicReadNum(long num) {
//        if (num > 10000) {
//            String a = String.valueOf(num);
//            String b = a.substring(0, (a.length() - 4));
//            String c = "";
//            if (Integer.parseInt(a.substring((a.length() - 3), (a.length() - 2))) == 0) {
//                if (Integer.parseInt(a.substring((a.length() - 4), (a.length() - 3))) == 0) {
//                    c = "";
//                } else {
//                    c = a.substring((a.length() - 4), (a.length() - 3));
//                }
//            } else {
//                c = a.substring((a.length() - 4), (a.length() - 2));
//            }
//            return b + "." + c + "万";
//        } else {
//            return num + "";
//        }
        return TransformationFansData(num);
    }

    /**
     * 格式化优粉丝数量
     */
    public static String TransformationFansData(long num) {
        String saleNum = "";
        if (num > 9999) {
            if (num > 99990000) {
                saleNum = "9999万+";
            } else {
                if (num % 10000 == 0 || num % 10000 < 999) {
                    return num / 10000 + "万";
                }
                double x = (double) num / 10000;
                String v = String.valueOf(x);
                if (v.indexOf(".") > 0) {
                    saleNum = v.substring(0, v.indexOf(".") + 2) + "万";
                } else {
                    saleNum = v + "万";
                }
            }
            return saleNum;
        } else {
            return String.valueOf(num);
        }
    }

    /**
     * 获取服务区域
     *
     * @param itemDestination
     * @return
     */
    public static String getServiceArea(List<String> itemDestination) {
        if (itemDestination == null) {
            return "无";
        }
        String area = "";
        for (String a : itemDestination) {
            area = area + "、" + a;
        }

        if (area.length() > 0) {
            area = area.substring(1, area.length());
        }
        return area;
    }

    /**
     * 获取服务区域
     *
     * @param serviceAreas
     * @return
     */
    public static String getConServiceArea(List<ServiceArea> serviceAreas) {
        if (serviceAreas == null) {
            return "无";
        }
        String area = "";
        for (ServiceArea a : serviceAreas) {
            area = area + "、" + a.areaName;
        }

        if (area.length() > 0) {
            area = area.substring(1, area.length());
        }
        return area;
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return "00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /***
     * 格式化距离m转km保留有效一位
     * @param str
     * @return
     */
    public static String subZeroAndDot(double str) {
        String sb = "";
        DecimalFormat df = new DecimalFormat("#########.0");
//        double s = 1000.9;


        String indexStr = df.format(str);
        if (indexStr.indexOf(".") > 0) {
            indexStr = indexStr.replaceAll("0+?$", "");//去掉多余的0
            indexStr = indexStr.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        if (indexStr.equals(".0")) {
            sb = "0";
        } else {
            sb = indexStr;
        }

        return sb;
    }

    /**
     * @param context
     * @return
     */
    public static String formatWalletMoney(Context context, long rmbFen) {
        return getFlagRmb(context) + new DecimalFormat("#,##0.00").format(converRMb2Yun(rmbFen));
    }

    public static String formatWalletMoneyNoFlg(long rmbFen) {
        return new DecimalFormat("#,##0.00").format(converRMb2Yun(rmbFen));
    }

}
