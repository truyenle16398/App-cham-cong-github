package com.example.myapplication.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ListRegisterForLeaveResponse {
    @SerializedName("employee")
    @Expose
    private String employee;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("leavefrom")
    @Expose
    private String leavefrom;
    @SerializedName("leaveto")
    @Expose
    private String leaveto;
    @SerializedName("returndate")
    @Expose
    private String returndate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("comment")
    @Expose
    private String comment;
}
