package com.quanyan.yhy.ui.order;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.common.tourist.TouristType;
import com.quanyan.yhy.ui.order.controller.OrderController;
import com.quanyan.yhy.ui.tab.homepage.order.DialogOrder;
import com.yhy.common.beans.calender.PickSku;
import com.yhy.common.beans.net.model.common.person.UserContact;
import com.yhy.common.beans.net.model.tm.ItemSkuVO;
import com.yhy.common.beans.net.model.tm.TmCreateOrderContext;
import com.yhy.common.beans.net.model.tm.TmCreateOrderParam;
import com.yhy.common.beans.net.model.tm.TmCreateOrderResultTO;
import com.yhy.common.beans.net.model.tm.TmItemSku;
import com.yhy.common.beans.net.model.tm.TmItemSkuList;
import com.yhy.common.beans.net.model.tm.VoucherResult;
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
 * Title:ScenicOrderActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-5-13
 * Time:11:00
 * Version 1.0
 * Description:  景区下单界面
 */
public class ScenicOrderActivity extends BaseActivity {

    private static final String ORDERID = "ORDERID";
    private static final String ORDERTYPE = "ORDERTYPE";
    private static final String START_DATE = "START_DATE";//出发日期
    private static final String TICKETTYPE = "TICKETTYPE";
    private static final String MERCHANTTITLE = "MERCHANTTITLE";
    private static final int CHOOSEDATE = 0x13;
    private static final int CHOOSELINKMAN = 0x12;
    private static final String SOFTTIME = "请选择出发日期";

    private static final int CHOOSECOUPON = 0x14;

    @ViewInject(R.id.view_order_bottom)
    private HotelOrderBottomTabView mOrderBottomTabView;
    @ViewInject(R.id.sa_iv_good)
    private ImageView mScenicImage;
    @ViewInject(R.id.sa_tv_good_name)
    private TextView mScenicTitle;
    @ViewInject(R.id.tv_secondtitle)
    private TextView mSecondTitle;
    @ViewInject(R.id.sa_tv_good_currentprice)
    private TextView mScenicPrice;
    @ViewInject(R.id.rl_select_date)
    private RelativeLayout mSelectDateLayout;
    @ViewInject(R.id.hotelorder_starttime_tv)
    private TextView mDateTextView;
    @ViewInject(R.id.nc_num_select1)
    private NumberChooseView numberChooseView;
    @ViewInject(R.id.unit_price)
    private TextView mUnitPrice;
    @ViewInject(R.id.select_linkman_layout)
    private RelativeLayout mSelectLinkmanLayout;
    @ViewInject(R.id.order_linkman_name)
    private EditText mLinkmanName;
    @ViewInject(R.id.order_linkman_tel)
    private EditText mLinkmanTel;
    @ViewInject(R.id.scenic_othersrq_et)
    private EditText mOthersrq;

    private OrderController mOrderController;

    private long mId;
    private String mType;

    private TmCreateOrderContext createOrderContext;
    private HashMap<String, PickSku> skuMaps;
    private long startDate = 0;
    private long endTime = 0;
    private long currentTime = 0;
    private long backTime = 0;
    private int buyCount = 1;
    private int maxBuyCount = 20;
    private long unitPrice = 0;

    private String linkManName;
    private String linkManTel;
    private String remark;
    private List<TmItemSku> filterSkuList;
    private TmItemSku tmItemSku;

    private String title;
    private String ticketTitle;

    private OrderTopView mOrderTopView;

    protected boolean isPayComplete = false;//是否支付过
    private Dialog mOrderCancelDialog;

    @ViewInject(R.id.tv_coupon)
    private TextView tv_coupon;
    @ViewInject(R.id.rl_coupon_layout)
    private RelativeLayout mRelCouponSelect;
    private VoucherResult voucherResult;
    private long sellerId = -1;

