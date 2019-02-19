package com.quanyan.yhy.ui.base.views.im;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

import com.quanyan.base.util.ScreenSize;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.views.MaskImage;
import com.quanyan.yhy.ui.base.views.MyPinchZoomImageView;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

public class ImImageView extends ViewSwitcher {
    private static final String TAG = ImImageView.class.getSimpleName();

    public static int DEFAULT_ERROR_DRAWABLE_RES_ID = 0;

    private String imageUrl;
    private String httpReferer;
    /**
     * remoteimageview real Width in px wrap_content (<=0)
     */
    private int imgBoxWidth = 0;
    /**
     * remoteimageview real Height in px wrap_content (<=0)
     */
    private int imgBoxHeight = 0;
    /**
     * 0: no round corner >0: round corner px size
     */
    private int roundCornerPx = 0;
    /**
     * true的时候每次都会从网络下载，并且不Cache到本地
     */
    private boolean noCache = false;
    private boolean maskShow = false;
    /**
     * if true, then use PinchZoomImageView instead.
     */
    private boolean pinchZoom;
    /**
     * fade in
     */
    private boolean fadeIn;
    /**
     * show exact progress
     */
    private boolean showProgress;

    private boolean autoLoad, isLoaded;

    /**
     * NOT USED YET
     */
    private int defaultImgRes = 0;
    /**
     * USED
     */
    private int defaultBgRes = 0;
    /**
     * NOT USED YET
     */
    private int errorImgRes = 0;
    private int errorBgRes = 0;
    private Drawable indeterminateDrawable;

    private ProgressBar loadingSpinner;
    private ImageView _imageView;

    private ImImageLoader imageLoader;
    private static ImImageLoader sharedImageLoader;


    public void setMaskeOnClick(OnClickListener clickListener) {
        if (maskShow && _imageView != null && pinchZoom == false) {
            _imageView.setOnClickListener(clickListener);
        }

    }

    public void setMaskOnLongClick(OnLongClickListener longClickListener) {
        if (maskShow && _imageView != null && pinchZoom == false) {
            _imageView.setOnLongClickListener(longClickListener);
        }
    }

    /**
     * @param context  the view's current context
     * @param imageUrl the URL of the remoteimageview to download and show
     * @param autoLoad Whether the download should start immediately after creating
     *                 the view. If set to false, use {@link #loadImage()} to
     *                 manually trigger the remoteimageview download.
     */
    public ImImageView(Context context, String imageUrl, boolean autoLoad,
                       boolean fadeIn, boolean pinchZoom, boolean showProgress,
                       boolean maskShow) {
        super(context);
        this.imageUrl = imageUrl;
        this.autoLoad = autoLoad;
        this.fadeIn = fadeIn;
        this.pinchZoom = pinchZoom;
        this.showProgress = showProgress;
        this.maskShow = maskShow;
        initialize(context, null);
    }

    /**
     * @param context          the view's current context
     * @param imageUrl         the URL of the remoteimageview to download and show
     * @param progressDrawable the drawable to be used for the
     *                         {@link ProgressBar} which is displayed while
     *                         the remoteimageview is loading
     * @param errorBgRes       the drawable to be used if a download error occurs
     * @param autoLoad         Whether the download should start immediately after creating
     *                         the view. If set to false, use {@link #loadImage()} to
     *                         manually trigger the remoteimageview download.
     */
    public ImImageView(Context context, String imageUrl,
                       Drawable progressDrawable, int errorBgRes, boolean autoLoad,
                       boolean fadeIn, boolean pinchZoom, boolean showProgress,
                       boolean maskShow) {
        super(context);
        this.imageUrl = imageUrl;
        this.indeterminateDrawable = progressDrawable;
        this.errorBgRes = errorBgRes;
        this.autoLoad = autoLoad;
        this.fadeIn = fadeIn;
        this.pinchZoom = pinchZoom;
        this.showProgress = showProgress;
        this.maskShow = maskShow;
        initialize(context, null);
    }

