package com.newyhy.fragment.live;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.newyhy.activity.HorizontalLiveActivity;
import com.newyhy.views.dialog.InputMsgDialog;
import com.quanyan.yhy.R;

import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.club.clubcontroller.ClubController;
import com.videolibrary.adapter.ChatListAdapter;
import com.videolibrary.chat.entity.LiveChatNotifyMessage;
import com.yhy.common.base.BaseNewFragment;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.eventbus.event.EvBusVideoPraiseChange;
import com.yhy.common.listener.OnMultiClickListener;
import com.yhy.common.utils.SPUtils;
import com.yhy.location.LocationManager;
import com.yhy.service.IUserService;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class LiveChatListFragment extends BaseNewFragment {

    private InputMsgDialog inputMsgDialog;
    private int mLastDiff;

    private RecyclerView rv_live_chart;
    private ChatListAdapter v_live_client_adapter;
    private ImageView iv_zan;
    private boolean isSupport;
    private long ugcId;
    private ClubController mClubController;

    @Autowired
    IUserService userService;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_live_chat;
    }

    @Override
    public void handleMessage(Message msg) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout et_comment  = view.findViewById(R.id.et_comment);
        mClubController = new ClubController(getFragmentContext(), mHandler);
        et_comment.setOnClickListener(v -> {
            if (inputMsgDialog == null){
                inputMsgDialog = new InputMsgDialog(getFragmentContext(),R.style.Theme_Light_Dialog);
                inputMsgDialog.setOnShowListener(dialog ->
                        mHandler.postDelayed(() -> inputMsgDialog.showSoftInput(),100)
                        );
                inputMsgDialog.setSendMsgClickCallBack(text -> {
                    HorizontalLiveActivity activity = (HorizontalLiveActivity) getActivity();
                    if (activity != null){
                        activity.sendMessage(text);
                    }
                });
                inputMsgDialog.show();

            }else {
                if (!inputMsgDialog.isShowing()){
                    inputMsgDialog.show();
                }
            }
        });
        iv_zan = view.findViewById(R.id.iv_zan);
        iv_zan.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Analysis.pushEvent(mActivity, AnEvent.PageFollew,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setId(String.valueOf(ugcId)).
                                setType("直播").
                                setList(false));

                if (userService.isLogin()) {
                    String outType = ValueConstants.TYPE_PRAISE_LIVESUP;
                    mClubController.doAddNewPraiseToComment(getContext(), ugcId, outType, isSupport ? ValueConstants.UNPRAISE : ValueConstants.PRAISE);
                    isSupport = !isSupport;
                    iv_zan.setImageResource(isSupport ? R.drawable.ic_zaned : R.drawable.ic_un_zan);
                    EventBus.getDefault().post(new EvBusVideoPraiseChange(ugcId,0,isSupport));
                } else {
                    NavUtils.gotoLoginActivity(getContext());
                }
            }
        });

        et_comment.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            //获取当前界面可视部分
            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            //获取屏幕的高度
            int screenHeight =  getActivity().getWindow().getDecorView().getRootView().getHeight();
            //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
            int heightDifference = screenHeight - r.bottom;

            if(heightDifference <= 0 && mLastDiff > 0){
                //软键盘收起状态
                if(inputMsgDialog!=null && inputMsgDialog.isShowing()){
                    inputMsgDialog.dismiss();
                }
            }
            mLastDiff = heightDifference;

        });

        rv_live_chart = view.findViewById(R.id.rv_live_chart);
        LinearLayoutManager manager = new LinearLayoutManager(getFragmentContext(),LinearLayoutManager.VERTICAL,false);
        rv_live_chart.setLayoutManager(manager);
        v_live_client_adapter = new ChatListAdapter(getFragmentContext(),new ArrayList());
        rv_live_chart.setAdapter(v_live_client_adapter);

    }

    public void setZanStatus(boolean isSupport,long ugcId){
        this.isSupport = isSupport;
        this.ugcId = ugcId;
        iv_zan.setImageResource(isSupport ? R.drawable.ic_zaned : R.drawable.ic_un_zan);
    }

    /**
     * 添加直播公告
     * @param affiche
     */
    public void addAffiche(String affiche){
        v_live_client_adapter.addAffiche(affiche);
    }

    public void addMessage(Object message){
        v_live_client_adapter.addMessage(message);
        rv_live_chart.scrollToPosition(v_live_client_adapter.getItemCount()-1);
    }

    public Context getFragmentContext(){
        if(getActivity() == null){
            return YHYBaseApplication.getInstance();
        }
        return getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
