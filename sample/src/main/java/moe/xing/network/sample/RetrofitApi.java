package moe.xing.network.sample;

import android.support.annotation.NonNull;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Qi Xingchen on 17-2-28.
 */

public class RetrofitApi {
    public interface test {
        @FormUrlEncoded
        @POST("user/login/")
        Observable<LoginBean> login(
                @Field("username") @NonNull String username,
                @Field("password") @NonNull String password
        );
    }
}
