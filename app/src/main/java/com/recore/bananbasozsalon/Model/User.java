package com.recore.bananbasozsalon.Model;

public class User {

    private String username;
    private String userId;
    private String userPhone;
    private String userAvatar;

    private Object userTimeStamp;


    public User() {
    }

    public User(String username, String userId, String userPhone, String userAvatar, Object userTimeStamp) {
        this.username = username;
        this.userId = userId;
        this.userPhone = userPhone;
        this.userAvatar = userAvatar;
        this.userTimeStamp = userTimeStamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public Object getUserTimeStamp() {
        return userTimeStamp;
    }

    public void setUserTimeStamp(Object userTimeStamp) {
        this.userTimeStamp = userTimeStamp;
    }
}
