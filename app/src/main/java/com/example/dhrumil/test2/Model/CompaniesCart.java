package com.example.dhrumil.test2.Model;

public class CompaniesCart {
    String pid, name, discount, quantity, price, username;

    public CompaniesCart() {
    }

    public CompaniesCart(String pid, String name, String discount, String quantity, String price, String username) {
        this.pid = pid;
        this.name = name;
        this.discount = discount;
        this.quantity = quantity;
        this.price = price;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
