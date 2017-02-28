package moe.xing.network.sample;

import com.google.gson.annotations.SerializedName;

import moe.xing.network.BaseBean;

/**
 * Created by Qi Xingchen on 16-12-9.
 */

public class LoginBean extends BaseBean {

    @SerializedName("data")
    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {

    }
}
