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

    @GET("show-info")//ct/
    Observable<InfoResponse> getinfo();//@Query("id") String id
    //lich su
    @GET("diary-attendance")//ct/
    Observable<List<DiaryAttendanceResponse>> diaryattendance();//@Path("id") String id
    //Danh sách đăng kí nghỉ phép
    @GET("list_register_for_leave")//ct/
    Observable<List<ListRegisterForLeaveResponse>> listRegisterForLeave();//@Path("id") String id

    //bao cao dang danh sach
    @GET("report")//ct/
    //bao cao dang lich
    Observable<List<ListReportResponse>> listReport();//@Path("id") String id
    @GET("reports")//ct/
    Observable<List<ListDateReportResponse>> listdateReport(@Query("date") String date);


    @GET("logout")//ct/
    Observable<MessageResponse> logout();
    @POST("checkin")//ct/
    Observable<Boolean> checkIn();//@Query("id") String id


    @PUT("checkout")//ct/
    Observable<CheckOutResponse> checkOut();//@Query("id") String id

    @GET("list_register_for_leave")//ct/
    Observable<List<VacationResponse>> approvalsabbatical();

    @GET("leavehistory")//ct/
    Observable<List<VacationResponse>> leavehistory();

    @GET("leavehistory/{id}")//ct/
    Observable<VacationResponse> detailleavehistory(@Path("id") String id);

    @GET("showtype")//ct/
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

    @POST("createSacbbticalleave")
    @FormUrlEncoded
    Single<String> createsacbbticalleave(@Field("leavefrom") String leavefrom,
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

    @Headers({"Accept: application/json"
            //  , "Content-Type : application/json"
    })

    @POST("login")
    @FormUrlEncoded
    Call<User> login(@Field("email") String email,
                     @Field("password") String password);

}
