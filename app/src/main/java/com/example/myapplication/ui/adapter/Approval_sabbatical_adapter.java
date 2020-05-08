package com.example.myapplication.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.response.MessageResponse;
import com.example.myapplication.network.response.VacationResponse;
import com.example.myapplication.ui.fragment.ApprovalSabbaticalFragment;
import com.example.myapplication.ui.fragment.AttendanceFragment;
import com.example.myapplication.ui.fragment.CreateVacationFragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Approval_sabbatical_adapter extends RecyclerView.Adapter<Approval_sabbatical_adapter.Approval_sabbatical_holder> {
    private Context context;
    private List<VacationResponse> items;
    Dialog dialog;
    private int po;

    public Approval_sabbatical_adapter(Context context, List<VacationResponse> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Approval_sabbatical_adapter.Approval_sabbatical_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_leave_history,parent,false);
        Approval_sabbatical_adapter.Approval_sabbatical_holder vholder = new Approval_sabbatical_adapter.Approval_sabbatical_holder(v);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_approval_sabbatical);
        vholder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String referen = items.get(vholder.getAdapterPosition()).getReference();
                po = vholder.getAdapterPosition();
                Log.d("nnn", "onClick: getReference +" +po);
                ApiClient.getService().detailleavehistory(items.get(vholder.getAdapterPosition()).getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<VacationResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(VacationResponse vacationResponses) {
                                Log.d("nnn", "on check name : "+ vacationResponses.getRole_id());
                                TextView dialog_name = dialog.findViewById(R.id.tv_detail_name);
                                TextView dialog_role = dialog.findViewById(R.id.tv_detail_role);
                                TextView dialog_dayoff = dialog.findViewById(R.id.tv_detail_dayoff);
                                TextView dialog_reason = dialog.findViewById(R.id.tv_detail_reason);
                                TextView dialog_dayreturn = dialog.findViewById(R.id.tv_detail_dayreturn);
                                Button ok = dialog.findViewById(R.id.btn_approval);
                                Button back = dialog.findViewById(R.id.btn_approval_back);
                                EditText edtcmt = dialog.findViewById(R.id.edt_comment);

                                long noOfDaysBetween = 0;
                                LocalDate dateBefore = LocalDate.parse(vacationResponses.getLeavefrom());
                                LocalDate dateAfter = LocalDate.parse(vacationResponses.getLeaveto());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
                                if (noOfDaysBetween ==0){
                                    dialog_dayoff.setText("(1 ngày) "+dateBefore);
                                } else {
                                    dialog_dayoff.setText("("+noOfDaysBetween+" ngày) "+"từ ngày "+dateBefore+" tới ngày "+dateAfter);
                                }
                                dialog_name.setText(vacationResponses.getEmployee());
                                if (items.get(vholder.getAdapterPosition()).getRole_id().equals("1")){
                                    dialog_role.setText("Nhân viên");
                                } else{
                                    dialog_role.setText("Quản lý");
                                }
                                dialog_dayreturn.setText(vacationResponses.getReturndate());
                                dialog_reason.setText(vacationResponses.getReason());
                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String referen = items.get(vholder.getAdapterPosition()).getReference();
                                        Log.d("nnn", "onClick: getReference +" +referen);
                                        approvalsabbatical(items.get(vholder.getAdapterPosition()).getId(),"1",edtcmt.getText().toString());
                                        sendnotification("1",referen);
                                    }
                                });
                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        approvalsabbatical(items.get(vholder.getAdapterPosition()).getId(),"2",edtcmt.getText().toString());
                                        sendnotification("0",referen);
                                    }
                                });
                                dialog.show();
//                                Toast.makeText(context, ""+vacationResponses.get(vholder.getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();

//                                Log.d("nnn", "onClick: "+vacationResponses.get(vholder.getAdapterPosition()).getLeavefrom());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("nnn", "onError: "+e.getMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
        return vholder;
        //https://www.youtube.com/watch?v=Zd0TUuoPP-s
    }

    public static void sendnotification(String type,String referen) {
        ApiClient.getService().notificationmanager(type,referen).subscribeOn(Schedulers.io())
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

    private void approvalsabbatical(String id,String status,String comment) {
        ApiClient.getService().approvalsabbatical(id,status,comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MessageResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MessageResponse messageResponse) {
                        Toast.makeText(context, ""+messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                        Log.d("nnn", "onNext: "+ messageResponse.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("nnn", "onError:approvalsabbatical  "+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        removeitem(po);
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void onBindViewHolder(@NonNull Approval_sabbatical_adapter.Approval_sabbatical_holder holder, int position) {
        final String element = items.get(position).getId();
        VacationResponse response = items.get(position);
        int status = Integer.parseInt(response.getStatus());
        if (status == 0){
            holder.tvstatus.setText("Đang chờ duyệt");
        } else {
            holder.tvstatus.setText("Đã duyệt");
        }
        long noOfDaysBetween = 0;
        LocalDate dateBefore = LocalDate.parse(response.getLeavefrom());
        LocalDate dateAfter = LocalDate.parse(response.getLeaveto());
        noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
//        Log.d("nnn",  response.getStatus()+" = "+response.getLeaveto()+" - "+response.getLeavefrom()+"---------"+noOfDaysBetween);
        if (noOfDaysBetween ==0){
            holder.tvdayoff.setText("1(chi tiết)");
        } else {
            holder.tvdayoff.setText(noOfDaysBetween+"(chi tiết)");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Approval_sabbatical_holder extends RecyclerView.ViewHolder {
        TextView tvdayoff,tvstatus;
        LinearLayout linearLayout;
        public Approval_sabbatical_holder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.test_item);
            tvdayoff= itemView.findViewById(R.id.tv_dayoff_vacation);
            tvstatus = itemView.findViewById(R.id.tv_status_vacation);
        }
    }
    public void removeitem(int position){
        items.remove(position);
        notifyDataSetChanged();
    }
}