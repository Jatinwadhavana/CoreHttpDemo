package com.example.corehttpdemo.employee.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.corehttpdemo.R;
import com.example.corehttpdemo.employee.models.EmployeeModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EmployeeListingAdapter extends RecyclerView.Adapter<EmployeeListingAdapter.EmployeeHolder> {

    private final List<EmployeeModel> employeeModels;
    private final OnRowClicked onRowClicked;

    public static class EmployeeHolder extends RecyclerView.ViewHolder {
        public TextView tvENo, tvEDOB, tvEName;
        private final CheckBox chkRow;
        private final LinearLayout linRowItem;

        public EmployeeHolder(View view) {
            super(view);
            linRowItem = view.findViewById(R.id.linRowItem);
            chkRow = view.findViewById(R.id.chkRow);
            tvENo = view.findViewById(R.id.tvENo);
            tvEName = view.findViewById(R.id.tvEName);
            tvEDOB = view.findViewById(R.id.tvEDOB);
        }
    }

    public EmployeeListingAdapter(List<EmployeeModel> employeeModels,OnRowClicked onRowClicked) {
        this.employeeModels = employeeModels;
        this.onRowClicked = onRowClicked;
    }

    @NotNull
    @Override
    public EmployeeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list_item, parent, false);

        return new EmployeeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EmployeeHolder holder, final int position) {
        final EmployeeModel movie = employeeModels.get(position);

        holder.chkRow.setChecked(employeeModels.get(position).isChecked());
        holder.tvENo.setText(movie.geteNo());
        holder.tvEName.setText(movie.geteName());
        holder.tvEDOB.setText(movie.geteDob());

        holder.linRowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowClicked.onRowClicked(position);
            }
        });
        holder.chkRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employeeModels.get(position).setChecked(!employeeModels.get(position).isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeModels.size();
    }
}