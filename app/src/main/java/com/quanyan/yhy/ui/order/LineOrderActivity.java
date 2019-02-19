package com.quanyan.yhy.ui.order;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.harwkin.nb.camera.TimeUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.DateUtil;
import com.quanyan.base.util.RegexUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.calendar.ScreenUtil;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.common.tourist.TouristType;
import com.quanyan.yhy.ui.common.tourist.model.DeleteUserContact;
import com.quanyan.yhy.ui.order.controller.OrderController;
import com.quanyan.yhy.ui.order.entity.PersonnalInformation;
import com.quanyan.yhy.ui.order.entity.TmValueVOCombo;
import com.quanyan.yhy.ui.tab.homepage.order.DialogOrder;
import com.quanyan.yhy.view.LabelLayout;
import com.yhy.common.beans.calender.PickSku;
import com.yhy.common.beans.net.model.common.person.UserContact;
import com.yhy.common.beans.net.model.common.person.VisitorInfoList;
import com.yhy.common.beans.net.model.tm.ItemSkuPVPairVO;
import com.yhy.common.beans.net.model.tm.ItemSkuVO;
import com.yhy.common.beans.net.model.tm.TmCreateOrderContext;
import com.yhy.common.beans.net.model.tm.TmCreateOrderParam;
import com.yhy.common.beans.net.model.tm.TmCreateOrderResultTO;
import com.yhy.common.beans.net.model.tm.TmItemSku;
import com.yhy.common.beans.net.model.tm.TmItemSkuList;
import com.yhy.common.beans.net.model.tm.TmValueVO;
import com.yhy.common.beans.net.model.tm.VoucherResult;
import com.yhy.common.beans.net.model.trip.SalesPropertyVO;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:LineOrderActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-3-3
 * Time:9:56
 * Version 1.0
 * Description:线路订单填写界面
 */
public class LineOrderActivity extends BaseActivity {

    private static final String ORDERID = "ORDERID";
    private static final String ORDERTYPE = "ORDERTYPE";

    private static final String PERSON_TYPE = "PERSON_TYPE";//人员类型
    private static final String START_DATE = "START_DATE";//出发日期
    private static final String LINE_PACKAGE = "LINE_PACKAGE";//套餐
    private static final String ROOMTYPE = "单房差";
    private static final String ADULTTYPE = "成人";
    private static final String CHOOSETIMETYPE = "请选择出发日期";

    private static final int CHOOSETOURIST = 0x11;
    private static final int CHOOSELINKMAN = 0x12;
    private static final int CHOOSEDATE = 0x13;
    private static final int CHOOSECOUPON = 0x14;
    @ViewInject(R.id.view_order_bottom)
    private HotelOrderBottomTabView view_order_bottom;

    @ViewInject(R.id.sa_iv_good)
    private ImageView mImageView;
    @ViewInject(R.id.sa_tv_good_name)
    private TextView mTitle;
    @ViewInject(R.id.tv_label_last)
    private TextView mPriceLast;
    @ViewInject(R.id.sa_tv_good_currentprice)
    private TextView mPrice;
    @ViewInject(R.id.add_topic_popular_labels)
    private LabelLayout mCombo;
    @ViewInject(R.id.rl_select_date)
    private RelativeLayout mSelectDataLayout;
    @ViewInject(R.id.hotelorder_starttime_tv)
    private TextView mDataTextView;
    @ViewInject(R.id.order_layout)
    private LinearLayout mOrderLayout;
    @ViewInject(R.id.rl_select_customer)
    private RelativeLayout mSelectCustomer;
    @ViewInject(R.id.ll_select_customer)
    private LinearLayout mCustomerLayout;
    @ViewInject(R.id.rl_select_linkman)
    private RelativeLayout mSelectLinkman;
    @ViewInject(R.id.ll_select_linkman)
    private LinearLayout mLinkmanLayout;
    @ViewInject(R.id.otherRequirements_tv)
    private EditText mRemark;
    @ViewInject(R.id.tv_orderconfig_tips)
    private TextView tips;
    @ViewInject(R.id.tips_image)
    private ImageView mTipsImage;

    @ViewInject(R.id.select_customer_view)
    private View mCustomerView;
    @ViewInject(R.id.select_linkman_view)
    private View mLinkmanView;

    @ViewInject(R.id.select_customer_line)
    private View mCustomerLine;
    @ViewInject(R.id.select_linkman_line)
    private View mLinkmanLine;

    @ViewInject(R.id.tv_select_customer)
    private TextView mSelectCustomerTips;
    @ViewInject(R.id.tv_select_linkman)
    private TextView mSelectLinkmanTips;
    @ViewInject(R.id.rl_coupon_layout)
    private RelativeLayout mRelCouponSelect;
    @ViewInject(R.id.tv_coupon)
    private TextView mCoupon;

    private HashMap<String, PickSku> skuMaps;
    private long endTime = 0;
    private VisitorInfoList visitorInfoList;
    private LayoutInflater mInflater;
    private long contactId = -1;
    public long[] touristIds;

    private OrderController mOrderController;
    private TmCreateOrderContext createOrderContext;
    private long itemID;


    private int clickPostion = -1;
    private long personTypeId = 0;
    private long linePackageId = 0;
    private long lineSelectId = 0;

    private List<ItemSkuVO> dateSkuList;
    private List<ItemSkuVO> personSkuList;
    private long startDate;
    private long currentTime;

    private List<PersonnalInformation> personnalInformations;

    private List<TmValueVO> valueVOs;
    private String otherRequirements;

    private List<TmItemSku> filterList;

    private String mType;

    private EditText mEmailEdit;
    private String mLinkmanEmail;
    private int mAllHotelCount;

    private OrderTopView mOrderTopView;
    protected boolean isPayComplete = false;//是否支付过
    private Dialog mOrderCancelDialog;

    private long sellerId = -1;
    private VoucherResult result;
    //是否选择出发日期
    private boolean isChooseDate = false;

    public static void gotoLineOrderActivity(Context context, long id, String type) {
        Intent intent = new Intent(context, LineOrderActivity.class);
        intent.putExtra(ORDERID, id);
        intent.putExtra(ORDERTYPE, type);
        context.startActivity(intent);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_lineorder, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new OrderTopView(this);
        mOrderTopView.setOrderTopTitle(getResources().getString(R.string.order_title));
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (!isChooseDate || isPayComplete) {
                finish();
            } else {
                mOrderCancelDialog = DialogOrder.cancelOrder(LineOrderActivity.this);
            }
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mOrderCancelDialog != null) {
            mOrderCancelDialog.dismiss();
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        itemID = getIntent().getLongExtra(ORDERID, -1);
        mType = getIntent().getStringExtra(ORDERTYPE);
        dateSkuList = new ArrayList<ItemSkuVO>();
        personSkuList = new ArrayList<ItemSkuVO>();
        personnalInformations = new ArrayList<PersonnalInformation>();
        valueVOs = new ArrayList<TmValueVO>();
        mOrderController = new OrderController(this, mHandler);
        view_order_bottom.setSubmitText(getResources().getString(R.string.order_submit_text));
        mInflater = LayoutInflater.from(this);
//        startDate = DateUtil.convert2long(DateUtil.getTodayDate("yyyy-MM-dd"), "yyyy-MM-dd");
        currentTime = DateUtil.convert2long(DateUtil.getTodayDate("yyyy-MM-dd"), "yyyy-MM-dd");
        view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, 0));
        view_order_bottom.setDetailsLayoutGone();
//        mPriceLast.setVisibility(View.GONE);
        setTips();
        mOrderController.getCreateOrderContext(LineOrderActivity.this, itemID);
        initClick();
    }

    private void initClick() {
        mOrderTopView.setBackClickListener(new OrderTopView.BackClickListener() {
            @Override
            public void clickBack() {
                if (!isChooseDate || isPayComplete) {
                    finish();
                } else {
                    mOrderCancelDialog = DialogOrder.cancelOrder(LineOrderActivity.this);
                }
            }
        });

        //选择日期
        mSelectDataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }

                if (clickPostion == -1) {
                    Toast.makeText(LineOrderActivity.this, getResources().getString(R.string.line_order_tips_combo), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dateSkuList == null || dateSkuList.size() == 0) return;

                NavUtils.gotoSingleWithSkuSelectCalendar(LineOrderActivity.this, startDate, endTime, currentTime, skuMaps, CHOOSEDATE, "Line");
            }
        });

        //选择游客信息
        mSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }

                TCEventHelper.onEvent(LineOrderActivity.this, AnalyDataValue.LINE_SUBMIT_SEL_VISITORS);
