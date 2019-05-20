package moe.xing.network.sample;


import androidx.annotation.NonNull;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Qi Xingchen on 17-2-28.
 * retrofit api
 */

public class RetrofitApi {
    public interface test {
        @SuppressWarnings("SameParameterValue")
        @FormUrlEncoded
        @POST("user/login/")
        Observable<LoginBean> login(
                @Field("username") @NonNull String username,
                @Field("password") @NonNull String password,
                @Field("branch_no") @NonNull String branch_no
        );
    }
}
