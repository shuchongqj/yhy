package com.harwkin.nb.camera;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseFragment;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.DirConstants;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;

import org.akita.util.MessageUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class PageBigImageFragment extends BaseFragment {
    @ViewInject(R.id.pin_image)
    private PhotoView mRivImage;
    @ViewInject(R.id.img_load)
    private GifImageView mImageLoad;
    @ViewInject(R.id.rl_img_load)
    private View mImageLoadLayout;
    @ViewInject(R.id.img_gif)
    private ImageView mGifImage;
    //    private AnimationDrawable mAnimationDrawable;
    private String mData;
    //    private DisplayImageOptions mOptions;
    public static final String EXTRA_DATA = "DATA_STRING";

    private MenuPopView mMenuPopView;
    private Bitmap mbitmap = null;
    private GifDrawable mloadedImage = null;
    //GIF下载路径
    private String mGifPath = "";
    private int mMode;
    private boolean showGif = false;

    public static PageBigImageFragment getPageBigImageFragment(String str, int mode, boolean showGif) {
        PageBigImageFragment fragment = new PageBigImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SPUtils.EXTRA_DATA, str);
        bundle.putInt(SPUtils.EXTRA_MODE, mode);
        bundle.putBoolean(SPUtils.EXTRA_SHOW_GIF, showGif);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        mData = getArguments().getString(SPUtils.EXTRA_DATA);
        mMode = getArguments().getInt(SPUtils.EXTRA_MODE);
        showGif = getArguments().getBoolean(SPUtils.EXTRA_SHOW_GIF, true);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.view_page_imageview, container, false); // 加载fragment布局