    public static void gotoScenicOrderActivity(Context context, long id, String type, String ticketType, String merchantTitle) {
        Intent intent = new Intent(context, ScenicOrderActivity.class);
        intent.putExtra(ORDERID, id);
        intent.putExtra(ORDERTYPE, type);
        intent.putExtra(TICKETTYPE, ticketType);
        intent.putExtra(MERCHANTTITLE, merchantTitle);
        context.startActivity(intent);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_newscenicorder, null);
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
            if (!isPayComplete) {
                mOrderCancelDialog = DialogOrder.cancelOrder(ScenicOrderActivity.this);
            } else {
                finish();
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
        ticketTitle = getIntent().getStringExtra(TICKETTYPE);
        title = getIntent().getStringExtra(MERCHANTTITLE);
        mId = getIntent().getLongExtra(ORDERID, -1);
        mType = getIntent().getStringExtra(ORDERTYPE);
        tmItemSku = new TmItemSku();
        filterSkuList = new ArrayList<TmItemSku>();
        mOrderController = new OrderController(this, mHandler);
        currentTime = DateUtil.convert2long(DateUtil.getTodayDate("yyyy-MM-dd"), "yyyy-MM-dd");
        mOrderBottomTabView.setSubmitText(getResources().getString(R.string.order_submit_text));
        mOrderBottomTabView.setDetailsLayoutGone();
        mUnitPrice.setText(StringUtil.converRMb2YunWithFlag(ScenicOrderActivity.this, 0));
        tmItemSku.title = title;
        getContextMsg();
        initClick();
    }

    private void initClick() {
        mLinkmanName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    mLinkmanName.setText(str1);
                    mLinkmanName.setSelection(start);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mOrderTopView.setBackClickListener(new OrderTopView.BackClickListener() {
            @Override
            public void clickBack() {
                if (!isPayComplete) {
                    mOrderCancelDialog = DialogOrder.cancelOrder(ScenicOrderActivity.this);
                } else {
                    finish();
                }
            }
        });

        mOrderBottomTabView.setSubmitClickListener(new HotelOrderBottomTabView.SubmitClick() {
            @Override
            public void submit() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(AnalyDataValue.KEY_PTYPE, ItemType.SCENIC);
                params.put(AnalyDataValue.KEY_PNAME, title);
                params.put(AnalyDataValue.KEY_PID, mId + "");
//                params.put(AnalyDataValue.KEY_UID, String.valueOf(SPUtils.getUid(ScenicOrderActivity.this)));
                TCEventHelper.onEvent(ScenicOrderActivity.this, AnalyDataValue.TC_ID_SUBMIT_ORDER, params);
                configParams();
            }

            @Override
            public void gotoSalesDetails() {
//                List<TmItemSku> a = new ArrayList<TmItemSku>();
//                TmItemSkuList tmItemSkuList = new TmItemSkuList();
//                a.add(tmItemSku);
//                tmItemSkuList.mSkuVOs = a;
//                NavUtils.gotoHotelOrderDetailsActivity(ScenicOrderActivity.this, tmItemSkuList, "", ItemType.SPOTS, voucherResult);
            }
        });

        mRelCouponSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoOrderCouponActivity(ScenicOrderActivity.this, sellerId, unitPrice * buyCount, CHOOSECOUPON);
            }
        });

        //选择日期
        mSelectDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                if (skuMaps == null || skuMaps.size() == 0) {
                    return;
                }
                NavUtils.gotoSingleWithSkuSelectCalendar(ScenicOrderActivity.this, startDate, endTime, currentTime, skuMaps, CHOOSEDATE, null);
            }
        });

        //选择联系人
        mSelectLinkmanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
