package com.example.collagemanagementsystem.Model;

public class Notice {
    String noticeId,description="",image="",date;

    public Notice(){}
    public Notice(String noticeId, String description, String image,String date) {
        this.noticeId = noticeId;
        this.description = description;
        this.image = image;
        this.date = date;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
