package com.example.myapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.network.response.ListReportResponse;

import java.util.List;

import lombok.NonNull;

public class List_report_date_adapter extends RecyclerView.Adapter<List_report_date_adapter.List_report_date_holder>{
    private Context context;
    private List<ListReportResponse> items;

    public List_report_date_adapter(Context context, List<ListReportResponse> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public List_report_date_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_date_report, parent, false);
        return new List_report_date_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull List_report_date_holder holder, int position) {
        ListReportResponse item = items.get(position);
        holder.tv_timein.setText(item.getTimein());
        holder.tv_timeout.setText(item.getTimeout());
        holder.tv_totalhours.setText(item.getTotalhours());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class List_report_date_holder extends RecyclerView.ViewHolder {
        TextView tv_timein, tv_timeout, tv_totalhours;
        public List_report_date_holder(@NonNull View itemView) {
            super(itemView);
            tv_timein = itemView.findViewById(R.id.tv_timein);
            tv_timeout = itemView.findViewById(R.id.tv_timeout);
            tv_totalhours = itemView.findViewById(R.id.tv_totalhours);
        }
    }
}
