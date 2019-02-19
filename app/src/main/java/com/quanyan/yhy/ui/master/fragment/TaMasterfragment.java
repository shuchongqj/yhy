package com.quanyan.yhy.ui.master.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.lidroid.xutils.ViewUtils;
import com.quanyan.base.BaseFragment;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.CommonUrl;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.comment.ValueCommentType;
import com.quanyan.yhy.ui.common.WebViewActivity;
import com.quanyan.yhy.view.NetWorkErrorView;
import com.yhy.common.beans.net.model.common.ComIconInfo;
import com.yhy.common.beans.net.model.common.ComIconList;
import com.yhy.common.beans.net.model.master.MasterCertificates;
import com.yhy.common.beans.net.model.master.TalentInfo;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.service.IUserService;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 达人主页====TA的主页
 * Created by wujm on 2016-3-4.
 */
public class TaMasterfragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private NestedScrollView error_view_contain;
    private NetWorkErrorView error_view;

    private NestedScrollView view_contain;
    private long MasterHomepageID;
    private RecyclerAdapter mAdapter2;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView person_info_club_list;
    public WebView sa_open_html;
    private List<ComIconInfo> mAllIconList;
    private RelativeLayout rlAuthentication;
    private View view = null;
    public static final int PEFECT_RESULT = 9;

    @Autowired
    IUserService userService;
    @Deprecated
    public TaMasterfragment() {
    }

    public static TaMasterfragment createTaMasrerfragment(long MasrerId, TalentInfo value) {
        TaMasterfragment fragment = new TaMasterfragment();
        Bundle bundle = new Bundle();
        bundle.putLong(SPUtils.EXTRA_ID, MasrerId);
        bundle.putSerializable(SPUtils.KEY_PROBATE, value);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(), R.layout.fagment_tamaster, null);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ViewUtils.inject(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            MasterHomepageID = bundle.getLong(SPUtils.EXTRA_ID, -1);
            talentInfo = (TalentInfo) bundle.getSerializable(SPUtils.KEY_PROBATE);

        }
        addFooterApproveView(view);
    }

    /**
     * 信息认证
     */
    @SuppressLint("JavascriptInterface")
    private void addFooterApproveView(View view) {

        try {
            String comIcons = SPUtils.getComIcons(getContext());
            ComIconList comIconList = ComIconList.deserialize(comIcons);
            if (comIconList != null) {
                mAllIconList = comIconList.comIconInfoList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        view_contain = view.findViewById(R.id.id_stickynavlayout_innerscrollview);//errorView

        error_view_contain = view.findViewById(R.id.error_view_contain);//errorView
        error_view = view.findViewById(R.id.error_view);//errorView

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        person_info_club_list = (RecyclerView) view.findViewById(R.id.person_info_club_list);
        sa_open_html = (WebView) view.findViewById(R.id.sa_open_html);
        rlAuthentication = (RelativeLayout) view.findViewById(R.id.home_listview_rl_vip2);
        person_info_club_list.setLayoutManager(mLinearLayoutManager);
        mAdapter2 = new RecyclerAdapter();
        person_info_club_list.setAdapter(mAdapter2);
        if (talentInfo != null) {
            mAdapter2.setClubListBeans(talentInfo.certificates, talentInfo.certificateType);
        }
        WebSettings settings = sa_open_html.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setAllowFileAccess(false);
        settings.setDomStorageEnabled(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB) {
            settings.setDisplayZoomControls(false);
        }
        // 添加js交互接口类，并起别名 imagelistner
        sa_open_html.addJavascriptInterface(new JavascriptInterface(getActivity()), "imagelistner");
        // 不使用缓存，只从网络获取数据
//        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        sa_open_html.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                mHandler.sendEmptyMessageDelayed(MSG_HOOK_IMAGE_CLICK,100);
                hideLoadingView();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mHandler.sendEmptyMessageDelayed(WebViewActivity.MSG_SHOW_ERROR_PAGE, 100);

            }
        });
        againLoadUrl(talentInfo.isHasMainPage);
    }

    public void againLoadUrl(boolean isLoadUrl) {
//        System.out.println("wjm====" + isLoadUrl);
        hideErrorView(null);
        hideLoadingView();
        //判断是否有主营业
        if (isLoadUrl) {
            //是否有连接
            if (MasterHomepageID > 0 && SPUtils.getShareDefaultUrl(getActivity(), CommonUrl.KEY_EXPERT) != null) {
//                System.out.println("wjm====" + SPUtils.getShareDefaultUrl(getActivity(), CommonUrl.KEY_EXPERT) + MasterHomepageID);
                view_contain.setVisibility(View.VISIBLE);
                error_view_contain.setVisibility(View.GONE);
                sa_open_html.loadUrl(SPUtils.getShareDefaultUrl(getActivity(), CommonUrl.KEY_EXPERT) + MasterHomepageID);
            } else {
                if (talentInfo == null) {
                    return;
                }
                if (talentInfo.certificates == null || talentInfo.certificates.size() == 0) {
                    showNoDataPage();
                }
            }
        } else {
            if (talentInfo == null) {
                return;
            }
            if (talentInfo.certificates == null || talentInfo.certificates.size() == 0) {
                showNoDataPage();
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /***
     * 销毁前保存状态
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public TalentInfo talentInfo;


    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        hideErrorView(null);
        switch (msg.what) {
            case WebViewActivity.MSG_SHOW_ERROR_PAGE:
                if (talentInfo.certificates == null || talentInfo.certificates.size() == 0) {
                    showNoDataPage();
                }
                break;
//            case MSG_HOOK_IMAGE_CLICK:
//                // html加载完成之后，添加监听图片的点击js函数
//                addImageClickListner();
//                HarwkinLogUtil.info("addImageClickListner");
//                break;
        }
    }

    private void showNoDataPage() {
        if (getActivity() == null) {
            return;
        }

        view_contain.setVisibility(View.GONE);
        error_view_contain.setVisibility(View.VISIBLE);

        if (MasterHomepageID > 0 && userService.getLoginUserId() == MasterHomepageID) {
            error_view.show(getString(R.string.label_hint_tamaster_title_nothing),
                    getString(R.string.label_hint_tamaster_content_nothing), getString(R.string.label_hint_evaluate_nothing_godata), new ErrorViewClick() {
                        @Override
                        public void onClick(View view) {
                            NavUtils.gotoPictureAndTextActivity(getActivity(), ValueCommentType.PIC_AND_TEXT_EXPERTHOME, null, null, PEFECT_RESULT, -1);


                        }
                    });
//            showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_hint_tamaster_title_nothing),
//                    getString(R.string.label_hint_tamaster_content_nothing), getString(R.string.label_hint_evaluate_nothing_godata), new ErrorViewClick() {
//                        @Override
//                        public void onClick(View view) {
//                            NavUtils.gotoPictureAndTextActivity(getActivity(), ValueCommentType.PIC_AND_TEXT_EXPERTHOME, null, null, PEFECT_RESULT, -1);
//
//
//                        }
//                    });
            //setButtonColorRed();
        } else {
            error_view.show(getString(R.string.label_hint_disc_nothing),
                    " ", "", null);
//            showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_hint_disc_nothing),
//                    " ", "", null);

        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    public String getIconUrl(int code, int type) {
        if (mAllIconList == null) {
            return null;
        }
        for (ComIconInfo bean : mAllIconList) {
            if (bean.code == code && bean.type == type) {
                return bean.icon;
            }
        }
        return null;
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<MasterCertificates> mClubListBeans = new ArrayList<>();
        int certificateType;

        public RecyclerAdapter() {
        }

        public void setClubListBeans(List<MasterCertificates> beans, int certificateType) {
            if (beans != null && beans.size() > 0) {
                this.mClubListBeans = beans;
                this.certificateType = certificateType;
            } else {
                rlAuthentication.setVisibility(View.GONE);
                person_info_club_list.setVisibility(View.GONE);
                mClubListBeans.clear();
            }

            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_master_about, null);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            MasterCertificates kaClub = mClubListBeans.get(position);
            if (kaClub != null) {
                String imgUrl = getIconUrl(kaClub.id, certificateType);
                if (imgUrl != null) {
//                    BaseImgView.loadimg(holder.mImageView,
//                            imgUrl,
//                            R.mipmap.icon_default_128_128,
//                            R.mipmap.icon_default_128_128,
//                            R.mipmap.icon_default_128_128,
//                            ImageScaleType.EXACTLY,
//                            -1,
//                            -1,
//                            -1);

                    ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(imgUrl), R.mipmap.icon_default_128_128, holder.mImageView);

                }
                if (!StringUtil.isEmpty(kaClub.name)) {
                    holder.mTextView.setText(kaClub.name);
                } else {
                    holder.mTextView.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return mClubListBeans.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView;
        public ImageView iv_minster_tag;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.cell_person_info_club_name);
            mImageView = (ImageView) itemView.findViewById(R.id.cell_person_info_club_head);
            iv_minster_tag = (ImageView) itemView.findViewById(R.id.iv_minster_tag);
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            Message msg = new Message();
            Bundle data = new Bundle();

//
            data.putBoolean("value", checkURL(SPUtils.getShareDefaultUrl(getActivity(), CommonUrl.KEY_EXPERT) + MasterHomepageID));
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();

            if (data.getBoolean("value")) {
                sa_open_html.loadUrl(SPUtils.getShareDefaultUrl(getActivity(), CommonUrl.KEY_EXPERT) + MasterHomepageID);
            } else {
                mHandler.sendEmptyMessageDelayed(WebViewActivity.MSG_SHOW_ERROR_PAGE, 100);
            }
        }
    };

    public boolean checkURL(String url) {
        boolean value = false;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            int code = conn.getResponseCode();
            if (code != 200) {
                value = false;
            } else {
                value = true;
            }
        } catch (Exception e) {
            value = false;
            e.printStackTrace();
        }
        return value;
    }

//    private final static int MSG_HOOK_IMAGE_CLICK = 0x2200;
//    // 注入js函数监听
//    private void addImageClickListner() {
//        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
//        sa_open_html.loadUrl("javascript:(function(){" +
//                "var objs = document.getElementsByTagName(\"img\"); " +
//                "for(var i=0;i<objs.length;i++)  " +
//                "{"
//                + "    objs[i].onclick=function()  " +
//                "    {  "
//                + "        window.imagelistner.openImage(this.src);  " +
//                "    }  " +
//                "}" +
//                "})()");
//    }

    // js通信接口
    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

//        public void openImage(String img) {
//            if(StringUtil.isEmpty(img)){
//                return ;
//            }
//            HarwkinLogUtil.info(img);
//            ArrayList<String> pics = new ArrayList<String>();
//            pics.add(img);
//            NavUtils.gotoLookBigImage(context, pics, 1);
//        }

        public void openImage(String[] img, int pos) {
            if (img == null) {
                return;
            }
            ArrayList<String> pics = new ArrayList<String>();
            for (String str : img) {
                pics.add(str);
            }
            HarwkinLogUtil.info(pics.toArray().toString());
            NavUtils.gotoLookBigImage(context, pics, pos);
        }
    }
}
