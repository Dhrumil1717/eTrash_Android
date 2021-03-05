package com.example.dhrumil.test2.Model;

public class ModelCompany {
    String company;
    String name;
    String price;
    String quantity;
    String uID;

    public ModelCompany(String company, String name, String price, String quantity, String uID) {
        this.company = company;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.uID = uID;
    }


    public ModelCompany() {
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }
}
