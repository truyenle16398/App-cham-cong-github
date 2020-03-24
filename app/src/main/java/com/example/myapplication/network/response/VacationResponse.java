package com.example.myapplication.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class VacationResponse {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idno")
    @Expose
    private String idno;
    @SerializedName("name")
    @Expose
    private String name;
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
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("role_id")
    @Expose
    private String role_id;
}
