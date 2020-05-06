package com.example.myapplication.network;

import com.example.myapplication.network.response.CheckOutResponse;
import com.example.myapplication.network.response.DiaryAttendanceResponse;
import com.example.myapplication.network.response.InfoResponse;
import com.example.myapplication.network.response.ListDateReportResponse;
import com.example.myapplication.network.response.ListRegisterForLeaveResponse;
import com.example.myapplication.network.response.ListReportResponse;
import com.example.myapplication.network.response.MessageResponse;
import com.example.myapplication.network.response.TypeResponse;
import com.example.myapplication.network.response.VacationResponse;
import com.example.myapplication.ui.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {

    @GET("show-info")
    Observable<InfoResponse> getinfo();
    //lich su
    @GET("diary-attendance")//ct/
    Observable<List<DiaryAttendanceResponse>> diaryattendance();

    //Danh sách đăng kí nghỉ phép
    @GET("show-sabbatical")//show-sabbatical //  list_register_for_leave
    Observable<List<ListRegisterForLeaveResponse>> listRegisterForLeave();

    //bao cao dang danh sach
    @GET("report-list")//show-sabbatical //  report
    Observable<List<ListReportResponse>> listReport();
    //bao cao dang lich
    @GET("report-calendar")//show-sabbatical //  reports
    Observable<List<ListDateReportResponse>> listdateReport(@Query("date") String date);

    //
    @GET("logout")//ct/
    Observable<MessageResponse> logout();

    // check in out
    @POST("checkin")
    Observable<String> checkin();

    @PUT("checkout")
    Observable<CheckOutResponse> checkout();


    @GET("show-sabbatical")///report-list // list_register_for_leave
    Observable<List<VacationResponse>> approvalsabbatical();

    //
    @GET("show-history")
    Observable<List<VacationResponse>> leavehistory();

    @GET("sabbatical-details/{id}")
    Observable<VacationResponse> detailleavehistory(@Path("id") String id);

    @GET("showtype")
    Observable<List<TypeResponse>> showtype();
    // thong tin
    @PUT("update-info")
    @FormUrlEncoded
    Completable updateinfo(@Field("name") String name,
                           @Field("email") String email);
    //doi mat khau
    @PUT("update-pass")
    @FormUrlEncoded
    Observable<MessageResponse> updatepass(@Field("oldpass") String oldpass,
                                           @Field("newpass") String newpass);
    //dang nhap
    @FormUrlEncoded
    @POST("login")
    Single<User> loginnew(@Field("email") String email,
                          @Field("password") String password);

    @POST("create-sabbatical")
    @FormUrlEncoded
    Single<MessageResponse> createsacbbticalleave(@Field("leavefrom") String leavefrom,
                                         @Field("leaveto") String leaveto,
                                         @Field("returndate") String returndate,
                                         @Field("typeid") String typeid,
                                         @Field("type") String type,
                                         @Field("reason") String reason);
    //doi mat khau
    @PUT("approval-sabbatical/{id}")
    Observable<MessageResponse> approvalsabbatical(@Path("id") String id,
                                                   @Query("status") String status,
                                                   @Query("comment") String comment);


    // luu token firebase vao csdl
    @POST("save-notification")
    Single<String> savetokenfirebase(@Query("token") String token);

    // gui thong bao của th admin gui di
    @POST("show-notificationmanager/{type}/{reference}")
    Single<MessageResponse> notificationmanager(@Path("type") String type,
                                                @Path("reference") String reference);

    // gui thong bao của th xin nghỉ
    @POST("show-notificationuser")
    Single<MessageResponse> notificationuser();



    @Headers({"Accept: application/json"
            //  , "Content-Type : application/json"
    })

    @POST("login")
    @FormUrlEncoded
    Call<User> login(@Field("email") String email,
                     @Field("password") String password);

}