//                if (getAllCount() > 0) {
//                NavUtils.gotoVisitorListActivity(LineOrderActivity.this, CHOOSETOURIST, VisitorListActivity.TYPE_FROM_VISITOR, visitorInfoList);
//                    NavUtils.gotoVisitorListActivity(LineOrderActivity.this, CHOOSETOURIST, VisitorListActivity.TYPE_FROM_VISITOR, visitorInfoList, getAllCount());
//                } else {
//                    Toast.makeText(LineOrderActivity.this, "成人数量和儿童数量不能为0", Toast.LENGTH_SHORT).show();
//                }
                if (getAllCount() > 0) {
                    if (mType.equals(ItemType.TOUR_LINE_ABOARD) || mType.equals(ItemType.FREE_LINE_ABOARD)) {//境外游选择游客
                        NavUtils.gotoCommonUseTouristActivity(LineOrderActivity.this, CHOOSETOURIST, TouristType.ORDERTOURIST, TouristType.TRAVELOUT, visitorInfoList, getAllCount());
                    } else {//境内游选择游客
                        NavUtils.gotoCommonUseTouristActivity(LineOrderActivity.this, CHOOSETOURIST, TouristType.ORDERTOURIST, TouristType.TRAVELIN, visitorInfoList, getAllCount());
                    }
                } else {
                    Toast.makeText(LineOrderActivity.this, "成人数量和儿童数量不能为0", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //选择联系人
        mSelectLinkman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                TCEventHelper.onEvent(LineOrderActivity.this, AnalyDataValue.LINE_SUBMIT_SEL_CONTACTS);
//                NavUtils.gotoPersonListActivity(LineOrderActivity.this, CHOOSELINKMAN);
                if (mType.equals(ItemType.TOUR_LINE_ABOARD) || mType.equals(ItemType.FREE_LINE_ABOARD)) {//境外游选择联系人
                    NavUtils.gotoCommonUseTouristActivity(LineOrderActivity.this, CHOOSELINKMAN, TouristType.ORDERCONTACTS, TouristType.TRAVELOUT);
                } else {//境内游选择联系人
                    NavUtils.gotoCommonUseTouristActivity(LineOrderActivity.this, CHOOSELINKMAN, TouristType.ORDERCONTACTS, TouristType.TRAVELIN);
                }
            }
        });
        mRelCouponSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoOrderCouponActivity(LineOrderActivity.this, sellerId, getAllPrice(personnalInformations), CHOOSECOUPON);
            }
        });
        //底部提交订单按钮
//        view_order_bottom.setSubmitClickListener(new OrderBottomTabView.SubmitClick() {
//            @Override
//            public void submit() {
//                if (createOrderContext != null) {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put(AnalyDataValue.KEY_PTYPE, AnalyDataValue.getType(mType));
//                    params.put(AnalyDataValue.KEY_PNAME, createOrderContext.itemInfo.title);
//                    params.put(AnalyDataValue.KEY_PID, createOrderContext.itemInfo.id + "");
////                    params.put(AnalyDataValue.KEY_UID, String.valueOf(SPUtils.getUid(LineOrderActivity.this)));
//                    TCEventHelper.onEvent(LineOrderActivity.this, AnalyDataValue.TC_ID_SUBMIT_ORDER, params);
//                }
//                configParams();
//            }
//        });

        view_order_bottom.setSubmitClickListener(new HotelOrderBottomTabView.SubmitClick() {
            @Override
            public void submit() {
                if (createOrderContext != null) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(AnalyDataValue.KEY_PTYPE, AnalyDataValue.getType(mType));
                    params.put(AnalyDataValue.KEY_PNAME, createOrderContext.itemInfo.title);
                    params.put(AnalyDataValue.KEY_PID, createOrderContext.itemInfo.id + "");
//                    params.put(AnalyDataValue.KEY_UID, String.valueOf(SPUtils.getUid(LineOrderActivity.this)));
                    TCEventHelper.onEvent(LineOrderActivity.this, AnalyDataValue.TC_ID_SUBMIT_ORDER, params);
                }
                configParams();
            }

            @Override
            public void gotoSalesDetails() {
//                TmItemSkuList tmItemSkuList = new TmItemSkuList();
//                tmItemSkuList.mSkuVOs = filterSkuList(personnalInformations);
//                tmItemSkuList.hasBreakfast = "";
//                NavUtils.gotoHotelOrderDetailsActivity(LineOrderActivity.this, tmItemSkuList, "", mType, result);
            }
        });

    }

    /**
     * 设置重要条款
     */
    private void setTips() {
        mTipsImage.setVisibility(View.GONE);
//        String importantClause = getResources().getString(R.string.line_order_tips_caluse);
//        SpannableString importantClauseSpan = new SpannableString(importantClause);
//        ClickableSpan importantClauseClickableSpan = new ImportantClauseSpanClickSpan(importantClause, this);
//        importantClauseSpan.setSpan(importantClauseClickableSpan, 0, importantClause.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tips.setText(getResources().getString(R.string.line_order_tips));
//        tips.append(importantClauseSpan);
//        tips.setMovementMethod(LinkMovementMethod.getInstance());
    }

    class ImportantClauseSpanClickSpan extends ClickableSpan {
        String string;
        Context context;

        public ImportantClauseSpanClickSpan(String str, Context context) {
            super();
            this.string = str;
            this.context = context;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.main));
        }

        @Override
        public void onClick(View widget) {
            //TODO 重要条款
//            gotoViewProtocol();
        }
    }