//                NavUtils.gotoPersonListActivity(ScenicOrderActivity.this, CHOOSELINKMAN);
                NavUtils.gotoCommonUseTouristActivity(ScenicOrderActivity.this, CHOOSELINKMAN, TouristType.ORDERCONTACTS, TouristType.TRAVELIN);
            }
        });

        numberChooseView.setNumberChooseListener(new NumberChooseView.NumberClickListener() {
            @Override
            public void onReduce(int num) {
                buyCount = num;
//                mOrderBottomTabView.setBottomPrice(StringUtil.converRMb2YunWithFlag(unitPrice * buyCount));
                tmItemSku.num = num;

                voucherResult = null;
                tv_coupon.setText("");
                mOrderBottomTabView.setDiscountLayout(0);
                mOrderBottomTabView.setBottomPrice(StringUtil.converRMb2YunWithFlag(ScenicOrderActivity.this, unitPrice * buyCount));
            }

            @Override
            public void onIncrease(int num) {
                buyCount = num;
                tmItemSku.num = num;

                voucherResult = null;
                tv_coupon.setText("");
                mOrderBottomTabView.setDiscountLayout(0);
                mOrderBottomTabView.setBottomPrice(StringUtil.converRMb2YunWithFlag(ScenicOrderActivity.this, unitPrice * buyCount));
            }


            @Override
            public void onReduce() {
//                if (SOFTTIME.equals(mDateTextView.getText().toString())) {
//                    ToastUtil.showToast(ScenicOrderActivity.this, getResources().getString(R.string.order_select_date));
//                }
            }

            @Override
            public void onIncrease() {
//                if (SOFTTIME.equals(mDateTextView.getText().toString())) {
//                    ToastUtil.showToast(ScenicOrderActivity.this, getResources().getString(R.string.order_select_date));
//                }
            }
        });
    }

    /**
     * 获取上下文接口
     */
    private void getContextMsg() {
        if (mId == -1) {
            ToastUtil.showToast(this, getResources().getString(R.string.pay_dataerror));
            return;
        }
        mOrderController.getCreateOrderContext(ScenicOrderActivity.this, mId);
    }

    /**
     * 提交订单时对参数进行校验
     */
    private void configParams() {
        if (backTime <= 0) {
            ToastUtil.showToast(this, getResources().getString(R.string.order_date_null));
            return;
        }
        if (buyCount <= 0) {
            ToastUtil.showToast(this, getResources().getString(R.string.order_buycount_null));
            return;
        }
        linkManName = mLinkmanName.getText().toString();
        //姓名验证
        if (!RegexUtil.isName(linkManName) || RegexUtil.isBeforOrEnd(linkManName)) {
            ToastUtil.showToast(this, getString(R.string.name_error_limit));
            return;
        }
        linkManTel = mLinkmanTel.getText().toString();
        if (TextUtils.isEmpty(linkManTel)) {
            ToastUtil.showToast(this, getResources().getString(R.string.order_linkmantel_null));
            return;
        }
        if (!RegexUtil.isMobile(linkManTel)) {
            ToastUtil.showToast(this, getResources().getString(R.string.hotel_order_linkman_tel_error));
            return;
        }
        remark = mOthersrq.getText().toString();

        createOrder();
    }

    /**
     * 请求提交订单接口
     */
    private void createOrder() {
        filterSkuList.add(tmItemSku);
        TmCreateOrderParam param = new TmCreateOrderParam();
        param.itemId = mId;
        param.otherInfo = remark;
        param.contactName = linkManName;
        param.contactPhone = linkManTel;
        param.skuList = filterSkuList;
        param.enterTime = currentTime;
        param.itemType = mType;
        if (voucherResult != null) {
            param.voucherId = voucherResult.id;
        }
        mOrderController.doCreateOrder(ScenicOrderActivity.this, param);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        switch (msg.what) {
            case ValueConstants.MSG_CLUB_DETAIL_INFO_OK://获取上下文成功
                hideErrorView(null);
                createOrderContext = (TmCreateOrderContext) msg.obj;
                setContextView(createOrderContext);
                break;
            case ValueConstants.MSG_CLUB_DETAIL_INFO_KO://获取上下文失败
                showNetErrorView(null, msg.arg1);
                break;
            case ValueConstants.MSG_CREATE_ORDER_OK://提交订单成功
                handleOrderData((TmCreateOrderResultTO) msg.obj);
                break;
            case ValueConstants.MSG_CREATE_ORDER_KO://提交订单失败
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
        }
    }

    /**
     * 根据请求下来的上下文数据显示界面
     *
     * @param tmCreateOrderContext
     */
    private void setContextView(TmCreateOrderContext tmCreateOrderContext) {
        if (tmCreateOrderContext == null) {
            return;
        }

        if (tmCreateOrderContext.startTime > 0) {
            startDate = tmCreateOrderContext.startTime;
        }

        if (!TextUtils.isEmpty(title)) {
            mScenicTitle.setText(title + "(" + ticketTitle + ")");
        }

        if (tmCreateOrderContext.itemInfo == null) {
            return;
        }

        mSecondTitle.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(tmCreateOrderContext.itemInfo.title)) {
            mSecondTitle.setText(tmCreateOrderContext.itemInfo.title);
        } else {
            mSecondTitle.setText("");
        }
        mScenicPrice.setText(StringUtil.converRMb2YunWithFlag(ScenicOrderActivity.this, tmCreateOrderContext.itemInfo.marketPrice));
        mOrderBottomTabView.setBottomPrice(StringUtil.converRMb2YunWithFlag(ScenicOrderActivity.this, 0));
