package com.example.myapplication.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ListReportResponse {
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
}
