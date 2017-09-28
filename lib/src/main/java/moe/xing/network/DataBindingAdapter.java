package moe.xing.network;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.net.URI;
import java.util.List;

import moe.xing.baseutils.network.cookies.MyCookiesManager;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Qi Xingchen on 2017/7/27.
 * glide DataBindingAdapter
 */

public class DataBindingAdapter {

    /**
     * 从网络加载图片
     */
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url) {
        if (TextUtils.isEmpty(url)) {
            view.setImageDrawable(null);
            return;
        }
        List<Cookie> cookies = new MyCookiesManager().loadForRequest(HttpUrl.get(URI.create(url)));
        StringBuilder cookieString = new StringBuilder();
        for (Cookie cookie : cookies) {
            cookieString.append(cookie.name()).append("=").append(cookie.value()).append(";");
        }

        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("User-Agent", RetrofitNetwork.UA())
                .addHeader("Cookie", cookieString.toString())
                .build());
        try {
            Glide.with(view.getContext()).load(glideUrl)
                    .apply(new RequestOptions().centerCrop()).into(view);
        } catch (IllegalArgumentException ignore) {
        }
    }

    /**
     * 加载资源图片
     */
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, int url) {
        if (url == 0) {
            view.setImageDrawable(null);
            return;
        }
        try {
            Glide.with(view.getContext()).load(url).into(view);
        } catch (IllegalArgumentException ignore) {
        }
    }

    /**
     * 从文件加载图片
     */
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, File url) {
        if (url == null || !url.exists()) {
            view.setImageDrawable(null);
            return;
        }
        try {
            Glide.with(view.getContext()).load(url).into(view);
        } catch (IllegalArgumentException ignore) {
        }
    }
}
