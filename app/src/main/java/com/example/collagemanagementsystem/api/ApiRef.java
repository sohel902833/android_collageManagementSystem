package com.example.collagemanagementsystem.api;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ApiRef {
    public static final DatabaseReference departmentRef= FirebaseDatabase.getInstance().getReference().child("CMG").child("Department");
    public static final DatabaseReference teacherRef= FirebaseDatabase.getInstance().getReference().child("CMG").child("Teachers");
    public static final DatabaseReference studentRef= FirebaseDatabase.getInstance().getReference().child("CMG").child("Student");
    public static final DatabaseReference batchRef= FirebaseDatabase.getInstance().getReference().child("CMG").child("Batches");
    public static final DatabaseReference noticeRef= FirebaseDatabase.getInstance().getReference().child("CMG").child("Notice");
    public static final DatabaseReference classRef= FirebaseDatabase.getInstance().getReference().child("CMG").child("Class");
}
