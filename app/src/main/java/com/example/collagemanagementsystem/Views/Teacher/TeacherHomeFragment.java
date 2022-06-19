package com.example.collagemanagementsystem.Views.Teacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collagemanagementsystem.Adapter.BatchSpinnerAdapter;
import com.example.collagemanagementsystem.Adapter.DepartmentSpinnerAdapter;
import com.example.collagemanagementsystem.Adapter.SpinnerArrayAdapter;
import com.example.collagemanagementsystem.Adapter.SpinnerListAdapter;
import com.example.collagemanagementsystem.Adapter.TeacherClassListAdapter;
import com.example.collagemanagementsystem.Interfaces.CustomDialogClickListner;
import com.example.collagemanagementsystem.LocalDb.UserDb;
import com.example.collagemanagementsystem.Model.Batch;
import com.example.collagemanagementsystem.Model.Department;
import com.example.collagemanagementsystem.Model.Teacher;
import com.example.collagemanagementsystem.Model.TeacherClass;
import com.example.collagemanagementsystem.R;
import com.example.collagemanagementsystem.Services.CustomDialog;
import com.example.collagemanagementsystem.Views.Admin.BatchListActivity;
import com.example.collagemanagementsystem.Views.Admin.StudentListActivity;
import com.example.collagemanagementsystem.api.ApiRef;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeacherHomeFragment extends Fragment {
    public TeacherHomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private FloatingActionButton addNewClassFloatingButton;
    private ProgressDialog progressDialog;
    private CustomDialog customDialog;
    private UserDb userDb;
    List<Batch> batchList=new ArrayList<>();
    List<TeacherClass> teacherClassList=new ArrayList<>();
    private TeacherClassListAdapter teacherClassListAdapter;


    String groupList[]={"A","B"};
    String semesterList[]={"1","2","3","4","5","6","7","8"};
    String shiftList[]={"First","Second"};
    List<String> sessionList=new ArrayList<>();
    List<Department> departmentList=new ArrayList<>();


    //initialize batch variables
    String departmentId="",group="",shift="",semester="",session="";
    boolean departmentFirst=true,groupFirst=true,shiftFirst=true,semesterFirst=true,sessionFirst=true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_teacher_home, container, false);
        init(view);

        for(int i=2012; i<=2040; i++){
            sessionList.add(i+"-"+(i+1));
        }

        teacherClassListAdapter=new TeacherClassListAdapter(getContext(),teacherClassList);
        recyclerView.setAdapter(teacherClassListAdapter);

        addNewClassFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateNewClassDialog();
            }
        });


        teacherClassListAdapter.setOnItemClickListner(new TeacherClassListAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position, TeacherClass teacherClass) {
                Intent intent=new Intent(getActivity(),TeacherSingleClassRoomActivitity.class);
                intent.putExtra("classId",teacherClass.getClassId());
                startActivity(intent);

            }

            @Override
            public void onEdit(int position, TeacherClass teacherClass) {

            }

            @Override
            public void onDelete(int position, TeacherClass teacherClass) {
                customDialog.show("Are You Sure? You Want To Delete This Class?");
                customDialog.onActionClick(new CustomDialogClickListner() {
                    @Override
                    public void onPositiveButtonClicked(View view, AlertDialog dialog) {
                        deleteClass(teacherClass,dialog);
                    }

                    @Override
                    public void onNegativeButtonClicked(View view, AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });


        return view;
    }

    private void init(View view) {
        recyclerView=view.findViewById(R.id.teacherClassListRecyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addNewClassFloatingButton=view.findViewById(R.id.addNewClassFloatingButtonId);
        progressDialog=new ProgressDialog(getContext());
        customDialog=new CustomDialog(getActivity());
        userDb=new UserDb(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        //getBatchList();


        ApiRef.departmentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                if(snapshot.exists()){
                    departmentList.clear();
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        Department department=snapshot1.getValue(Department.class);
                        departmentList.add(department);
                    }
                }else{
                   // Toast.makeText(getContext(), "No Department Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), ""+databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Teacher teacher=userDb.getTeacherData();

        Query query = ApiRef.classRef
                .orderByChild("teacherPhone")
                .equalTo(teacher.getPhone());
            query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        teacherClassList.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            TeacherClass teacherClass=snapshot.getValue(TeacherClass.class);
                            teacherClassList.add(teacherClass);
                            teacherClassListAdapter.notifyDataSetChanged();
                        }
                        progressDialog.dismiss();
                    }else{
                        progressDialog.dismiss();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    private void showCreateNewClassDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View view=getLayoutInflater().inflate(R.layout.create_new_class_dialog_layout,null);
        builder.setView(view);

        EditText subjectNameEt=view.findViewById(R.id.cnc_subjectNameEt);
        EditText subjectCodeEt=view.findViewById(R.id.cnc_subjectCodeEt);

        Spinner departmentSpinner=view.findViewById(R.id.b_d_departmentSpinnerId);
        Spinner groupSpinner=view.findViewById(R.id.b_d_groupSpinnerId);
        Spinner shiftSpinner=view.findViewById(R.id.b_d_shiftSpinnerId);
        Spinner sessionSpinner=view.findViewById(R.id.b_d_sessionSpinnerId);
        Spinner semesterSpinner=view.findViewById(R.id.b_d_semesterSpinnerId);



        SpinnerArrayAdapter groupAdapter=new SpinnerArrayAdapter(getContext(),groupList);
        SpinnerArrayAdapter shiftAdapter=new SpinnerArrayAdapter(getContext(),shiftList);
        SpinnerArrayAdapter semesterAdapter=new SpinnerArrayAdapter(getContext(),semesterList);
        SpinnerListAdapter sessionAdapter=new SpinnerListAdapter(getContext(),sessionList);
        DepartmentSpinnerAdapter departmentAdapter=new DepartmentSpinnerAdapter(getContext(),departmentList);

        groupSpinner.setAdapter(groupAdapter);
        shiftSpinner.setAdapter(shiftAdapter);
        semesterSpinner.setAdapter(semesterAdapter);
        sessionSpinner.setAdapter(sessionAdapter);
        departmentSpinner.setAdapter(departmentAdapter);


        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    departmentId = departmentList.get(i).getDepartmentId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        shiftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    shift = shiftList[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    group = groupList[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    semester = semesterList[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    session=sessionList.get(i);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });








        Button saveButton=view.findViewById(R.id.cnc_addNewClassButton);
        Button cancelButton=view.findViewById(R.id.cnc_cancelClassDialogButton);
        TextView titleTv=view.findViewById(R.id.cnc_TitleTvId);

        final AlertDialog dialog=builder.create();
        dialog.show();


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectCode=subjectCodeEt.getText().toString();
                String subjectName=subjectNameEt.getText().toString();
                if(subjectName.isEmpty()){
                    subjectNameEt.setError("Enter Subject Name.");
                    subjectNameEt.requestFocus();
                }else if(subjectCode.isEmpty()){
                    subjectCodeEt.setError("Enter Subject Code.");
                    subjectCodeEt.requestFocus();
                }else if(departmentId.isEmpty()){
                    Toast.makeText(getContext(), "Please Select Department", Toast.LENGTH_SHORT).show();
                }else if(group.isEmpty()){
                    Toast.makeText(getContext(), "Please Select Group", Toast.LENGTH_SHORT).show();
                }else if(shift.isEmpty()){
                    Toast.makeText(getContext(), "Please Select Shift", Toast.LENGTH_SHORT).show();
                }else if(semester.isEmpty()){
                    Toast.makeText(getContext(), "Please Select Semester", Toast.LENGTH_SHORT).show();
                }else if(session.isEmpty()){
                    Toast.makeText(getContext(), "Please Select Session", Toast.LENGTH_SHORT).show();
                }else{
                    checkBatchExists(subjectCode,subjectName,dialog);
                }


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }



    private  void checkBatchExists(String subjectCode, String subjectName, AlertDialog dialog) {
        progressDialog.setTitle("Please Wait.");
        progressDialog.setMessage("Creating New Class.");
        progressDialog.show();

        ApiRef.batchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    boolean batchExists=false;
                    String batchId="";
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Batch batch=snapshot.getValue(Batch.class);
                        if(batch.getDepartmentId().equals(departmentId) && batch.getGroup().equals(group) && batch.getShift().equals(shift) && batch.getSemester().equals(semester) && batch.getSession().equals(session)){
                            batchExists=true;
                            batchId=batch.getBatchId();
                            break;
                        }
                    }

                    if(batchExists){
                        checkClassExists(subjectCode,subjectName,batchId,dialog);
                    }else{
                        Toast.makeText(getContext(), "No Batch Found With This Department,Group,Shift,Semester,Session ", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }


                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "No Batch Found With This Department,Group,Shift,Semester,Session ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });


    }


    private void checkClassExists(String subjectCode, String subjectName,String batchId, AlertDialog dialog) {
        progressDialog.setTitle("Please Wait.");
        progressDialog.setMessage("Creating New Class.");
        progressDialog.show();

        Query query = ApiRef.classRef
                .orderByChild("subjectCode")
                .equalTo(subjectCode);
       query.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){
                   boolean isExists=false;
                   Teacher teacher=userDb.getTeacherData();
                   for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                       TeacherClass teacherClass=snapshot.getValue(TeacherClass.class);
                       if(teacherClass.getBatchId().equals(batchId) && teacherClass.getTeacherPhone().equals(teacher.getPhone())){
                           isExists=true;
                           break;
                       }
                   }
                   if(isExists){
                       progressDialog.dismiss();
                       Toast.makeText(getContext(), "Already One Class Exists With Same Batch And Same Subject.", Toast.LENGTH_SHORT).show();
                   }else{
                       createClass(subjectCode,subjectName,batchId,dialog);
                   }
               }else{
                   createClass(subjectCode,subjectName,batchId,dialog);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
               Toast.makeText(getContext(), "Class Create Failed.", Toast.LENGTH_SHORT).show();
           }
       });

    }

    private void createClass(String subjectCode, String subjectName,String selectedBatchId, AlertDialog dialog) {
        String classId=ApiRef.classRef.push().getKey();
        Teacher teacher=userDb.getTeacherData();
        TeacherClass teacherClass=new TeacherClass(classId,teacher.getPhone(),selectedBatchId,subjectCode,subjectName);
        ApiRef.classRef.child(classId)
                .setValue(teacherClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            dialog.dismiss();
                            Toast.makeText(getContext(), "New Class Created.", Toast.LENGTH_SHORT).show();

                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Class Create Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void deleteClass(TeacherClass teacherClass,AlertDialog dialog) {
        progressDialog.setTitle("Please Wait.");
        progressDialog.setMessage("Deleting Class.");
        progressDialog.show();
        ApiRef.classRef.child(teacherClass.getClassId())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Class Deleted", Toast.LENGTH_SHORT).show();

                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Class Delete Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}