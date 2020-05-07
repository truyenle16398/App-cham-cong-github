package com.example.myapplication.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DiffUtil;
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
import com.example.myapplication.network.response.MessageResponse;
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
    private ProgressDialog dialog;
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

//        if(savedInstanceState != null){
//            seconds = savedInstanceState.getInt("seconds");
//            running = savedInstanceState.getBoolean("running");
//            wasRunning = savedInstanceState.getBoolean("wasRunning");
//        }
        initWidget();
        runTimer();
        onclick();
        dialogrunning();
        return view;
    }

    private void dialogrunning() {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("please wait...");
        dialog.show();
        getdata();
        check();
        dialog.dismiss();
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
                .subscribe(new Observer<MessageResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MessageResponse messageResponse) {
                        if (messageResponse.getMessage().equals("1")){
                            btnCheckInCheckOut.setText("CHECK OUT");
//                            SessionManager.getInstance().setKeySaveCheck(true);
                            btnCheckInCheckOut.setBackgroundResource(R.drawable.btn_checkout);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(AttendanceFragment.this).attach(AttendanceFragment.this).commit();
                        } else {
                            Toast.makeText(getActivity(), "Hôm nay bạn đã Checkin!", Toast.LENGTH_SHORT).show();
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
                            String a = checkOutResponse.getTotalhours();
                            String[] parts = a.split("\\.");
                            String t = parts[0]; // 004
                            String t1 = parts[1]; // 034556

                            Toast.makeText(getActivity(), "Bạn đã làm được: "+t+" giờ "+t1+ " phút", Toast.LENGTH_SHORT).show();
                            Log.d("nnn", "aaaaaa: "+ checkOutResponse.getTotalhours());
                            btnCheckInCheckOut.setText("CHECK IN");
                            SessionManager.getInstance().setKeySaveCheck(false);
                            btnCheckInCheckOut.setBackgroundResource(R.drawable.shape_drawable);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(AttendanceFragment.this).attach(AttendanceFragment.this).commit();
                        } else {
                            Toast.makeText(getActivity(), ""+checkOutResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void check() {
//        if (SessionManager.getInstance().CheckKeyInOut()) {
//            btnCheckInCheckOut.setText("CHECK OUT");
//            btnCheckInCheckOut.setBackgroundResource(R.drawable.btn_checkout);
//        } else {
////            isCheckIn = false;
//            btnCheckInCheckOut.setText("CHECK IN");
//            SessionManager.getInstance().setKeySaveCheck(false);
//            btnCheckInCheckOut.setBackgroundResource(R.drawable.shape_drawable);
//        }
        ApiClient.getService().check()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MessageResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MessageResponse messageResponse) {
                        if (messageResponse.getMessage().equals("in"))
                        {
                            btnCheckInCheckOut.setVisibility(View.VISIBLE);
                            btnCheckInCheckOut.setText("CHECK IN");
                            btnCheckInCheckOut.setBackgroundResource(R.drawable.shape_drawable);
                        } else if (messageResponse.getMessage().equals("out")){
                            btnCheckInCheckOut.setVisibility(View.VISIBLE);
                            btnCheckInCheckOut.setText("CHECK OUT");
                            btnCheckInCheckOut.setBackgroundResource(R.drawable.btn_checkout);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("nnn", "onError: check check in out  "+ e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void onclick() {
        btnCheckInCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnCheckInCheckOut.getText().toString().equals("CHECK IN")) {
//                    Toast.makeText(getActivity(), "bấm check in mới đúng", Toast.LENGTH_SHORT).show();
                    checkInUser();
                    adapter.notifyDataSetChanged();
                } else if (btnCheckInCheckOut.getText().toString().equals("CHECK OUT")){
//                    Toast.makeText(getActivity(), "bấm check OUT kìa", Toast.LENGTH_SHORT).show();
                    checkOutUser();
                } else {
                    Toast.makeText(getActivity(), "Lỗi Khoa", Toast.LENGTH_SHORT).show();
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