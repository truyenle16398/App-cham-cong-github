package com.example.myapplication.retrofit;

import com.example.myapplication.ui.MainActivity;
import com.example.myapplication.ui.model.User;
import com.example.myapplication.ui.model.info;
import com.example.myapplication.ui.model.itemah;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DataClient {
    //    @FormUrlEncoded
//    @Headers({"Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImQzOTE5MGMyZDFjMGYzZmIwODQyMTA2MjM4ZTQ0NzJiZDgxYWEwNTFkMDVjNDAyZDZlNWNjN2Y5MjU2OWY3ZGFiZTcyMTQzOGQyMGVkMTk2In0.eyJhdWQiOiIxIiwianRpIjoiZDM5MTkwYzJkMWMwZjNmYjA4NDIxMDYyMzhlNDQ3MmJkODFhYTA1MWQwNWM0MDJkNmU1Y2M3ZjkyNTY5ZjdkYWJlNzIxNDM4ZDIwZWQxOTYiLCJpYXQiOjE1ODMzNzg1MzAsIm5iZiI6MTU4MzM3ODUzMCwiZXhwIjoxNjE0OTE0NTMwLCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.iWmFQrgg-TWY4zu_wYzJi4AT7gS_E015-QmWnenQMUmtxpa6xEdctsRYTN_-X3NxRjW_395dYe9SBMXYPKHbWw4YFQ1d4vikCIaNOedrYf4Ldz1c1T5f9QU1ZEIqjFIlGNNJsjPOngGlXg5ZkoUt8fxKAv4P34rx6i5Zd7VFBKdEzjIvAeDkRLHzdkdHgb9e792WauuR5mkH7ufWRHyLKua2m4dkfA98SCxupomMJtuTi4SU-X5GsBZh94Il7xLYi6ryhoDNnr0YPmX6GLWEuf-lNVYOgnVtN3gdBg4PCwgFD48Vmt-6LfltZUlcEt4TM50H-NhSsN7vwuNdYUAj-aTBrwmbsUxfkHbqkq8zv5q0kD6ou_6MhWj3pKNqyEXLROa6DWFRsq3AzQM6zSsH46v18Fq57PO3pMFDyLvX8g1sOqm2NKzca9nNNy_6AVPCDgqvhUFqTaqN4SvO0G1Wa3X6vOEfiarUuYsdFkjTzSsTBOiLmeW_FNmbMh_20LS46XCuDi5bPgYGHUQg8QYDee8E6kR2QOaR36InQJ_QmTjt61OzX4PGNpL-8qq5T9MV48plD-3VzwAfs9HpelLpbKiBh03k-fFuaAf4SUjNDywh1HnIQZEze232XD-Iiw3AwjYzIzhtkD-Mzo_970HUNNifrbPUxkzhMPL4xHojqP8c"})
    @GET("show-info")
    Call<info> getinfo();//@Query("id") String id

    //lich su
    @GET("/cit/at{id}.php")
//ct/
    Call<List<itemah>> getah(@Path("id") String id);//@Path("id") String id


    @PUT("updateinfo/{id}")
    @FormUrlEncoded
    Call<info> updateinfo(@Path("id") String id,
                          @Field("name") String name,
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
