package com.example.dhrumil.test2.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.dhrumil.test2.R;


public class Fragment1 extends Fragment {

    static Fragment1 fragment1;

    public static Fragment1 newInstance() {
        fragment1 = new Fragment1();
        return fragment1;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment1, container, false);
    }

}
