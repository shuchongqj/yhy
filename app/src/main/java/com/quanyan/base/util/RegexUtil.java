package com.quanyan.base.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with Android Studio.
 * Title:RegexTool
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2015-11-13
 * Time:13:22
 * Version 1.0
 */

public class RegexUtil {
    //------------------常量定义
    /**
     * Email正则表达式="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
     */
    //public static final String EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";;
    public static final String EMAIL = "\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";
    //^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$
    //public static final String EMAIL = "^[a-zA-Z]+(\\.\\w+)*@\\w+(\\.\\w+)+";
    //联系人
    public static final String NAME = "^[\\u4e00-\\u9fa5a-zA-Z\\s]{2,10}$";
    //仅支持中文字母和空格
    public static final String LIMIT = "^[\\u4e00-\\u9fa5a-zA-Z\\s]+$";
    //判断是否为汉字
    public static final String CHINESECHARACTER = "[\\u4e00-\\u9fa5]";
    //联系人
    //public static final String NAME = "^[\\s][\\u4e00-\\u9fa5a-zA-Z\\s]{1,19}[\\s]$";

    //密码
    public static final String PASSWORD = "^(?=.*\\d)(?=.*[a-zA-Z]).{6,16}$";
    //话题标签
    public static final String TOPIC = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{2,30}$";
    //昵称
    public static final String NICK = "^[\\u4e00-\\u9fa5a-zA-Z0-9-_]{2,15}$";
    //收货地址
    public static final String ADDRESS = "^[\\u4e00-\\u9fa5\\w]{5,100}";
    //护照
    //public static final String PASSPORT = "^1[45][0-9]{7}|G[0-9]{8}|P[0-9]{7}|S[0-9]{7,8}|D[0-9]+$";
    public static final String PASSPORT = "^[GEge][0-9]{8}$";
    //军人证
    public static final String SOLDIERCARD = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{0,20}$";
    //港澳通行证
    public static final String HKCARD = "^[WCwc][0-9]{8}$";
    //首尾空格
    public static final String BASEBEFOREOREND = "(^[\\s+][\\s\\S]*)|([\\s\\S]*[\\s]+$)";
    public static final String BASEBEFORE = "^[\\s]";
    public static final String BASEEND = "[\\s]$";
    /**
     * 电话号码正则表达式= (^(\d{2,4}[-_－—]?)?\d{3,8}([-_－—]?\d{3,8})?([-_－—]?\d{1,7})?$)|(^0?1[35]\d{9}$)
     */
    public static final String PHONE = "(^(\\d{2,4}[-_－—]?)?\\d{3,8}([-_－—]?\\d{3,8})?([-_－—]?\\d{1,7})?$)|(^0?1[35]\\d{9}$)" ;
    /**
     * 手机号码正则表达式=^(13[0-9]|15[0-9]|18[0-9])\d{8}$
     */
    //public static final String MOBILE ="^(13[0-9]|15[0-9]|18[0-9])\\d{8}$";
    public static final String MOBILE ="^1\\d{10}$";

