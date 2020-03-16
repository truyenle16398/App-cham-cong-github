package com.example.myapplication.ui.fragment;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.response.CheckOutResponse;
import com.example.myapplication.network.response.DiaryAttendanceResponse;
import com.example.myapplication.network.response.InfoResponse;
import com.example.myapplication.ui.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.adapter.Attendance_history_adapter;
import com.example.myapplication.ui.model.itemah;
import com.example.myapplication.retrofit.APIUtils;
import com.example.myapplication.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static android.provider.FontsContract.Columns.WEIGHT;


public class AttendanceFragment extends Fragment {
    private final String SHARED_PREFERENCES_NAME ="savecheck";
    private DataClient mgetdata;
    RecyclerView recyclerView;
    Attendance_history_adapter adapter;
    private String id,reference,idno,date,employee,timein,timeout,totalhours,status_timein,status_timeout,reason,comment;
    private  int seconds = 0;
    private boolean running = false;
    private boolean isCheckIn = false;
    private  boolean wasRunning;
    private Button btnCheckInCheckOut;
    private TextView time_view, txtEmployee, txtTimeTotal;
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
        initLayout();
        addData();
        return view;
    }

    public void addData(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(String.valueOf(isCheckIn),this.isCheckIn);
        editor.putBoolean(String.valueOf(running),this.running);
        //editor.apply();
        editor.commit();
        Toast.makeText(getActivity(), "Save Check", Toast.LENGTH_SHORT).show();
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
    private void initLayout() {
        checkInUser();
        checkOutUser();
    }
    private void checkInUser(){

        ApiClient.getService().checkIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        isCheckIn = aBoolean;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("nnn", "onError: show info" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
//                        Toast.makeText(getActivity(), "neee", Toast.LENGTH_SHORT).show();

                        if (isCheckIn){
                            Toast.makeText(getActivity(), "Check In Successful", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), "Check In Faile", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    private void checkOutUser(){

        ApiClient.getService().checkOut()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CheckOutResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CheckOutResponse checkOutResponse) {
                        id = checkOutResponse.getId();
                        reference = checkOutResponse.getReference();
                        idno = checkOutResponse.getIdno();
                        date = checkOutResponse.getDate();
                        employee = checkOutResponse.getEmployee();
                        timein = checkOutResponse.getTimein();
                        timeout = checkOutResponse.getTimeout();
                        totalhours = checkOutResponse.getTotalhours();
                        status_timein = checkOutResponse.getStatus_timein();
                        status_timeout = checkOutResponse.getStatus_timeout();
                        reason = checkOutResponse.getReason();
                        comment = checkOutResponse.getComment();
                        Log.d("v", "yyyyyyyyyyyyyyyyyyy  " +checkOutResponse.getTimeout());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("nnn", "onError: show info" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
//                        Toast.makeText(getActivity(), "neee", Toast.LENGTH_SHORT).show();

                        Log.d("onComplete", "xxxxxxxxxxxxxxxxxxxxxx  ");

                    }
                });
    }
    private void onclick() {
        btnCheckInCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCheckIn) {
                    isCheckIn = true;
                    btnCheckInCheckOut.setText("CHECK OUT");
                    btnCheckInCheckOut.setBackgroundResource(R.drawable.btn_checkout);
                    running = true;
//                    Toast.makeText(getActivity(), "Check In", Toast.LENGTH_SHORT).show();
                } else {
                    isCheckIn = false;
                    btnCheckInCheckOut.setText("CHECK IN");
                    running = false;
                    seconds = 0;
                    btnCheckInCheckOut.setBackgroundResource(R.drawable.shape_drawable);
//                    Toast.makeText(getActivity(), "Check Out", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initWidget() {
        btnCheckInCheckOut = view.findViewById(R.id.btn_check);
        time_view = view.findViewById(R.id.time_view);
        txtEmployee = view.findViewById(R.id.txtEmployee);
        txtTimeTotal = view.findViewById(R.id.txtTimeTotal);
        recyclerView = view.findViewById(R.id.recyclerview_at);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mgetdata = APIUtils.getdata();
        txtEmployee.setText(MainActivity.namee);

    }

    private void runTimer() {
        final  TextView time_view = view.findViewById(R.id.time_view);
        final Handler handler = new Handler();
        handler.post(new Runnable(){

            @Override
            public void run() {
                int hour = seconds / 3600;
                int minute = (seconds % 3600) / 60;
                int sec = seconds % 60;

                String time = String.format("%d:%02d:%02d", hour, minute, sec);
                time_view.setText(time);
                if (running){
                    seconds++;
                }
                handler.postDelayed(this,1000);
            }
        });
    }
}
