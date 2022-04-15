package com.example.breakdownbreaker;

public class user {
    String name;
    String email;
    String vehicle_no,contact;


    public user(String name, String email,String vehicle_no,String contact) {
        this.name=name;
        this.email=email;
        this.vehicle_no=vehicle_no;
        this.contact=contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public user(){

    }
}
