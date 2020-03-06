package com.example.myapplication.ui.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.response.InfoResponse;
import com.example.myapplication.ui.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.model.info;
import com.example.myapplication.retrofit.APIUtils;
import com.example.myapplication.retrofit.DataClient;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoFragment extends Fragment {

    private View view;
    private SharedPreferences pref;
    private TextView tv_message;
    private AlertDialog dialog;
    private ProgressBar progress;
    private EditText edtname,edtemail,edtstatus,edtrole,et_old_password,et_new_password;
    private Button btnupdate,btnchangepass;
    private DataClient mGetData;
    private String name,email,role,status;
    private String passold ="",passnew="";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info, container, false);
        anhxa();
//        Log.d("nnn", "onCreateView: "+ MainActivity.idd);
        mGetData = APIUtils.getdata();
//        getInfo();  // show thông tin
        initLayout();
        onclick(); ///

        return view;
    }

    private void initLayout() {
        getInfoUser();
    }
    private void getInfoUser(){

        ApiClient.getService().getinfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<InfoResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(InfoResponse getInfoUser) {
                        name = getInfoUser.getName();
                        email = getInfoUser.getEmail();
                        edtname.setText(getInfoUser.getName());
                        edtemail.setText(getInfoUser.getEmail());
                        edtrole.setText(getInfoUser.getRole_id());
                        edtstatus.setText(getInfoUser.getStatus());

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("nnn", "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
//                        Toast.makeText(getActivity(), "neee", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onclick() {

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateinfo();
            }
        });
        //
        btnchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                showDialog();
//                Toast.makeText(getActivity(), "show "+MainActivity.idd, Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        et_old_password = (EditText)view.findViewById(R.id.et_old_password);
        et_new_password = (EditText)view.findViewById(R.id.et_new_password);
        tv_message = (TextView)view.findViewById(R.id.tv_message);
        progress = (ProgressBar)view.findViewById(R.id.progress);
        builder.setView(view);
        builder.setTitle("Đổi mật khẩu mới");
        builder.setPositiveButton("Đổi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_password = et_old_password.getText().toString();
                String new_password = et_new_password.getText().toString();
                if(!old_password.isEmpty() && !new_password.isEmpty()){

                    progress.setVisibility(View.VISIBLE);
                    changePassword(old_password,new_password);
//                    changePassword(pref.getString("pass",""),old_password,new_password);

                }else {

                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Fields are empty");
                }
            }
        });
    }

    private void changePassword(String old_password1,String new_password1) {
        Call<String> callback  = mGetData.updatepass(MainActivity.idd,old_password1,new_password1);
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("nnn", "onResponse: "+response.toString());
                String a = response.body();
                if (a.equals("win")){
                    Toast.makeText(getActivity(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.GONE);
                    dialog.dismiss();
                }else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                Log.d("nnn", "onFailure: "+t.getMessage());
            }
        });
    }

    private void updateinfo() {
        final String nameedt = edtname.getText().toString();
        final String emailedt = edtemail.getText().toString();
        Log.d("nnn", "onClick: "+email+"=="+emailedt +" và "+ name+"=="+nameedt);
        if (name.equals(nameedt) && email.equals(emailedt)){

            Toast.makeText(getContext(), "Bạn chưa chỉnh sửa!", Toast.LENGTH_SHORT).show();
        }
        else {
            if (nameedt.equals("") || emailedt.equals("")){
                Toast.makeText(getContext(), "Tên và Email là bắt buộc!", Toast.LENGTH_SHORT).show();
            }
            else {
                ApiClient.getService().updateinfo(nameedt,emailedt)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d("nnn", "onSubscribe: "+ d.toString());
                            }

                            @Override
                            public void onComplete() {
                                Toast.makeText(getActivity(), "Cập nhật thành công!!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("nnn", "onError: "+ e.getMessage());
                            }
                        });
            }
        }

    }

    private void getInfo() {
        Log.d("nnn", "getInfo: "+MainActivity.tokenn);
        Call<info> callback = mGetData.getinfo();
        callback.enqueue(new Callback<info>() {
            @Override
            public void onResponse(Call<info> call, Response<info> response) {

                info manginfo =  response.body();
                if (manginfo!=null){
                    name = manginfo.getName();
                    email = manginfo.getEmail();
                    role = manginfo.getRoleId();
                    status = manginfo.getStatus();
                    edtname.setText(name);
                    edtemail.setText(email);
                    edtrole.setText(role);
                    edtstatus.setText(status);
                }
            }

            @Override
            public void onFailure(Call<info> call, Throwable t) {
                Log.d("nnn", "onFailure: "+t.getMessage());
            }
        });
    }

    private void anhxa() {
        edtname = view.findViewById(R.id.edt_name);
        edtemail = view.findViewById(R.id.edt_email);
        edtrole = view.findViewById(R.id.edt_role);
        edtstatus = view.findViewById(R.id.edt_status);
        btnupdate = view.findViewById(R.id.btnchangeinfo);
        btnchangepass = view.findViewById(R.id.btnchangepass);
        edtemail.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }
}