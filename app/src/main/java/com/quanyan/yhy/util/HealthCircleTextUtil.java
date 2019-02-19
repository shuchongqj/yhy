package com.quanyan.yhy.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.quanyan.base.view.customview.ClickPreventableTextView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.discovery.view.AddLiveEditText;
import com.yhy.common.beans.net.model.param.WebParams;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with Android Studio.
 * Title:HealthCircleTextUtil
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/6/25
 * Time:下午4:49
 * Version 1.1.0
 */
public class HealthCircleTextUtil {
    private static HashMap<Integer,Boolean> map;
    /**
     * @param ctx 上下文对象
     * @param tv 需要设置textview
     */
    // 直接拷贝这些代码到你希望的位置，然后在TextView设置了文本之后调用就ok了
    public static void SetLinkClickIntercept(Context ctx, TextView tv) {
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = tv.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) tv.getText();
            //获取map保存设置span文字的状态
            map = getMap(tv.getText().toString());
            SpannableStringBuilder spannable = new SpannableStringBuilder(text);
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            if (urls.length == 0) {
                spannable = highlightKeyword(ctx, text.toString(),spannable);
                tv.setText(spannable);
                return;
            }

            // 只拦截 http:// URI
            LinkedList<String> myurls = new LinkedList<String>();
            for (URLSpan uri : urls) {
                String uriString = uri.getURL();
                if (uriString.indexOf("http://") == 0 || uriString.indexOf("https://") == 0) {
                    myurls.add(uriString);
                }
            }
            //循环把链接发过去
            for (URLSpan uri : urls) {
                String uriString = uri.getURL();
                if (uriString.indexOf("http://") == 0 || uriString.indexOf("https://") == 0) {
                    MyURLSpan myURLSpan = new MyURLSpan(ctx, uriString, myurls);
                    spannable.setSpan(myURLSpan, sp.getSpanStart(uri),
                            sp.getSpanEnd(uri), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    setMap(sp.getSpanStart(uri),sp.getSpanEnd(uri));
                }
            }

            spannable = highlightKeyword(ctx, text.toString(), spannable);
            tv.setText(spannable);
        }
    }


    /**
     * 处理TextView中的链接点击事件
     * 链接的类型包括：url，号码，email，地图
     * 这里只拦截url，即 http:// 开头的URI
     */
    private static class MyURLSpan extends ClickableSpan {
        private String mUrl;                    // 当前点击的实际链接
        private LinkedList<String> mUrls; // 根据需求，一个TextView中存在多个link的话，这个和我求有关，可已删除掉
        private final Context mContext;

        // 无论点击哪个都必须知道该TextView中的所有link，因此添加改变量
        MyURLSpan(Context ctx, String url, LinkedList<String> urls) {
            mUrl = url;
            mUrls = urls;
            mContext = ctx;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(mContext.getResources().getColor(R.color.neu_19a6db));
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            if (widget instanceof ClickPreventableTextView) {
                if (((ClickPreventableTextView)widget).ignoreSpannableClick()) {
                    return;
                }
                ((ClickPreventableTextView)widget).preventNextClick();
            }
            if(widget.getTag() != null){
                // TODO: 16/7/25 处理longclick事件
                widget.setTag(null);
                return;
            }
            WebParams wp = new WebParams();
            wp.setUrl(mUrl);
            NavUtils.openBrowser(mContext, wp);
        }
    }

    /** 设置话题高亮和点击
     * @param str
     */
    public static SpannableStringBuilder highlightKeyword(Context ctx, String str, SpannableStringBuilder sp) {
        Pattern p = Pattern.compile(AddLiveEditText.regex);
        Matcher m = p.matcher(str);

        while (m.find()) {  //通过正则查找，逐个高亮
            int start = m.start();
            int end = m.end();
            sp.setSpan(new LinkSpan(ctx, m.group()), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            setMap(start, end);
        }
//        setContentText(str,sp);
        return sp;

    }

    /**  确定是否只含有话题,返回除话题之外的文字数量
     * @param str
     * @return
     */
    public static int onlyHasTopic(String str){
        if(str == null) {
            return 0;
        }

        Pattern p = Pattern.compile(AddLiveEditText.regex);
        Matcher m = p.matcher(str);

        while (m.find()) {  //通过正则查找，逐个高亮
            int start = m.start();
            int end = m.end();
            str = str.replace(str.substring(start, end),"");
            m = p.matcher(str);
            LogUtils.e("loza start:" + start + " end:" + end + "str: " + str);
        }
        return str.trim().length() ;
    }



    private static class LinkSpan extends ClickableSpan {
        private final Context mContext;
        private String mLink;
        LinkSpan(Context ctx, String url) {
            mLink = url;
            mContext = ctx;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(mContext.getResources().getColor(R.color.neu_57b6e2));
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            if (widget instanceof ClickPreventableTextView) {
                if (((ClickPreventableTextView)widget).ignoreSpannableClick()) {
                    return;
                }
                ((ClickPreventableTextView)widget).preventNextClick();
            }

            if(widget.getTag() != null){
                // TODO: 16/7/25 处理longclick事件
                widget.setTag(null);
                return;
            }
            //NavUtils.gotoTopicDetailsActivity(mContext, mLink, -1);
            NavUtils.gotoNewTopicDetailsActivity(mContext, mLink, -1);
        }
    }

    /*****************************************************************************************************************/

    /**
     * 创建map对象保存每个文字的状态（如果高亮则设为false，反之为true）
     * true：普通文字
     * false：话题或链接文字
     * **/
    public static HashMap<Integer,Boolean> getMap(String text){
        HashMap<Integer,Boolean> map = new HashMap<Integer,Boolean>();
        for(int i=0;i<text.length();i++) {
            map.put(i, true);
        }
        return map;
    }

    /**
     * 设置话题或链接文字为false
     * map：记录map
     * start：起始位置
     * end：终止位置
     * */
    public static void setMap(int start,int end){
        for(int i=start; i<end; i++){
            map.put(i,false);
        }
    }

    /**
     * 设置普通文字链接效果
     * */
    public static SpannableStringBuilder setContentText(String text,SpannableStringBuilder sp){
        Pattern p = Pattern . compile(
                "[\ud83c-\ud83c\udfff]|[\ud83d-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE) ;
        Matcher m = p.matcher(text);

        while (m.find()) {  //通过正则查找，逐个高亮
            int start = m.start();
            int end = m.end();
            setMap(start, end);
        }
        return sp;
    }


    /** 获得结尾为...的字符
     * @param source
     * @return
     */
    public static String getCurrentString(String source) {
        Pattern emoji = Pattern.compile(
                "[\ud83c-\ud83c\udfff]|[\ud83d-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);


        int start = 0, end= 0;
        Matcher emojiMatcher = emoji.matcher(source);
        while (emojiMatcher.find()) {
            start = emojiMatcher.start();
            end = emojiMatcher.end();
        }

        if (end == source.length()){
            return source.substring(0, start ) + "...";
        } else {
            return source.substring(0, source.length() -1 ) + "...";
        }
    }
    /*****************************************************************************************************************/


    /**  返回话题四的起始位置
     * @param str
     * @return 起始位置
     */
    public static int getTopic4Start(String str){
        if(str == null) {
            return 0;
        }

        Pattern p = Pattern.compile(AddLiveEditText.regex);
        Matcher m = p.matcher(str);
        int i = 0;
        int start = 0;
        while (m.find()) {  //通过正则查找，逐个高亮
            i++;
            if(4 == i){
                start = m.start();
            }
        }
        return start;
    }


    /**  判断str中的话题数量
     * @param str
     * @return 话题数量
     */
    public static int getTopicCount(String str){
        if(str == null) {
            return 0;
        }

        Pattern p = Pattern.compile(AddLiveEditText.regex);
        Matcher m = p.matcher(str);

        int i = 0;
        while (m.find()) {  //通过正则查找，逐个高亮
            i++;
        }
        return i;
    }

}
