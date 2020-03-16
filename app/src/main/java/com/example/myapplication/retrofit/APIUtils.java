package com.example.myapplication.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class APIUtils {
    public static final String baseurl = "http://192.168.1.27:80/cham-cong/api/";//http://10.0.0.216:8888/cit/
    //http://10.0.0.216:8888/cham-cong/api/
    //nhà // 192.168.1.5
    public static DataClient getdata() {
        return RetrofitClient.getClient(baseurl).create(DataClient.class);
    }
}
