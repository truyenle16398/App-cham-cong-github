package com.example.myapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.model.itemah;

import java.util.List;

public class Attendance_history_adapter extends RecyclerView.Adapter<Attendance_history_adapter.Attendance_history_holder> {

    private Context context;
    private List<itemah> items;

    public Attendance_history_adapter(Context context, List<itemah> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Attendance_history_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_attendance_history,parent,false);
        return new Attendance_history_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Attendance_history_holder holder, int position) {
        itemah item = items.get(position);
        holder.tvdate.setText(item.getDate());
        holder.tvcheckin.setText(item.getTimein());
        holder.tvcheckout.setText(item.getTimeout());
        holder.tvsumtime.setText(item.getTotalhours());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Attendance_history_holder extends RecyclerView.ViewHolder {
        TextView tvdate,tvcheckin,tvcheckout,tvsumtime;
        public Attendance_history_holder(@NonNull View itemView) {
            super(itemView);
            tvdate = itemView.findViewById(R.id.tv_date_h);
            tvcheckin = itemView.findViewById(R.id.tv_checkin_h);
            tvcheckout = itemView.findViewById(R.id.tv_checkout_h);
            tvsumtime = itemView.findViewById(R.id.tv_sumtime_h);
        }
    }
}
