package com.example.myapplication.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.response.ListDateReportResponse;
import com.example.myapplication.network.response.ListReportResponse;
import com.example.myapplication.ui.adapter.List_report_adapter;
import com.example.myapplication.ui.adapter.List_report_date_adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListReportDateFragment extends DialogFragment{

    TextView textView;
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
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String a = year+"-"+(month+1)+"-"+dayOfMonth;
                getdata(a);

            }
        });
        return view;
    }



    private void getdata(String a) {

        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("please wait...");
        dialog.show();
        ApiClient.getService().listdateReport(a).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ListDateReportResponse>>() {


                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ListDateReportResponse> listDateReportResponses) {
                        Log.d("nnn", "onNext: "+ a);
                        ArrayList<ListDateReportResponse> arrayList = (ArrayList<ListDateReportResponse>) listDateReportResponses;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
                        try {
                            Date current = sdf.parse(a);
                            sdf.setLenient(false);
                            Date today = new Date();
                            String s = sdf.format(today);
                            if (current.compareTo(today)>=0){
//                                Toast.makeText(getActivity(), "Ngày này chưa tới!!", Toast.LENGTH_SHORT).show();
                                textView.setText("Ngày này chưa tới!!");
                                textView.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                if (arrayList.size()==0){
//                                    Toast.makeText(getActivity(), "Ngày này bạn không làm!", Toast.LENGTH_SHORT).show();
                                    textView.setText("Ngày này bạn không làm!!");
                                    textView.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                } else {
                                    textView.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    adapter = new List_report_date_adapter(getActivity(),listDateReportResponses);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                                    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("nnn", "onError: date "+ e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }

    private void initView() {
        textView = view.findViewById(R.id.tvthongbao);
        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recyclerview_list_report_date);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }


}
