package com.example.collagemanagementsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collagemanagementsystem.Model.DashboardDataModel;
import com.example.collagemanagementsystem.Model.Department;
import com.example.collagemanagementsystem.R;

import java.util.List;


public class DepartmentListAdapter extends RecyclerView.Adapter<DepartmentListAdapter.MyViewHolder>{

    private Context context;
    private List<Department> dataList;
    private  OnItemClickListner listner;

    public DepartmentListAdapter(Context context, List<Department> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.department_item_layout,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       Department item=dataList.get(position);
        holder.depNameTv.setText(""+item.getDepartmentName());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listner!=null){
                    listner.onDelete(holder.getAdapterPosition(),item);
                }
            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listner!=null){
                    listner.onEdit(holder.getAdapterPosition(),item);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        TextView depNameTv;
        Button editButton,deleteButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            depNameTv=itemView.findViewById(R.id.di_departmentNameTvId);
            editButton=itemView.findViewById(R.id.di_editButtonId);
            deleteButton=itemView.findViewById(R.id.di_deleteButtonId);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listner!=null){
                int position=getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    listner.onItemClick(position);
                }
            }
        }

    }
    public interface  OnItemClickListner{
        void onItemClick(int position);
        void onEdit(int position,Department department);
        void onDelete(int position,Department department);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        this.listner=listner;
    }


}
