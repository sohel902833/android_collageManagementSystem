package com.example.collagemanagementsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collagemanagementsystem.Model.Batch;
import com.example.collagemanagementsystem.Model.Department;
import com.example.collagemanagementsystem.Model.Teacher;
import com.example.collagemanagementsystem.R;
import com.example.collagemanagementsystem.api.ApiRef;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class BatchListAdapter extends RecyclerView.Adapter<BatchListAdapter.MyViewHolder>{

    private Context context;
    private List<Batch> dataList;
    private  OnItemClickListner listner;

    public BatchListAdapter(Context context, List<Batch> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.batch_item_layout,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       Batch item=dataList.get(position);

        ApiRef.departmentRef.child(item.getDepartmentId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Department department=dataSnapshot.getValue(Department.class);
                    holder.departmentTv.setText(""+department.getDepartmentName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.groupTv.setText("Group:"+item.getGroup());
        holder.shiftTv.setText("Shift: "+item.getShift());
        holder.semesterTv.setText("Sem: "+item.getSemester());
        holder.sessionTv.setText("Session: "+item.getSession());
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
        TextView departmentTv,groupTv,shiftTv,semesterTv,sessionTv;
        Button editButton,deleteButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            departmentTv=itemView.findViewById(R.id.bi_departmentTv);
            groupTv=itemView.findViewById(R.id.bi_groupTv);
            shiftTv=itemView.findViewById(R.id.bi_shiftTv);
            semesterTv=itemView.findViewById(R.id.bi_semesterTv);
            sessionTv=itemView.findViewById(R.id.bi_sessionTv);
            editButton=itemView.findViewById(R.id.bi_editButtonId);
            deleteButton=itemView.findViewById(R.id.bi_deleteButtonId);
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
        void onEdit(int position,Batch batch);
        void onDelete(int position,Batch batch);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        this.listner=listner;
    }


}
