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

public class ThirdFragment extends Fragment {

    public ThirdFragment() {

    }
    public static ThirdFragment getInstance(){
        Bundle args = new Bundle();
        ThirdFragment fragment = new ThirdFragment();
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
