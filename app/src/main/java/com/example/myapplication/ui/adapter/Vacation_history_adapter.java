package com.example.myapplication.ui.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Observable;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.response.VacationResponse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class Vacation_history_adapter extends RecyclerView.Adapter<Vacation_history_adapter.Vacation_history_holder>  {

    private Context context;
    private List<VacationResponse> items;
    Dialog dialog;

    public Vacation_history_adapter(Context context, List<VacationResponse> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Vacation_history_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_leave_history,parent,false);
        Vacation_history_holder vholder = new Vacation_history_holder(v);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_detailsleave);
        vholder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("nnn", "onClick: vacation "+items.get(vholder.getAdapterPosition()).getId() );
                ApiClient.getService().detailleavehistory(items.get(vholder.getAdapterPosition()).getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<VacationResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(VacationResponse vacationResponses) {
                                TextView dialog_name = dialog.findViewById(R.id.tv_detail_name);
                                TextView dialog_role = dialog.findViewById(R.id.tv_detail_role);
                                TextView dialog_dayoff = dialog.findViewById(R.id.tv_detail_dayoff);
                                TextView dialog_reason = dialog.findViewById(R.id.tv_detail_reason);
                                TextView dialog_status = dialog.findViewById(R.id.tv_detail_status);
                                TextView dialog_dayreturn = dialog.findViewById(R.id.tv_detail_dayreturn);
                                LinearLayout linearLayout = dialog.findViewById(R.id.background_status);

                                long noOfDaysBetween = 0;
                                LocalDate dateBefore = LocalDate.parse(vacationResponses.getLeavefrom());
                                LocalDate dateAfter = LocalDate.parse(vacationResponses.getLeaveto());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                                Toast.makeText(context, "day  "+date1, Toast.LENGTH_SHORT).show();
//                                String date1 = simpleDateFormat.format(vacationResponses.getLeavefrom());
//                                String date2 = simpleDateFormat.format(vacationResponses.getLeaveto());
                                noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
                                if (noOfDaysBetween ==0){
                                    dialog_dayoff.setText("(1 ngày) "+dateBefore);
                                } else {
                                    dialog_dayoff.setText("("+noOfDaysBetween+" ngày) "+"từ ngày "+dateBefore+" tới ngày "+dateAfter);
                                }
                                dialog_name.setText(vacationResponses.getEmployee());
                                if (items.get(vholder.getAdapterPosition()).getId().equals("1")){
                                    dialog_role.setText("Nhân viên");
                                } else{
                                    dialog_role.setText("Quản lý");
                                }
                                dialog_dayreturn.setText(vacationResponses.getReturndate());
                                dialog_reason.setText(vacationResponses.getReason());
//                                dialog_status.setText("aaaaaa "+vacationResponses.getStatus());
                                if (Integer.parseInt(vacationResponses.getStatus())==0){
                                linearLayout.setBackgroundColor(Color.parseColor("#f79307"));
                                    dialog_status.setText("Đơn xin nghỉ đang được chờ duyệt");
                                } else {
                                    linearLayout.setBackgroundColor(Color.parseColor("#8FEC27"));
                                    dialog_status.setText("Đơn xin nghỉ đã được duyệt");
                                }
                                dialog_status.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
//                                Toast.makeText(context, ""+vacationResponses.get(vholder.getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();

//                                Log.d("nnn", "onClick: "+vacationResponses.get(vholder.getAdapterPosition()).getLeavefrom());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("nnn", "onError: Vacation_history_adapter "+e.getMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
//
            }
        });
        return vholder;
        //https://www.youtube.com/watch?v=Zd0TUuoPP-s
    }

    @Override
    public void onBindViewHolder(@NonNull Vacation_history_holder holder, int position) {
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

    public class Vacation_history_holder extends RecyclerView.ViewHolder {
        TextView tvdayoff,tvstatus;
        LinearLayout linearLayout;
        public Vacation_history_holder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.test_item);
            tvdayoff= itemView.findViewById(R.id.tv_dayoff_vacation);
            tvstatus = itemView.findViewById(R.id.tv_status_vacation);
        }
    }
    public static long betweenDates(Date firstDate, Date secondDate) throws IOException
    {
        return ChronoUnit.DAYS.between(firstDate.toInstant(), secondDate.toInstant());
    }
}
