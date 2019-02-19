package com.harwkin.nb.camera.album;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.harwkin.nb.camera.CameraManager;
import com.harwkin.nb.camera.adapter.BucketAdapter;
import com.harwkin.nb.camera.adapter.BucketItemAdapter;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;

import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.yhy.common.beans.album.MediaItem;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SelectMediaActivity extends BaseActivity {
    protected static final String TAG = SelectMediaActivity.class.getSimpleName();

    public static final String INTENT_ACTION = "BUCKET_ID";

    private GridView gv_bucket_item;

    private ListView lv_photo_album;

    private TextView tv_select_bucket;

    private BucketItemAdapter mBucketItemAdapter;

    private BucketAdapter mBucketAdapter;

    private AlbumHelper helper;

    private List<MediaItem> selectData = new ArrayList<>();
    private List<MediaItem> adapterData;

    private List<MediaBucket> dataList;

    private int maxSelect = 0;

    private PopupWindow mPopupWindow;

    private int origin;

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_image, null);
    }

    private BaseNavView mBaseNavView;
    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText("相机胶卷");
        mBaseNavView.setRightText("完成");
        mBaseNavView.setRightTextClick(selectOK);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (getIntent() != null) {
            maxSelect = getIntent().getIntExtra(
                    CameraManager.MAX_SELECT_ACTION, 0);
        }
        findView();
        initData();
        initBucket();
    }

    private void findView() {
        gv_bucket_item = findViewById(R.id.gv_bucket_item);
        gv_bucket_item.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mBucketItemAdapter.getItem(position).mediaType == 1 ) {
                    //如果媒体类型是图片
                    View viewById = view.findViewById(R.id.iv_image_select);
                    if (mBucketItemAdapter.getItem(position).isSelected) {
                        viewById.setVisibility(View.GONE);
                        mBucketItemAdapter.getItem(position).isSelected = false;
                        selectData.remove(mBucketItemAdapter.getItem(position));
                    } else {
                        if (selectData.size() >= maxSelect)
                            ToastUtil.showToast(SelectMediaActivity.this, "最多选择" + maxSelect + "张图片");
                        else {
                            viewById.setVisibility(View.VISIBLE);
                            mBucketItemAdapter.getItem(position).isSelected = true;
                            selectData.add(mBucketItemAdapter.getItem(position));
                        }
                    }
                }else if (mBucketItemAdapter.getItem(position).mediaType == 2 ){//否则媒体类型是视频（暂时只有视频）
                    if (selectData.size() == 0) {
                        //返回相册视频
                        MediaItem video = mBucketItemAdapter.getItem(position);
                        getVideoFromGallery(video);
                    }else {
                        ToastUtil.showToast(SelectMediaActivity.this,"已选择图片");
                    }
                }

            }
        });
        findViewById(R.id.ll_select_bucket).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPop();
                    }
                });
        tv_select_bucket = findViewById(R.id.tv_select_bucket);
    }

    OnClickListener selectOK = new OnClickListener() {

        @Override
        public void onClick(View v) {
            getListMediaPath();
        }

    };

    private void initBucket() {
        if (null == mPopupWindow) {
            View view = getLayoutInflater().inflate(R.layout.pop_list_bucket, null);
            mPopupWindow = PopUtil.CreatePop(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
            lv_photo_album = view.findViewById(R.id.lv_photo_album);
            mBucketAdapter = new BucketAdapter(this, dataList);
            mBucketAdapter.setSelectOptions(0);
            lv_photo_album.setAdapter(mBucketAdapter);
            lv_photo_album.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showBucketImg(position);
                    mBucketAdapter.setSelectOptions(position);
                    dismissPop();
                }
            });
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismissPop();
                }
            });
            if (dataList.size()>0) tv_select_bucket.setText(dataList.get(0).bucketName);
        }

    }

    private void showBucketImg(int position) {
        MediaBucket mediaBucket = dataList.get(position);
        tv_select_bucket.setText(mediaBucket.bucketName);
        adapterData.clear();
        adapterData.addAll(mediaBucket.mediaList);
        mBucketItemAdapter.notifyDataSetChanged();
    }

    private void dismissPop() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    private void showPop() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            dismissPop();
        }
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(findViewById(R.id.main), Gravity.TOP,
                    0, 0);
        }
    }

    private void initData() {
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        if (getIntent().getBooleanExtra(SPUtils.EXTRA_MEDIA,false)) {
            //获取视频/图片
            dataList = helper.getImageAndVideoBucketList(true);
        } else if (getIntent().getBooleanExtra(SPUtils.EXTRA_VIDEO,false)){
            //获取视频
            dataList = helper.getVideoBucketList(true);
        }else {
            //获取图片
            dataList = helper.getImageBucketList(true);
        }
        for (MediaBucket mediaBucket : dataList) {
           Collections.sort(mediaBucket.mediaList, new Comparator<MediaItem>() {
                @Override
                public int compare(MediaItem lhs, MediaItem rhs) {
                    if (lhs.mediaCreateTime > rhs.mediaCreateTime)
                        return -1;
                    else if (lhs.mediaCreateTime == rhs.mediaCreateTime)
                        return 0;
                    else
                        return 1;
                }
            });
        }
        adapterData = new ArrayList<>();
        adapterData.addAll(dataList.get(0).mediaList);
        mBucketItemAdapter = new BucketItemAdapter(this, adapterData);
        gv_bucket_item.setAdapter(mBucketItemAdapter);
    }

    private void getListMediaPath() {
        Intent intent = getIntent();
        intent.putParcelableArrayListExtra(CameraManager.MAX_SELECT_ACTION, (ArrayList<? extends Parcelable>) selectData);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    private void getVideoFromGallery(MediaItem video){
        Intent intent = getIntent();
        intent.putExtra(SPUtils.EXTRA_VIDEO, video);
        setResult(CameraManager.GET_VIDEO, intent);
        this.finish();
    }



}
