package com.example.dhrumil.test2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dhrumil.test2.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CompanyAddNewItem extends AppCompatActivity {
    String CategoryName, description, price, pname, saveCurrentDate, saveCurrentTime, productRandomKey, DownloadImageUrl, stock;
    Button addproduct;
    ImageView productimage;
    EditText productname, productdescription, productprice, productstock;
    Uri ImageUri;
    StorageReference ProductImagesRef;
    DatabaseReference ProductRef;
    ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_add_new_item);

        CategoryName = getIntent().getExtras().get("category").toString();
        //it will create a storage with the name of product Images
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        addproduct = findViewById(R.id.addproduct);
        productname = findViewById(R.id.product_name);
        productstock = findViewById(R.id.product_stock);
        productdescription = findViewById(R.id.productdescription);
        productprice = findViewById(R.id.productprice);
        productimage = findViewById(R.id.select_product_image);
        loadingbar = new ProgressDialog(this);


        productimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });


    }

    private void openGallery() {

        Intent ii = new Intent();
        ii.setAction(Intent.ACTION_GET_CONTENT);
        ii.setType("image/*");
        startActivityForResult(ii, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            productimage.setImageURI(ImageUri);
        }

    }

    private void ValidateProductData() {

        description = productdescription.getText().toString();
        price = productprice.getText().toString();
        pname = productname.getText().toString();
        stock = productstock.getText().toString();

        if (ImageUri == null) {
            Toast.makeText(this, "Product Image is Mandatory", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Description is Mandatory", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(pname)) {
            Toast.makeText(this, "Product Name is Mandatory", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Price is Mandatory", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(stock)) {
            Toast.makeText(this, "Add Available Stock ", Toast.LENGTH_SHORT).show();
        } else {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {
        loadingbar.setTitle("Adding new product");
        loadingbar.setMessage("Please wait, while we add your products to the database");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd,MM,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());


        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filepath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filepath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(CompanyAddNewItem.this, "Error occured due to " + message, Toast.LENGTH_LONG).show();
                loadingbar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CompanyAddNewItem.this, "Product Image uploaded successfully", Toast.LENGTH_LONG).show();


                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        DownloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                })
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    DownloadImageUrl = task.getResult().toString();
                                    Toast.makeText(CompanyAddNewItem.this, "Getting product image url successfully..", Toast.LENGTH_SHORT).show();
                                    SaveProductInfoToDatabase();
                                }
                            }
                        });
            }
        });
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("companyname", Prevalent.currentOnlineCompany.getCompanyName());
        productMap.put("stock", stock);
        productMap.put("pid", productRandomKey);
        productMap.put("name", pname);
        productMap.put("category", CategoryName);
        productMap.put("price", price);
        productMap.put("description", description);
        productMap.put("image", DownloadImageUrl);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);

        ProductRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), CompanyCategories.class));
                    loadingbar.dismiss();
                    Toast.makeText(CompanyAddNewItem.this, "Product is added successfully to database..", Toast.LENGTH_SHORT).show();
                } else {
                    loadingbar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(CompanyAddNewItem.this, "This Error occured" + message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
