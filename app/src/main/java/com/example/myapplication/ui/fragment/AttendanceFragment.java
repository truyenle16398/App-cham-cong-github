package com.example.myapplication.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.response.CheckOutResponse;
import com.example.myapplication.network.response.DiaryAttendanceResponse;
import com.example.myapplication.ui.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.SessionManager;
import com.example.myapplication.ui.adapter.Attendance_history_adapter;
import com.example.myapplication.ui.model.itemah;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AttendanceFragment extends Fragment {
    private final String SHARED_PREFERENCES_NAME ="savecheck";
    RecyclerView recyclerView;
    Attendance_history_adapter adapter;
    private String id,reference,idno,date,employee,timein,timeout,totalhours,status_timein,status_timeout,reason,comment;
    private  int seconds = 0;
    private boolean running = false;
    private  boolean wasRunning;
    private Button btnCheckInCheckOut;
    private TextView txtEmployee, txtTimeTotal;
    View view;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attendance, container, false);
        initWidget();
        if(savedInstanceState != null){
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        runTimer();
        onclick();
        getdata();
//        initLayout();
        addData();
        return view;
    }

    private void getdata() {
        ApiClient.getService().diaryattendance().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DiaryAttendanceResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<DiaryAttendanceResponse> diaryAttendanceResponses) {
                        ArrayList<DiaryAttendanceResponse> arrayList = (ArrayList<DiaryAttendanceResponse>) diaryAttendanceResponses;
                        adapter = new Attendance_history_adapter(getActivity(),arrayList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("nnn", "onError: " +e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    private void checkInUser(){

        ApiClient.getService().checkin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String string) {
                        if (string.equals("1")){
//                            Toast.makeText(getActivity(), "Check In", Toast.LENGTH_SHORT).show();
                            btnCheckInCheckOut.setText("CHECK OUT");
                            SessionManager.getInstance().setKeySaveCheck(true);
                            btnCheckInCheckOut.setBackgroundResource(R.drawable.btn_checkout);
                        } else {
                            Toast.makeText(getActivity(), "CheckIn thất bại!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("nnn", "onError: check in " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    private void checkOutUser(){

        ApiClient.getService().checkout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CheckOutResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CheckOutResponse checkOutResponse) {
                        if (checkOutResponse != null){

                            Toast.makeText(getActivity(), "Bạn đã làm được: "+checkOutResponse.getTotalhours()+ " phút", Toast.LENGTH_SHORT).show();
                            btnCheckInCheckOut.setText("CHECK IN");
                            SessionManager.getInstance().setKeySaveCheck(false);
                            btnCheckInCheckOut.setBackgroundResource(R.drawable.shape_drawable);
                        } else {
                            Toast.makeText(getActivity(), "CheckOut thất bại!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("nnn", "onError: checkOutUser " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void addData() {
        if (SessionManager.getInstance().CheckKeyInOut()) {
            btnCheckInCheckOut.setText("CHECK OUT");
            btnCheckInCheckOut.setBackgroundResource(R.drawable.btn_checkout);
        } else {
//            isCheckIn = false;
            btnCheckInCheckOut.setText("CHECK IN");
            SessionManager.getInstance().setKeySaveCheck(false);
            btnCheckInCheckOut.setBackgroundResource(R.drawable.shape_drawable);
        }
    }

    private void onclick() {
        btnCheckInCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SessionManager.getInstance().CheckKeyInOut()) {
                    checkOutUser();
                } else {
                    checkInUser();
                }


//                if (!isCheckIn) {
//                    isCheckIn = true;
//                    btnCheckInCheckOut.setText("CHECK OUT");
//                    btnCheckInCheckOut.setBackgroundResource(R.drawable.btn_checkout);
//                    running = true;
////                    Toast.makeText(getActivity(), "Check In", Toast.LENGTH_SHORT).show();
//                } else {
//                    isCheckIn = false;
//                    btnCheckInCheckOut.setText("CHECK IN");
//                    SessionManager.getInstance().setKeySaveCheck(false);
//                    running = false;
//                    seconds = 0;
//                    btnCheckInCheckOut.setBackgroundResource(R.drawable.shape_drawable);
////                    Toast.makeText(getActivity(), "Check Out", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    private void initWidget() {
        btnCheckInCheckOut = view.findViewById(R.id.btn_check);
        txtEmployee = view.findViewById(R.id.txtEmployee);
        txtTimeTotal = view.findViewById(R.id.txtTimeTotal);
        recyclerView = view.findViewById(R.id.recyclerview_at);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        txtEmployee.setText(MainActivity.namee);

    }

    private void runTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable(){

            @Override
            public void run() {
                int hour = seconds / 3600;
                int minute = (seconds % 3600) / 60;
                int sec = seconds % 60;

                String time = String.format("%d:%02d:%02d", hour, minute, sec);
                if (running){
                    seconds++;
                }
                handler.postDelayed(this,1000);
            }
        });
    }
}