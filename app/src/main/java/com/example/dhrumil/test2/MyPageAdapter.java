package com.example.dhrumil.test2;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.dhrumil.test2.Fragment.Fragment1;
import com.example.dhrumil.test2.Fragment.Fragment2;
import com.example.dhrumil.test2.Fragment.Fragment3;

class MyPageAdapter extends FragmentPagerAdapter {
    public MyPageAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);

    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return Fragment1.newInstance();

            case 1:
                return Fragment2.newInstance();

            case 2:
                return Fragment3.newInstance();

            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return 3;
    }


}