//    //查看重要条款
//    private void gotoViewProtocol() {
//        if (!StringUtil.isEmpty(mAgreenmentUrl)) {
//            NavUtils.openBrowser(this, "重要条款", ImageUtils.getImageFullUrl(mAgreenmentUrl), true);
//        } else {
//            ToastUtil.showToast(this, getString(R.string.toast_no_agreenment_url));
//        }
//    }


    private void configParams() {
        filterList = filterSkuList(personnalInformations);

//        //判断单房差数量
//        if (isMoreGood()) {
//            ToastUtil.showToast(LineOrderActivity.this, getString(R.string.line_order_tips_good_less));
//            return;
//        }

        if (filterList == null || filterList.size() == 0) {
            Toast.makeText(LineOrderActivity.this, getResources().getString(R.string.line_order_tips_good), Toast.LENGTH_SHORT).show();
            return;
        }

        if (touristIds == null || touristIds.length == 0) {
            Toast.makeText(LineOrderActivity.this, getResources().getString(R.string.line_order_tips_tourist), Toast.LENGTH_SHORT).show();
            return;
        }

        if (getAllCount() > touristIds.length) {
            Toast.makeText(LineOrderActivity.this, getResources().getString(R.string.line_order_tips_num), Toast.LENGTH_SHORT).show();
            return;
        } else if (getAllCount() < touristIds.length) {
            Toast.makeText(LineOrderActivity.this, getResources().getString(R.string.line_order_linkman_delete_tips), Toast.LENGTH_SHORT).show();
            return;
        }

        if (contactId == -1) {
            Toast.makeText(LineOrderActivity.this, getResources().getString(R.string.line_order_tips_linkman), Toast.LENGTH_SHORT).show();
            return;
        }

        mLinkmanEmail = mEmailEdit.getText().toString();
        if (TextUtils.isEmpty(mLinkmanEmail)) {
            Toast.makeText(LineOrderActivity.this, getResources().getString(R.string.line_order_email_null), Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (!RegexUtil.isEmail(mLinkmanEmail)) {
                Toast.makeText(LineOrderActivity.this, getResources().getString(R.string.line_order_email_error), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        otherRequirements = mRemark.getText().toString();

        createOrder();
    }

    //判断单房差库存
//    private boolean isMoreGood() {
//        if (personSkuList != null && personSkuList.size() > 0) {
//            for (int j = 0; j < personSkuList.size(); j++) {
//                if (personnalInformations.get(r).getSkuID() == personSkuList.get(j).id) {
//                    if (mAllHotelCount > personSkuList.get(j).stockNum) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }

    private void createOrder() {
        TmCreateOrderParam param = new TmCreateOrderParam();
        param.itemId = itemID;
        param.contactId = contactId;
        param.touristIds = touristIds;
        param.itemType = mType;
        param.enterTime = currentTime;
        param.otherInfo = otherRequirements;
        param.skuList = filterList;
        param.email = mLinkmanEmail;
        if (r != -1) {
            param.roomAmount = personnalInformations.get(r).getNum();
        }
        if (result != null) {
            param.voucherId = result.id;
        }
        mOrderController.doCreateOrder(LineOrderActivity.this, param);
    }

    /**
     * 筛选出要传到服务器端的SKU列表
     *
     * @param list
     * @return
     */
    private List<TmItemSku> filterSkuList(List<PersonnalInformation> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        List<TmItemSku> tmItemSkus = new ArrayList<TmItemSku>();

//        if (r != -1) {
//            int a = list.get(r).getNum();
//            int b = list.get(p).getNum();
////        //单房差数量
//            mAllHotelCount = getAllHotelCount(b, a);
//            for (int i = 0; i < list.size(); i++) {
//                if (i == r) {
//                    if (mAllHotelCount != 0) {
//                        TmItemSku tm = new TmItemSku();
//                        tm.skuId = list.get(r).getSkuID();
//                        tm.num = mAllHotelCount;
//                        tm.price = list.get(r).getPrice();
//                        tm.title = list.get(r).getText();
//                        tmItemSkus.add(tm);
//                    }
//                } else {
//                    if (list.get(i).getNum() != 0) {
//                        TmItemSku tm = new TmItemSku();
//                        tm.skuId = list.get(i).getSkuID();
//                        tm.num = list.get(i).getNum();
//                        tm.price = list.get(i).getPrice();
//                        tm.title = list.get(i).getText();
//                        tmItemSkus.add(tm);
//                    }
//                }
//            }
//        } else {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNum() != 0) {
                TmItemSku tm = new TmItemSku();
                tm.skuId = list.get(i).getSkuID();
                tm.num = list.get(i).getNum();
                tm.price = list.get(i).getPrice();
                tm.title = list.get(i).getText();
                tmItemSkus.add(tm);
            }
        }
//        }
        return tmItemSkus;
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        switch (msg.what) {
            case ValueConstants.MSG_CLUB_DETAIL_INFO_OK://获取订单上下文成功
                hideErrorView(null);
                createOrderContext = (TmCreateOrderContext) msg.obj;
                if (createOrderContext == null) {
                    Toast.makeText(LineOrderActivity.this, getResources().getString(R.string.line_order_tips_fails), Toast.LENGTH_SHORT).show();
                } else {
                    resolveSku(createOrderContext);
                }
                break;
            case ValueConstants.MSG_CLUB_DETAIL_INFO_KO://获取订单上下文失败
                showNetErrorView(null, msg.arg1);
                break;
            case ValueConstants.MSG_CREATE_ORDER_OK://下订单成功
                handleOrderData((TmCreateOrderResultTO) msg.obj);
                break;
            case ValueConstants.MSG_CREATE_ORDER_KO://下订单失败
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
        }
    }


    /**
     * 处理提交订单成功
     *
     * @param result
     */

    private void handleOrderData(TmCreateOrderResultTO result) {
        isPayComplete = true;
        if (result == null) {
            Toast.makeText(LineOrderActivity.this, getResources().getString(R.string.line_order_tips_failure), Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.success) {
            NavUtils.gotoOrderConfigActivity(LineOrderActivity.this, mType, result, 0, 0, null);
            finish();
        } else {
            Toast.makeText(LineOrderActivity.this, getResources().getString(R.string.line_order_tips_failure), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 解析上下文信息
     *
     * @param tm
     */
    private void resolveSku(TmCreateOrderContext tm) {
        if (tm == null) {
            return;
        }
        if (tm.itemInfo == null) {
            return;
        }

        if (tm.startTime > 0) {
            startDate = tm.startTime;
        }

        if (!TextUtils.isEmpty(tm.itemInfo.title)) {
            mTitle.setText(tm.itemInfo.title);
        }

        sellerId = tm.itemInfo.sellerId;
        mPrice.setText(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, tm.itemInfo.marketPrice));

        if (!TextUtils.isEmpty(tm.itemInfo.itemPic)) {
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(tm.itemInfo.itemPic), R.mipmap.icon_default_215_215, mImageView);
        } else {
            mImageView.setImageResource(R.mipmap.icon_default_215_215);
        }
        resolveSalesProperty(tm.itemInfo.salesPropertyList);
    }


    /**
     * 解析后台返回的数据信息
     *
     * @param salesPropertyList
     */
    private void resolveSalesProperty(List<SalesPropertyVO> salesPropertyList) {
        if (salesPropertyList == null || salesPropertyList.size() == 0) {
            return;
        }
        for (int i = 0; i < salesPropertyList.size(); i++) {
            if (salesPropertyList.get(i).valueVOList != null && salesPropertyList.get(i).valueVOList.size() != 0) {
                if (salesPropertyList.get(i).type.equals(PERSON_TYPE)) {
                    personTypeId = salesPropertyList.get(i).id;
                    valueVOs = salesPropertyList.get(i).valueVOList;
                    setPriceInformation(valueVOs);
                } else if (salesPropertyList.get(i).type.equals(START_DATE)) {//日期
//                    startDateId = salesPropertyList.get(i).id;
                } else if (salesPropertyList.get(i).type.equals(LINE_PACKAGE)) {//套餐
                    linePackageId = salesPropertyList.get(i).id;
//                    setCombo(salesPropertyList.get(i).valueVOList);
                    filterComboStock(salesPropertyList.get(i).valueVOList);
                }
            }
        }
    }


    /**
     * 筛选每个套餐是否有库存，如果有库存套餐可以点击，如果没有库存套餐不可点击
     *
     * @param list
     */
    private void filterComboStock(List<TmValueVO> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        if (createOrderContext == null && createOrderContext.itemInfo == null) {
            return;
        }

        if (createOrderContext.itemInfo.skuList == null || createOrderContext.itemInfo.skuList.size() == 0) {
            showUnclickCombo(list);
            return;
        }

        List<ItemSkuVO> skuList = createOrderContext.itemInfo.skuList;
        List<TmValueVOCombo> listCombo = new ArrayList<TmValueVOCombo>();

        for (int i = 0; i < list.size(); i++) {
            TmValueVOCombo tm = new TmValueVOCombo();
            tm.setTmValueVO(list.get(i));
            tm.setIsClick(false);
            long ID = list.get(i).id;
            List<ItemSkuVO> a = new ArrayList<ItemSkuVO>();
            for (int j = 0; j < skuList.size(); j++) {
                List<ItemSkuPVPairVO> itemSkuPVPairVO = skuList.get(j).itemSkuPVPairList;
                for (int t = 0; t < itemSkuPVPairVO.size(); t++) {
                    if (itemSkuPVPairVO.get(t).pType.equals(LINE_PACKAGE)) {
                        if (linePackageId == itemSkuPVPairVO.get(t).pId && ID == itemSkuPVPairVO.get(t).vId) {
                            a.add(skuList.get(j));
                            break;
                        }
                    }
                }
            }
            tm.setSkuList(a);
            listCombo.add(tm);
        }

        for (int i = 0; i < listCombo.size(); i++) {
            for (int j = 0; j < listCombo.get(i).getSkuList().size(); j++) {
                if (listCombo.get(i).getSkuList().get(j).stockNum > 0) {
                    listCombo.get(i).setIsClick(true);
                    break;
                }
            }
        }

        setCombo(listCombo);
    }

    //没有sku也显示
    private void showUnclickCombo(List<TmValueVO> list) {
        LabelLayout.LayoutParams marginLayoutParams = new LabelLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        marginLayoutParams.topMargin = 20;
        marginLayoutParams.rightMargin = 30;
        marginLayoutParams.leftMargin = 30;
        if (mCombo != null) {
            mCombo.removeAllViews();
        }
        for (int i = 0; i < list.size(); i++) {
            if (!TextUtils.isEmpty(list.get(i).text)) {
                TextView textView = new TextView(getApplicationContext());
                textView.setText(list.get(i).text);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(15);
                textView.setPadding(40, 10, 40, 10);
                textView.setClickable(false);
                textView.setBackgroundResource(R.drawable.line_order_combo_nopressed);
                textView.setTextColor(getResources().getColor(R.color.order_999999));
                mCombo.addView(textView, marginLayoutParams);
            }
        }
    }


    /**
     * 设置套餐类型
     */
    private boolean isCallClicked = false;
    private int mFirstAvaiabelIndex = -1;
    private boolean isFirstComing = true;

    private void setCombo(List<TmValueVOCombo> listCombo) {
        LabelLayout.LayoutParams marginLayoutParams = new LabelLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        marginLayoutParams.topMargin = 20;
        marginLayoutParams.rightMargin = 30;
        marginLayoutParams.leftMargin = 30;
        List<TextView> textViews = new ArrayList<TextView>();
        if (mCombo != null) {
            mCombo.removeAllViews();
        }
        for (int i = 0; i < listCombo.size(); i++) {
            if (listCombo.get(i).getTmValueVO() != null) {
                if (!TextUtils.isEmpty(listCombo.get(i).getTmValueVO().text)) {
                    TextView textView = new TextView(getApplicationContext());
                    textView.setText(listCombo.get(i).getTmValueVO().text);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(15);
                    textView.setPadding(40, 10, 40, 10);
                    if (i == clickPostion) {
                        if (listCombo.get(i).isClick()) {
                            textView.setClickable(true);
                            textView.setBackgroundResource(R.drawable.line_order_combo_pressed);
                            textView.setTextColor(getResources().getColor(R.color.main));
                            textView.setOnClickListener(new TagClick(LineOrderActivity.this, i, 1, textViews, listCombo));
                            mDataTextView.setTextColor(getResources().getColor(R.color.ac_title_bg_color));
                            mSelectCustomerTips.setTextColor(getResources().getColor(R.color.order_999999));
                            mSelectLinkmanTips.setTextColor(getResources().getColor(R.color.order_999999));
                        } else {
                            textView.setBackgroundResource(R.drawable.line_order_combo_nopressed);
                            textView.setTextColor(getResources().getColor(R.color.order_999999));
                            textView.setClickable(false);
                            mDataTextView.setTextColor(getResources().getColor(R.color.order_999999));
                            mSelectCustomerTips.setTextColor(getResources().getColor(R.color.order_999999));
                            mSelectLinkmanTips.setTextColor(getResources().getColor(R.color.order_999999));
                        }
                    } else {
                        if (listCombo.get(i).isClick()) {
                            textView.setClickable(true);
                            textView.setBackgroundResource(R.drawable.line_order_combo_normal);
                            textView.setTextColor(getResources().getColor(R.color.tv_color_gray3));
                            textView.setOnClickListener(new TagClick(LineOrderActivity.this, i, 0, textViews, listCombo));
                            //默认第一个套餐为选中态
                            if (!isCallClicked) {
                                isCallClicked = true;
                                mFirstAvaiabelIndex = i;
                            }
                        } else {
                            textView.setBackgroundResource(R.drawable.line_order_combo_nopressed);
                            textView.setTextColor(getResources().getColor(R.color.order_999999));
                            textView.setClickable(false);
                        }
                    }
                    textViews.add(textView);
                    mCombo.addView(textView, marginLayoutParams);
                    //第一个可用的套餐默认选中态
                    if (mFirstAvaiabelIndex != -1 && mFirstAvaiabelIndex == i && isFirstComing) {
                        textView.callOnClick();
                        isFirstComing = false;
                    }
                }
            }
        }
    }

    /**
     * 设置价格信息界面
     *
     * @param list
     */
    private void setPriceInformation(List<TmValueVO> list) {
        if (list == null || list.size() == 0) {
            return;
        }
//        if (personnalInformations == null) {
//            personnalInformations = new ArrayList<PersonnalInformation>();
//        } else {
//            personnalInformations.clear();
//        }
        mOrderLayout.removeAllViews();
        boolean isSortRoomType = false;
        for (int i = 0; i < list.size(); i++) {
            View v = mInflater.inflate(R.layout.lineorder_numchoose_item, null);
//            PersonnalInformation ps = new PersonnalInformation();
            TextView tv1 = (TextView) v.findViewById(R.id.order_item_title_tv);
            ImageView iv = (ImageView) v.findViewById(R.id.order_item_title_iv);
            TextView tv2 = (TextView) v.findViewById(R.id.price_tv);
            NumberChooseView numberChooseView = (NumberChooseView) v.findViewById(R.id.nc_num_select);
            if (!TextUtils.isEmpty(list.get(i).text)) {
                if (list.get(i).text.equals(ROOMTYPE)) {
                    iv.setVisibility(View.VISIBLE);
                    tv1.setText(getResources().getString(R.string.line_order_single_supplement));
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            showSingleSupplementWindow(v);
//                            showSingleSupplementDialog();
                            LineOrderDialog.showSingleSupplementDialog(LineOrderActivity.this, getString(R.string.label_dlg_title_single_supplement), getString(R.string.order_wanr), true);
                        }
                    });
                    tv1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LineOrderDialog.showSingleSupplementDialog(LineOrderActivity.this, getString(R.string.label_dlg_title_single_supplement), getString(R.string.order_wanr), true);
                        }
                    });
                } else {
                    iv.setVisibility(View.GONE);
                    tv1.setText(list.get(i).text);
                }
            }
            numberChooseView.initCheckValue(0, 0, 0);

