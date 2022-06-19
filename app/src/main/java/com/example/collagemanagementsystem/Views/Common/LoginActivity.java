package com.example.collagemanagementsystem.Views.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.collagemanagementsystem.LocalDb.UserDb;
import com.example.collagemanagementsystem.Model.Teacher;
import com.example.collagemanagementsystem.Model.User;
import com.example.collagemanagementsystem.R;
import com.example.collagemanagementsystem.Services.AppBar;
import com.example.collagemanagementsystem.Views.Admin.AdminMainActivity;
import com.example.collagemanagementsystem.Views.Teacher.TeacherMainActivity;
import com.example.collagemanagementsystem.api.ApiRef;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private  String STATE="teacher";

    private Toolbar toolbar;
    private AppBar appBar;
    private EditText phoneEt,passwordEt;
    private Button teacherLoginButton,studentLoginButton;
    private static  final String ADMIN_PHONE="zubu";
    private static  final String ADMIN_PASSWORD="zubu";


    //other variables

    UserDb userDb;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        changeState(STATE);


        studentLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(STATE.equals("student")){
                    changeState("teacher");
                }else{
                    changeState("student");
                }

            }
        });


        teacherLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone=phoneEt.getText().toString();
                String password=passwordEt.getText().toString().trim();
                if(phone.isEmpty()){
                    if(STATE.equals("student")){
                        phoneEt.setError("Enter Your ID");
                        phoneEt.requestFocus();
                    }
                }else if(password.isEmpty()){
                    passwordEt.setError("Enter Your Password");
                    passwordEt.requestFocus();
                }else{
                    loginUser(phone,password);
                }
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        User user=userDb.getUserData();
        if(user!=null) {
            if (user.getUserType().equals(User.ADMIN)) {
                sendUserToAdminMainActivity();
            }if (user.getUserType().equals(User.TEACHER)) {
                sendUserToTeacherMainActivity();
            }
        }
    }

    private void init(){
        //setup appbar
        toolbar=findViewById(R.id.appBarId);
        appBar=new AppBar(this);
        appBar.init(toolbar,"Login");
        appBar.hideBackButton();
        //end setup appbar

        //finding components

        phoneEt=findViewById(R.id.l_poneEt);
        passwordEt=findViewById(R.id.l_passwordEt);
        studentLoginButton=findViewById(R.id.l_studentLoginButton);
        teacherLoginButton=findViewById(R.id.l_teacherLoginButton);


        //initialize others
        userDb=new UserDb(this);
        progressDialog=new ProgressDialog(this);


    }




    private void changeState(String s) {
        STATE=s;
        if(STATE.equals("student")){
            phoneEt.setHint("Enter Your ID");
            teacherLoginButton.setText("Continue As a Student");
            studentLoginButton.setText("Continue As a Teacher");
        }else if(STATE.equals("teacher")){
            phoneEt.setHint("Phone Number");
            studentLoginButton.setText("Continue As a Student");
            teacherLoginButton.setText("Continue As a Teacher");
        }
    }

    private void loginUser(String phone, String password) {
        //check admin or not
        if(phone.equals(ADMIN_PHONE) && password.equals(ADMIN_PASSWORD)){
            //this user is admin
            User user=new User(phone,password,User.ADMIN);
            userDb.setUserData(user);
            sendUserToAdminMainActivity();
        }else{
            //check user trying to login as a teacher or student
            if(STATE.equals("student")){
                //this user trying to login as a student check student db
                Toast.makeText(this, "Student Login", Toast.LENGTH_SHORT).show();
            }else if(STATE.equals("teacher")){
                //this user trying to login as a teacher check teacher db
                loginTeacher(phone,password);
                Toast.makeText(this, "Teacher Login", Toast.LENGTH_SHORT).show();

            }
        }


    }

    private void loginTeacher(String phone, String password) {
        progressDialog.setMessage("Login Teacher");
        progressDialog.setTitle("Please Wait.");

        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiRef.teacherRef.child(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Teacher teacher=dataSnapshot.getValue(Teacher.class);
                            if(teacher.getPassword().equals(password)){
                                User user =new User(teacher.getPhone(),teacher.getPassword(),User.TEACHER);
                                userDb.setUserData(user);
                                userDb.setTeacherData(teacher);
                                sendUserToTeacherMainActivity();
                                Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Password Doesn't Matched.", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "No Teacher Found With This Phone Number", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void sendUserToTeacherMainActivity() {
        Intent intent=new Intent(LoginActivity.this, TeacherMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void sendUserToAdminMainActivity() {
        Intent intent=new Intent(LoginActivity.this, AdminMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}