    public ImImageView(Context context, AttributeSet attributes) {
        super(context, attributes);
        TypedArray a = context.obtainStyledAttributes(attributes, R.styleable.ImImageView, 0, 0);
        if (a != null) {
            imageUrl = a.getString(R.styleable.ImImageView_imageUrl);
            autoLoad = a.getBoolean(R.styleable.ImImageView_autoLoad, false);
            fadeIn = a.getBoolean(R.styleable.ImImageView_fadeIn, false);
            imgBoxHeight = (int) a.getDimension(R.styleable.ImImageView_imgBoxHeight, 0.0f);
            imgBoxWidth = (int) a.getDimension(R.styleable.ImImageView_imgBoxWidth, 0.0f);
            roundCornerPx = (int) a.getDimension(R.styleable.ImImageView_roundCorner, 0.0f);
            noCache = a.getBoolean(R.styleable.ImImageView_noCache, false);
            defaultImgRes = a.getResourceId(R.styleable.ImImageView_defaultImgRes, 0);
            defaultBgRes = a.getResourceId(R.styleable.ImImageView_defaultBgRes, 0);
            errorImgRes = a.getResourceId(R.styleable.ImImageView_errorImgRes, 0);
            errorBgRes = a.getResourceId(R.styleable.ImImageView_errorBgRes, 0);
            noCache = a.getBoolean(R.styleable.ImImageView_noCache, false);
            pinchZoom = a.getBoolean(R.styleable.ImImageView_pinchZoom, false);
            showProgress = a.getBoolean(R.styleable.ImImageView_showProgress, false);
            maskShow = a.getBoolean(R.styleable.ImImageView_maskShow, true);
            int indeterminateDrawableRes = a.getResourceId(R.styleable.ImImageView_indeterminateDrawable, 0);
            if (indeterminateDrawableRes != 0) {
                indeterminateDrawable = context.getResources().getDrawable(
                        indeterminateDrawableRes);
            }
            a.recycle();
            initialize(context, attributes);
        }

    }

    private void initialize(Context context, AttributeSet attributes) {
        if (sharedImageLoader == null) {
            this.imageLoader = new ImImageLoader(context);
        } else {
            this.imageLoader = sharedImageLoader;
        }

        // ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
        // 125.0f, preferredItemHeight / 2.0f);
        // anim.setDuration(500L);

        if (fadeIn) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(500L);
            setInAnimation(anim);
        }

        addLoadingSpinnerView(context);
        addImageView(context, attributes);

