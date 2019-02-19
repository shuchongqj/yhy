package com.harwkin.nb.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.view.customview.viewpagerindicator.CirclePageIndicator;
import com.quanyan.yhy.R;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PageBigImageActivity extends BaseActivity {
    public static String IMAGE_LIST_DATA = "IMAGE_LIST_DATA";
    public static String SELECT_LIST_POSITION = "SELECT_LIST_POSITION";
    public static String SHOW_DELETE_BTN = "SHOW_DELETE_BTN";
    public static String NEED_RESULT = "NEED_RESULT";
    public static final int REQ_CHOOSE_MAP = 10079;
    PhotoViewGroupPager mVpImages;
    ImageViewerPagerAdapter mAdapter;
    private int mSelectPosition;
    private TextView mTextIndicator;
    private ArrayList<String> mData;
    private boolean mNeedResult = false;

    private CirclePageIndicator mPicturesDot;
    private boolean isTextIndicate = false;
    private boolean showGif = true;

    public static Intent getIntent(Context context,
                                   ArrayList<String> data,
                                   int selectPosition,
                                   boolean canDel,
                                   boolean needResult,
                                   int mode) {
        Intent intent = new Intent(context, PageBigImageActivity.class);
        intent.putStringArrayListExtra(IMAGE_LIST_DATA, data);
        intent.putExtra(SELECT_LIST_POSITION, selectPosition);
        intent.putExtra(SHOW_DELETE_BTN, canDel);
        intent.putExtra(NEED_RESULT, needResult);
        intent.putExtra(SPUtils.EXTRA_MODE, mode);
        return intent;
    }

    public static Intent getIntent(Context context,
                                   ArrayList<String> data,
                                   int selectPosition,
                                   boolean canDel,
                                   boolean needResult,
                                   int mode, boolean isText,boolean showGif) {
        Intent intent = new Intent(context, PageBigImageActivity.class);
        intent.putStringArrayListExtra(IMAGE_LIST_DATA, data);
        intent.putExtra(SELECT_LIST_POSITION, selectPosition);
        intent.putExtra(SHOW_DELETE_BTN, canDel);
        intent.putExtra(NEED_RESULT, needResult);
        intent.putExtra(SPUtils.EXTRA_MODE,mode);
        intent.putExtra(SPUtils.EXTRA_SHOW_GIF,showGif);
        intent.putExtra("flag",isText);
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_page_big_image, null);
    }

    private BaseNavView mBaseNavView;
    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        return null;
    }

    @Override
    public boolean isTopCover() {
        return true;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitleBarBackground(Color.TRANSPARENT);
        isTextIndicate = getIntent().getBooleanExtra("flag", false);
        mData = (ArrayList<String>) getIntent().getSerializableExtra(IMAGE_LIST_DATA);

        initViews();
        initData();
    }

    private void initData() {
        mSelectPosition = getIntent().getIntExtra(SELECT_LIST_POSITION, 0);
        mNeedResult = getIntent().getBooleanExtra(NEED_RESULT, false);
        showGif = getIntent().getBooleanExtra(SPUtils.EXTRA_SHOW_GIF,true);
        if (mData != null) {
            mTextIndicator.setText((mSelectPosition + 1) + "/" + mData.size());
            if (mAdapter.getCount() > mSelectPosition) {
                mVpImages.setCurrentItem(mSelectPosition);
            }
            mAdapter.notifyDataSetChanged();
            if (getIntent().getBooleanExtra(SHOW_DELETE_BTN, false)) {
//                setRightImageView(R.mipmap.ic_button_del, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        delData();
//                    }
//                });
            } else {
//                setRightImageView(R.drawable.ic_menu_more, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (TimeUtil.isFastDoubleClick()) {
//                            return;
//                        }
//                        try {
//                            mAdapter.getMap().get(mData.get(mVpImages.getCurrentItem())).PopView(v);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
            }
        }
        //初始化图片的点
        if (mAdapter.getCount() > 0) {
            mPicturesDot.setCount(mAdapter.getCount());
            if(mAdapter.getCount() == 1 || isTextIndicate) {
                mPicturesDot.setVisibility(View.INVISIBLE);
            } else {
                mPicturesDot.setVisibility(View.VISIBLE);
            }
        } else {
            mPicturesDot.setVisibility(View.INVISIBLE);
        }

        mVpImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                refreshTitle(i + 1);
                mPicturesDot.setCurrentItem(i);
                mTextIndicator.setText((i+1) + "/" + mData.size());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        refreshTitle(mSelectPosition + 1);
    }

    private void delData() {
        int pos = mVpImages.getCurrentItem();
        mData.remove(pos);
        if (mData.size() <= 0) {
            finish();
        }
        mAdapter.notifyDataSetChanged();
        mVpImages.setCurrentItem(pos - 1 >= 0 ? pos - 1 : 0);
        refreshTitle(pos - 1 >= 0 ? pos : 1);
    }

    /**
     * 刷新标题栏
     *
     * @param index
     */
    private void refreshTitle(int index) {
        if(mData == null || mData.size() == 0){
            return ;
        }
        mBaseNavView.setTitleText(index + "/" + mData.size());
    }

    private void initViews() {
//        setLeftImageView(R.mipmap.arrow_back_gray, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        mVpImages = (PhotoViewGroupPager) findViewById(R.id.vp_images);
        mPicturesDot = (CirclePageIndicator)findViewById(R.id.cpi_pictures_dot);
        mTextIndicator = (TextView) findViewById(R.id.vp_images_indicator_text);

        if(isTextIndicate){
            mPicturesDot.setVisibility(View.GONE);
            mTextIndicator.setVisibility(View.VISIBLE);
        }else{
            mPicturesDot.setVisibility(View.VISIBLE);
            mTextIndicator.setVisibility(View.GONE);
        }

        mAdapter = new ImageViewerPagerAdapter(getSupportFragmentManager());
        mVpImages.setAdapter(mAdapter);

        mPicturesDot.setViewPager(mVpImages);
        mPicturesDot.setInfiniteLoop(false);
    }


    class ImageViewerPagerAdapter extends FragmentStatePagerAdapter {
        Map<String, PageBigImageFragment> map = new HashMap<String, PageBigImageFragment>();

        public ImageViewerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
//            if(i >= mlist.size()){
//                mlist.add(i,PageBigImageFragment.getPageBigImageFragment(mData.get(i)));
//            }
            map.put(mData.get(i),PageBigImageFragment.getPageBigImageFragment(mData.get(i),
                    getIntent().getIntExtra(SPUtils.EXTRA_MODE,-1),showGif));
            return map.get(mData.get(i));
        }

        @Override
        public int getCount() {
            return mData != null ? mData.size() : 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        public Map<String, PageBigImageFragment> getMap(){
            return map;
        }
    }

    @Override
    public void finish() {
        if (mNeedResult) {
            Intent extrasIntent = new Intent();
            if (mData != null) {
                extrasIntent.putExtra(IMAGE_LIST_DATA, mData);
            }
            setResult(Activity.RESULT_OK, extrasIntent);
        }
        super.finish();
    }
}
