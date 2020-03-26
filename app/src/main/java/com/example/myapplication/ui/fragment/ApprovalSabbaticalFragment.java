package com.example.myapplication.ui.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.response.VacationResponse;
import com.example.myapplication.ui.MainActivity;
import com.example.myapplication.ui.SessionManager;
import com.example.myapplication.ui.adapter.Approval_sabbatical_adapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ApprovalSabbaticalFragment extends Fragment {

    View view;
    Approval_sabbatical_adapter adapter;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_approval_sabbatical,container,false);
        String role = SessionManager.getInstance().getKeyRole();
        if (role.equals("1")){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            Toast.makeText(getActivity(), "Bạn không đủ quyền sử dụng chức năng này!!", Toast.LENGTH_SHORT).show();
        } else {
            anhxa();
            getdata();
        }
        return view;
    }
    private void getdata() {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("please wait...");
        dialog.show();
        ApiClient.getService().approvalsabbatical().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<VacationResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<VacationResponse> vacationResponses) {
                        ArrayList<VacationResponse> arrayList = (ArrayList<VacationResponse>) vacationResponses;
//                        Log.d("nnn", "onNext: "+arrayList.get(0).getStatus());
                        adapter = new Approval_sabbatical_adapter(getActivity(),arrayList);
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
        recyclerView = view.findViewById(R.id.recyclerview_approval_sabbatical);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}