    /**
     * Integer正则表达式 ^-?(([1-9]\d*$)|0)
     */
    public static final String  INTEGER = "^-?(([1-9]\\d*$)|0)";
    /**
     * 正整数正则表达式 >=0 ^[1-9]\d*|0$
     */
    public static final String  INTEGER_NEGATIVE = "^[1-9]\\d*|0$";
    /**
     * 负整数正则表达式 <=0 ^-[1-9]\d*|0$
     */
    public static final String  INTEGER_POSITIVE = "^-[1-9]\\d*|0$";
    /**
     * Double正则表达式 ^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$
     */
    public static final String  DOUBLE ="^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$";
    /**
     * 正Double正则表达式 >=0  ^[1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0$　
     */
    public static final String  DOUBLE_NEGATIVE ="^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0$";
    /**
     * 负Double正则表达式 <= 0  ^(-([1-9]\d*\.\d*|0\.\d*[1-9]\d*))|0?\.0+|0$
     */
    public static final String  DOUBLE_POSITIVE ="^(-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*))|0?\\.0+|0$";
    /**
     * 年龄正则表达式 ^(?:[1-9][0-9]?|1[01][0-9]|120)$ 匹配0-120岁
     */
    public static final String  AGE="^(?:[1-9][0-9]?|1[01][0-9]|120)$";
    /**
     * 邮编正则表达式  [0-9]\d{5}(?!\d) 国内6位邮编
     */
    public static final String  CODE="[0-9]\\d{5}(?!\\d)";
    //public static final String CODE = "^[0-9]\\d{5}$";
    /**
     * 匹配由数字、26个英文字母或者下划线组成的字符串 ^\w+$
     */
    public static final String STR_ENG_NUM_="^\\w+$";
    /**
     * 匹配由数字和26个英文字母组成的字符串 ^[A-Za-z0-9]+$
     */
    public static final String STR_ENG_NUM="^[A-Za-z0-9]+";
    /**
     * 匹配由26个英文字母组成的字符串  ^[A-Za-z]+$
     */
    public static final String STR_ENG="^[A-Za-z]+$";
    /**
     * 过滤特殊字符串正则
     * regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
     */
    public static final String STR_SPECIAL="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
    /***
     * 日期正则 支持：
     *  YYYY-MM-DD
     *  YYYY/MM/DD
     *  YYYY_MM_DD
     *  YYYYMMDD
     *  YYYY.MM.DD的形式
     */
    public static final String DATE_ALL="((^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._]?)(10|12|0?[13578])([-\\/\\._]?)(3[01]|[12][0-9]|0?[1-9])$)" +
            "|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._]?)(11|0?[469])([-\\/\\._]?)(30|[12][0-9]|0?[1-9])$)" +
            "|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._]?)(0?2)([-\\/\\._]?)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|(^([3579][26]00)" +
            "([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)" +
            "|(^([1][89][0][48])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|(^([2-9][0-9][0][48])([-\\/\\._]?)" +
            "(0?2)([-\\/\\._]?)(29)$)" +
            "|(^([1][89][2468][048])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|(^([2-9][0-9][2468][048])([-\\/\\._]?)(0?2)" +
            "([-\\/\\._]?)(29)$)|(^([1][89][13579][26])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|" +
            "(^([2-9][0-9][13579][26])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$))";
    /***
     * 日期正则 支持：
     *  YYYY-MM-DD
     */
    public static final String DATE_FORMAT1="(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";

    /**
     * URL正则表达式
     * 匹配 http www ftp
     */
    public static final String URL = "^(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?" +
            "(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*" +
            "(\\w*:)*(\\w*\\+)*(\\w*\\.)*" +
            "(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$";

    /**
     * 身份证正则表达式
     */
    /*public static final String IDCARD="((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65)[0-9]{4})" +
            "(([1|2][0-9]{3}[0|1][0-9][0-3][0-9][0-9]{3}" +
            "[Xx0-9])|([0-9]{2}[0|1][0-9][0-3][0-9][0-9]{3}))";*/
    public static final String IDCARD="((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65)[0-9]{4})" +
            "(([1|2][0-9]{3}(([0][1-9])|([1][0-2]))(([0][1-9])|([1-2][0-9])|([3][0-1]))[0-9]{3}" +
            "[Xx0-9])|([0-9]{2}[0|1][0-9][0-3][0-9][0-9]{3}))";
    //public static final String IDCARD = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";

    /**
     * 机构代码
     */
    public static final String JIGOU_CODE = "^[A-Z0-9]{8}-[A-Z0-9]$";

    /**
     * 匹配数字组成的字符串  ^[0-9]+$
     */
    public static final String STR_NUM = "^[0-9]+$";

