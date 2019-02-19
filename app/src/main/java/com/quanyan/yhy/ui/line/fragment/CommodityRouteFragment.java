package com.quanyan.yhy.ui.line.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harwkin.nb.camera.ImageUtils;
import com.quanyan.base.BaseFragment;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.view.customview.ObservableScrollView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.line.CommodityDetailActivity;
import com.quanyan.yhy.ui.line.lineinterface.IUpdateTab;
import com.quanyan.yhy.ui.line.view.RightPanelView;
import com.quanyan.yhy.ui.travel.controller.TravelController;
import com.yhy.common.beans.net.model.tm.TmRouteDayInfo;
import com.yhy.common.beans.net.model.trip.LineItemDetail;
import com.yhy.common.beans.net.model.trip.TextItem;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/4/5
 * Time:14:42
 * Version 1.0
 */
public class CommodityRouteFragment extends BaseFragment implements RightPanelView.OverViewItemClick,
        IUpdateTab, ObservableScrollView.ScrollViewListener {

    private ObservableScrollView mScrollView;
    private LinearLayout mScrolParentView;
    private LinearLayout mHeaderView;
    private TextView mOverViewTitle;

    private long mLineId = -1;

    private TravelController mTravelController;
    private String mPageType;
    private LineItemDetail mLineDetail;

    private List<LineItemInfo> mLineItemInfos = new ArrayList<>();

    private List<Integer> mIntegerList = new ArrayList<>();

    public static CommodityRouteFragment getInstance(long lineId, String pageType) {
        CommodityRouteFragment fragment = new CommodityRouteFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(SPUtils.EXTRA_ID, lineId);
        bundle.putString(SPUtils.EXTRA_TYPE, pageType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTravelController = new TravelController(getActivity(), mHandler);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mLineId = bundle.getLong(SPUtils.EXTRA_ID);
            mPageType = bundle.getString(SPUtils.EXTRA_TYPE);
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(), R.layout.fg_commodity_route_detail, null);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());
    }

    public TextView getOverViewTitle() {
        return mOverViewTitle;
    }

    @Override
    public void overViewClick(int pos) {
        if (-1 != pos) {
            if (pos == mIntegerList.size() - 1) {
                View childView = mScrolParentView.getChildAt(pos + 1);
                if (childView != null) {
                    mScrollView.scrollTo(0, childView.getTop());
                }
                return;
            }
            mOverViewTitle.setText(mLineItemInfos.get(pos).day + "  |  "
                    + (mLineItemInfos.get(pos).title.length() > 9 ?
                    (mLineItemInfos.get(pos).title.substring(0, 9)+"...") : mLineItemInfos.get(pos).title));
        }
        mOverViewTitle.setVisibility(View.VISIBLE);
        Animation animation_in = AnimationUtils.loadAnimation(getActivity(), R.anim.right_in);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.right_out);
        if(getActivity() != null) {
            ((CommodityDetailActivity) getActivity()).getRightPanelView().setAnimation(animation);
        }
        mOverViewTitle.setAnimation(animation_in);
        animation_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mOverViewTitle.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mOverViewTitle.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(getActivity() != null) {
                    ((CommodityDetailActivity) getActivity()).getRightPanelView().setClickable(false);
                    ((CommodityDetailActivity) getActivity()).getRightPanelView().setListClickable(false);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(getActivity() != null) {
                    ((CommodityDetailActivity) getActivity()).getRightPanelView().setClickable(true);
                    ((CommodityDetailActivity) getActivity()).getRightPanelView().setListClickable(true);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        View childView = mScrolParentView.getChildAt(pos + 1);
        if (childView != null) {
            mScrollView.scrollTo(0, childView.getTop());
        }
    }

    @Override
    public void updateTabContent() {

    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        View childView = mScrolParentView.getChildAt(0);
        View childView2 = mScrolParentView.getChildAt(1);
        if (childView != null && childView2 != null) {
            int height = childView.findViewById(R.id.header_commodity_detail_route_label).getTop();
            int height1 = childView2.getTop();
            int topMin = height1 - height;
            Collections.sort(mIntegerList);

            if (y <= height) {
                mOverViewTitle.setText(getString(R.string.tv_trip_overview));
//                FrameLayout.LayoutParams layoutParams =
//                        (FrameLayout.LayoutParams) mOverViewTitle.getLayoutParams();
//                int dy = y - oldy;
//                int tempHeigh = layoutParams.topMargin;
//                if (tempHeigh - dy <= topMin) {
//                    tempHeigh = topMin;
//                } else if (tempHeigh - dy >= height1) {
//                    tempHeigh = height1;
//                } else {
//                    tempHeigh -= dy;
//                }
//                layoutParams.topMargin = tempHeigh;
//                mOverViewTitle.setLayoutParams(layoutParams);
                mOverViewTitle.setTranslationY(Math.min(mInitOverTitleTopHeight, mInitOverTitleTopHeight - y));
            } else {
                for (int i = 0, j = i + 1; i < mIntegerList.size() - 1; i++, j++) {
                    int integer = mIntegerList.get(i) - topMin;
                    int integer2 = mIntegerList.get(j) - topMin;

                    if (integer <= y && integer2 > y) {
                        mOverViewTitle.setVisibility(View.VISIBLE);
                        String day = mLineItemInfos.get(i).day;
                        String title = mLineItemInfos.get(i).title;

                        mOverViewTitle.setText(day + (TextUtils.isEmpty(title)? "" : "  |  " +
                                (mLineItemInfos.get(i).title.length() > 9 ?
                                        (mLineItemInfos.get(i).title.substring(0, 9)+"...")
                                        : mLineItemInfos.get(i).title)));
                        if(getActivity() != null) {
                            ((CommodityDetailActivity) getActivity()).getRightPanelView().setSelection(i);
                        }
                        break;
                    }
                    if (j == mIntegerList.size() - 1 && y > integer2 && y <= integer2 + 100) {
                        mOverViewTitle.setText(mLineItemInfos.get(j).day);
                    } else if (j == mIntegerList.size() - 1 && y > integer2 + 100) {
                        mOverViewTitle.setVisibility(View.GONE);
                        if(getActivity() != null) {
                            ((CommodityDetailActivity) getActivity()).getRightPanelView().setSelection(j);
                        }
                    }
                }
//                FrameLayout.LayoutParams layoutParams =
//                        (FrameLayout.LayoutParams) mOverViewTitle.getLayoutParams();
//                layoutParams.topMargin = height1 - height;
//                mOverViewTitle.setLayoutParams(layoutParams);
                mOverViewTitle.setTranslationY(topMin);
            }
        }
    }

    private TextView mCommodityIntroContent;

    private void initView(View parentView) {
        mScrollView = (ObservableScrollView) parentView.findViewById(R.id.id_stickynavlayout_innerscrollview);
        mOverViewTitle = (TextView) parentView.findViewById(R.id.line_overview_title);

        mScrolParentView = (LinearLayout) mScrollView.getChildAt(0);
        mScrollView.setScrollViewListener(this);
        mOverViewTitle.setOnClickListener(mOverViewClick);
        if(getActivity() != null) {
            ((CommodityDetailActivity) getActivity()).getRightPanelView().setOverViewItemClick(this);
        }

        mHeaderView = (LinearLayout) View.inflate(getActivity(), R.layout.header_commodity_detail_info, null);
        ((TextView) mHeaderView.findViewById(R.id.header_commodity_detail_info_content)).setText(
                R.string.text_wonderful_play_notice_title);
        mCommodityIntroContent = (TextView) mHeaderView.findViewById(R.id.header_commodity_detail_info_content);

//        mScrolParentView.addView(mHeaderView, mScrolParentView.getChildCount() - 1);
        mScrolParentView.addView(mHeaderView);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(getActivity() == null){
            return;
        }
        switch (msg.what) {
            case ValueConstants.MSG_LINE_DETAIL_OK:
                if (ItemType.FREE_LINE.equals(mPageType) || ItemType.TOUR_LINE.equals(mPageType) ||
                        ItemType.FREE_LINE_ABOARD.equals(mPageType) || ItemType.TOUR_LINE_ABOARD.equals(mPageType)) {
                    LineItemDetail lineDetail = (LineItemDetail) msg.obj;
                    mLineDetail = lineDetail;
                    if (lineDetail != null) {
                        handleLineDetail(lineDetail);
                    }
                }
                break;
            case ValueConstants.MSG_LINE_DETAIL_ERROR:
                break;
        }
    }

    private TextView mFooterNoticeTv;

    /**
     * 添加底部提醒注意文本
     *
     * @param parent
     */
    private void addNoticeFooterView(LinearLayout parent) {
        LinearLayout linearLayout = new LinearLayout(getActivity().getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.neu_f4f4f4));
        linearLayout.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        int padd_lr = ScreenSize.convertDIP2PX(getActivity(), 15);
        int padd_bt = ScreenSize.convertDIP2PX(getActivity(), 10);
        linearLayout.setPadding(padd_lr, padd_bt, padd_lr, padd_bt);
        linearLayout.setGravity(Gravity.CENTER);
        mFooterNoticeTv = new TextView(getActivity().getApplicationContext());
        mFooterNoticeTv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mFooterNoticeTv.setTextColor(getActivity().getResources().getColor(R.color.neu_999999));
        mFooterNoticeTv.setTextSize(12);
        mFooterNoticeTv.setText("*该行程为，卖家提供的参考行程，请联系卖家确认。");
        linearLayout.addView(mFooterNoticeTv);
//        parent.addView(linearLayout, parent.getChildCount() -1);
        parent.addView(linearLayout, parent.getChildCount());
    }

    /**
     * 处理线路列表数据
     *  @param parentView
     * @param item
     * @param hasRoute
     */
    private void handleLineItem(LinearLayout parentView, final List<LineItemInfo> item, boolean hasRoute) {
        View view = null;
        for (int itemIndex = 0; itemIndex < item.size(); itemIndex++) {

            view = View.inflate(getActivity(), R.layout.item_commodity_fragment, null);
            if(!hasRoute){
                ((ImageView)view.findViewById(R.id.item_commodity_type_img)).setImageResource(R.mipmap.icon_free_pack_red);
            }
            if (itemIndex == item.size() - 1) {
                view.findViewById(R.id.item_commodity_bottom_line).setVisibility(View.GONE);
                view.findViewById(R.id.item_commodity_left_line).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.item_commodity_bottom_line).setVisibility(View.VISIBLE);
                view.findViewById(R.id.item_commodity_left_line).setVisibility(View.VISIBLE);
            }
            if(mLineDetail.route == null || mLineDetail.route.size() == 0){
                ((TextView) view.findViewById(R.id.item_commodity_title)).setText(
                        (TextUtils.isEmpty(item.get(itemIndex).day) ? "" : item.get(itemIndex).day));
            }else{
                ((TextView) view.findViewById(R.id.item_commodity_title)).setText(
                        (TextUtils.isEmpty(item.get(itemIndex).day) ? "" : item.get(itemIndex).day)
                                + (TextUtils.isEmpty(item.get(itemIndex).title) ? "" : "  |  " + item.get(itemIndex).title));
            }
            ((TextView) view.findViewById(R.id.item_commodity_content)).setText(
                    TextUtils.isEmpty(item.get(itemIndex).description) ? "" : item.get(itemIndex).description);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.item_commodity_img_layout);
            final List<String> picList = item.get(itemIndex).pics;
            if (picList == null || picList.size() == 0) {
                relativeLayout.setVisibility(View.GONE);
            } else {
                relativeLayout.setVisibility(View.VISIBLE);
                ImageView imageView = (ImageView) view.findViewById(R.id.item_commodity_img);
//                BaseImgView.loadimg(imageView, picList.get(0), R.mipmap.ic_default_list_big
//                        , R.mipmap.ic_default_list_big, R.mipmap.ic_default_list_big, ImageScaleType.EXACTLY,
//                        750, 360, 0);

                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(picList.get(0)), R.mipmap.ic_default_list_big, 750, 360, imageView);

                ((TextView) view.findViewById(R.id.item_commodity_img_totals)).setText(
                        String.format(getString(R.string.label_pic_total), item.get(itemIndex).pics.size())
                );
            }

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 16/3/31 查看图片列表
                    if (picList != null && picList.size() > 0) {
                        ArrayList<String> pics = new ArrayList<String>();
                        for (String str : picList) {
                            if (str.startsWith("http:\\") || str.startsWith("https:\\")) {
                                pics.add(str);
                            } else {
                                pics.add(ImageUtils.getImageFullUrl(str));
                            }
                        }
                        NavUtils.gotoLookBigImage(getActivity(), pics, 0);
                    }
                }
            });
