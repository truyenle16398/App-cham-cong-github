package com.example.myapplication.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.network.response.ListRegisterForLeaveResponse;

import java.util.List;

import lombok.NonNull;

public class List_register_for_leave_adapter extends RecyclerView.Adapter<List_register_for_leave_adapter.List_register_for_leave_holder> {

    private Context context;
    private List<ListRegisterForLeaveResponse> items;

    public List_register_for_leave_adapter(Context context, List<ListRegisterForLeaveResponse> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public List_register_for_leave_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_register_for_leave, parent, false);
        return new List_register_for_leave_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull List_register_for_leave_holder holder, int position) {
        ListRegisterForLeaveResponse item = items.get(position);
        holder.tv_emp_list.setText(item.getEmployee());
        holder.tv_type_list.setText(item.getType());
        holder.tv_leavefrom_list.setText(item.getLeavefrom());
        holder.tv_leaveto_list.setText(item.getLeaveto());
        holder.tv_returndate_list.setText(item.getReturndate());
        holder.tv_status_list.setText(item.getStatus());
        holder.tv_comment_list.setText(item.getComment());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class List_register_for_leave_holder extends RecyclerView.ViewHolder {
        TextView tv_emp_list, tv_type_list, tv_leavefrom_list, tv_leaveto_list, tv_returndate_list, tv_status_list, tv_comment_list;
        public List_register_for_leave_holder(@NonNull View itemView) {
            super(itemView);
            tv_emp_list = itemView.findViewById(R.id.tv_emp_list);
            tv_type_list = itemView.findViewById(R.id.tv_type_list);
            tv_leavefrom_list = itemView.findViewById(R.id.tv_leavefrom_list);
            tv_leaveto_list = itemView.findViewById(R.id.tv_leaveto_list);
            tv_returndate_list = itemView.findViewById(R.id.tv_returndate_list);
            tv_status_list = itemView.findViewById(R.id.tv_status_list);
            tv_comment_list = itemView.findViewById(R.id.tv_comment_list);
        }
    }
}