//            ps.setText(list.get(i).text);
//            ps.setTextView(tv2);
//            ps.setPid(personTypeId);
//            ps.setVid(list.get(i).id);
//            ps.setType(list.get(i).type);
//            ps.setNumberChooseView(numberChooseView);
//            personnalInformations.add(ps);
            if (isSortRoomType) {
                mOrderLayout.addView(v, mOrderLayout.getChildCount() - 1);
            } else {
                if (list.get(i).text.equals(ROOMTYPE)) {
                    isSortRoomType = true;
                }
                mOrderLayout.addView(v);
            }
        }
    }

    /**
     * 显示提示单房差提示对话框
     */
    Dialog mSingleSupplymentDlg;

    private void showSingleSupplementDialog() {
        if (mSingleSupplymentDlg == null) {
            mSingleSupplymentDlg = DialogUtil.showMessageDialog2(this,
                    getString(R.string.label_dlg_title_single_supplement),
                    getString(R.string.order_wanr),
                    getString(R.string.label_btn_ok),
                    null,
                    null,
                    null);
        }
        mSingleSupplymentDlg.show();
    }

    /**
     * 显示提示单房差提示对话框
     */
    private void showSingleSupplementWindow(View v) {
        LinearLayout layout = new LinearLayout(this);
        layout.setBackgroundResource(R.mipmap.ic_single_supplement_tip_bg);
        TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv.setText(R.string.order_wanr);
        tv.setPadding(30, 30, 30, 30);
        tv.setTextColor(getResources().getColor(R.color.neu_cccccc));
        layout.addView(tv);

        PopupWindow popupWindow = new PopupWindow(layout, ScreenUtil.getScreenWidth(v.getContext()) - 30 * 2, 200);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.transparent)));

        int[] location = new int[2];
        v.getLocationOnScreen(location);

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - 85, location[1] + 50);
    }

    /**
     * 筛选出该套餐下 Sku
     *
     * @param comID
     * @param selectID
     */
    private void filterDateSku(long comID, long selectID) {
        if (dateSkuList == null) {
            dateSkuList = new ArrayList<ItemSkuVO>();
        } else {
            dateSkuList.clear();
        }
        if (createOrderContext != null && createOrderContext.itemInfo != null) {
            if (createOrderContext.itemInfo.skuList != null && createOrderContext.itemInfo.skuList.size() != 0) {
                for (int i = 0; i < createOrderContext.itemInfo.skuList.size(); i++) {
                    if (createOrderContext.itemInfo.skuList.get(i).itemSkuPVPairList != null && createOrderContext.itemInfo.skuList.get(i).itemSkuPVPairList.size() != 0) {
                        for (int j = 0; j < createOrderContext.itemInfo.skuList.get(i).itemSkuPVPairList.size(); j++) {
                            if (createOrderContext.itemInfo.skuList.get(i).itemSkuPVPairList.get(j).pType.equals(LINE_PACKAGE)) {
                                if (createOrderContext.itemInfo.skuList.get(i).itemSkuPVPairList.get(j).pId == comID && createOrderContext.itemInfo.skuList.get(i).itemSkuPVPairList.get(j).vId == selectID) {
                                    dateSkuList.add(createOrderContext.itemInfo.skuList.get(i));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }


        Object[] objects = PickSku.getPickSkus(startDate, dateSkuList);
        skuMaps = (HashMap<String, PickSku>) objects[1];
        endTime = (long) objects[0];
    }

    /**
     * 筛选出该套餐，该日期下的SKU
     */
    private void filterPersonSku(PickSku picSku, List<ItemSkuVO> dateSkuList) {
        if (picSku == null) {
            return;
        }
        if (dateSkuList == null || dateSkuList.size() == 0) {
            return;
        }
        if (personSkuList == null) {
            personSkuList = new ArrayList<ItemSkuVO>();
        } else {
            personSkuList.clear();
        }
        for (int i = 0; i < dateSkuList.size(); i++) {
            if (dateSkuList.get(i).itemSkuPVPairList != null && dateSkuList.get(i).itemSkuPVPairList.size() != 0) {
                for (int j = 0; j < dateSkuList.get(i).itemSkuPVPairList.size(); j++) {
                    if (dateSkuList.get(i).itemSkuPVPairList.get(j).pType.equals(START_DATE)) {
                        if (dateSkuList.get(i).itemSkuPVPairList.get(j).pId == picSku.pid && dateSkuList.get(i).itemSkuPVPairList.get(j).vId == picSku.vid) {
                            personSkuList.add(dateSkuList.get(i));
                            break;
                        }
                    }
                }
            }
        }
        setPersonnalPrice();
    }

    int p = 0;//记录成人的位置
    int r = -1;//记录单房差的位置

    /**
     * 设置人员价格信息
     */
    private void setPersonnalPrice() {
        if (personSkuList == null || personSkuList.size() == 0) {
            return;
        }


        //找出成人和单房差的位置
//        for (int i = 0; i < personnalInformations.size(); i++) {
//            if (personnalInformations.get(i).getText().equals(ADULTTYPE)) {
//                p = i;
//            } else if (personnalInformations.get(i).getText().equals(ROOMTYPE)) {
//                r = i;
//            }
//        }


        //找出成人和单房差的位置
//        for (int i = 0; i < personSkuList.size(); i++) {
//            for (int j = 0; j < personSkuList.get(i).itemSkuPVPairList.size(); j++) {
//                if (personSkuList.get(i).itemSkuPVPairList.get(j).pType.equals(PERSON_TYPE)) {
//                    if (personSkuList.get(i).itemSkuPVPairList.get(j).equals(ADULTTYPE)) {
//                        p = i;
//                    } else if (personSkuList.get(i).itemSkuPVPairList.get(j).equals(ROOMTYPE)) {
//                        r = i;
//                    }
//                }
//            }
//        }

        if (personnalInformations == null) {
            personnalInformations = new ArrayList<PersonnalInformation>();
        } else {
            personnalInformations.clear();
        }
        mOrderLayout.removeAllViews();
//        boolean isSortRoomType = false;
        for (int i = 0; i < personSkuList.size(); i++) {
            View v = mInflater.inflate(R.layout.lineorder_numchoose_item, null);
            PersonnalInformation ps = new PersonnalInformation();
            TextView tv1 = (TextView) v.findViewById(R.id.order_item_title_tv);
            ImageView iv = (ImageView) v.findViewById(R.id.order_item_title_iv);
            TextView tv2 = (TextView) v.findViewById(R.id.price_tv);
            NumberChooseView numberChooseView = (NumberChooseView) v.findViewById(R.id.nc_num_select);
            for (int j = 0; j < personSkuList.get(i).itemSkuPVPairList.size(); j++) {
                if (personSkuList.get(i).itemSkuPVPairList.get(j).pType.equals(PERSON_TYPE)) {
                    if (!TextUtils.isEmpty(personSkuList.get(i).itemSkuPVPairList.get(j).vTxt)) {
                        if (personSkuList.get(i).itemSkuPVPairList.get(j).vTxt.equals(ROOMTYPE)) {
                            r = i;
                            iv.setVisibility(View.VISIBLE);
                            tv1.setText(getResources().getString(R.string.line_order_single_supplement));
                            tv2.setVisibility(View.VISIBLE);
                            tv2.setText(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, personSkuList.get(i).price) /*+ getResources().getString(R.string.line_order_room_count)*/);
                            iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                            showSingleSupplementWindow(v);
//                                    showSingleSupplementDialog();
                                    LineOrderDialog.showSingleSupplementDialog(LineOrderActivity.this, getString(R.string.label_dlg_title_single_supplement), getString(R.string.order_wanr), true);
                                }
                            });
                            tv1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LineOrderDialog.showSingleSupplementDialog(LineOrderActivity.this, getString(R.string.label_dlg_title_single_supplement), getString(R.string.order_wanr), true);
                                }
                            });
                        } else {
                            iv.setVisibility(View.GONE);
                            if (personSkuList.get(i).itemSkuPVPairList.get(j).vTxt.equals(ADULTTYPE)) {
                                p = i;
                                if (personSkuList.get(i).stockNum <= 0) {
                                    numberChooseView.initCheckValue(0, 0, 0);
                                    ps.setNum(0);
                                } else {
                                    ps.setNum(1);
                                    numberChooseView.initCheckValue(personSkuList.get(i).stockNum, 0, 1);
                                }
                            } else {
                                if (personSkuList.get(i).stockNum <= 0) {
                                    numberChooseView.initCheckValue(0, 0, 0);
                                    ps.setNum(0);
                                } else {
                                    ps.setNum(0);
                                    numberChooseView.initCheckValue(personSkuList.get(i).stockNum, 0, 0);
                                }
                            }
                            tv1.setText(personSkuList.get(i).itemSkuPVPairList.get(j).vTxt);
                            tv2.setText(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, personSkuList.get(i).price));
                            ps.setTotalPrice(personSkuList.get(i).price);
                        }
                    }

                    ps.setVid(personSkuList.get(i).itemSkuPVPairList.get(j).vId);
                    ps.setType(personSkuList.get(i).itemSkuPVPairList.get(j).pType);
                    ps.setText(personSkuList.get(i).itemSkuPVPairList.get(j).vTxt);
                }
            }

            ps.setSkuID(personSkuList.get(i).id);

