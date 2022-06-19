package com.example.collagemanagementsystem.Views.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collagemanagementsystem.LocalDb.UserDb;
import com.example.collagemanagementsystem.Model.Teacher;
import com.example.collagemanagementsystem.R;
import com.example.collagemanagementsystem.Services.AppBar;
import com.example.collagemanagementsystem.api.ApiRef;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddNewTeacherActivity extends AppCompatActivity {
    public static final String ACTION_EDIT="edit";

    private Toolbar toolbar;
    private AppBar appBar;

    private EditText teacherNameEt,teacherPhoneEt,teacherIdEt,teacherPasswordEt,emailEt;
    private Button createTeacherButton;
    private TextView titleTv;

    private ProgressDialog progressDialog;
    String email="";

     String action="";
     String editUserPhone="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_teacher);
        init();
        action=getIntent().getStringExtra("action");
        editUserPhone=getIntent().getStringExtra("phone");

        if(action.equals(ACTION_EDIT)){
            titleTv.setText("Edit Teacher");
            createTeacherButton.setText("Update Teacher");
            teacherPhoneEt.setVisibility(View.GONE);
            teacherIdEt.setVisibility(View.GONE);
        }

        teacherIdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String text=charSequence.toString();
                    teacherPasswordEt.setText(""+text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        createTeacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String teacherName=teacherNameEt.getText().toString();
                String teacherId=teacherIdEt.getText().toString();
                String teacherPhone=teacherPhoneEt.getText().toString();
                String password=teacherPasswordEt.getText().toString().trim();
                 email=emailEt.getText().toString().trim();
                if(teacherName.isEmpty()){
                    teacherNameEt.setError("Enter Teacher Name.");
                    teacherNameEt.requestFocus();
                }else if(teacherId.isEmpty()){
                    teacherIdEt.setError("Enter Teacher Id");
                    teacherIdEt.requestFocus();
                }else if(teacherPhone.isEmpty()){
                    teacherPhoneEt.setError("Enter Teacher Phone Number.");
                    teacherPhoneEt.requestFocus();
                }else if(password.isEmpty()){
                    teacherPasswordEt.setError("Enter Password.");
                    teacherPasswordEt.requestFocus();
                }else{
                    if(action.equals(ACTION_EDIT)){
                        updateTeacher(teacherName,teacherId,teacherPhone.trim(),password);
                    }else{
                        createTeacher(teacherName,teacherId,teacherPhone.trim(),password);
                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(action.equals(ACTION_EDIT)) {
            progressDialog.setMessage("Loading..");
            progressDialog.show();
            ApiRef.teacherRef.child(editUserPhone).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    if (dataSnapshot.exists()) {
                        Teacher teacher = dataSnapshot.getValue(Teacher.class);
                        teacherNameEt.setText("" + teacher.getName());
                        teacherPhoneEt.setText("" + teacher.getPhone());
                        emailEt.setText("" + teacher.getEmail());
                        teacherPasswordEt.setText("" + teacher.getPassword());
                        teacherIdEt.setText("" + teacher.getCode());
                    } else {
                        Toast.makeText(AddNewTeacherActivity.this, "No Teacher Found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(AddNewTeacherActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private void init(){
        //setup appbar
        toolbar=findViewById(R.id.appBarId);
        appBar=new AppBar(this);
        appBar.init(toolbar,"Add New Teacher");
        appBar.hideBackButton();
        //end setup appbar;

        teacherIdEt=findViewById(R.id.ant_teacherId);
        teacherNameEt=findViewById(R.id.ant_teacherNameEt);
        teacherPhoneEt=findViewById(R.id.ant_teacherPhoneEt);
        teacherPasswordEt=findViewById(R.id.ant_PasswordEt);
        createTeacherButton=findViewById(R.id.ant_AddTeacherButtonId);
        emailEt=findViewById(R.id.ant_teacherEmailEt);
        titleTv=findViewById(R.id.ant_TitleTv);

        progressDialog=new ProgressDialog(this);



    }
    private void createTeacher(String teacherName, String code, String phone, String password) {
        progressDialog.setMessage("Creating New Teacher..");
        progressDialog.setTitle("Please Wait..");
        progressDialog.show();

        //check already teacher exists or not with same phone number and id
        Query query = ApiRef.teacherRef
                .orderByChild("phone")
                .equalTo(phone);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    progressDialog.dismiss();
                    Toast.makeText(AddNewTeacherActivity.this, "Teacher already Exists With This Phone", Toast.LENGTH_SHORT).show();
                }else{
                    //no teacher exists using this phone or id
                    String teacherId=ApiRef.teacherRef.push().getKey();
                    Teacher teacher=new Teacher(teacherId,teacherName,email,phone,password,code);
                    ApiRef.teacherRef.child(phone)
                            .setValue(teacher).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        Toast.makeText(AddNewTeacherActivity.this, "Teacher Created", Toast.LENGTH_SHORT).show();
                                        teacherNameEt.setText("");
                                        emailEt.setText("");
                                        teacherPhoneEt.setText("");
                                        teacherIdEt.setText("");
                                        teacherPasswordEt.setText("");
                                    }else{
                                        Toast.makeText(AddNewTeacherActivity.this, "Teacher Create Failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(AddNewTeacherActivity.this, "Teacher Create Failed.", Toast.LENGTH_SHORT).show();
            }
        });





    }
    private void updateTeacher(String teacherName, String code, String phone, String password) {
        progressDialog.setMessage("Updating Teacher..");
        progressDialog.setTitle("Please Wait..");
        progressDialog.show();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("name",teacherName);
        hashMap.put("email",email);
        hashMap.put("password",password);
        ApiRef.teacherRef.child(phone)
                .updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(AddNewTeacherActivity.this, "Teacher Updated.", Toast.LENGTH_SHORT).show();
                            teacherNameEt.setText("");
                            emailEt.setText("");
                            teacherPhoneEt.setText("");
                            teacherIdEt.setText("");
                            teacherPasswordEt.setText("");
                            finish();
                        }else{
                            Toast.makeText(AddNewTeacherActivity.this, "Teacher Update Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




    }

}