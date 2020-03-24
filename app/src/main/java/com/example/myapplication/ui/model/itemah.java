package com.example.myapplication.ui.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class itemah {

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimein() {
        return timein;
    }

    public void setTimein(String timein) {
        this.timein = timein;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getTotalhours() {
        return totalhours;
    }

    public void setTotalhours(String totalhours) {
        this.totalhours = totalhours;
    }

}
