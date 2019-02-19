package com.harwkin.nb.camera.pop;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.harwkin.nb.camera.CameraHandler;
import com.harwkin.nb.camera.CropBuilder;
import com.harwkin.nb.camera.callback.CameraImageListener;
import com.harwkin.nb.camera.callback.SelectMoreListener;
import com.harwkin.nb.camera.options.CameraOptions;
import com.harwkin.nb.camera.type.OpenType;
import com.quanyan.yhy.R;
import com.quanyan.yhy.database.DBManager;
import com.quanyan.yhy.ui.base.utils.SDCardUtil;


/**
 * Custom PopWindow
 *
 * @author zhaocheng
 */
public class CameraPop extends PopupWindow implements OnClickListener,
        OnTouchListener {
    private LinearLayout ll_show_del;
    private TextView btn_take_photo, btn_pick_photo, btn_cancel, btn_del_photo, btn_delete_pic;
    private View mMenuView;
    private Context mContext;
    private Fragment mFragment;
    private CameraPopListener mPopListern;
    private CameraHandler mCameraHandler;
    private int clickId;
    private View clickView;
    private LinearLayout mLLTakeVideo;
    private LinearLayout mLLTakeVideoDraft;
    private CancelListener listener;

    public CameraPop(Fragment fragment, CameraPopListener listener,
                     CameraImageListener imageListener) {
        super(null == fragment ? null : fragment.getActivity());
        initFragmentContext(fragment, listener);
        init();
        mCameraHandler = new CameraHandler(fragment, imageListener);
    }

    public CameraPop(Fragment fragment, CameraPopListener listener,
                     SelectMoreListener moreListener) {
        super(null == fragment ? null : fragment.getActivity());
        initFragmentContext(fragment, listener);
        init();
        mCameraHandler = new CameraHandler(fragment, moreListener);
    }

    public CameraPop(Context context, CameraPopListener listener,
                     CameraImageListener imageListener) {
        super(context);
        initContext(context, listener);
        init();
        mCameraHandler = new CameraHandler(context, imageListener);
    }

    public CameraPop(Context context, CameraPopListener listener,
                     SelectMoreListener moreListener) {
        super(context);
        initContext(context, listener);
        init();
        mCameraHandler = new CameraHandler(context, moreListener);
    }

    public CameraPop(Context context, CameraPopListener listener,
                     CameraImageListener imageListener, OpenType openType) {
        super(context);
        initContext(context, listener);
        init();
        mCameraHandler = new CameraHandler(context, imageListener);
        CameraOptions options = mCameraHandler.getOptions();
        options.setOpenType(openType);
    }

    public void setDel(boolean isDel) {
        if (isDel)
            ll_show_del.setVisibility(View.VISIBLE);
        else
            ll_show_del.setVisibility(View.GONE);
    }


    private void initFragmentContext(Fragment fragment, CameraPopListener listener) {
        this.mContext = null == fragment ? null : fragment.getActivity();
        mFragment = fragment;
        this.mPopListern = listener;
    }


    private void initContext(Context context, CameraPopListener listener) {
        this.mContext = context;
        this.mPopListern = listener;
    }


    public void showOnlyDelView(View view) {
        if (!isShowing()) {
            if (SDCardUtil.checkSDCard()) {
                setDel(true);
                btn_take_photo.setVisibility(View.GONE);
                btn_pick_photo.setVisibility(View.GONE);
                buildMenu(view);
            } else {
                // MessageUtil.showShortToast(mContext, mContext.getResources()
                // .getString(R.string.no_sdcard));
            }
        }
    }

    OnClickListener popItemClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_take_photo) {
                mPopListern.onCamreaClick(mCameraHandler.getOptions());
            } else if (id == R.id.btn_pick_photo) {
                mPopListern.onPickClick(mCameraHandler.getOptions());
            } else if (id == R.id.btn_del_photo) {
                mPopListern.onDelClick();
            } else if (id == R.id.ll_take_video) {
                mPopListern.onVideoClick();
            } /*else if (id == R.id.ll_take_video_draft) {
                mPopListern.onVideoDraftClick();
            }*/ else if (id == R.id.btn_delete_pic) {
                mPopListern.onDelClick();
            }
            dismiss();
        }

    };

    /**
     * 视频和视频操作
     *
     * @param video
     * @param photo
     */
    public void showMenuList(boolean video, boolean photo) {
        mLLTakeVideo.setVisibility(video ? View.VISIBLE : View.GONE);
//        mLLTakeVideoDraft.setVisibility(video ? View.VISIBLE : View.GONE);

        btn_take_photo.setVisibility(photo ? View.VISIBLE : View.GONE);
        btn_pick_photo.setVisibility(photo ? View.VISIBLE : View.GONE);
        //有视频草稿就显示
        if (!video || DBManager.getInstance(mContext).doGetVideoList().size() == 0) {
            mLLTakeVideoDraft.setVisibility(View.GONE);
        } else {
            mLLTakeVideoDraft.setVisibility(View.VISIBLE);
        }
    }

    public LinearLayout getViewdel() {
        return ll_show_del;
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.alert_dialog, null);
        ll_show_del = (LinearLayout) mMenuView.findViewById(R.id.ll_show_del);
        ll_show_del.setVisibility(View.GONE);
        btn_del_photo = (TextView) mMenuView.findViewById(R.id.btn_del_photo);
        btn_take_photo = (TextView) mMenuView.findViewById(R.id.btn_take_photo);
        btn_pick_photo = (TextView) mMenuView.findViewById(R.id.btn_pick_photo);
        btn_delete_pic = (TextView) mMenuView.findViewById(R.id.btn_delete_pic);
        btn_cancel = (TextView) mMenuView.findViewById(R.id.btn_cancel);

        mLLTakeVideo = (LinearLayout) mMenuView.findViewById(R.id.ll_take_video);
        mLLTakeVideoDraft = (LinearLayout) mMenuView.findViewById(R.id.ll_take_video_draft);
        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PopupAnimation);
        ColorDrawable dw = new ColorDrawable(mContext.getResources().getColor(
                R.color.transparent_50));
        this.setBackgroundDrawable(dw);

        btn_cancel.setOnClickListener(this);
        btn_pick_photo.setOnClickListener(popItemClick);
        btn_take_photo.setOnClickListener(popItemClick);
        btn_del_photo.setOnClickListener(popItemClick);
        mLLTakeVideo.setOnClickListener(popItemClick);
        mLLTakeVideoDraft.setOnClickListener(popItemClick);
        mMenuView.setOnTouchListener(this);
        btn_delete_pic.setOnClickListener(popItemClick);
    }

    public void showMenu(View view, int clickId) {
        this.clickId = clickId;
        showMenu(view);
    }

    /**
     * 弹出选择菜单
     */
    public void showMenu(View view) {
        this.clickView = view;

        if (!isShowing()) {
            if (SDCardUtil.checkSDCard()) {
                buildMenu(view);
            } else {
                // MessageUtil.showShortToast(mContext, mContext.getResources()
                // .getString(R.string.no_sdcard));
            }
        }
    }

    public void showMenu(View view, String pic, String delete) {
        btn_pick_photo.setText(pic);
        btn_take_photo.setVisibility(View.GONE);
        btn_delete_pic.setVisibility(View.VISIBLE);
        btn_delete_pic.setText(delete);

        showMenu(view);
    }

    private void buildMenu(View view) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘

        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                0, 0); // 设置layout在PopupWindow中显示的位置
    }

    public void setCropBuilder(CropBuilder builder) {
        mCameraHandler.getOptions().setCropBuilder(builder);
    }

    public void setListener(CancelListener listener) {
        this.listener = listener;
    }

    public void process() {
        try {
            mCameraHandler.process();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void processWithMedia() {
        try {
            mCameraHandler.processWithMedia();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void rebuildUI() {
        btn_pick_photo.setText(mContext.getText(R.string.select_from_gallery));
        btn_take_photo.setVisibility(View.VISIBLE);
        btn_delete_pic.setVisibility(View.GONE);
    }

    public View getClickView() {
        return clickView;
    }

    public void setClickView(View clickView) {
        this.clickView = clickView;
    }


    public int getClickId() {
        return clickId;
    }

    public void setClickId(int clickId) {
        this.clickId = clickId;
    }

    public void forResult(int requestCode, int resultCode, Intent data) {
        mCameraHandler.forResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onCancel();
        }
        dismiss();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int height = mMenuView.findViewById(R.id.pop_layout).getTop();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (y < height) {
                if (listener != null) {
                    listener.onCancel();
                }
                dismiss();
            }
        }
        return true;

    }

    public interface CancelListener {
        void onCancel();
    }
}