    /**
     * "0-100位字符（包含字母、数字、中文、一般特殊符号：， 、 。 ． ？ ！ ～ ＄ ％ ＠ ＆ ＃ ＊ ?； ︰ … ‥ ﹐ ﹒ ˙ ? ‘ ’ “ ” 〝 〞 ‵）
     */
    public static final String EDIT_CONTENT = "[a-zA-Z0-9\\u4e00-\\u9fa5~!@#$%&*;',?~！￥……‘；：”“’。，、？]{1,}";

    /**
     * 搜索验证字符输入为合法的1-50位字符（包含字母、数字、中文）
     */
    public static final String SEARCH_VALIDATION = "[a-zA-Z0-9\u4e00-\u9fa5]{0,50}";

    /**
     * 搜索字符验证
     * @param searchValues
     * @return
     */
    public static boolean isIllegaParameter(String searchValues){
        return Regular(searchValues, SEARCH_VALIDATION);
    }
////------------------验证方法
    /**
     * 判断字段是否为空 符合返回ture
     * @param str
     * @return boolean
     */
    public static synchronized boolean StrisNull(String str) {
        return null == str || str.trim().length() <= 0 ? true : false ;
    }
    /**
     * 判断字段是非空 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean StrNotNull(String str) {
        return !StrisNull(str) ;
    }
    /**
     * 字符串null转空
     * @param str
     * @return boolean
     */
    public static  String nulltoStr(String str) {
        return StrisNull(str)?"":str;
    }
    /**
     * 字符串null赋值默认值
     * @param str    目标字符串
     * @param defaut 默认值
     * @return String
     */
    public static  String nulltoStr(String str,String defaut) {
        return StrisNull(str)?defaut:str;
    }
    /**
     * 判断字段是否为Email 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isEmail(String str) {
        return Regular(str,EMAIL);
    }
    //联系人姓名
    public static boolean isName(String str){
        return Regular(str,NAME);
    }
    //判断是否为汉字
    public static boolean isChineseCharacter(String str){
        return Regular(str, CHINESECHARACTER);
    }
    //仅包含中文字母和空格
    public static boolean isLimit(String str){
        return Regular(str, LIMIT);
    }

    //判断联系人姓名的合法性
    /*public static boolean isName (String name){
        if(StringUtil.Length(name) >= 2 && StringUtil.Length(name) <= 20 && isLimit(name)){
            return true;
        }
        return false;
    }*/

    //密码
    public static boolean isPassword(String str){
        return Regular(str,PASSWORD);
    }

    //话题标签
    public static boolean isTopic(String str){
        return Regular(str,TOPIC);
    }
    //昵称匹配
    public static boolean isNick(String str){
        return Regular(str, NICK);
    }

    //收货地址
    public static boolean isAddress(String str){
        return Regular(str,ADDRESS);
    }

    //护照
    public static boolean isPassport(String str){
        return Regular(str,PASSPORT);
    }

    //军人证
    public static boolean isSoldierCard(String str){
        return Regular(str,SOLDIERCARD);
    }

    //港澳通行证
    public static boolean isHkcard(String str){
        return Regular(str,HKCARD);
    }

