package moe.xing.network.sample;


import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import moe.xing.baseutils.Init;
import moe.xing.baseutils.utils.LogHelper;
import moe.xing.network.BaseBean;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Qi Xingchen on 16-12-1.
 * <p>
 * 网络
 */

public class RetrofitNetwork extends moe.xing.network.RetrofitNetwork {
    private static RetrofitNetwork mInstance;

    private RetrofitNetwork() {

        ArrayList<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(new SampleInterceptor());

        retrofit = new Retrofit.Builder()
                //.callbackExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .client(okHttpClient(new ArrayList<Interceptor>(), interceptors).build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://crm.yunyuer.com/scy/api/")
                .build();
    }

    /**
     * 获取实例(并不线程安全,也不保证绝对单例,因为并不重要)
     */
    public static RetrofitNetwork getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitNetwork();
        }
        return mInstance;
    }

    /**
     * 网络结果转换器
     * 调度线程并判断 API 结果是否成功
     *
     * @see #sOperator()
     */
    @NonNull
    public static <T extends BaseBean> Observable.Transformer<T, T> preHandle() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> responseObservable) {
                return responseObservable.lift(RetrofitNetwork.<T>sOperator())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 网络结果转换器
     * 判断 API 结果是否成功
     * 失败的操作调用 {@link Subscriber#onError(Throwable)} 并传递失败原因
     * 成功的操作调用 {@link Subscriber#onNext(Object)} 传递结果
     */
    @NonNull
    public static <T extends BaseBean> Observable.Operator<T, T> sOperator() {
        return new Observable.Operator<T, T>() {
            @Override
            public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
                return new Subscriber<T>() {
                    @Override
                    public void onCompleted() {
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (Init.isDebug()) {
                            subscriber.onError(e);
                        } else {
                            subscriber.onError(new Throwable("网络错误,请检查网络"));
                        }
                        LogHelper.e(e);
                    }

                    @Override
                    public void onNext(T baseBean) {
                        if ("1".equals(baseBean.getRet())) {
                            subscriber.onNext(baseBean);
                        } else {
                            Throwable t = new Throwable(baseBean.getErrMsg());

                            subscriber.onError(t);
                        }
                    }
                };
            }
        };
    }


    /**
     * 插入UA 的 Interceptor
     */
    private static class SampleInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            String defaultUA = chain.request().headers().get("User-Agent");

            builder.removeHeader("User-Agent");
            builder.addHeader("User-Agent", defaultUA + UA());

            // TODO: 2016/5/17 0017 gzip is close!
            builder.removeHeader("Accept-Encoding");
            builder.addHeader("Accept-Encoding", "identity");

            return chain.proceed(builder.build());
        }
    }

}
