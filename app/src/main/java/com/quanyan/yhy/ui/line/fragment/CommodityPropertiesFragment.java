package com.quanyan.yhy.ui.line.fragment;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.quanyan.base.BaseFragment;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.ui.line.lineinterface.IUpdateTab;
import com.yhy.common.beans.net.model.item.CityActivityDetail;
import com.yhy.common.beans.net.model.shop.MerchantItem;
import com.yhy.common.beans.net.model.trip.PropertyVO;
import com.yhy.common.beans.net.model.trip.TextItem;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:CommoditySouvenirFragment
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-17
 * Time:10:18
 * Version 1.0
 */

public class CommodityPropertiesFragment extends BaseFragment implements IUpdateTab {

    private long mLineId;
    //    private LinearLayout mLContent;
//    private LayoutInflater mInflater;
    private String mPageType;
    private LinearLayout mScrollContainer;
    private TableLayout mPropetiesLayout;
    private RelativeLayout rlEmpty;

    public static CommodityPropertiesFragment getInstance(long lineId, String pageType) {
        CommodityPropertiesFragment fragment = new CommodityPropertiesFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(SPUtils.EXTRA_ID, lineId);
        bundle.putString(SPUtils.EXTRA_TYPE, pageType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(), R.layout.fr_souvenir_detail, null);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mLineId = bundle.getLong(SPUtils.EXTRA_ID, -1);
            mPageType = bundle.getString(SPUtils.EXTRA_TYPE);
        }
        mScrollContainer = (LinearLayout) view.findViewById(R.id.ll_souvenir_content);
        mPropetiesLayout = (TableLayout) view.findViewById(R.id.fr_commodoty_detail_properties_layout);
        rlEmpty = (RelativeLayout) view.findViewById(R.id.rl_empty);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)rlEmpty.getLayoutParams();
        params.height = com.quanyan.yhy.ui.common.calendar.ScreenUtil.getScreenHeight(getContext())*2/3;
        rlEmpty.setLayoutParams(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (getActivity() == null) {
            return;
        }
        switch (msg.what) {
            case ValueConstants.MSG_LINE_DETAIL_OK:
                if (ItemType.NORMAL.equals(mPageType)) {
                    MerchantItem merchantItem = (MerchantItem) msg.obj;
                    if (merchantItem != null) {
                        handleDatas(merchantItem);
                    }
                } else if (ItemType.CITY_ACTIVITY.equals(mPageType)) {
                    CityActivityDetail cityActivityDetail = (CityActivityDetail) msg.obj;
                    if (cityActivityDetail != null) {
                        handleDatas(cityActivityDetail);
                    }
                } else if (ItemType.MASTER_CONSULT_PRODUCTS.equals(mPageType)) {
                    MerchantItem merchantItem = (MerchantItem) msg.obj;
                    if (merchantItem != null && merchantItem.itemVO != null && merchantItem.itemVO.propertyList != null && merchantItem.itemVO.propertyList.size() > 0) {
                        for (int i = 0; i < merchantItem.itemVO.propertyList.size(); i++) {
                            addMasterConsultNotesList(mScrollContainer, merchantItem.itemVO.propertyList.get(i));
                        }
                    } else {
                        rlEmpty.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case ValueConstants.MSG_LINE_DETAIL_ERROR:
                break;
        }
    }

    /**
     * 同城活动详情
     *
     * @param cityActivityDetail
     */
    private void handleDatas(CityActivityDetail cityActivityDetail) {
        StringBuilder stringBuilder = new StringBuilder();
        if (cityActivityDetail.itemVO != null) {
            if (!TextUtils.isEmpty(cityActivityDetail.itemVO.description)) {
                getView().findViewById(R.id.product_spot_info_layout).setVisibility(View.VISIBLE);
                ((TextView) getView().findViewById(R.id.product_spot_info_content)).setText(
                        cityActivityDetail.itemVO.description);
            }
            if (cityActivityDetail.itemVO.propertyList != null &&
                    cityActivityDetail.itemVO.propertyList.size() > 0) {
                addProperties(cityActivityDetail.itemVO.propertyList);
            } else {
                mPropetiesLayout.setVisibility(View.GONE);
                rlEmpty.setVisibility(View.VISIBLE);

            }
        }

        if (cityActivityDetail.needKnow != null) {
            List<TextItem> frontNeedKnow = cityActivityDetail.needKnow.frontNeedKnow;
            if (frontNeedKnow != null) {
                for (TextItem textItem : frontNeedKnow) {
                    addPurchaseNotesList(mScrollContainer, textItem);
                }
            }
        }
    }

    /**
     * 处理商品属性列表
     *
     * @param propertyVOList
     */
    private void addProperties(List<PropertyVO> propertyVOList) {
        int padding = ScreenSize.convertDIP2PX(getActivity().getApplicationContext(), 15);
        int padding_TB = ScreenSize.convertDIP2PX(getActivity().getApplicationContext(), 5);
        if (propertyVOList != null && propertyVOList.size() > 0) {
            for (PropertyVO propertyVO : propertyVOList) {
                if (!TextUtils.isEmpty(propertyVO.value)) {
                    TableRow tableRow = new TableRow(getActivity().getApplicationContext());
                    tableRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    TextView proTitle = new TextView(getActivity().getApplicationContext());
                    TextView proContent = new TextView(getActivity().getApplicationContext());
                    proContent.setSingleLine(false);
                    proContent.setPadding(padding, 0, 0, 0);
                    proTitle.setTextSize(15);
                    proTitle.setTextColor(getResources().getColor(R.color.neu_666666));
                    proContent.setTextSize(15);
                    proContent.setTextColor(getResources().getColor(R.color.neu_666666));
                    proTitle.setText(TextUtils.isEmpty(propertyVO.text) ? "" :
                            (propertyVO.text.length() > 6 ? propertyVO.text.substring(0, 6) + "\n" + propertyVO.text.substring(6)
                                    : propertyVO.text));
                    proContent.setText(propertyVO.value);
                    proTitle.setPadding(padding,
                            padding_TB,
                            padding,
                            padding_TB);
                    proContent.setPadding(padding,
                            padding_TB,
                            padding,
                            padding_TB);
                    tableRow.addView(proTitle);
                    tableRow.addView(proContent);
                    mPropetiesLayout.addView(tableRow);
                }
            }
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
        ((TextView) view.findViewById(R.id.item_purchase_note_title)).setText(TextUtils.isEmpty(textItem.title) ?
                "" : textItem.title);
        ((TextView) view.findViewById(R.id.item_purchase_note_content)).setText(TextUtils.isEmpty(textItem.content) ?
                "" : textItem.content);
        parent.addView(view, parent.getChildCount() - 1);
    }

    private void addMasterConsultNotesList(LinearLayout parent, PropertyVO textItem) {
        View view = View.inflate(getActivity(), R.layout.item_purchase_note, null);
        ((TextView) view.findViewById(R.id.item_purchase_note_title)).setText(TextUtils.isEmpty(textItem.text) ?
                "" : textItem.text);
        ((TextView) view.findViewById(R.id.item_purchase_note_content)).setText(TextUtils.isEmpty(textItem.value) ?
                "" : textItem.value);
        parent.addView(view, parent.getChildCount() - 1);
    }

    /**
     * 普通商品详情
     *
     * @param result
     */
    private void handleDatas(MerchantItem result) {
        if (result.itemVO != null && result.itemVO.propertyList != null &&
                result.itemVO.propertyList.size() > 0) {
            addProperties(result.itemVO.propertyList);
        } else {
            rlEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateTabContent() {

    }
}
