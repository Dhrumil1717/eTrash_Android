package com.example.dhrumil.test2.Model;

public class TrashProducts {
    String name, description, price, date, image, pid, time;

    public TrashProducts() {
    }

    public TrashProducts(String name, String description, String price, String date, String image, String pid, String time) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.date = date;
        this.image = image;
        this.pid = pid;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
