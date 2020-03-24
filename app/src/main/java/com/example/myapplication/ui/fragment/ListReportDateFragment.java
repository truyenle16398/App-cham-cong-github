package com.example.myapplication.ui.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.myapplication.R;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.response.ListReportResponse;
import com.example.myapplication.ui.adapter.List_report_date_adapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.NonNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListReportDateFragment extends DialogFragment{
    CalendarView  calendarView;
    View view;
    RecyclerView recyclerView;
    List_report_date_adapter adapter;
    public ListReportDateFragment() {
        // Required empty public constructor
    }

    //How to handle event when clicking on calendar item android java
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_report_date, container, false);
//        final TextView textView = view.findViewById(R.id.text_gallery);
        initView();
        getdata();
        showDialog();
        return view;
    }

    private void showDialog() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {

            }
        });
    }


    private void getdata() {

        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("please wait...");
        dialog.show();
        ApiClient.getService().listReport().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ListReportResponse>>() {


                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ListReportResponse> listReportResponses) {
                        ArrayList<ListReportResponse> arrayList = (ArrayList<ListReportResponse>) listReportResponses;
                        adapter = new List_report_date_adapter(getActivity(),arrayList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initView() {
        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recyclerview_list_report_date);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }


}
