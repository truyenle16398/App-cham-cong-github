package com.example.myapplication.ui.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.response.MessageResponse;
import com.example.myapplication.network.response.TypeResponse;
import com.example.myapplication.ui.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.SneakyThrows;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class CreateVacationFragment extends Fragment {

    HashMap<Integer,String> spinnerMap;
    View view;
    RadioButton rd1,rdn;
    Spinner spinner;
    LinearLayout lnl1,lnln;
    EditText edtreason;
    Button btn_1,btn_n1,btn_n2,btn_ok,btn_back,btn_returnday;
    String str_1,str_n1,str_n2,str_returnday;
    int day,month,year;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_vacation, container, false);
        anhxa();
        onclick();
        getdatatype();
        return view;
    }
    private void onclick() {
        rd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnl1.setVisibility(View.VISIBLE);
                lnln.setVisibility(View.GONE);
            }
        });
        rdn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnl1.setVisibility(View.GONE);
                lnln.setVisibility(View.VISIBLE);
            }
        });
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btn_1.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                        str_1 = year+"-"+(month+1)+"-"+dayOfMonth;
                    }
                }, year, month, day);
                dialog.show();
            }
        });
        btn_n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btn_n1.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                        str_n1 = year+"-"+(month+1)+"-"+dayOfMonth;
                    }
                }, year, month, day);
                dialog.show();
            }
        });
        btn_n2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btn_n2.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                        str_n2 = year+"-"+(month+1)+"-"+dayOfMonth;
                    }
                }, year, month, day);
                dialog.show();
            }
        });
        btn_returnday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btn_returnday.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                        str_returnday = year+"-"+(month+1)+"-"+dayOfMonth;
                    }
                }, year, month, day);
                dialog.show();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reason = edtreason.getText().toString();
                String type = spinner.getSelectedItem().toString();
                String id = spinnerMap.get(spinner.getSelectedItemPosition());
                if (reason==null){
                    Toast.makeText(getActivity(), "Vui lòng nhập lý do!!", Toast.LENGTH_SHORT).show();
                } else if(str_returnday==null){
                    Toast.makeText(getActivity(), "Vui lòng chọn ngày trở lại!!", Toast.LENGTH_SHORT).show();
                } else {
                    if (rd1.isChecked()){
                        if (str_1==null){
                            Toast.makeText(getActivity(), "Vui lòng chọn ngày nghỉ!!", Toast.LENGTH_SHORT).show();
                        } else {
                            RequestLeave(str_1, str_1, str_returnday, id, type, reason);
                        }
                    }
                    if (rdn.isChecked()){
//                        Log.d("nnn", "onClick: "+ str_n1 +" & "+str_n2);
                        if (str_n1==null){
                            Toast.makeText(getActivity(), "Vui lòng chọn ngày bắt đầu nghỉ!!", Toast.LENGTH_SHORT).show();
                        } else if (str_n2==null){
                            Toast.makeText(getActivity(), "Vui lòng chọn ngày kết thúc nghỉ!!", Toast.LENGTH_SHORT).show();
                        }else {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
                            try {
                                Date dateBefore = sdf.parse(str_n1);
                                Date dateAfter = sdf.parse(str_n2);
                                Date c = Calendar.getInstance().getTime();
                                String a = sdf.format(c);
                                Date current = sdf.parse(a);
                                if (dateBefore.compareTo(current)>=0){
                                    if (dateBefore.after(dateAfter)) {
                                        Toast.makeText(getActivity(), "Ngày không hợp lệ!!", Toast.LENGTH_SHORT).show();
//                                  Log.d("nnn", "onClick: "+ str_n1 +" > "+str_n2);
                                    }else {
//                                  Log.d("nnn", "onClick: "+ str_n1 +" < "+str_n2);
                                        RequestLeave(str_n1,str_n2,str_returnday,id,type,reason);
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Ngày bắt đầu không hợp lệ!!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
//                else if(str_n1==null){
//
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Hì Hì!!!", Toast.LENGTH_SHORT).show();
//                androidx.fragment.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ListRegisterForLeaveFragment bdf = new ListRegisterForLeaveFragment();
//                ft.replace(R.id.nav_host_fragment, bdf);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                ft.addToBackStack(null);
//                ft.commit();
            }
        });

    }

    public void sendnotification() {
        ApiClient.getService().notificationuser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MessageResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(MessageResponse messageResponse) {
                        if (messageResponse.getSuccess().equals("1")){
//                            Toast.makeText(getActivity(), "Gửi thông báo oke!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("nnn", " send notification create lỗi thêm ");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("nnn", "onError: send notification create "+ e.getMessage());
                    }
                });
    }

    private void RequestLeave(String leavefrom,String leaveto, String returndate,String typeid, String type,String reason) {
        ApiClient.getService().createsacbbticalleave(leavefrom,leaveto,returndate,typeid,type,reason).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<MessageResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onSuccess(MessageResponse s) {
                if (s.getMessage()!=null){
                    Toast.makeText(getActivity(), s.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    sendnotification();
                } else {
                    Toast.makeText(getActivity(), "Lỗi thêm", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(Throwable e) {
                Log.d("nnn", "onError: "+ e.getMessage());
            }
        });
    }
    private void anhxa() {
        spinner = view.findViewById(R.id.spinnertype);
        rd1 = view.findViewById(R.id.radio1);
        rdn = view.findViewById(R.id.radion);
        lnl1 = view.findViewById(R.id.linearclick1);
        lnln = view.findViewById(R.id.linearclickn);
        edtreason = view.findViewById(R.id.edt_vacation_reason);
        btn_1 = view.findViewById(R.id.btn_vacation_1);
        btn_n1 = view.findViewById(R.id.btn_vacation_n_1);
        btn_n2 = view.findViewById(R.id.btn_vacation_n_2);
        btn_ok = view.findViewById(R.id.btn_vacation_ok);
        btn_back = view.findViewById(R.id.btn_vacation_back);
        btn_returnday = view.findViewById(R.id.btn_vacation_return);
        final Calendar cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);
    }

    private void getdatatype() {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("please wait...");
        dialog.show();
        ApiClient.getService().showtype().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<TypeResponse>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<TypeResponse> typeResponses) {
                ArrayList<String> arrayList = new ArrayList<>();
                String[] spinnerArray = new String[typeResponses.size()];
                spinnerMap = new HashMap<Integer, String>();
                for (int i =0; i< typeResponses.size();i++){
                    arrayList.add(typeResponses.get(i).getId()+"."+ typeResponses.get(i).getType());
                    spinnerMap.put(i,typeResponses.get(i).getId());
                    spinnerArray[i] = typeResponses.get(i).getType();
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,spinnerArray);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("nnn", "onError:_type "+ e.getMessage());
            }

            @Override
            public void onComplete() {
                dialog.dismiss();
            }
        });
    }
}
