package com.example.googlesignin;
public class helperclass {
 String company,title,location,salary,duration,gender,link,posted_by;
public helperclass(){
}
    public helperclass(String company,String title,String location,String salary,String duration,String gender,String link,String posted_by){
        this.company = company;
        this.duration=duration;
        this.gender=gender;
        this.link=link;
        this.salary=salary;
        this.title=title;
        this.location=location;
        this.posted_by=posted_by;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public void setPosted_by(String posted_by) {
        this.posted_by = posted_by;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getLocation() {
        return location;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