//            if (personSkuList.get(i).stockNum <= 0) {
//                numberChooseView.initCheckValue(0, 0, 0);
//                ps.setNum(0);
//            } else {
//                if (r != -1) {
//                    if (i == p) {
//                        ps.setNum(1);
//                        numberChooseView.initCheckValue(personSkuList.get(i).stockNum, 0, 1);
//                    } else if (i != p && i != r) {
//                        ps.setNum(0);
//                        numberChooseView.initCheckValue(personSkuList.get(i).stockNum, 0, 0);
//                    }
//                } else {
//                    if (i == p) {
//                        ps.setNum(1);
//                        numberChooseView.initCheckValue(personSkuList.get(i).stockNum, 0, 1);
//                    } else {
//                        ps.setNum(0);
//                        numberChooseView.initCheckValue(personSkuList.get(i).stockNum, 0, 0);
//                    }
//                }
//            }

            ps.setTextView(tv2);
            ps.setPid(personTypeId);
            ps.setNumberChooseView(numberChooseView);
            ps.setPrice(personSkuList.get(i).price);

            personnalInformations.add(ps);
            mOrderLayout.addView(v);
        }


//        for (int j = 0; j < personnalInformations.size(); j++) {
//            for (int i = 0; i < personSkuList.size(); i++) {
//                if (personSkuList.get(i).itemSkuPVPairList != null && personSkuList.get(i).itemSkuPVPairList.size() != 0) {
//                    for (int t = 0; t < personSkuList.get(i).itemSkuPVPairList.size(); t++) {
//                        if (personSkuList.get(i).itemSkuPVPairList.get(t).pType.equals(PERSON_TYPE)) {
//                            if (personnalInformations.get(j).getPid() == personSkuList.get(i).itemSkuPVPairList.get(t).pId && personnalInformations.get(j).getVid() == personSkuList.get(i).itemSkuPVPairList.get(t).vId) {
//                                personnalInformations.get(j).setSkuID(personSkuList.get(i).id);
//                                if (personnalInformations.get(j).getText().equals(ROOMTYPE)) {
//                                    personnalInformations.get(j).getTextView().setVisibility(View.VISIBLE);
//                                    personnalInformations.get(j).getTextView().setText(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, personSkuList.get(i).price) + getResources().getString(R.string.line_order_room_count));
//                                } else {
//                                    personnalInformations.get(j).getTextView().setText(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, personSkuList.get(i).price));
//                                }
//
//                                personnalInformations.get(j).setPrice(personSkuList.get(i).price);
//
//                                if (r != -1) {
//                                    if (j != r)
//                                        personnalInformations.get(j).setTotalPrice(personSkuList.get(i).price);
//                                } else {
//                                    personnalInformations.get(j).setTotalPrice(personSkuList.get(i).price);
//                                }
//
//                                if (personSkuList.get(i).stockNum <= 0) {
//                                    personnalInformations.get(j).getNumberChooseView().initCheckValue(0, 0, 0);
//                                    personnalInformations.get(j).setNum(0);
//                                } else {
//                                    if (r != -1) {
//                                        if (j == p) {
//                                            personnalInformations.get(j).setNum(1);
//                                            personnalInformations.get(j).getNumberChooseView().initCheckValue(personSkuList.get(i).stockNum, 0, 1);
//                                        } else if (j != p && j != r) {
//                                            personnalInformations.get(j).setNum(0);
//                                            personnalInformations.get(j).getNumberChooseView().initCheckValue(personSkuList.get(i).stockNum, 0, 0);
//                                        }
//                                    } else {
//                                        if (j == p) {
//                                            personnalInformations.get(j).setNum(1);
//                                            personnalInformations.get(j).getNumberChooseView().initCheckValue(personSkuList.get(i).stockNum, 0, 1);
//                                        } else {
//                                            personnalInformations.get(j).setNum(0);
//                                            personnalInformations.get(j).getNumberChooseView().initCheckValue(personSkuList.get(i).stockNum, 0, 0);
//                                        }
//                                    }
//                                }
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//        }


        //设置游客人数
        mSelectCustomerTips.setText(String.format(getResources().getString(R.string.line_order_linkman_tips), getAllCount()));
        setRoomNum();
        addPersonNumChooseListener();
    }

    /**
     * 设置房间的数量
     */
    private void setRoomNum() {
        if (personnalInformations == null || personnalInformations.size() == 0) {
            return;
        }
        if (r != -1) {
            if (personnalInformations.get(p).getNum() > 0) {
                personnalInformations.get(r).getNumberChooseView().initCheckValue(getHotelRoomCount(personnalInformations.get(p).getNum()), 1, getHotelRoomCount(personnalInformations.get(p).getNum()));
                personnalInformations.get(r).setNum(getHotelRoomCount(personnalInformations.get(p).getNum()));
                personnalInformations.get(r).setTotalPrice(getAllHotelPrice(personnalInformations.get(p).getNum(), getHotelRoomCount(personnalInformations.get(p).getNum()), personnalInformations.get(r).getPrice()));
            } else {
                personnalInformations.get(r).getNumberChooseView().initCheckValue(0, 0, 0);
                personnalInformations.get(r).setNum(0);
                personnalInformations.get(r).setTotalPrice(0);
            }
        }
        if (result != null) {
            view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations) - result.value));
        } else {
            view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations)));
        }
    }


    /**
     * 设置价格信息每条信息的监听事件
     */
    private void addPersonNumChooseListener() {
        if (personnalInformations == null && personnalInformations.size() == 0) {
            return;
        }

        //成人的加减按钮
        personnalInformations.get(p).getNumberChooseView().setNumberChooseListener(new NumberChooseView.NumberClickListener() {
            @Override
            public void onReduce(int num) {//减
                personnalInformations.get(p).setNum(num);
                if (r != -1) {
                    personnalInformations.get(r).getNumberChooseView().initCheckValue(num, getSSCount(num)/*getHotelRoomCount(num)*/, /*getHotelRoomCount(num)*/getSSCount(num));
                    personnalInformations.get(r).setNum(/*getHotelRoomCount(num)*/getSSCount(num));
                    long totalPrice = getAllHotelPrice(num, 0/*getHotelRoomCount(num)*/, personnalInformations.get(r).getPrice());
                    if (totalPrice <= 0) {
                        totalPrice = 0;
//                        personnalInformations.get(r).getTextView().setText(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, totalPrice) /*+ getResources().getString(R.string.line_order_room_count)*/);
                    } else {
//                        personnalInformations.get(r).getTextView().setText(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, totalPrice) /*+ getResources().getString(R.string.line_order_room_count)*/);
                    }
                    personnalInformations.get(r).setTotalPrice(totalPrice);
                }

                if (result != null) {
                    view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations) - result.value));
                } else {
                    view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations)));
                }
