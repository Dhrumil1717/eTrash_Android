package com.example.dhrumil.test2.Model;

public class CompanyBuys {
    private String Address, City, Date, Time, Name, Phone, State, TotalAmount;

    public CompanyBuys() {
    }

    public CompanyBuys(String address, String city, String date, String time, String name, String phone, String state, String totalAmount) {
        Address = address;
        City = city;
        Date = date;
        Time = time;
        Name = name;
        Phone = phone;
        State = state;
        TotalAmount = totalAmount;


    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }
}
