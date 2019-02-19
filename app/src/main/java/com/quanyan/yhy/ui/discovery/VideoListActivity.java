package com.quanyan.yhy.ui.discovery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.database.DBManager;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.discovery.adapter.VideoGridAdapter;
import com.quanyan.yhy.ui.shortvideo.MediaRecorderActivity;
import com.yhy.common.beans.net.model.VideoInfo;
import com.yhy.common.utils.SPUtils;
import com.yixia.camera.model.MediaObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoListActivity extends BaseActivity {
    private static final String TAG = "VideoListActivity";
    private static final int QUPAI_RECORD_REQUEST = 0x1001;
    private GridView mVideListGV;
    private BaseNavView mBaseNavView;
    private VideoGridAdapter mAdapter;
    private TextView mChooseTv;

    private boolean mIsEdit = false;
    private List<VideoInfo> mVideoList = new ArrayList<>();
    /**
     * 跳转到微视频列表
     * @param context
     */
    public static void gotoVideoListActivity(Context context){
        context.startActivity(new Intent(context,VideoListActivity.class));
    }

    /**
     * 跳转到微视频列表
     * @param context
     */
    public static void gotoVideoListActivity(Activity context, int reqCode){
        context.startActivityForResult(new Intent(context,VideoListActivity.class),reqCode);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mVideoList = DBManager.getInstance(this).doGetVideoList();

        mChooseTv = (TextView)findViewById(R.id.tv_choose_video);
        mVideListGV = (GridView)findViewById(R.id.ngv_video_list);

        mAdapter = new VideoGridAdapter(this,mVideoList);
        mAdapter.setPicNumChanged(new VideoGridAdapter.PicNumChanged() {
            @Override
            public void onPicNumChange(List<VideoInfo> data) {
                if(mAdapter.getDeletList().size() == 0){
                    mBaseNavView.setLeftTextColor(R.color.neu_cccccc);
                }else{
                    mBaseNavView.setLeftTextColor(R.color.neu_333333);
                }
            }
        });
        mVideListGV.setAdapter(mAdapter);
        mVideListGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mIsEdit){
                    return ;
                }
                if (position == mAdapter.getCount() - 1) {
//                    startRecordActivity();
                    Intent intent = new Intent(VideoListActivity.this, MediaRecorderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(SPUtils.EXTRA_TYPE, 2);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, QUPAI_RECORD_REQUEST);
                    return;
                } else {
                    if(!mIsEdit){
                        if(mAdapter.getSelectedVideo() == mVideoList.get(position).id){
                            NavUtils.gotoVideoPlayerctivty(VideoListActivity.this, mVideoList.get(position));
                        }else{
                            showSelectedMenu(mVideoList.get(position));
                        }
                        mAdapter.changeSelectedStatus(mVideoList.get(position));
                    }
                }
            }
        });
//        mVideListGV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position != mAdapter.getCount() - 1) {
//                    NavUtils.gotoVideoPlayerActivity(VideoListActivity.this, mVideoList.get(position));
//                }
//                return true;
//            }
//        });
    }

    /**
     * 显示发布菜单
     * @param videoInfo
     */
    private void showSelectedMenu(final VideoInfo videoInfo){
        mChooseTv.setVisibility(View.VISIBLE);
        mChooseTv.startAnimation(AnimationUtils.loadAnimation(this,R.anim.abc_slide_in_bottom));
        mChooseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(SPUtils.EXTRA_DATA,videoInfo);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            saveCache((MediaObject) data.getSerializableExtra(SPUtils.EXTRA_MEDIAOBJECT));
        }
    }

    /**
     * 保存到草稿箱
     */
    private void saveCache(MediaObject mMediaObject){
        if(mMediaObject != null){
//            mVideoData = mMediaObject;
//            mVideoResult = new EditorResult(mVideoData);
                VideoInfo videoInfo = new VideoInfo();
//                videoInfo.createDate = mVideoResult.getTimestamp();
//                videoInfo.duration = mVideoResult.getDuration() / (1000 *1000);
//                videoInfo.videoLocalPath = mVideoResult.getPath();
//                videoInfo.videoThumbLocalPath = mVideoPath + ".png";
//                videoInfo.id = System.currentTimeMillis();
                videoInfo.duration = mMediaObject.getDuration() / (1000 * 1000);
                videoInfo.videoLocalPath = mMediaObject.getOutputTempVideoPath();
                videoInfo.videoThumbLocalPath = mMediaObject.getOutputVideoThumbPath();
                videoInfo.id = System.currentTimeMillis();

                mVideoList.add(0,videoInfo);
                mAdapter.setData(mVideoList);
                Intent intent = new Intent();
                intent.putExtra(SPUtils.EXTRA_DATA,videoInfo);
                setResult(RESULT_OK,intent);
                finish();
        }
    }

    private String mVideoPath;
    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_video_list, null);    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(getString(R.string.label_title_video_list));
        mBaseNavView.setRightText(getString(R.string.label_btn_edit));
        mBaseNavView.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIsEdit){
                    mBaseNavView.setRightText(getString(R.string.label_btn_edit));
                    mIsEdit = false;
                    mBaseNavView.setLeftImage(R.mipmap.arrow_back_gray);
                    mBaseNavView.setLeftClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setResult(RESULT_CANCELED);
                            finish();
                        }
                    });
                    doExcTask();
                }else{
                    mBaseNavView.setLeftTextColor(R.color.neu_cccccc);
                    mChooseTv.setVisibility(View.GONE);
                    mBaseNavView.setRightText(getString(R.string.label_btn_finish));
                    mIsEdit = true;
                    mBaseNavView.setLeftText(getString(R.string.label_btn_revert));
                    mBaseNavView.setLeftClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            doRervetAll();
                        }
                    });
                }
                mAdapter.swichEditStatus(mIsEdit);
            }
        });
        return mBaseNavView;
    }

    /**
     * 撤销操作
     */
    private void doRervetAll(){
        mVideoList = DBManager.getInstance(this).doGetVideoList();
        mAdapter.setData(mVideoList);
    }

    /**
     * 执行完成动作
     */
    private void doExcTask(){
        if(mAdapter.getDeletList() != null){
            for(VideoInfo videoInfo:mAdapter.getDeletList()){
                deleteCacheVideo(videoInfo);
            }
        }
        mAdapter.reset();
    }

    /**
     * 删除缓存的视频文件和缩略图
     */
    private void deleteCacheVideo(VideoInfo videoInfo){
        if(videoInfo == null){
            return;
        }
        DBManager.getInstance(this).doDeleteVideo(videoInfo);
        com.harwkin.nb.camera.FileUtil.deleteFile(new File(videoInfo.videoLocalPath));
        com.harwkin.nb.camera.FileUtil.deleteFile(new File(videoInfo.videoThumbLocalPath));
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
}
