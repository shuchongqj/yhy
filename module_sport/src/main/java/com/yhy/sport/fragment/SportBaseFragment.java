package com.yhy.sport.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Nandy on 2018/7/4
 */
public abstract class SportBaseFragment extends Fragment {

    protected String TAG = this.getClass().getSimpleName();

    private boolean isViewInitialized = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.i(TAG, "onCreateView");
        View view = LayoutInflater.from(getContext()).inflate(getLayoutResId(), null);
        init(view);
        isViewInitialized = true;
        return view;
    }

    /**
     * get UI layout resource identifier
     *
     * @return
     */
    protected abstract int getLayoutResId();

    /**
     * init views
     *
     * @param view
     */
    protected abstract void init(View view);

    /**
     * do http request
     */
    protected abstract void getData();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.i(TAG, "setUserVisibleHint isVisibleToUser:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if (isViewInitialized && isVisibleToUser) {
            Log.i(TAG, "getData()");
            getData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewInitialized = false;
    }
}
