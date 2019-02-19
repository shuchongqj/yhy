package com.quanyan.yhy.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.lineabroad.activity.AbroadDestinationFragment;
import com.quanyan.yhy.ui.lineabroad.activity.HomeDestinationFragment;
import com.quanyan.yhy.ui.lineabroad.controller.AbroadController;
import com.yhy.common.beans.hotel.CutInfo;
import com.yhy.common.beans.net.model.user.Destination;
import com.yhy.common.beans.net.model.user.DestinationList;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MasterAdviceRegionView extends LinearLayout implements AdapterView.OnItemClickListener{

    public View view;
    public List<CutInfo> mCutInfos;
    public TextView mCancel, mDetermine, mDistanceTitle;
    public CutProgressBar mCutSelectView;
    public Context mContext;
    public TextView mPicSmall;
    public TextView mPicBig;
    public int distancePosition = 4;
    public LinearLayout mllHotelStraView;




    private HomeDestinationFragment mHomeDestinationFragment;
    private AbroadDestinationFragment mAbroadDestinationFragment;

    private QuickAdapter<Destination> mAdapter;
    private List<Destination> mDestinations;//服务器返回的数据
    private FragmentManager mFragmentManager;
    private AbroadController mController;
    private String mType;
    private Map<Integer, Boolean> mSelectedmap = new HashMap<Integer, Boolean>();
    private int mCurrentIndex = -1;
    private FragmentActivity mActivity;




    public MasterAdviceRegionView(Context context,String type,FragmentActivity mActivity) {
        super(context);

        this.  mType=type;
        this.mActivity=mActivity;
        init(context);
    }

    public MasterAdviceRegionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MasterAdviceRegionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MasterAdviceRegionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    ListView mAreaListView;
    public void init(Context mContext) {

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        this.mContext = getContext();

        this.view = View.inflate(mContext, R.layout.hotcity_fragment, this);

        mAreaListView = (ListView) view.findViewById(R.id.lv_area_name);



        mAdapter = new QuickAdapter<Destination>(mContext, R.layout.item_abroad_dest_area, new ArrayList<Destination>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, Destination item) {
                LinearLayout llTextView = helper.getView(R.id.ll_tv_area);
                TextView textView = helper.getView(R.id.tv_area_name);
                if (mSelectedmap.get(helper.getPosition())) {
                    llTextView.setBackgroundColor(Color.WHITE);
                } else {
                    llTextView.setBackgroundResource(R.drawable.dest_area_menu_selected);
                }
                helper.setText(R.id.tv_area_name, item.name);
            }
        };
        mAreaListView.setAdapter(mAdapter);

        mFragmentManager =mActivity.getSupportFragmentManager();
        mAreaListView.setOnItemClickListener(this);
    }
    //处理区域数据
    public void handlerData(DestinationList result) {
        if (result.value != null && result.value.size() > 0) {
            if (mDestinations == null) {
                mDestinations = new ArrayList<>();
            }
            mDestinations = result.value;
            mAdapter.addAll(mDestinations);
            mAdapter.notifyDataSetChanged();
            if (mDestinations != null && mDestinations.size() > 0) {
                for (int i = 0; i < mDestinations.size(); i++) {
                    mSelectedmap.put(i, false);
                    if (mDestinations.get(i).isInnerArea) {
                        mSelectedmap.put(i, true);
                        mCurrentIndex = i;
                        switchFragment(0, mDestinations.get(i));
                    }
                }
                //switchFragment(0, mDestinations.get(mDestinations.size() - 1));
            }

        } else {
        }

    }


    //切换fragment
    private synchronized void switchFragment(int pos, Destination destination) {
        switch (pos) {
            case 0:
                if (mHomeDestinationFragment == null) {
                    mHomeDestinationFragment = HomeDestinationFragment.getInstance(destination,
                            mType,
                            mActivity.getIntent().getStringExtra(SPUtils.EXTRA_TITLE),
                            mActivity.getIntent().getStringExtra(SPUtils.EXTRA_ITEM_TYPE),
                            mActivity.getIntent().getStringExtra(SPUtils.EXTRA_SOURCE));
                }
                if (!mHomeDestinationFragment.isAdded() && !mHomeDestinationFragment.isResumed()) {
//                    Bundle bundle_1 = new Bundle();
//                    bundle_1.putSerializable(SPUtils.EXTRA_DATA, destination);
//                    bundle_1.putString(SPUtils.EXTRA_TYPE, mType);
//                    mHomeDestinationFragment.setArguments(bundle_1);
                    if (mAbroadDestinationFragment != null) {
                        mFragmentManager.beginTransaction().hide(mAbroadDestinationFragment).commit();
                    }
                    mFragmentManager.beginTransaction().add(R.id.fl_dest_abroad_fragment2, mHomeDestinationFragment).commit();
                } else {
                    if (mAbroadDestinationFragment != null) {
                        mFragmentManager.beginTransaction().hide(mAbroadDestinationFragment).show(mHomeDestinationFragment).commit();
                    } else {
                        mFragmentManager.beginTransaction().show(mHomeDestinationFragment).commit();
                    }
                }

                break;
            case 1:
                if (mAbroadDestinationFragment == null) {
                    mAbroadDestinationFragment = AbroadDestinationFragment.getInstance(destination,
                            mActivity.getIntent().getStringExtra(SPUtils.EXTRA_TITLE),
                            mType,
                            mActivity.getIntent().getStringExtra(SPUtils.EXTRA_SOURCE));
                }
                if (!mAbroadDestinationFragment.isAdded() && !mAbroadDestinationFragment.isResumed()) {
                    if (mHomeDestinationFragment != null) {
                        mFragmentManager.beginTransaction().hide(mHomeDestinationFragment).commit();
                    }
//                    Bundle bundle_2 = new Bundle();
//                    bundle_2.putSerializable(SPUtils.EXTRA_DATA, destination);
//                    mAbroadDestinationFragment.setArguments(bundle_2);
                    if (mHomeDestinationFragment != null) {
                        mFragmentManager.beginTransaction().hide(mHomeDestinationFragment).commit();
                    }
                    mFragmentManager.beginTransaction().add(R.id.fl_dest_abroad_fragment2, mAbroadDestinationFragment).commit();
                } else {
                    if (mHomeDestinationFragment != null) {
                        mFragmentManager.beginTransaction().hide(mHomeDestinationFragment).show(mAbroadDestinationFragment).commit();
                    } else {
                        mFragmentManager.beginTransaction().show(mAbroadDestinationFragment).commit();
                    }
                    mAbroadDestinationFragment.setDestinations(destination);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //点击状态
        if (-1 != mCurrentIndex) {
            mSelectedmap.put(mCurrentIndex, false);
        }
        mSelectedmap.put(position, true);
        mCurrentIndex = position;

        mAdapter.notifyDataSetChanged();

        if (mDestinations.get(position).isInnerArea) {
            //境内目的地
            switchFragment(0, mDestinations.get(position));
        } else {
            //境外目的地
            switchFragment(1, mDestinations.get(position));
        }
    }
}
