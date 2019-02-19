package com.quanyan.pedometer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quanyan.pedometer.newpedometer.StepService;
import com.quanyan.pedometer.utils.PreferencesUtils;
import com.quanyan.pedometer.view.ColorArcProgressBar;
import com.quanyan.yhy.R;

public class StepItemFragment extends Fragment {

    private ColorArcProgressBar mColorArcProgressBar;

    private long mCurrentStepCount = -1;
    private int mTotalStepCount = -1;
    private boolean mIsResetStepCount = false;
    private double mDistance;
    private double mCalorie;
    private double mFat;
    private long mMaxSteps;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_item, container,false);
        mColorArcProgressBar = (ColorArcProgressBar) view.findViewById(R.id.fg_arc_progress_bar);
        mMaxSteps = PreferencesUtils.getInt(getActivity().getApplicationContext(), StepService.PEDOMETER_VIEW_MAX_STEPS, 20000);
        mColorArcProgressBar.setMaxValues(mMaxSteps);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        if (mColorArcProgressBar == null) {
            return;
        }
        if (mCurrentStepCount != -1) {
            mColorArcProgressBar.setCurrentValues(mCurrentStepCount);
            if(mCurrentStepCount > 200000){
                mColorArcProgressBar.setNoticeText("20W+");
            }else {
                if(mCurrentStepCount > mMaxSteps){
                    mColorArcProgressBar.setNoticeText(mCurrentStepCount + "");
                }
            }
            mCurrentStepCount = -1;
        }
//        mColorArcProgressBar.setCurrentValues(19000);
//        if (mTotalStepCount != -1) {
//            mColorArcProgressBar.setCurrentValues(mTotalStepCount);
//            mTotalStepCount = -1;
//        }
//        mTvDistance.setText(NumberUtil.floatFormat(getActivity(),
//                mDistance / 1000) + "公里");
//        mTvCalorie.setText((int) mCalorie + "卡路里");
//        mTvFat.setText((int) mFat + "克");
    }

    public void setCurrentStepCount(long currentStepCount) {
        mCurrentStepCount = currentStepCount;
    }

    public void setTotalStepCount(int totalStepCount) {
        mTotalStepCount = totalStepCount;
    }

    public void setIsResetStepCount(boolean isResetStepCount) {
        mIsResetStepCount = isResetStepCount;
    }

    public void setDistance(double distance) {
        mDistance = distance;
    }

    public void setCalorie(double calorie) {
        mCalorie = calorie;
    }

    public void setFat(double fat) {
        mFat = fat;
    }

    public void updateUI() {
        initData();
    }
}
