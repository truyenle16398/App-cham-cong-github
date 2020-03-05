package com.example.myapplication.ui.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.ui.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.adapter.Attendance_history_adapter;
import com.example.myapplication.ui.model.itemah;
import com.example.myapplication.retrofit.APIUtils;
import com.example.myapplication.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceHistoryFragment extends Fragment {

    View view;
    private DataClient mgetdata;
    RecyclerView recyclerView;
    List<itemah> list;
    Attendance_history_adapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attendance_history, container, false);
//        final TextView textView = view.findViewById(R.id.text_gallery);
        anhxa();
        mgetdata = APIUtils.getdata();
        getdata();

        return view;
    }

    private void getdata() {
        Call<List<itemah>> callback = mgetdata.getah(MainActivity.idd);
        callback.enqueue(new Callback<List<itemah>>() {
            @Override
            public void onResponse(Call<List<itemah>> call, Response<List<itemah>> response) {
                Log.d("nnn", "onResponsehistory: "+ response.toString());
                ArrayList<itemah> arrayList = (ArrayList<itemah>) response.body();
                adapter = new Attendance_history_adapter(getActivity(),arrayList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
//                recyclerView.setAdapter(adapter));
//                Toast.makeText(getContext(), response.body().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<itemah>> call, Throwable t) {
                Log.d("nnn", "onFailure: "+t.getMessage());
            }
        });
    }

    private void anhxa() {
        recyclerView = view.findViewById(R.id.recyclerview_h);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}