//                view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(getAllPrice(personnalInformations)));
                setBottom();
                setTouristTips(getAllCount(), touristIds);
            }

            @Override
            public void onIncrease(int num) {//加
                personnalInformations.get(p).setNum(num);
                if (r != -1) {
                    personnalInformations.get(r).getNumberChooseView().initCheckValue(num, getSSCount(num) /*getHotelRoomCount(num)*/, /*getHotelRoomCount(num)*/getSSCount(num));
                    personnalInformations.get(r).setNum(/*getHotelRoomCount(num)*/getSSCount(num));
                    long totalPrice = getAllHotelPrice(num, getHotelRoomCount(num), personnalInformations.get(r).getPrice());
                    if (totalPrice <= 0) {
                        totalPrice = 0;
//                        personnalInformations.get(r).getTextView().setText(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, totalPrice) /*+ getResources().getString(R.string.line_order_room_count)*/);
                    } else {
//                        personnalInformations.get(r).getTextView().setText(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, totalPrice) /*+ getResources().getString(R.string.line_order_room_count)*/);
                    }
                    personnalInformations.get(r).setTotalPrice(totalPrice);
                }
                if (result != null) {
                    view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations) - result.value));
                } else {
                    view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations)));
                }
//                view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(getAllPrice(personnalInformations)));
                setBottom();
                setTouristTips(getAllCount(), touristIds);
            }

            @Override
            public void onReduce() {

            }

            @Override
            public void onIncrease() {

            }
        });

//        //房间的加减按钮监听
        if (r != -1) {
            personnalInformations.get(r).getNumberChooseView().setNumberChooseListener(new NumberChooseView.NumberClickListener() {
                @Override
                public void onReduce(int num) {//减
                    personnalInformations.get(r).setNum(num);
                    long totalPrice = getAllHotelPrice(personnalInformations.get(p).getNum(), num, personnalInformations.get(r).getPrice());
                    if (totalPrice <= 0) {
                        totalPrice = 0;
//                        personnalInformations.get(r).getTextView().setText(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, totalPrice) + getResources().getString(R.string.line_order_room_count));
                    } else {
//                        personnalInformations.get(r).getTextView().setText(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, totalPrice) + getResources().getString(R.string.line_order_room_count));
                    }
                    personnalInformations.get(r).setTotalPrice(totalPrice);

                    if (result != null) {
                        view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations) - result.value));
                    } else {
                        view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations)));
                    }
                    setBottom();
//                view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(getAllPrice(personnalInformations)));
                }

                @Override
                public void onIncrease(int num) {//加
                    personnalInformations.get(r).setNum(num);
                    long totalPrice = getAllHotelPrice(personnalInformations.get(p).getNum(), num, personnalInformations.get(r).getPrice());
                    if (totalPrice <= 0) {
                        totalPrice = 0;
//                        personnalInformations.get(r).getTextView().setText(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, totalPrice) + getResources().getString(R.string.line_order_room_count));
                    } else {
//                        personnalInformations.get(r).getTextView().setText(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, totalPrice) + getResources().getString(R.string.line_order_room_count));
                    }
                    personnalInformations.get(r).setTotalPrice(totalPrice);

                    if (result != null) {
                        view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations) - result.value));
                    } else {
                        view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations)));
                    }
                    setBottom();
//                view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(getAllPrice(personnalInformations)));
                }

                @Override
                public void onReduce() {

                }

                @Override
                public void onIncrease() {

                }
            });
        }

        //剩余的加减按钮
        for (int i = 0; i < personnalInformations.size(); i++) {
            if (i != p) {
                final NumberChooseView numberChooseView = personnalInformations.get(i).getNumberChooseView();
                numberChooseView.setTag(i);
                personnalInformations.get(i).getNumberChooseView().setNumberChooseListener(new NumberChooseView.NumberClickListener() {
                    @Override
                    public void onReduce(int num) {//减
                        int m = (Integer) numberChooseView.getTag();
                        personnalInformations.get(m).setNum(num);
                        if (result != null) {
                            view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations) - result.value));
                        } else {
                            view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations)));
                        }
//                        view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(getAllPrice(personnalInformations)));
                        setBottom();
                        setTouristTips(getAllCount(), touristIds);
                    }

                    @Override
                    public void onIncrease(int num) {//加
                        int m = (Integer) numberChooseView.getTag();
                        personnalInformations.get(m).setNum(num);

                        if (result != null) {
                            view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations) - result.value));
                        } else {
                            view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations)));
                        }
