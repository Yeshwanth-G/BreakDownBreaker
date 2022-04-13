package com.example.breakdownbreaker;

public class user {
    String name;
    String email;
    String option,company,exp,link,address,gender,position;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public user(String name, String option, String email, String company, String link, String gender, String position, String exp) {
        this.name = name;
        this.email=email;
        this.option=option;
        this.email=email;
        this.company=company;
        this.link=link;
        this.address=address;
        this.gender=gender;
        this.position=position;
        this.exp=exp;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOption() {
        return option;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public user(){

    }
}
