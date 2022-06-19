package com.example.collagemanagementsystem.Views.Teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.collagemanagementsystem.R;

public class TeacherSingleClassRoomActivitity extends AppCompatActivity {

    private TextView subjectNameTv,subjectCodeTv,classDetailsTv;
    private CardView attendanceCardView,classTestCardView;
    private  String classId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_single_class_room_activitity);

        classId=getIntent().getStringExtra("classId");
        init();

    }
    private  void init(){
        subjectNameTv=findViewById(R.id.ats_subjectNameTvId);
        subjectCodeTv=findViewById(R.id.ats_subjectCodeTvId);
        classDetailsTv=findViewById(R.id.ats_classDetailsTvId);
        attendanceCardView=findViewById(R.id.ats_attendanceCardViewId);
        classTestCardView=findViewById(R.id.ats_classTestCardViewId);
    }
}