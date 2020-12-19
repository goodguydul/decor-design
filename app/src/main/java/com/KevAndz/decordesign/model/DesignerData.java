package com.KevAndz.decordesign.model;

public class DesignerData {

    private int id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String gender;
    private String birthdate;
    private String pictureimg_url;
    private String CV_url;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNames() {
        return name;
    }
    public void setNames(String name) {
        this.name = name;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
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
    public String getGender() { return gender; }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getBirthDate() { return birthdate; }
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
    public String getPictureimg_url() { return pictureimg_url; }
    public void setPictureimg_url(String pictureimg_url) {
        this.pictureimg_url = pictureimg_url;
    }
    public String getCV_url() { return CV_url; }
    public void setCV_url(String CV_url) {
        this.CV_url = CV_url;
    }
}
