package com.example.collagemanagementsystem.Views.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collagemanagementsystem.LocalDb.UserDb;
import com.example.collagemanagementsystem.Model.Batch;
import com.example.collagemanagementsystem.Model.Department;
import com.example.collagemanagementsystem.Model.TeacherClass;
import com.example.collagemanagementsystem.R;
import com.example.collagemanagementsystem.Services.AppBar;
import com.example.collagemanagementsystem.api.ApiRef;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class TeacherSingleClassRoomActivitity extends AppCompatActivity {

    private TextView subjectNameTv,subjectCodeTv,classDetailsTv;
    private CardView attendanceCardView,classTestCardView;
    private  String classId="";
    private UserDb userDb;
    private ProgressDialog progressDialog;

    private Toolbar toolbar;
    private AppBar appBar;
    private TeacherClass teacherClass;
    private Batch batch;
    private Department department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_single_class_room_activitity);

        classId=getIntent().getStringExtra("classId");
        init();

        attendanceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(teacherClass!=null){
                    Intent intent=new Intent(TeacherSingleClassRoomActivitity.this,StudentAttendanceActivity.class);
                    intent.putExtra("batchId",teacherClass.getBatchId());
                    intent.putExtra("classId",teacherClass.getClassId());
                    intent.putExtra("subjectCode",teacherClass.getSubjectCode());
                    intent.putExtra("subjectName",teacherClass.getSubjectName());
                    startActivity(intent);
                }
            }
        });


    }
    private  void init(){
        //setup appbar
        toolbar=findViewById(R.id.appBarId);
        appBar=new AppBar(this);
        appBar.init(toolbar,"Class Details");
        //end setup appbar;

        subjectNameTv=findViewById(R.id.ats_subjectNameTvId);
        subjectCodeTv=findViewById(R.id.ats_subjectCodeTvId);
        classDetailsTv=findViewById(R.id.ats_classDetailsTvId);
        attendanceCardView=findViewById(R.id.ats_attendanceCardViewId);
        classTestCardView=findViewById(R.id.ats_classTestCardViewId);
        userDb=new UserDb(this);
        progressDialog=new ProgressDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRef.classRef.child(classId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            teacherClass=dataSnapshot.getValue(TeacherClass.class);
                            subjectNameTv.setText(""+teacherClass.getSubjectName());
                            subjectCodeTv.setText("Subject Code: "+teacherClass.getSubjectCode());

                            //get department details
                            ApiRef.batchRef.child(teacherClass.getBatchId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                         batch=dataSnapshot.getValue(Batch.class);
                                        ApiRef.departmentRef.child(batch.getDepartmentId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){
                                                    progressDialog.dismiss();
                                                     department=dataSnapshot.getValue(Department.class);
                                                    String value="Department: "+department.getDepartmentName()+
                                                            "\n Group:"+batch.getGroup()+
                                                            "\n Shift: "+batch.getShift()+
                                                            "\n Semester:"+batch.getSemester()+
                                                            "\n Session: "+batch.getSession();
                                                    classDetailsTv.setText(value);
                                                }else{
                                                    progressDialog.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }else{
                                        progressDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    progressDialog.dismiss();
                                }
                            });




                        }else{
                            progressDialog.dismiss();
                            finish();
                            Toast.makeText(TeacherSingleClassRoomActivitity.this, "No Class Found.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressDialog.dismiss();
                        Toast.makeText(TeacherSingleClassRoomActivitity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }
}