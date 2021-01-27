package com.example.habii.nbmreadingapp;

public class ReadBy extends PostID {

    String email,name,pic,time;

    public ReadBy(){}

    public ReadBy(String email, String name, String pic, String time) {
        this.email = email;
        this.name = name;
        this.pic = pic;
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
