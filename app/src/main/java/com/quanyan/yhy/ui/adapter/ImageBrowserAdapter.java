package com.quanyan.yhy.ui.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.quanyan.yhy.R;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-03-06.
 */

public class ImageBrowserAdapter extends PagerAdapter {

    private Activity context;
    private List<String> picUrls;

    public ImageBrowserAdapter(Activity context, ArrayList<String> picUrls) {
        this.context = context;
        this.picUrls = picUrls;
    }


    @Override
    public int getCount() {

        return picUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.item_image_browser, null);
        ImageView pvShowImage = (ImageView) view.findViewById(R.id.pv_show_image);
        String picUrl = picUrls.get(position);
        final PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(pvShowImage);
        photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
        photoViewAttacher.setMinimumScale(1F);
//        ImageLoaderUtils.displayScaleImage(context, pvShowImage, picUrl, photoViewAttacher);
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(picUrl), pvShowImage, new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object o, Target target, boolean b) {
                return false;
            }

            @Override
            public boolean onResourceReady(Object o, Object o2, Target target, DataSource dataSource, boolean b) {
                if (photoViewAttacher != null) {
                    photoViewAttacher.update();
                }
                return false;
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
