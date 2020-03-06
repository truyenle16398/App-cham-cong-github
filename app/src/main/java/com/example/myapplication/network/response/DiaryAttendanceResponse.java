package com.example.myapplication.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class DiaryAttendanceResponse {
    @SerializedName("date")
    @Expose
    private String date;
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
