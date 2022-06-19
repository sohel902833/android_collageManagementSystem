package com.example.collagemanagementsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collagemanagementsystem.Model.Student;
import com.example.collagemanagementsystem.R;

import java.util.List;


public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.MyViewHolder>{

    private Context context;
    private List<Student> dataList;
    private  OnItemClickListner listner;

    public StudentAttendanceAdapter(Context context, List<Student> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.student_present_item_layout,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       Student item=dataList.get(position);

       holder.nameTv.setText(""+item.getName());
       holder.rollTv.setText("Roll: "+item.getRoll());
       holder.regTv.setText("Reg: "+item.getRegistration());


        holder.absentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listner!=null){
                    listner.onAbsent(holder.getAdapterPosition(),item);
                }
            }
        });
        holder.presentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listner!=null){
                    listner.onPresent(holder.getAdapterPosition(),item);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameTv,rollTv,regTv;
        Button presentButton,absentButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv=itemView.findViewById(R.id.spi_nameTv);
            rollTv=itemView.findViewById(R.id.spi_RollTv);
            regTv=itemView.findViewById(R.id.spi_RegTv);
            absentButton=itemView.findViewById(R.id.spi_absentButtonId);
            presentButton=itemView.findViewById(R.id.spi_presentButtonId);
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
        void onAbsent(int position,Student student);
        void onPresent(int position,Student student);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        this.listner=listner;
    }


}
