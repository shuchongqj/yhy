package com.yhy.webview.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.yhy.webview.R;
import com.yhy.webview.adapter.ImageBrowserAdapter;
import com.yhy.webview.utils.ImageLoaderUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-03-06.
 */

public class ImageFromWebView extends Activity implements View.OnClickListener{

    private ViewPager vpImageBrowser;
    private TextView tvImageIndex;
    private Button btnSave;

    private ImageBrowserAdapter adapter;
    private ArrayList<String> imgUrls;
    private int currentIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_preview);

        vpImageBrowser = (ViewPager) findViewById(R.id.vp_image_browser);
        tvImageIndex = (TextView) findViewById(R.id.tv_image_index);
        btnSave = (Button) findViewById(R.id.btn_save);

        btnSave.setOnClickListener(this);
        imgUrls = getIntent().getStringArrayListExtra("urls");
        adapter = new ImageBrowserAdapter(this, imgUrls);
        vpImageBrowser.setAdapter(adapter);
        final int size = imgUrls.size();

        if (size > 1) {
            tvImageIndex.setVisibility(View.VISIBLE);
            tvImageIndex.setText((1) + "/" + size);
        } else {
            tvImageIndex.setVisibility(View.GONE);
        }

        vpImageBrowser.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                currentIndex = arg0;
                int index = arg0 % size;
                tvImageIndex.setText((index + 1) + "/" + size);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        vpImageBrowser.setCurrentItem(0);
    }

    @Override
    public void onClick(View view) {
        ImageLoaderUtils.downLoadImage(imgUrls.get(currentIndex), this);
    }
}
