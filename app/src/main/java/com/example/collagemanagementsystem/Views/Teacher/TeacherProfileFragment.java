package com.example.collagemanagementsystem.Views.Teacher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.collagemanagementsystem.LocalDb.UserDb;
import com.example.collagemanagementsystem.R;
public class TeacherProfileFragment extends Fragment {

    public TeacherProfileFragment() {
        // Required empty public constructor
    }

    private UserDb userDb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_teacher_profile, container, false);
        Button logoutButton=view.findViewById(R.id.teacherLogoutButtonId);
        userDb=new UserDb(getActivity());
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDb.logoutUser(getActivity());
            }
        });

        return  view;
    }
}