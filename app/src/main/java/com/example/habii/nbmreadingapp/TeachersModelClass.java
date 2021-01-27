package com.example.habii.nbmreadingapp;

public class TeachersModelClass  extends  PostID{

    String name,initial,depertment,pic,email,phone,id;


    public TeachersModelClass(){}

    public TeachersModelClass(String name, String initial, String depertment, String pic, String email, String phone, String id) {
        this.name = name;
        this.initial = initial;
        this.depertment = depertment;
        this.pic = pic;
        this.email = email;
        this.phone = phone;
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getDepertment() {
        return depertment;
    }

    public void setDepertment(String depertment) {
        this.depertment = depertment;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
