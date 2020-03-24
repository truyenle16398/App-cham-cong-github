package com.example.myapplication.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.response.DiaryAttendanceResponse;
import com.example.myapplication.network.response.VacationResponse;
import com.example.myapplication.ui.adapter.Attendance_history_adapter;
import com.example.myapplication.ui.adapter.Vacation_history_adapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class VacationFragment extends Fragment{

    View view;
    Vacation_history_adapter adapter;
    RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vacation, container, false);
        anhxa();
        getdata();
        return view;
    }

    private void getdata() {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("please wait...");
        dialog.show();
        ApiClient.getService().leavehistory().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<VacationResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<VacationResponse> vacationResponses) {
                        ArrayList<VacationResponse> arrayList = (ArrayList<VacationResponse>) vacationResponses;
//                        Log.d("nnn", "onNext: "+arrayList.get(0).getStatus());
                        adapter = new Vacation_history_adapter(getActivity(),arrayList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapter);
                        dialog.dismiss();
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

    private void anhxa() {
        recyclerView = view.findViewById(R.id.recyclerview_vacation_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
