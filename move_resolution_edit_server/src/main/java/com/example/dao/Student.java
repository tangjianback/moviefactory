package com.example.dao;


public class Student {
    private String Sno;
    private String name;
    private String gender;
    private String  born_data;
    private String born_place;
    private String academy;

    public Student(){

    }
    public Student(String sno, String name, String gender, String born_data, String born_place, String academy) {
        this.Sno = sno;
        this.name = name;
        this.gender = gender;
        this.born_data = born_data;
        this.born_place = born_place;
        this.academy = academy;
    }

    public String getSno() {
        return Sno;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getBorn_data() {
        return born_data;
    }

    public String getBorn_place() {
        return born_place;
    }

    @Override
    public String toString() {
        return "Student{" +
                "Sno='" + Sno + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", born_data='" + born_data + '\'' +
                ", born_place='" + born_place + '\'' +
                ", academy='" + academy + '\'' +
                '}';
    }

    public void setSno(String sno) {
        Sno = sno;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBorn_data(String born_data) {
        this.born_data = born_data;
    }

    public void setBorn_place(String born_place) {
        this.born_place = born_place;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public String getAcademy() {
        return academy;
    }
}
