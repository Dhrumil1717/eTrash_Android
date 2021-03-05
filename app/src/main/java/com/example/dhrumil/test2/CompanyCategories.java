package com.example.dhrumil.test2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import io.paperdb.Paper;


public class CompanyCategories extends AppCompatActivity {
    Button LogoutBtn, CheckOrderBtn, buy, settings;
    private LinearLayout computer, keyboard, printer, scanner;
    private LinearLayout smartwatch, battery, cable, mic;
    private LinearLayout headphone, console, lights, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_categories);

        computer = findViewById(R.id.computer);
        keyboard = findViewById(R.id.keyboard);
        printer = findViewById(R.id.printer);
        scanner = findViewById(R.id.scanner);
        smartwatch = findViewById(R.id.smartwatch);
        battery = findViewById(R.id.battery);
        cable = findViewById(R.id.cable);
        mic = findViewById(R.id.mic);
        headphone = findViewById(R.id.headphones);
        console = findViewById(R.id.console);
        lights = findViewById(R.id.lights);
        mobile = findViewById(R.id.mobile);

        buy = findViewById(R.id.buy);
        settings = findViewById(R.id.settings);
        LogoutBtn = findViewById(R.id.logout_btn);
        CheckOrderBtn = findViewById(R.id.check_orders_btn);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CompanyCategories.this, SettingsActivity.class));
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CompanyBuy.class));
            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             logout();

                                         }
                                     }
        );

        CheckOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), CompanyNewOrdersActivity.class);
                startActivity(ii);
            }
        });

        computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), CompanyAddNewItem.class);
                ii.putExtra("category", "computer");
                startActivity(ii);
            }
        });

        lights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), CompanyAddNewItem.class);
                ii.putExtra("category", "lights");
                startActivity(ii);
            }
        });

        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), CompanyAddNewItem.class);
                ii.putExtra("category", "keyboard");
                startActivity(ii);
            }
        });

        printer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), CompanyAddNewItem.class);
                ii.putExtra("category", "printer");
                startActivity(ii);
            }
        });

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), CompanyAddNewItem.class);
                ii.putExtra("category", "scanner");
                startActivity(ii);
            }
        });

        smartwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), CompanyAddNewItem.class);
                ii.putExtra("category", "smartwatch");
                startActivity(ii);
            }
        });


        battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), CompanyAddNewItem.class);
                ii.putExtra("category", "battery");
                startActivity(ii);
            }
        });

        cable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), CompanyAddNewItem.class);
                ii.putExtra("category", "cable");
                startActivity(ii);
            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), CompanyAddNewItem.class);
                ii.putExtra("category", "mic");
                startActivity(ii);
            }
        });

        headphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), CompanyAddNewItem.class);
                ii.putExtra("category", "headphone");
                startActivity(ii);
            }
        });

        console.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), CompanyAddNewItem.class);
                ii.putExtra("category", "console");
                startActivity(ii);
            }
        });


        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), CompanyAddNewItem.class);
                ii.putExtra("category", "mobile");
                startActivity(ii);
            }
        });

    }

    private void logout() {
        Paper.init(this);
        Paper.book().destroy();
        finish();

        Intent ii = new Intent(getApplicationContext(), LoginActivity.class);
        ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(ii);
        finish();

    }
  /*  public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }*/
}
