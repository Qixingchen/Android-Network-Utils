package moe.xing.network.sample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import moe.xing.baseutils.Init;
import rx.Subscriber;
import rx.observers.SafeSubscriber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init.init(getApplication(), BuildConfig.DEBUG, BuildConfig.VERSION_NAME, "network sample");


        RetrofitNetwork.getInstance().retrofit.create(RetrofitApi.test.class)
                .login("2333", "2333", "2333").compose(RetrofitNetwork.<LoginBean>preHandle())
                .subscribe(new SafeSubscriber<>(new Subscriber<LoginBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onError(Throwable e) {
                        ((TextView) findViewById(R.id.text)).setText("login error" + e.getLocalizedMessage());
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(LoginBean loginBean) {
                        ((TextView) findViewById(R.id.text)).setText("login successful");
                    }
                }));
    }
}
