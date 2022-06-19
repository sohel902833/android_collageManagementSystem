package com.example.collagemanagementsystem.Model;

public class Student {
    String batchId,roll,registration="",name,phone,email,avatar="";

    public Student(){}
    public Student(String batchId, String roll, String registration, String name, String phone, String email, String avatar) {
        this.batchId = batchId;
        this.roll = roll;
        this.registration=registration;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.avatar = avatar;
    }


    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
