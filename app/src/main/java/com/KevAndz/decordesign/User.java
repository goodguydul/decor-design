package com.KevAndz.decordesign;

public class User {
    private String username, email, name, phonenumber, prof_img_url;
    private int id, user_level;
    public User(int id, String username, String email, String name, String phonenumber, String prof_img_url, int user_level) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.phonenumber = phonenumber;
        this.prof_img_url= prof_img_url;
        this.user_level= user_level;

    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPhonenumber() {
        return phonenumber;
    }
    public String getProf_img_url() {
        return prof_img_url;
    }
    public int getUserLevel() {
        return user_level;
    }
}
