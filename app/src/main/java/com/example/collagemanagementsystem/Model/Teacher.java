package com.example.collagemanagementsystem.Model;

public class Teacher {
    String teacherId,name,email,phone,password,code;
    public Teacher(){}

    public Teacher(String teacherId, String name, String email, String phone, String password, String code) {
        this.teacherId = teacherId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.code = code;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