//        mOrderBottomTabView.setBottomPrice(StringUtil.converRMb2YunWithFlag(tmCreateOrderContext.itemInfo.marketPrice));
        if (!TextUtils.isEmpty(tmCreateOrderContext.itemInfo.itemPic)) {
//            BaseImgView.loadimg(mScenicImage,
//                    ImageUtils.getImageFullUrl(tmCreateOrderContext.itemInfo.itemPic),
//                    R.mipmap.icon_default_215_215,
//                    R.mipmap.icon_default_215_215,
//                    R.mipmap.icon_default_215_215,
//                    ImageScaleType.EXACTLY,
//                    215,
//                    215,
//                    0);
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(tmCreateOrderContext.itemInfo.itemPic), R.mipmap.icon_default_215_215, 215, 215, mScenicImage);

        } else {
            mScenicImage.setImageResource(R.mipmap.icon_default_215_215);
        }

        Object[] objects = PickSku.getPickSkus(startDate, tmCreateOrderContext.itemInfo.skuList);
        if (objects != null && objects.length > 1) {
            skuMaps = (HashMap<String, PickSku>) objects[1];
            endTime = (long) objects[0];
        }
        sellerId = tmCreateOrderContext.itemInfo.sellerId;
    }

    /**
     * 筛选出选择哪一天的SKU
     *
     * @param pickSku
     * @param skuList
     */
    private void filterSelectDate(PickSku pickSku, List<ItemSkuVO> skuList) {
        if (pickSku == null) {
            return;
        }
        if (skuList == null || skuList.size() == 0) {
            return;
        }
        if (filterSkuList == null) {
            filterSkuList = new ArrayList<TmItemSku>();
        } else {
            filterSkuList.clear();
        }
        for (int i = 0; i < skuList.size(); i++) {
            if (skuList.get(i).itemSkuPVPairList != null && skuList.get(i).itemSkuPVPairList.size() != 0) {
                for (int j = 0; j < skuList.get(i).itemSkuPVPairList.size(); j++) {
                    if (skuList.get(i).itemSkuPVPairList.get(j).pType.equals(START_DATE)) {
                        if (skuList.get(i).itemSkuPVPairList.get(j).pId == pickSku.pid && skuList.get(i).itemSkuPVPairList.get(j).vId == pickSku.vid) {
                            tmItemSku.skuId = skuList.get(i).id;
                            break;
                        }
                    }
                }
            }
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
            Toast.makeText(ScenicOrderActivity.this, getResources().getString(R.string.line_order_tips_failure), Toast.LENGTH_SHORT).show();
            return;
        }
        if (result.success) {
            NavUtils.gotoOrderConfigActivity(ScenicOrderActivity.this, mType, result, 0, 0, title);
            finish();
        } else {
            Toast.makeText(ScenicOrderActivity.this, getResources().getString(R.string.line_order_tips_failure), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CHOOSEDATE:
                    PickSku pickSku = (PickSku) data.getSerializableExtra(SPUtils.EXTRA_SELECT_DATE);
                    if (pickSku != null) {
                        backTime = Long.parseLong(pickSku.date);
                        currentTime = Long.parseLong(pickSku.date);
                        mDateTextView.setText(DateUtil.convert2String(currentTime, "yyyy-MM-dd"));
                        mUnitPrice.setText(StringUtil.converRMb2YunWithFlag(ScenicOrderActivity.this, pickSku.price));
                        unitPrice = pickSku.price;
//                        if (pickSku.stockNum > 0) {
//                            buyCount = 1;
//                            numberChooseView.initCheckValue(pickSku.stockNum, buyCount, buyCount);
//                            mOrderBottomTabView.setBottomPrice(StringUtil.converRMb2YunWithFlag(unitPrice * buyCount));
//                        } else {
//                            buyCount = 0;
//                            numberChooseView.initCheckValue(buyCount, buyCount, buyCount);
//                            mOrderBottomTabView.setBottomPrice(StringUtil.converRMb2YunWithFlag(unitPrice * buyCount));
//                        }
//                        numberChooseView.initCheckValue(maxBuyCount, buyCount, buyCount);
                        buyCount = 1;
                        numberChooseView.initCheckValue(maxBuyCount, buyCount, buyCount);
//                        mOrderBottomTabView.setBottomPrice(StringUtil.converRMb2YunWithFlag(unitPrice * buyCount));
                        if (voucherResult != null) {
                            mOrderBottomTabView.setBottomPrice(StringUtil.converRMb2YunWithFlag(ScenicOrderActivity.this, unitPrice * buyCount - voucherResult.value));
                        } else {
                            mOrderBottomTabView.setBottomPrice(StringUtil.converRMb2YunWithFlag(ScenicOrderActivity.this, unitPrice * buyCount));
                        }
                        tmItemSku.num = buyCount;
                        tmItemSku.price = unitPrice;
                        filterSelectDate(pickSku, createOrderContext.itemInfo.skuList);
                    }

                    break;
                case CHOOSELINKMAN:
                    UserContact userContact = (UserContact) data.getSerializableExtra(SPUtils.EXTRA_DATA);
                    if (userContact != null) {
                        if (!TextUtils.isEmpty(userContact.contactName)) {
                            mLinkmanName.setText(userContact.contactName);
                        }
                        if (!TextUtils.isEmpty(userContact.contactPhone)) {
                            mLinkmanTel.setText(userContact.contactPhone);
                        }
                    } else {
                        mLinkmanName.setText("");
                        mLinkmanTel.setText("");
                    }
                    break;
                case CHOOSECOUPON:
                    if (data != null) {
                        voucherResult = (VoucherResult) data.getSerializableExtra(SPUtils.EXTRA_DATA);
                        tv_coupon.setText("-" + StringUtil.converRMb2YunWithFlag(ScenicOrderActivity.this, voucherResult.value));
                        mOrderBottomTabView.setDiscountLayout(voucherResult.value);
                        mOrderBottomTabView.setBottomPrice(StringUtil.converRMb2YunWithFlag(ScenicOrderActivity.this, unitPrice * buyCount - voucherResult.value));
                    }
                    break;
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case CHOOSECOUPON:
                    voucherResult = null;
                    tv_coupon.setText("");
                    mOrderBottomTabView.setBottomPrice(StringUtil.converRMb2YunWithFlag(ScenicOrderActivity.this, unitPrice * buyCount));
                    mOrderBottomTabView.setDiscountLayout(0);
                    break;
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
                getContextMsg();
                showLoadingView(getResources().getString(R.string.scenic_loading_notice));
            }
        });
    }

}