        if (autoLoad && imageUrl != null) {
            loadImage();
        } else {
            // if we don't have anything to load yet, don't show the progress
            // element
            setDisplayedChild(1);
        }
    }

    private void addLoadingSpinnerView(Context context) {
        LayoutParams lp;

        if (showProgress) {
            loadingSpinner = new ProgressBar(context, null,
                    android.R.attr.progressBarStyleHorizontal);
            lp = new LayoutParams(ScreenSize.dp2px(context, 36),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
        } else {
            loadingSpinner = new ProgressBar(context);
            loadingSpinner.setIndeterminate(true);
            if (this.indeterminateDrawable == null) {
                this.indeterminateDrawable = loadingSpinner
                        .getIndeterminateDrawable();
            } else {
                loadingSpinner.setIndeterminateDrawable(indeterminateDrawable);
                if (indeterminateDrawable instanceof AnimationDrawable) {
                    ((AnimationDrawable) indeterminateDrawable).start();
                }
            }

            lp = new LayoutParams(indeterminateDrawable.getIntrinsicWidth(),
                    indeterminateDrawable.getIntrinsicHeight());
            lp.gravity = Gravity.CENTER;
        }

        addView(loadingSpinner, 0, lp);
    }

    private void addImageView(final Context context, AttributeSet attributes) {
        if (maskShow == false) {
            if (pinchZoom) { // 缩放图
                if (attributes != null) {
                    _imageView = new MyPinchZoomImageView(context, attributes);
                } else {
                    _imageView = new MyPinchZoomImageView(context);
                }
            } else {
                if (attributes != null) {
                    _imageView = new ImageView(context, attributes);
                    _imageView.setScaleType(ScaleType.CENTER);
                } else {
                    _imageView = new ImageView(context);
                }
            }
        } else {
            if (attributes != null) {
                _imageView = new MaskImage(context, attributes);

            } else {
                _imageView = new MaskImage(context);
            }
        }
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        addView(_imageView, 1, lp);
    }

    public void setScaleType(ScaleType scaleType) {
        if (_imageView != null) {
            _imageView.setScaleType(scaleType);
        }
    }

    public void setDefaultBgRes(int defaultBgRes) {
        if (imageLoader != null) {
            imageLoader.setDefaultBgRes(defaultBgRes);
        }
    }

    /**
     * Use this method to trigger the remoteimageview download if you had
     * previously set autoLoad to false.
     */
    public void loadImage() {
        if (imageUrl == null) {
            Exception e = new IllegalStateException(
                    "remoteimageview URL is null; did you forget to set it for this view?");
            Log.e(TAG, e.toString(), e);
            return;
        }
        if (showProgress) {
            loadingSpinner.setProgress(0);
            imageLoader.loadImage(imageUrl, httpReferer, noCache,
                    loadingSpinner, _imageView, defaultBgRes,
                    new DefaultImageLoaderHandler(imgBoxWidth, imgBoxHeight,
                            roundCornerPx));
            setDisplayedChild(0);
        } else {
            imageLoader.loadImage(imageUrl, httpReferer, noCache, null,
                    _imageView, defaultBgRes, new DefaultImageLoaderHandler(
                            imgBoxWidth, imgBoxHeight, roundCornerPx));
            setDisplayedChild(1);
        }
    }

    /**
     * 异步加载本地SD卡图片
     *
     * @param path
     */
    public void loadLoaclImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
//        ImageLoaderUtil.loadLocalImage(_imageView, path);
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(path), _imageView);
    }

    /**
     * reset dummy image
     */
    public void resetDummyImage() {
        _imageView.setImageResource(android.R.drawable.ic_menu_gallery);
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * set the url of remote image. use this method, then call loadImage().
     *
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * to that kind of image which must be filled with referring url
     *
     * @param httpReferer referring url
     */
    public void setHttpReferer(String httpReferer) {
        this.httpReferer = httpReferer;
    }

    /**
     * Set noCache or not
     *
     * @param noCache If true, use no cache every loading
     */
    public void setNoCache(boolean noCache) {
        this.noCache = noCache;
    }

    /**
     * Box size in px. wrap_contant: <=0 Set it to scale the remoteimageview
     * using this box
     *
     * @param imgMaxWidth
     * @param imgMaxHeight
     */
    public void setImageBoxSize(int imgMaxWidth, int imgMaxHeight) {
        this.imgBoxWidth = imgMaxWidth;
        this.imgBoxHeight = imgMaxHeight;
    }

    /**
     * Often you have resources which usually have an remoteimageview, but some
     * don't. For these cases, use this method to supply a placeholder drawable
     * which will be loaded instead of a web remoteimageview.
     * <p/>
     * Use this method to set local image.
     *
     * @param imageResourceId the resource of the placeholder remoteimageview drawable
     */
    public void setLocalImage(int imageResourceId) {
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    imageResourceId);
            _imageView.setImageBitmap(bitmap);
        } catch (OutOfMemoryError ooe) {
            Log.e(TAG, ooe.toString(), ooe);
        }

        setDisplayedChild(1);
    }

    /**
     * Often you have resources which usually have an remoteimageview, but some
     * don't. For these cases, use this method to supply a placeholder bitmap
     * which will be loaded instead of a web remoteimageview.
     * <p/>
     * Use this method to set local image.
     *
     * @param bitmap the bitmap of the placeholder remoteimageview drawable
     */
    public void setLocalImage(Bitmap bitmap) {
        _imageView.setImageBitmap(bitmap);
        setDisplayedChild(1);
    }

    @Override
    public void reset() {
        super.reset();
        this.setDisplayedChild(0);
    }

    /**
     * 一般不需要调用，会自动释放。 建议对于setImageBoxSize后的riv，在页面onDestroy时调用。
     * 注：此方法对于返回的上一页面中有相同图片的不适用，慎用
     */
    public void release() {
        if (_imageView == null)
            return;

        Drawable drawable = _imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }

        _imageView.setImageBitmap(null);
    }

    private class DefaultImageLoaderHandler extends ImImageLoaderHandler {

        public DefaultImageLoaderHandler(int imgMaxWidth, int imgMaxHeight,
                                         int roundCornerPx) {
            super(_imageView, imageUrl, errorBgRes, imgMaxWidth, imgMaxHeight,
                    roundCornerPx);
        }

        @Override
        protected boolean handleImageLoaded(Bitmap bitmap, Message msg) {
            if (onImageLoadedListener != null) {
                onImageLoadedListener.onImageLoaded(bitmap);
            }
            boolean wasUpdated = super.handleImageLoaded(bitmap, msg);
            if (wasUpdated) {
                isLoaded = true;
                setDisplayedChild(1);
            }
            return wasUpdated;
        }
    }

    /**
     * Returns the URL of the remoteimageview to show. Corresponds to the view
     * attribute ignition:imageUrl.
     *
     * @return the remoteimageview URL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Whether or not the remoteimageview should be downloaded immediately after
     * view inflation. Corresponds to the view attribute ignition:autoLoad
     * (default: true).
     *
     * @return true if auto downloading of the remoteimageview is enabled
     */
    public boolean isAutoLoad() {
        return autoLoad;
    }

    /**
     * The remoteimageview view that will render the downloaded remoteimageview.
     *
     * @return the {@link ImageView}
     */
    public ImageView getImageView() {
        return _imageView;
    }

    /**
     * The progress bar that is shown while the remoteimageview is loaded.
     *
     * @return the {@link ProgressBar}
     */
    public ProgressBar getProgressBar() {
        return loadingSpinner;
    }

    /**
     * 图片加载完成时，可以监听到，从而拿到图片的信息，譬如大小宽高等。
     */
    private OnImageLoadedListener onImageLoadedListener;

    public void setOnLoadOverListener(
            OnImageLoadedListener onImageLoadedListener) {
        this.onImageLoadedListener = onImageLoadedListener;
    }

    public interface OnImageLoadedListener {
        void onImageLoaded(Bitmap bitmap);
    }

    @Override
    public void setOnClickListener(final OnClickListener listener) {
        if (_imageView != null) {
            _imageView.setOnClickListener(listener);
        }
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener listener) {
        // TODO Auto-generated method stub
        if (_imageView != null) {
            _imageView.setOnLongClickListener(listener);
        }
    }
}
