package com.quanyan.yhy.ui.tab.homepage.logistics;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseFragment;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.shop.helper.ShopViewHelper;
import com.yhy.common.beans.net.model.tm.ExpressDetailInfo;
import com.yhy.common.beans.net.model.tm.ExpressInfo;
import com.yhy.common.beans.net.model.tm.PackageDetail;
import com.yhy.common.beans.net.model.tm.PackageResult;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;

import java.util.List;


public class LogisticsFragment extends BaseFragment {

    @ViewInject(R.id.iv_logistical_goods)
    private ImageView ivLogisGoods;

    @ViewInject(R.id.tv_logistical_num)
    private TextView tvLogisGoodsNum;

    @ViewInject(R.id.tv_logistical_number)
    private TextView mTvLgOrderNumber;

    @ViewInject(R.id.tv_logistical_company_name)

    private TextView mTvLgorderCompanyName;

    @ViewInject(R.id.lv_logistical_packet)

    private ListView mListView;

    private QuickAdapter<ExpressDetailInfo> mLgAdapter;
    PackageDetail mPackageDetail;

    public static LogisticsFragment newInstance(PackageDetail mPackageDetail) {
        LogisticsFragment myOrderListFragment = new LogisticsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SPUtils.EXTRA_TYPE_LOGIS, mPackageDetail);
        myOrderListFragment.setArguments(bundle);
        return myOrderListFragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            mPackageDetail = (PackageDetail) bundle.getSerializable(SPUtils.EXTRA_TYPE_LOGIS);
            if (mPackageDetail != null)
                updateUi(mPackageDetail);
        }


    }

    public void updateUi(PackageDetail mPackageDetail) {
        tvLogisGoodsNum.setText(String.valueOf(mPackageDetail.itemNum)+"件商品");
//        BaseImgView.loadimg(ivLogisGoods,
//                mPackageDetail.itemPic,
//                R.mipmap.icon_default_150_150,
//                R.mipmap.icon_default_150_150,
//                R.mipmap.icon_default_150_150,
//                ImageScaleType.EXACTLY,
//                300,
//                300,
//                0);

        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(mPackageDetail.itemPic),
                R.mipmap.icon_default_150_150, 300 ,300, ivLogisGoods);
        PackageResult mPackageResult= mPackageDetail.packageRet;
        if(mPackageResult!=null){
            mTvLgOrderNumber.setText(mPackageResult.expressNo);
            mTvLgorderCompanyName.setText(mPackageResult.expressCompany);
        }
        ExpressInfo mExpressInfo = mPackageDetail.expressInfo;
        if (mExpressInfo!=null){
            List<ExpressDetailInfo>   expressDetailInfoList=mExpressInfo.expressDetailList;
            if(expressDetailInfoList!=null&&expressDetailInfoList.size()>0){

                mListView.setAdapter(mLgAdapter = new QuickAdapter<ExpressDetailInfo>(getActivity(), R.layout.logistics_list_item) {
                    @Override
                    protected void convert(BaseAdapterHelper helper, final ExpressDetailInfo item) {
                        ShopViewHelper.handleLogisticsItem(getActivity(), helper, item);
                    }
                });

                mLgAdapter.replaceAll(expressDetailInfoList);

            }

        }
    }

    @Override
    public View onLoadContentView() {
        View inflate = View.inflate(getActivity(), R.layout.fragment_logistics, null);
        ViewUtils.inject(this, inflate);
        return inflate;
    }
}