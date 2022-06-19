package com.example.collagemanagementsystem.Views.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.collagemanagementsystem.R;
import com.example.collagemanagementsystem.Services.AppBar;
import com.example.collagemanagementsystem.Views.Teacher.TeacherHomeFragment;
import com.example.collagemanagementsystem.Views.Teacher.TeacherNoticeFragment;
import com.example.collagemanagementsystem.Views.Teacher.TeacherProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentMainActivity extends AppCompatActivity {

    private AppBar appBar;
    private Toolbar toolbar;


    private BottomNavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        init();
        appBar.init(toolbar, "Your Classes");
        appBar.hideBackButton();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new StudentHomeFragment()).commit();
        navigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.menu_homeId: {
                        selectedFragment = new StudentHomeFragment();
                        appBar.setAppBarText("Your Classes");
                        break;
                    }
                    case R.id.menu_NoticeId: {
                        selectedFragment = new StudentNoticeFragment();
                        appBar.setAppBarText("Notice");
                        break;
                    }
                    case R.id.menu_teacherProfileId: {
                        selectedFragment = new StudentProfileFragment();
                        appBar.setAppBarText("Profile");
                        break;
                    }
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                        selectedFragment).commit();
                return true;
            }
        });
    }

    private void init() {
        appBar = new AppBar(this);
        toolbar = findViewById(R.id.appbarId);

        //navigation
        navigationView = findViewById(R.id.main_nav);
    }

}