package com.example.tintooth.UserObject;
import java.io.Serializable;
public class UserObject implements Serializable {
    private String uid, name, phone,gender, description, notificationKey;
    private Boolean selected = false;
    private UserObject(String uid){
        this.uid = uid;
    }
    public UserObject(String name, String phone,String gender,String description ,String notificationKey) {
        this.name = name;
        this.phone = phone;
        this.gender=gender;
        this.description=description;
        this.notificationKey = notificationKey;
    }


    public String getGender() {
        return gender;
    }
    public void setGender(String sex) {
        this.gender = gender;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getNotificationKey() {
        return notificationKey;
    }}