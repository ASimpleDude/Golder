package com.example.tintooth.Cards;

public class cards {
    private String userId;
    private String name;
    private String profileImageUrl;
    private String gender;
    private String description;
    private String phone;

    public cards(String userId){
        this.userId = userId;
    }

    public cards(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public cards(String userId, String name, String profileImageUrl, String gender, String description, String phone) {
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.gender = gender;
        this.description = description;
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