//            parentView.addView(view, parentView.getChildCount() -1);
            parentView.addView(view, parentView.getChildCount());
            if(hasRoute) {
                if (0 == itemIndex) {
                    final View finalView = view;
                    view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                finalView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                            mInitOverTitleTopHeight = finalView.getTop() +
                                    finalView.findViewById(R.id.item_commodity_title).getTop();
                            HarwkinLogUtil.info("init top height : " + mInitOverTitleTopHeight);
//                            FrameLayout.LayoutParams mOverViewTitleLayoutParams =
//                                    (FrameLayout.LayoutParams) mOverViewTitle.getLayoutParams();
//                            mOverViewTitleLayoutParams.topMargin = mInitOverTitleTopHeight;
//                            mOverViewTitle.setLayoutParams(mOverViewTitleLayoutParams);
//                            mOverViewTitle.setVisibility(View.VISIBLE);
                            mOverViewTitle.setTranslationY(mInitOverTitleTopHeight);
                        }
                    });
                }
                addGlobalLayout(view);
            }
        }
    }

    private int mInitOverTitleTopHeight;
    /**
     * 处理线路详情数据格式（统一LineItemInfo: 有行程的线路，和无行程的线路）
     *
     * @param lineDetail
     */
    private void handleLineDetail(LineItemDetail lineDetail) {
        if (lineDetail.route != null && lineDetail.route.size() > 0) {
            //判断是否显示行程概览
            mOverViewTitle.setVisibility(View.VISIBLE);
        }
        if (lineDetail.itemVO != null) {
            mCommodityIntroContent.setText(TextUtils.isEmpty(lineDetail.itemVO.description) ?
                    "" : lineDetail.itemVO.description);
        }

        mLineItemInfos.clear();
        StringBuilder stringBuilder = new StringBuilder();

//        for (int i = 0; i < 7; i++) {
//            mLineItemInfos.add(new LineItemInfo());
//        }
//        handleLineItem(mScrolParentView, mLineItemInfos);

        if (lineDetail.route != null && lineDetail.route.size() > 0) {
            //当前有行程， 或自己填写的有行程
            for (TmRouteDayInfo tmRouteDayInfo : lineDetail.route) {
                LineItemInfo lineItemInfo = new LineItemInfo();
                lineItemInfo.day = String.format(getString(R.string.label_day), tmRouteDayInfo.day);
                if (tmRouteDayInfo.detailList != null && tmRouteDayInfo.detailList.size() != 0) {
                    String title = tmRouteDayInfo.detailList.get(0).name;
                    lineItemInfo.title = TextUtils.isEmpty(title) ? "" : title;
                    String descrip = tmRouteDayInfo.detailList.get(0).shortDesc;
                    lineItemInfo.description = TextUtils.isEmpty(descrip) ? "" : descrip;
                    lineItemInfo.pics = tmRouteDayInfo.detailList.get(0).pics;
                }
                mLineItemInfos.add(lineItemInfo);
            }
            LineItemInfo lineItemInfo = new LineItemInfo();
            lineItemInfo.day = getString(R.string.label_route_end_notice);
            mLineItemInfos.add(lineItemInfo);

            handleLineItem(mScrolParentView, mLineItemInfos, true);
            if(getActivity() != null) {
                ((CommodityDetailActivity) getActivity())
                        .getRightPanelView().bindViewData(mLineItemInfos);
            }
        }

        List<LineItemInfo> lineItemInfos = new ArrayList<>();
        if (lineDetail.routePlan != null) {
            if (lineDetail.routePlan.departTrafficInfo != null) {
                LineItemInfo lineItemInfo = new LineItemInfo();
                stringBuilder.append(TextUtils.isEmpty(lineDetail.routePlan.departTrafficInfo.description) ?
                        "" : lineDetail.routePlan.departTrafficInfo.description);
                lineItemInfo.day = "去程信息";
                lineItemInfo.title = lineDetail.routePlan.departTrafficInfo.type;
                lineItemInfo.description = stringBuilder.toString();
                lineItemInfos.add(lineItemInfo);
                stringBuilder.delete(0, stringBuilder.length());
            }
            if (lineDetail.routePlan.backTrafficInfo != null) {
                LineItemInfo lineItemInfo = new LineItemInfo();
                stringBuilder.append(TextUtils.isEmpty(lineDetail.routePlan.backTrafficInfo.description) ?
                        "" : lineDetail.routePlan.backTrafficInfo.description);
                lineItemInfo.day = "回程信息";
                lineItemInfo.title = lineDetail.routePlan.backTrafficInfo.type;
                lineItemInfo.description = stringBuilder.toString();
                lineItemInfos.add(lineItemInfo);
                stringBuilder.delete(0, stringBuilder.length());
            }
            if (!TextUtils.isEmpty(lineDetail.routePlan.hotelInfo)) {
                LineItemInfo lineItemInfo = new LineItemInfo();
                stringBuilder.append(lineDetail.routePlan.hotelInfo);
                lineItemInfo.day = "酒店信息";
                lineItemInfo.title = "";
                lineItemInfo.description = stringBuilder.toString();
                lineItemInfos.add(lineItemInfo);
                stringBuilder.delete(0, stringBuilder.length());
            }
            if (!TextUtils.isEmpty(lineDetail.routePlan.scenicInfo)) {
                LineItemInfo lineItemInfo = new LineItemInfo();
                stringBuilder.append(lineDetail.routePlan.scenicInfo);
                lineItemInfo.day = "景区信息";
                lineItemInfo.title = "";
                lineItemInfo.description = stringBuilder.toString();
                lineItemInfos.add(lineItemInfo);
                stringBuilder.delete(0, stringBuilder.length());
            }
//            LineItemInfo lineItemInfo = new LineItemInfo();
//            lineItemInfo.day = mContext.getString(R.string.label_route_end_notice);
//            lineItemInfo.title = "";
//            mLineItemInfos.add(lineItemInfo);
//            handleLineItem(mScrolParentView, mLineItemInfos, true);
            if(lineItemInfos != null && lineItemInfos.size() > 0) {
                handleLinePlanItem(mScrolParentView, lineItemInfos);
            }
//            if(getActivity() != null) {
//                ((CommodityDetailActivity) getActivity())
//                        .getRightPanelView().bindViewData(mLineItemInfos);
//            }
        }
        if (lineDetail.needKnow != null && lineDetail.needKnow.frontNeedKnow != null
                && lineDetail.needKnow.frontNeedKnow.size() > 0) {
            int size = lineDetail.needKnow.frontNeedKnow.size();
            for (int needknowIndex = 0; needknowIndex < size; needknowIndex++) {
                addPurchaseNotesList(mScrolParentView, lineDetail.needKnow.frontNeedKnow.get(needknowIndex));
            }
            addNoticeFooterView(mScrolParentView);
        }
    }

    /**
     * 交通信息
     * @param scrolParentView
     * @param lineItemInfos
     */
    private void handleLinePlanItem(LinearLayout scrolParentView, List<LineItemInfo> lineItemInfos) {
        for(int index = 0; index < lineItemInfos.size(); index ++){
            View view = View.inflate(getActivity(), R.layout.item_purchase_note, null);
            View topView = view.findViewById(R.id.item_purchase_note_top_view);
            if(index != 0) {
                topView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
            }
            String day = lineItemInfos.get(index).day;
            String description = lineItemInfos.get(index).description;
            String title = lineItemInfos.get(index).title;
            ((TextView) view.findViewById(R.id.item_purchase_note_title)).setText(
                    (TextUtils.isEmpty(day) ? "" : day)
            );
            if(TextUtils.isEmpty(title)){
                ((TextView) view.findViewById(R.id.item_purchase_note_content)).setText(
                                (TextUtils.isEmpty(description) ? "" : description)
                );
            }else{
//                ((TextView) view.findViewById(R.id.item_purchase_note_content)).setText(
//                        "出行方式：" + TrafficTypeUtil.getTrafficType(title) + "\n" +
//                                (TextUtils.isEmpty(description) ? "" : description)
//                );
            }
//            scrolParentView.addView(view, scrolParentView.getChildCount() -1);
            scrolParentView.addView(view, scrolParentView.getChildCount());
        }
    }

    /**
     * 购买须知列表
     *
     * @param parent
     * @param textItem
     */
    private void addPurchaseNotesList(LinearLayout parent, TextItem textItem) {
        View view = View.inflate(getActivity(), R.layout.item_purchase_note, null);
        ((TextView) view.findViewById(R.id.item_purchase_note_title)).setText(textItem.title);
        ((TextView) view.findViewById(R.id.item_purchase_note_content)).setText(textItem.content);
//        parent.addView(view, parent.getChildCount() -1);
        parent.addView(view, parent.getChildCount());
    }

    /**
     * 行程概览点击事件
     */
    private View.OnClickListener mOverViewClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mOverViewTitle.setVisibility(View.GONE);
            Animation animation_out = AnimationUtils.loadAnimation(getActivity(), R.anim.right_out);
            mOverViewTitle.setAnimation(animation_out);

            if(getActivity() != null) {
                ((CommodityDetailActivity) getActivity()).getRightPanelView().setVisibility(View.VISIBLE);
            }
            Animation animation_in = AnimationUtils.loadAnimation(getActivity(), R.anim.right_in);
            if(getActivity() != null) {
                ((CommodityDetailActivity) getActivity()).getRightPanelView().setAnimation(animation_in);
            }

            if (!((CommodityDetailActivity) getActivity()).getStickyNavLayout().isTopHidden) {
                ((CommodityDetailActivity) getActivity()).getStickyNavLayout().scrollToTop();

                View overView = mScrolParentView.getChildAt(1);
                if (overView != null) {
                    View view = overView.findViewById(R.id.item_commodity_title);
                    if(view != null) {
                        int height = view.getTop();
                        mScrollView.smoothScrollTo(0, height);
                    }
                }
            }
        }
    };

    private void addGlobalLayout(final View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                mIntegerList.add(view.getTop());
            }
        });
    }
}