    //首尾空格
    public static boolean isBeforOrEnd(String str){
        return Regular(str,BASEBEFOREOREND);
    }
    /**
     * 判断是否为电话号码 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isPhone(String str) {
        return Regular(str,PHONE);
    }
    /**
     * 判断是否为手机号码 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isMobile(String str) {
        return Regular(str,MOBILE);
    }
    /**
     * 判断是否为Url 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isUrl(String str) {
        return Regular(str,URL);
    }
    /**
     * 判断字段是否为数字 正负整数 正负浮点数 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isNumber(String str) {
        return Regular(str,DOUBLE);
    }
    /**
     * 判断字段是否为INTEGER  符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isInteger(String str) {
        return Regular(str,INTEGER);
    }
    /**
     * 判断字段是否为正整数正则表达式 >=0 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isINTEGER_NEGATIVE(String str) {
        return Regular(str,INTEGER_NEGATIVE);
    }
    /**
     * 判断字段是否为负整数正则表达式 <=0 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isINTEGER_POSITIVE(String str) {
        return Regular(str,INTEGER_POSITIVE);
    }
    /**
     * 判断字段是否为DOUBLE 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isDouble(String str) {
        return Regular(str,DOUBLE);
    }
    /**
     * 判断字段是否为正浮点数正则表达式 >=0 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isDOUBLE_NEGATIVE(String str) {
        return Regular(str,DOUBLE_NEGATIVE);
    }
    /**
     * 判断字段是否为负浮点数正则表达式 <=0 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isDOUBLE_POSITIVE(String str) {
        return Regular(str,DOUBLE_POSITIVE);
    }
    /**
     * 判断字段是否为日期 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isDate(String str) {
        return Regular(str,DATE_ALL);
    }
    /**
     * 验证2010-12-10
     * @param str
     * @return
     */
    public static  boolean isDate1(String str) {
        return Regular(str,DATE_FORMAT1);
    }
    /**
     * 判断字段是否为年龄 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isAge(String str) {
        return Regular(str,AGE) ;
    }
    /**
     * 判断字段是否超长
     * 字串为空返回fasle, 超过长度{leng}返回ture 反之返回false
     * @param str
     * @param leng
     * @return boolean
     */
    public static  boolean isLengOut(String str,int leng) {
        return StrisNull(str)?false:str.trim().length() > leng ;
    }
    /**
     * 判断字段是否为身份证 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isIdCard(String str) {
        if(StrisNull(str)) return false ;
        if(str.trim().length() == 15 || str.trim().length() == 18) {
            return Regular(str,IDCARD);
        }else {
            return false ;
        }

    }
    /**
     * 判断字段是否为邮编 符合返回ture
     * @param str
     * @return boolean
     */
    public static  boolean isCode(String str) {
        return Regular(str,CODE) ;
    }
    /**
     * 判断字符串是不是全部是英文字母
     * @param str
     * @return boolean
     */
    public static boolean isEnglish(String str) {
        return Regular(str,STR_ENG) ;
    }
    /**
     * 判断字符串是不是全部是英文字母+数字
     * @param str
     * @return boolean
     */
    public static boolean isENG_NUM(String str) {
        return Regular(str,STR_ENG_NUM) ;
    }
    /**
     * 判断字符串是不是全部是英文字母+数字+下划线
     * @param str
     * @return boolean
     */
    public static boolean isENG_NUM_(String str) {
        return Regular(str,STR_ENG_NUM_) ;
    }
    /**
     * 过滤特殊字符串 返回过滤后的字符串
     * @param str
     * @return boolean
     */
    public static  String filterStr(String str) {
        Pattern p = Pattern.compile(STR_SPECIAL);
        Matcher m = p.matcher(str);
        return   m.replaceAll("").trim();
    }

    /**
     * 是否包含特殊字符
     * @param str
     * @return
     */
    public static boolean isContainSpecialStr(String str) {
        Pattern p = Pattern.compile(EDIT_CONTENT);
        Matcher m = p.matcher(str);
        return  m.matches();
    }

    /**
     * 校验机构代码格式
     * @return
     */
    public static boolean isJigouCode(String str){
        return Regular(str,JIGOU_CODE) ;
    }

    /**
     * 判断字符串是不是数字组成
     * @param str
     * @return boolean
     */
    public static boolean isSTR_NUM(String str) {
        return Regular(str,STR_NUM) ;
    }

    /**
     * 匹配是否符合正则表达式pattern 匹配返回true
     * @param str 匹配的字符串
     * @param pattern 匹配模式
     * @return boolean
     */
    private static  boolean Regular(String str,String pattern){
        if(null == str || str.trim().length()<=0)
            return false;
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
