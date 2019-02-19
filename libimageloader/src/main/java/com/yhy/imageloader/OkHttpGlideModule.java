package com.yhy.imageloader;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * A {@link GlideModule} implementation to replace Glide's default
 * {@link java.net.HttpURLConnection} based {@link com.bumptech.glide.load.model.ModelLoader}
 * with an OkHttp based {@link com.bumptech.glide.load.model.ModelLoader}.
 * <p/>
 * <p> If you're using gradle, you can include this module simply by depending on the aar, the
 * module will be merged in by manifest merger. For other build systems or for more more
 * information, see {@link GlideModule}. </p>
 */

@GlideModule
public class OkHttpGlideModule extends AppGlideModule {
//    @Override
//    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
//        registry.append(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(RetrofitUtils.getOkHttpClient()));
//    }

    //    @Override
//    public void applyOptions(Context context, GlideBuilder builder) {
//        // Do nothing.
//    }
//
//    @Override
//    public void registerComponents(Context context, Glide glide) {
//        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(RetrofitUtils.getOkHttpClient()));
//    }
//
//    @Override
//    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
//        glide.re(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(RetrofitUtils.getOkHttpClient()));
//
//    }
//
//    @Override
//    public String glideName() {
//        return null;
//    }
//
//    @Override
//    public Class<? extends Annotation> annotationType() {
//        return null;
//    }
}