//                        view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(getAllPrice(personnalInformations)));
                        setBottom();

                        setTouristTips(getAllCount(), touristIds);
                    }

                    @Override
                    public void onReduce() {

                    }

                    @Override
                    public void onIncrease() {

                    }
                });
            }
        }
    }

    private void setBottom() {
        if (result != null) {
            result = null;
            mCoupon.setText("");
            view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations)));
            view_order_bottom.setDiscountLayout(0);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CHOOSELINKMAN://联系人
                    UserContact userContact = (UserContact) data.getSerializableExtra(SPUtils.EXTRA_DATA);
                    if (userContact != null) {
                        mLinkmanLayout.removeAllViews();
                        contactId = userContact.id;
                        View view = mInflater.inflate(R.layout.line_order_customer_item, null);
                        TextView order_addtourist_name_title = (TextView) view.findViewById(R.id.order_addtourist_name_title);
                        TextView order_addtourist_name = (TextView) view.findViewById(R.id.order_addtourist_name);
                        TextView order_addtourist_code_title = (TextView) view.findViewById(R.id.order_addtourist_code_title);
                        TextView order_addtourist_code = (TextView) view.findViewById(R.id.order_addtourist_code);
                        mEmailEdit = (EditText) view.findViewById(R.id.order_linkman_email);
                        order_addtourist_name_title.setText("联系人");
                        order_addtourist_code_title.setText("手机");
                        mSelectLinkmanTips.setVisibility(View.VISIBLE);
                        //去掉邮箱填写的提示
                        mSelectLinkmanTips.setText("");
                        if (!StringUtil.isEmpty(userContact.contactEmail)) {
                            mEmailEdit.setText(userContact.contactEmail);
                        }
//                        mSelectLinkmanTips.setText("请填写邮箱");
//                        mSelectLinkmanTips.setTextColor(getResources().getColor(R.color.main));
                        if (!TextUtils.isEmpty(userContact.contactName)) {
                            order_addtourist_name.setText(userContact.contactName);
                        }
                        if (!TextUtils.isEmpty(userContact.contactPhone)) {
                            order_addtourist_code.setText(userContact.contactPhone);
                        }
                        mLinkmanView.setVisibility(View.VISIBLE);
                        mLinkmanLine.setVisibility(View.VISIBLE);
                        mLinkmanLayout.addView(view);
                        mEmailEdit.addTextChangedListener(textWatcher);
                    } else {
                        mLinkmanLayout.removeAllViews();
//                        mLinkmanView.setVisibility(View.GONE);
                        mLinkmanView.setVisibility(View.VISIBLE);
                        mLinkmanLine.setVisibility(View.GONE);
                    }
                    break;
                case CHOOSETOURIST://添加游客
                    visitorInfoList = (VisitorInfoList) data.getSerializableExtra(SPUtils.EXTRA_DATA);
                    setTouristView(visitorInfoList);
                    break;
                case CHOOSEDATE:
                    PickSku pickSku = (PickSku) data.getSerializableExtra(SPUtils.EXTRA_SELECT_DATE);
                    if (pickSku != null) {
                        isChooseDate = true;
                        currentTime = Long.parseLong(pickSku.date);
                        mDataTextView.setText(DateUtil.convert2String(currentTime, "yyyy-MM-dd"));

                        mDataTextView.setTextColor(getResources().getColor(R.color.neu_333333));
                        mSelectCustomerTips.setTextColor(getResources().getColor(R.color.ac_title_bg_color));
                        mSelectLinkmanTips.setTextColor(getResources().getColor(R.color.ac_title_bg_color));
                    }
                    filterPersonSku(pickSku, dateSkuList);
                    break;
                case CHOOSECOUPON:
                    if (data != null) {
                        result = (VoucherResult) data.getSerializableExtra(SPUtils.EXTRA_DATA);
                        mCoupon.setText("-" + StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, result.value));
                        if (result != null) {
                            view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations) - result.value));
                        } else {
                            view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations)));
                        }
                        view_order_bottom.setDiscountLayout(result.value);
                    }
                    break;
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case CHOOSETOURIST://添加游客
                    DeleteUserContact mDeleteUserContact = (DeleteUserContact) data.getSerializableExtra(SPUtils.EXTRA_DATA);
                    if (mDeleteUserContact != null) {
                        deleteTorist(mDeleteUserContact);
                    }
                    break;
                case CHOOSECOUPON:
                    result = null;
                    mCoupon.setText("");
                    view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, getAllPrice(personnalInformations)));
                    view_order_bottom.setDiscountLayout(0);
                    break;
            }
        }
    }

    /**
     * 删除游客
     *
     * @param mDeleteUserContact
     */
    private void deleteTorist(DeleteUserContact mDeleteUserContact) {
        if (mDeleteUserContact == null) {
            return;
        }

        if (mDeleteUserContact.mUsers == null || mDeleteUserContact.mUsers.size() == 0) {
            return;
        }

        if (visitorInfoList == null) {
            return;
        }

        if (visitorInfoList.value == null || visitorInfoList.value.size() == 0) {
            return;
        }

        for (int i = 0; i < visitorInfoList.value.size(); i++) {
            for (int j = 0; j < mDeleteUserContact.mUsers.size(); j++) {
                if (visitorInfoList.value.get(i).id == mDeleteUserContact.mUsers.get(j).id) {
                    visitorInfoList.value.remove(i);
                }
            }
        }

        setTouristView(visitorInfoList);
    }

    /**
     * 设置游客界面
     *
     * @param visitorInfoList
     */
    private void setTouristView(VisitorInfoList visitorInfoList) {
        if (visitorInfoList == null) {
            return;
        }
        touristIds = null;
        if (visitorInfoList != null) {
            if (visitorInfoList.value != null && visitorInfoList.value.size() != 0) {
                touristIds = new long[visitorInfoList.value.size()];
                mCustomerLayout.removeAllViews();
                mCustomerView.setVisibility(View.VISIBLE);
                mCustomerLine.setVisibility(View.VISIBLE);
                for (int i = 0; i < visitorInfoList.value.size(); i++) {
                    touristIds[i] = visitorInfoList.value.get(i).id;
                    View view = mInflater.inflate(R.layout.line_order_customer_item, null);
                    RelativeLayout email_layout = (RelativeLayout) view.findViewById(R.id.email_layout);
                    TextView order_addtourist_name_title = (TextView) view.findViewById(R.id.order_addtourist_name_title);
                    TextView order_addtourist_name = (TextView) view.findViewById(R.id.order_addtourist_name);
                    TextView order_addtourist_code_title = (TextView) view.findViewById(R.id.order_addtourist_code_title);
                    TextView order_addtourist_code = (TextView) view.findViewById(R.id.order_addtourist_code);
                    RelativeLayout code_layout = (RelativeLayout) view.findViewById(R.id.code_layout);
                    RelativeLayout enname_layout = (RelativeLayout) view.findViewById(R.id.enname_layout);
                    TextView order_addtourist_enname = (TextView) view.findViewById(R.id.order_addtourist_enname);
                    email_layout.setVisibility(View.GONE);
                    order_addtourist_name_title.setText("姓名");

                    if (mType.equals(ItemType.TOUR_LINE_ABOARD) || mType.equals(ItemType.FREE_LINE_ABOARD)) {
                        enname_layout.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(visitorInfoList.value.get(i).firstName)) {
                            if (!TextUtils.isEmpty(visitorInfoList.value.get(i).lastName)) {
                                order_addtourist_enname.setText(visitorInfoList.value.get(i).firstName + " " + visitorInfoList.value.get(i).lastName);
                            } else {
                                enname_layout.setVisibility(View.GONE);
                            }
                        } else {
                            enname_layout.setVisibility(View.GONE);
                        }
                    } else {
                        enname_layout.setVisibility(View.GONE);
                    }

                    //最新联系人
                    if (mType.equals(ItemType.TOUR_LINE_ABOARD) || mType.equals(ItemType.FREE_LINE_ABOARD)) {
                        order_addtourist_code_title.setText("护照");
                        if (visitorInfoList.value.get(i).passportCert != null) {
                            order_addtourist_code.setText(visitorInfoList.value.get(i).passportCert.cert.cardNO);
                        } else {
                            order_addtourist_code.setText("暂无填写");
                        }
                    } else {
                        if (visitorInfoList.value.get(i).idCert != null) {
                            order_addtourist_code_title.setText("身份证");
                            order_addtourist_code.setText(visitorInfoList.value.get(i).idCert.cert.cardNO);
                        } else {
                            if (visitorInfoList.value.get(i).passportCert != null) {
                                order_addtourist_code_title.setText("护照");
                                order_addtourist_code.setText(visitorInfoList.value.get(i).passportCert.cert.cardNO);
                            } else {
                                if (visitorInfoList.value.get(i).militaryCert != null) {
                                    order_addtourist_code_title.setText("军人证");
                                    order_addtourist_code.setText(visitorInfoList.value.get(i).militaryCert.cert.cardNO);
                                } else {
                                    if (visitorInfoList.value.get(i).hkMacaoCert != null) {
                                        order_addtourist_code_title.setText("港澳通行证");
                                        order_addtourist_code.setText(visitorInfoList.value.get(i).hkMacaoCert.cert.cardNO);
                                    } else {
                                        code_layout.setVisibility(View.GONE);
                                        order_addtourist_code_title.setText("");
                                        order_addtourist_code.setText("");
                                    }
                                }
                            }
                        }
                    }

                    order_addtourist_name.setText(visitorInfoList.value.get(i).contactName);
                    mCustomerLayout.addView(view);
                }
            } else {
                mCustomerLayout.removeAllViews();
//                mCustomerView.setVisibility(View.GONE);
                mCustomerView.setVisibility(View.VISIBLE);
                mCustomerLine.setVisibility(View.GONE);
            }
        } else {
            mCustomerLayout.removeAllViews();
//            mCustomerView.setVisibility(View.GONE);
            mCustomerView.setVisibility(View.VISIBLE);
            mCustomerLine.setVisibility(View.GONE);
        }
        setTouristTips(getAllCount(), touristIds);
    }

    /**
     * 套餐标签点击事件
     */
    private class TagClick implements View.OnClickListener {
        private Activity mContext;
        private int i;
        private int num;
        private List<TextView> mList;
        private List<TmValueVOCombo> listCombo;

        public TagClick(Activity context, int i, int num, List<TextView> mList, List<TmValueVOCombo> listCombo) {
            this.mContext = context;
            this.i = i;
            this.num = num;
            this.mList = mList;
            this.listCombo = listCombo;
        }

        @Override
        public void onClick(View v) {
            resertTextView();
            //点击的不是同一个套餐的时候筛选出该套餐的SKU
            if (clickPostion != i) {
                mDataTextView.setText(R.string.label_default_choose_start_date);
                currentTime = DateUtil.convert2long(DateUtil.getTodayDate("yyyy-MM-dd"), "yyyy-MM-dd");
                lineSelectId = listCombo.get(i).getTmValueVO().id;
                setPriceInformation(valueVOs);
                filterDateSku(linePackageId, lineSelectId);

                view_order_bottom.setBottomPrice(StringUtil.converRMb2YunWithFlag(LineOrderActivity.this, 0));

                result = null;
                mCoupon.setText("");
                view_order_bottom.setDiscountLayout(0);

                touristIds = null;
                visitorInfoList = null;
                mCustomerLayout.removeAllViews();
                mSelectCustomerTips.setText("请选择游客");

                mDataTextView.setTextColor(getResources().getColor(R.color.ac_title_bg_color));
                mSelectCustomerTips.setTextColor(getResources().getColor(R.color.order_999999));
                mSelectLinkmanTips.setTextColor(getResources().getColor(R.color.order_999999));
            }
            if (num == 1) {
                num = 0;
                mList.get(i).setBackgroundResource(R.drawable.line_order_combo_normal);
                mList.get(i).setTextColor(getResources().getColor(R.color.tv_color_gray3));
            } else if (num == 0) {
                num = 1;
                mList.get(i).setBackgroundResource(R.drawable.line_order_combo_pressed);
                mList.get(i).setTextColor(getResources().getColor(R.color.main));
            }
            clickPostion = i;
        }

        private void resertTextView() {
            num = 0;
            for (int i = 0; i < mList.size(); i++) {
                TextView textView = mList.get(i);
                if (listCombo.get(i).isClick()) {
                    textView.setTextColor(getResources().getColor(R.color.tv_color_gray3));
                    textView.setBackgroundResource(R.drawable.line_order_combo_normal);
                } else {
                    textView.setBackgroundResource(R.drawable.line_order_combo_nopressed);
                    textView.setTextColor(getResources().getColor(R.color.order_999999));
                }
            }
        }
    }


    /**
     * 计算总价
     *
     * @return
     */
    private long getAllPrice(List<PersonnalInformation> personnalInformations) {
        if (personnalInformations == null || personnalInformations.size() == 0) {
            return 0;
        }
        long totalPrice = 0;
        for (int i = 0; i < personnalInformations.size(); i++) {
            if (r != -1) {
                if (i == r) {
                    totalPrice += getAllHotelPrice(personnalInformations.get(p).getNum(), personnalInformations.get(i).getNum(), personnalInformations.get(i).getPrice());
                } else {
                    totalPrice += (long) (personnalInformations.get(i).getPrice() * personnalInformations.get(i).getNum());
                }
            } else {
                totalPrice += (long) (personnalInformations.get(i).getPrice() * personnalInformations.get(i).getNum());
            }
        }
        return totalPrice;
    }


    /**
     * 根据成人数量动态计算房间数
     *
     * @param audCount
     * @return
     */
    private int getHotelRoomCount(int audCount) {
        return audCount / 2 + audCount % 2;
    }

    /**
     * 根据成人数和房间数计算单房差
     *
     * @param audCount
     * @param roomCount
     * @param price
     * @return
     */
    private long getAllHotelPrice(int audCount, int roomCount, long price) {
//        return (roomCount * 2 - audCount) * price;
        return roomCount * price;
    }

    /**
     * 获取单房差的数量
     *
     * @param audCount
     * @return
     */
    private int getSSCount(int audCount) {
        return (audCount % 2 == 0) ? 0 : 1;
    }

    /**
     * 获取单房差的数量
     *
     * @param audCount
     * @param roomCount
     * @return
     */
    private int getAllHotelCount(int audCount, int roomCount) {
        return (roomCount * 2 - audCount);
    }


    /**
     * 获得总的出行人数
     *
     * @return
     */
    private int getAllCount() {
        int allCount = 0;
        for (int i = 0; i < personnalInformations.size(); i++) {
            if (r != -1) {
                if (i != r)
                    allCount += personnalInformations.get(i).getNum();
            } else {
                allCount += personnalInformations.get(i).getNum();
            }
        }
        return allCount;
    }


    /**
     * 对邮箱EditText的监听
     */
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            String str = mEmailEdit.getText().toString();
            if (TextUtils.isEmpty(str)) {
                if (mSelectLinkmanTips.getVisibility() == View.GONE) {
                    mSelectLinkmanTips.setVisibility(View.VISIBLE);
                }
                mSelectLinkmanTips.setText("请填写联系人邮箱");
            } else {
                if (!RegexUtil.isEmail(str)) {
                    if (mSelectLinkmanTips.getVisibility() == View.GONE) {
                        mSelectLinkmanTips.setVisibility(View.VISIBLE);
                    }
                    mSelectLinkmanTips.setText("请填写正确的邮箱");
                } else {
                    mSelectLinkmanTips.setVisibility(View.GONE);
                }
            }
        }
    };


    /**
     * 设置游客提示信息
     *
     * @param allSelectCount getAllCount()
     */
    private void setTouristTips(int allSelectCount, long[] touristIds) {
        if (touristIds == null || touristIds.length == 0) {
            if (allSelectCount > 0) {
                mSelectCustomerTips.setTextColor(getResources().getColor(R.color.ac_title_bg_color));
                mSelectCustomerTips.setText(String.format(getResources().getString(R.string.line_order_linkman_tips), allSelectCount));
            } else {
                mSelectCustomerTips.setTextColor(getResources().getColor(R.color.ac_title_bg_color));
                mSelectCustomerTips.setText(getResources().getString(R.string.line_order_linkman_ttips));
            }
        } else {
            if (allSelectCount == touristIds.length) {
                mSelectCustomerTips.setTextColor(getResources().getColor(R.color.order_999999));
                mSelectCustomerTips.setText("");
            } else if (allSelectCount > touristIds.length) {
                mSelectCustomerTips.setTextColor(getResources().getColor(R.color.ac_title_bg_color));
                mSelectCustomerTips.setText(String.format(getResources().getString(R.string.line_order_linkman_tips), allSelectCount));
            } else if (allSelectCount < touristIds.length) {
                mSelectCustomerTips.setTextColor(getResources().getColor(R.color.ac_title_bg_color));
                mSelectCustomerTips.setText(getResources().getString(R.string.line_order_linkman_delete_tips));
//                mSelectCustomerTips.setText(String.format(getResources().getString(R.string.line_order_linkman_delete_tips), allSelectCount));
            }
        }
    }


    /**
     * 展示错误页面
     *
     * @param arg1
     */
    protected void showNetErrorView(ViewGroup parent, int arg1) {
        showErrorView(parent, ErrorCode.NETWORK_UNAVAILABLE == arg1 ?
                IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
            @Override
            public void onClick(View view) {
                mOrderController.getCreateOrderContext(LineOrderActivity.this, itemID);
                showLoadingView(getResources().getString(R.string.scenic_loading_notice));
            }
        });
    }

}
