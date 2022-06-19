package com.example.collagemanagementsystem.Model;

public class Batch {
    String batchId,batchName="",departmentId,group,shift,semester,session;

    Batch(){}

    public Batch(String batchId, String batchName, String departmentId, String group, String shift, String semester, String session) {
        this.batchId = batchId;
        this.batchName = batchName;
        this.departmentId = departmentId;
        this.group = group;
        this.shift = shift;
        this.semester = semester;
        this.session = session;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
