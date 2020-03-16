package com.example.myapplication.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.response.DiaryAttendanceResponse;
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

public class AttendanceHistoryFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    List<itemah> list;
    Attendance_history_adapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attendance_history, container, false);
//        final TextView textView = view.findViewById(R.id.text_gallery);
        anhxa();
        getdata();

        return view;
    }

    private void getdata() {

        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("please wait...");
        dialog.show();
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
                        dialog.dismiss();
                    }
                });
    }

    private void anhxa() {
        recyclerView = view.findViewById(R.id.recyclerview_h);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}
