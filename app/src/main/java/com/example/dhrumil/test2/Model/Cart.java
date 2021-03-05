package com.example.dhrumil.test2.Model;

public class Cart {
    String pid, name, discount, quantity, price, companyname, username;

    public Cart() {
    }

    public Cart(String pid, String name, String discount, String quantity, String price, String companyname, String username) {
        this.pid = pid;
        this.name = name;
        this.discount = discount;
        this.quantity = quantity;
        this.price = price;
        this.companyname = companyname;
        this.username = username;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
