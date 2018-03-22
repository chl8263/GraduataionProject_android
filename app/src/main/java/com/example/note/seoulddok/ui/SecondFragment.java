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

public class SecondFragment extends Fragment {

    public SecondFragment() {

    }
    public static SecondFragment getInstance(){
        Bundle args = new Bundle();
        SecondFragment fragment = new SecondFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.secondfragment,container,false);
        return view;
    }
}
