package com.newyhy.views.ninelayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.harwkin.nb.camera.ImageUtils;
import com.newyhy.views.RoundImageView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.location.LocationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Yhy定制九宫格
 * Created by user on 2018/4/19.
 */

public class YHYNineGridLayout extends NineGridLayout {

    public long id;
    public String page;

    public YHYNineGridLayout(Context context) {
        super(context);
    }

    public YHYNineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("CheckResult")
    @Override
    protected boolean displayOneImage(RoundImageView imageView, String url, int parentWidth) {
        return false;
    }

    @Override
    protected void displayImage(RoundImageView imageView, String url) {
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(url), R.mipmap.icon_default_215_215, imageView, 3, true);
    }

    @Override
    protected void onClickImage(int position, String url, List<String> urlList) {
        ArrayList<String> tmpList = new ArrayList<>();
        for (String str : urlList) {
            tmpList.add(ImageUtils.getImageFullUrl(str));
        }
        NavUtils.gotoLookBigImage(mContext, tmpList, position);

        // 埋点
        Analysis.pushEvent(getContext(), AnEvent.Pic,
                new Analysis.Builder().
                        setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                        setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                        setId(String.valueOf(id)).
                        setType("动态").
                        setPage(page));

    }
}
