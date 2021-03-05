package com.example.dhrumil.test2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class AboutUs extends AppCompatActivity {
    ViewPager viewPager;

    WormDotsIndicator dotsIndicator;
    FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        viewPager = findViewById(R.id.ViewPager);
        final Integer iniFrag = getIntent().getIntExtra("iniFrag", 0);
        viewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(iniFrag);

        dotsIndicator = findViewById(R.id.worm_dots_indicator);
        viewPager = findViewById(R.id.ViewPager);

        dotsIndicator.setViewPager(viewPager);
    }
}
