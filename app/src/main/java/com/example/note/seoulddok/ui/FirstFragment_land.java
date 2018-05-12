package com.example.note.seoulddok.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.note.seoulddok.R;


/**
 * Created by gyun_home on 2018-03-18.
 */

public class FirstFragment_land extends Fragment {

    public FirstFragment_land() {

    }
    public static FirstFragment_land getInstance(){
        Bundle args = new Bundle();
        FirstFragment_land fragment = new FirstFragment_land();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thirdfragment,container,false);
        return view;
    }
}
