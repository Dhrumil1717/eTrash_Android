package com.example.dhrumil.test2.Prevalent;

import com.example.dhrumil.test2.Model.Companies;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Prevalent {
    public static final String UserPhoneKey = "CompanyName";
    public static final String UserPasswordKey = "Password";
    public static Companies currentOnlineCompany = new Companies();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Companies");
}
