package com.example.dhrumil.test2.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.dhrumil.test2.R;


public class Fragment2 extends Fragment {


    static Fragment2 fragment2;

    public static Fragment2 newInstance() {
        fragment2 = new Fragment2();
        return fragment2;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment2, container, false);
    }

}
