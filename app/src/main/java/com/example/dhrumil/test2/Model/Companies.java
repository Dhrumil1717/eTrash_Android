package com.example.dhrumil.test2.Model;

public class Companies {
    private String CompanyName, Email, GstNo, Password, PhoneNumber, image, address;

    public Companies() {

    }

    public Companies(String companyName, String email, String gstNo, String password, String phoneNumber, String image, String address) {
        CompanyName = companyName;
        Email = email;
        GstNo = gstNo;
        Password = password;
        PhoneNumber = phoneNumber;
        this.image = image;
        this.address = address;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGstNo() {
        return GstNo;
    }

    public void setGstNo(String gstNo) {
        GstNo = gstNo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
