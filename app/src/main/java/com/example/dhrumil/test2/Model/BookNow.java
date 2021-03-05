package com.example.dhrumil.test2.Model;

public class BookNow {
    String address, date, time, name;

    public BookNow() {
    }

    public BookNow(String address, String date, String time, String name) {
        this.address = address;
        this.date = date;
        this.time = time;
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
