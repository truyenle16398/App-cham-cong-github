package com.example.myapplication.network;

import com.example.myapplication.network.response.DiaryAttendanceResponse;
import com.example.myapplication.network.response.InfoResponse;
import com.example.myapplication.ui.model.User;
import com.example.myapplication.ui.model.info;
import com.example.myapplication.ui.model.itemah;

import java.util.List;

import io.reactivex.Completable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import io.reactivex.Observable;



public interface ApiService {

    @GET("show-info")//ct/
    Observable<InfoResponse> getinfo();//@Query("id") String id
    //lich su
    @GET("diary-attendance")//ct/
    Observable<List<DiaryAttendanceResponse>> diaryattendance();//@Path("id") String id


    @PUT("update-info")
    @FormUrlEncoded
    Completable updateinfo(@Field("name") String name,
                           @Field("email") String email);

    @Headers({"Accept: application/json"
            //  , "Content-Type : application/json"
    })

    @POST("login")
    @FormUrlEncoded
    Call<User> login(@Field("email") String email,
                     @Field("password") String password);

    @PUT("updatepass/{id}")
    @FormUrlEncoded
    Call<String> updatepass(@Path("id") String id,
                            @Field("oldpass") String oldpass,
                            @Field("newpass") String newpass);

    @POST("/cit/logout.php")
    @FormUrlEncoded
    Call<String> logout(@Field("id") String id,
                        @Field("token") String token);
}