//        ViewUtils.inject(this, view);
//        init();
//        return view;
//    }


    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(), R.layout.view_page_imageview, null);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ViewUtils.inject(this, view);
        setTitleBarBackground(Color.TRANSPARENT);
        init();
    }

    private boolean isNetPath(String imgPath) {
        return !TextUtils.isEmpty(imgPath)
                && imgPath.toLowerCase().contains("http");
    }

    private void init() {
//        mOptions = new DisplayImageOptions.Builder()
//                .resetViewBeforeLoading(false)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .showImageForEmptyUri(R.mipmap.icon_default_310_180)
//                .showImageOnFail(R.mipmap.icon_default_310_180)
//                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
//                .imageScaleType(ImageScaleType.EXACTLY).build();
        mImageLoad.setImageResource(R.drawable.ic_loading);
        mGifImage.setOnClickListener(v -> getActivity().finish());
        mRivImage.setOnClickListener(v -> getActivity().finish());
        mRivImage.setOnPhotoTapListener((view, x, y) -> {

        });

        mRivImage.setOnViewTapListener((view, x, y) -> {

        });
        if (!TextUtils.isEmpty(mData)) {
            if (isNetPath(CommonUtil.getImageFullUrl(mData))) {// 加载网络图片
                loadImage();
            } else {// 加载本地图片
                String str = mData.toLowerCase();
                int indexOf = str.lastIndexOf(".gif");
                if (indexOf > 0 && showGif) {
                    mImageLoadLayout.setVisibility(View.VISIBLE);
                    mRivImage.setVisibility(View.GONE);
                    mGifImage.setVisibility(View.VISIBLE);
                    mGifImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    mGifImage.setAdjustViewBounds(true);
                    Glide.with(mGifImage.getContext())
                            .asGif()
                            .load(mData)
                            .apply(requestOptions)
                            .listener(requestListener)
                            .into(mGifImage);
//                    Glide.with(mGifImage.getContext())
//                            .load(mData)
//                            .fitCenter().error(R.mipmap.icon_default_310_180)
//                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                            .placeholder(R.mipmap.icon_default_310_180)
//                            .crossFade().listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            mImageLoadLayout.setVisibility(View.GONE);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            mImageLoadLayout.setVisibility(View.GONE);
//                            return false;
//                        }
//                    }).into(mGifImage);
                } else {
                    mImageLoadLayout.setVisibility(View.GONE);
                    mRivImage.setVisibility(View.VISIBLE);
//                    ImageLoaderUtil.loadLocalImage(mRivImage, mData);
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(mData), mRivImage);
                }
            }
        }
    }

    /**
     * 显示更多菜单
     */
    private void showMoreMenuDialog() {
        new AlertDialog.Builder(getActivity()).setItems(R.array.menu_big_pic, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mbitmap == null) {
                    return;
                }
                try {
                    String fileName = System.currentTimeMillis() + ".png";
                    saveFile(mbitmap, fileName, DirConstants.DIR_PIC_ORIGIN);
//                    ImageUtils.saveToGallary(getActivity(),new File(DirConstants.DIR_PIC_ORIGIN +fileName),fileName);
                    ToastUtil.showToast(getActivity(), getString(R.string.label_toast_save_to_gallary_ok));
                } catch (IOException e) {
                    ToastUtil.showToast(getActivity(), getString(R.string.label_toast_save_to_gallary_ko));
                    e.printStackTrace();
                }
            }
        }).show();
    }

    RequestListener requestListener = new RequestListener() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object o, Target target, boolean b) {
            mImageLoadLayout.setVisibility(View.GONE);
            return false;
        }

        @Override
        public boolean onResourceReady(Object o, Object o2, Target target, DataSource dataSource, boolean b) {
            mImageLoadLayout.setVisibility(View.GONE);
            return false;
        }
    };

    RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .error(R.mipmap.icon_default_310_180)
            .placeholder(R.mipmap.icon_default_310_180);

    public void loadImage() {
        String str = mData.toLowerCase();
        int indexOf = str.lastIndexOf(".gif");
        if (indexOf > 0 && showGif) {
            //gif 图片下载列子
            mImageLoadLayout.setVisibility(View.VISIBLE);
            mRivImage.setVisibility(View.GONE);
            mGifImage.setVisibility(View.VISIBLE);
            mGifImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mGifImage.setAdjustViewBounds(true);
            Glide.with(mGifImage.getContext())
                    .asGif()
                    .load(mData)
                    .apply(requestOptions)
                    .listener(requestListener)
                    .into(mGifImage);
        } else {

            mImageLoadLayout.setVisibility(View.VISIBLE);
            mRivImage.setVisibility(View.GONE);

            Glide.with(mGifImage.getContext())
                    .load(mData)
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                            mImageLoadLayout.setVisibility(View.GONE);
                            mRivImage.setVisibility(View.VISIBLE);
                            try {
                                mRivImage
                                        .setImageResource(R.mipmap.icon_default_310_180);
                                ToastUtil.showToast(getActivity(), getString(R.string.error_network_retry));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                            // tom add
                            BitmapDrawable bd = (BitmapDrawable) drawable;
                            Bitmap loadedImage = bd.getBitmap();

                            showMore(loadedImage);
                            mImageLoadLayout.setVisibility(View.GONE);
                            mRivImage.setVisibility(View.VISIBLE);
                            mGifImage.setVisibility(View.GONE);
                            mRivImage.setImageBitmap(loadedImage);
//                            mAnimationDrawable.stop();

                            if (mbitmap != null) {
                                mRivImage.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        showMoreMenuDialog();
                                        return true;
                                    }
                                });
                            }
                            return false;
                        }
                    })
                    .into(mGifImage);
        }
    }

    private void showMoreGif(GifDrawable loadedImage) {
        mloadedImage = loadedImage;
    }

    public void PopView(View v) {
//        MenuPopView menuPopView = getMenuPopView();
//        if (null != menuPopView) {
//            menuPopView.showRightMorePop(v);
//        }

        final String[] strArray = getResources().getStringArray(R.array.save_bitmap_menu);
        MenuUtils.showAlert(getActivity(), null, strArray, null, new MenuUtils.OnAlertSelectId() {
            @Override
            public void onClick(int whichButton) {
                switch (whichButton) {
                    case 0://保存图片
                        if (mbitmap != null) {
                            try {
//                            saveFile = DirConstants.DIR_IMAGE;
                                String fileName = "img"
                                        + System.currentTimeMillis() + ".jpg";
                                saveFile(mbitmap, fileName, DirConstants.DIR_PICTURE);
                                MessageUtil.showLongToast(getActivity(),
                                        "图片保存路径 " + DirConstants.DIR_PICTURE + " 文件夹");
                            } catch (IOException e) {
                                MessageUtil.showLongToast(getActivity(),
                                        "保存失败！");
                            }
                        } else if (mloadedImage != null && !TextUtils.isEmpty(mGifPath)) {
                            try {
//                            saveFile = DirConstants.DIR_IMAGE;
                                String fileName = "img"
                                        + System.currentTimeMillis() + ".gif";
                                saveFile(mGifPath, fileName, DirConstants.DIR_PICTURE);
                                MessageUtil.showLongToast(getActivity(),
                                        "图片保存路径 " + DirConstants.DIR_PICTURE + " 文件夹");
                            } catch (IOException e) {
                                MessageUtil.showLongToast(getActivity(),
                                        "保存失败！");
                            }
                        } else {
                            MessageUtil.showLongToast(getActivity(),
                                    "保存失败！");
                        }
                        break;
                    default:
                        break;
                }
            }
        }).show();
    }

    private MenuPopView getMenuPopView() {
        if (null != mMenuPopView) {
            return mMenuPopView;
        }
        MenuMoreItem item = new MenuMoreItem();
        item.itemText = "保存到手机";
        List<MenuMoreItem> list = new ArrayList<MenuMoreItem>();
        list.add(item);
        mMenuPopView = new MenuPopView(getActivity(), list, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {


                }
                if (null != mMenuPopView) {
                    mMenuPopView.dismiss();
                }
            }
        });
        return mMenuPopView;
    }

    private void showMore(Bitmap bitmap) {
        mbitmap = bitmap;
    }

    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public void saveFile(Bitmap bm, String fileName, String saveFile) throws IOException {
        File file = new File(saveFile);
        if (!file.exists()) {
            file.mkdir();
        }
        File save = new File(saveFile + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(save));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                    save.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(save)));
    }

    /**
     * 保存GIF文件
     *
     * @param downloadPath
     * @param fileName
     * @param saveFile
     * @throws IOException
     */
    public void saveFile(String downloadPath, String fileName, String saveFile) throws IOException {
        File file = new File(saveFile);
        if (!file.exists()) {
            file.mkdir();
        }
        File save = new File(saveFile + fileName);
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(downloadPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(downloadPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(save);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
//                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
