package com.example.collagemanagementsystem.Views.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collagemanagementsystem.Adapter.DepartmentListAdapter;
import com.example.collagemanagementsystem.Interfaces.CustomDialogClickListner;
import com.example.collagemanagementsystem.Model.Department;
import com.example.collagemanagementsystem.R;
import com.example.collagemanagementsystem.Services.AppBar;
import com.example.collagemanagementsystem.Services.CustomDialog;
import com.example.collagemanagementsystem.api.ApiRef;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DepartmentListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AppBar appBar;
    private RecyclerView recyclerView;
    private FloatingActionButton addDepartmentFloatingButton;

    private List<Department> departmentList=new ArrayList<>();
    private ProgressDialog progressDialog;
    private DepartmentListAdapter departmentListAdapter;

    private CustomDialog customDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_list);

        init();
        departmentListAdapter=new DepartmentListAdapter(this,departmentList);
        recyclerView.setAdapter(departmentListAdapter);

        addDepartmentFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNewDepartmentDialog();
            }
        });

        departmentListAdapter.setOnItemClickListner(new DepartmentListAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onEdit(int position, Department department) {
               showUpdateDepartmentDialog(department);
            }

            @Override
            public void onDelete(int position, Department department) {
                    customDialog.show("Are You Sure?You Want To Delete This Department");
                    customDialog.onActionClick(new CustomDialogClickListner() {
                        @Override
                        public void onPositiveButtonClicked(View view, AlertDialog dialog) {
                            deleteDepartment(department,dialog);
                        }

                        @Override
                        public void onNegativeButtonClicked(View view, AlertDialog dialog) {
                            dialog.dismiss();
                        }
                    });
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog.setTitle("Loading..");
        progressDialog.setMessage("");
        progressDialog.show();

        ApiRef.departmentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                   progressDialog.dismiss();
                   if(snapshot.exists()){
                           departmentList.clear();
                           for(DataSnapshot snapshot1:snapshot.getChildren()){
                               Department department=snapshot1.getValue(Department.class);
                               departmentList.add(department);
                               departmentListAdapter.notifyDataSetChanged();
                           }
                   }else{
                       Toast.makeText(DepartmentListActivity.this, "No Department Found", Toast.LENGTH_SHORT).show();
                   }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(DepartmentListActivity.this, ""+databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void init(){
        appBar=new AppBar(this);
        toolbar=findViewById(R.id.appBarId);
        appBar.init(toolbar,"Department");

        recyclerView=findViewById(R.id.departmentListRecyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addDepartmentFloatingButton=findViewById(R.id.addNewDepartmentFloatingButtonId);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading...");


        customDialog=new CustomDialog(this);

    }

    private void showAddNewDepartmentDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(DepartmentListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.add_department_dialog,null);
        builder.setView(view);

        EditText departmentNameEt=view.findViewById(R.id.departmentNameEt);
        Button saveButton=view.findViewById(R.id.addNewDepartmentButtonId);
        TextView titleTv=view.findViewById(R.id.departmentAddDialogTitleTv);
        final AlertDialog dialog=builder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String departmentName=departmentNameEt.getText().toString();

                if(departmentName.isEmpty()){
                    departmentNameEt.setError("Please Enter Department Name");
                    departmentNameEt.requestFocus();
                }else{
                    saveDepartment(departmentName,dialog);
                }
            }
        });
    }
    private void showUpdateDepartmentDialog(Department department) {
        AlertDialog.Builder builder=new AlertDialog.Builder(DepartmentListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.add_department_dialog,null);
        builder.setView(view);

        EditText departmentNameEt=view.findViewById(R.id.departmentNameEt);
        Button saveButton=view.findViewById(R.id.addNewDepartmentButtonId);
        TextView titleTv=view.findViewById(R.id.departmentAddDialogTitleTv);


        titleTv.setText("Update Department");
        departmentNameEt.setText(""+department.getDepartmentName());
        saveButton.setText("Update");

        final AlertDialog dialog=builder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String departmentName=departmentNameEt.getText().toString();

                if(departmentName.isEmpty()){
                    departmentNameEt.setError("Please Enter Department Name");
                    departmentNameEt.requestFocus();
                }else{
                    updateDepartment(departmentName,department.getDepartmentId(),dialog);
                }
            }
        });
    }

    private void saveDepartment(String departmentName, AlertDialog dialog) {
        progressDialog.setMessage("Creating New Department.");
        progressDialog.setTitle("Please Wait.");
        progressDialog.show();

        String departmentId=String.valueOf(System.currentTimeMillis());

        Department department=new Department(departmentId,departmentName);
        ApiRef.departmentRef.child(departmentId)
                .setValue(department)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            progressDialog.dismiss();
                            Toast.makeText(DepartmentListActivity.this, "New Department Created.", Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(DepartmentListActivity.this, "Department Creating Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
    private void updateDepartment(String departmentName,String departmentId, AlertDialog dialog) {
        progressDialog.setMessage("Updating Department.");
        progressDialog.setTitle("Please Wait.");
        progressDialog.show();

        HashMap<String,Object> updateMap=new HashMap<>();
        updateMap.put("departmentName",departmentName);
        ApiRef.departmentRef.child(departmentId)
                .updateChildren(updateMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            progressDialog.dismiss();
                            Toast.makeText(DepartmentListActivity.this, "Department Updated.", Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(DepartmentListActivity.this, "Department Update Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
    private void deleteDepartment(Department department,AlertDialog dialog) {
        progressDialog.setMessage("Deleting Department.");
        progressDialog.setTitle("Please Wait.");
        progressDialog.show();

        ApiRef.departmentRef.child(department.getDepartmentId())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            progressDialog.dismiss();
                            Toast.makeText(DepartmentListActivity.this, "Department Deleted.", Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(DepartmentListActivity.this, "Department Delete Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}