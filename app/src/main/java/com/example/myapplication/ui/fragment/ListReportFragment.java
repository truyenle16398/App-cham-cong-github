package com.example.myapplication.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.response.ListRegisterForLeaveResponse;
import com.example.myapplication.network.response.ListReportResponse;
import com.example.myapplication.ui.adapter.List_register_for_leave_adapter;
import com.example.myapplication.ui.adapter.List_report_adapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListReportFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    List_report_adapter adapter;

    public ListReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_report, container, false);
//        final TextView textView = view.findViewById(R.id.text_gallery);
        initView();
        getdata();

        return view;
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
                        adapter = new List_report_adapter(getActivity(),arrayList);
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
                        dialog.dismiss();
                    }
                });
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recyclerview_list_report);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

}
