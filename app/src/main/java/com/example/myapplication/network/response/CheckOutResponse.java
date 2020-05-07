package com.example.myapplication.network.response;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CheckOutResponse {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("idno")
    @Expose
    private String idno;
    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("employee")
    @Expose
    private String employee;

    @SerializedName("timein")
    @Expose
    private String timein;

    @SerializedName("timeout")
    @Expose
    private String timeout;

    @SerializedName("totalhours")
    @Expose
    private String totalhours;

    @SerializedName("status_timein")
    @Expose
    private String status_timein;

    @SerializedName("status_timeout")
    @Expose
    private String status_timeout;


    @SerializedName("reason")
    @Expose
    private String reason;


    @SerializedName("comment")
    @Expose
    private String comment;

    @SerializedName("message")
    @Expose
    private String message;
}