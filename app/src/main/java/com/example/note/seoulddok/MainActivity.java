package com.example.note.seoulddok;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.note.seoulddok.network.PahoClient;
import com.example.note.seoulddok.ui.FirstFragment;
import com.example.note.seoulddok.ui.SecondFragment;
import com.example.note.seoulddok.ui.ThirdFragment;

import java.util.List;

/**
 * Created by Note on 2018-03-22.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private ThirdFragment thirdFragment;
    private PahoClient pahoClient;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        checkPermision(Contact.PERMISSIONS);
        init();
        initFragment();

        pahoClient = PahoClient.getInstance();
        pahoClient.setContext(getApplicationContext());
        pahoClient.start();
    }

    @Override
    public void onClick(View view) {

    }

    public void init() {

    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "종료하시려면 한번더 눌러주세요", Toast.LENGTH_SHORT).show();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initStatusbar() {
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#@color/colorPrimary"));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermision(String[] permision) {
        requestPermissions(permision, MY_PERMISSION_REQUEST_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                int cnt = permissions.length;
                for (int i = 0; i < cnt; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    } else {
                    }
                }
                break;
        }
    }

    //////////-------------/////////   fragmnt 설정부분    ////////////////------------------///////
    public void initFragment() {
        onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navi_first:
                        SelectNavView("first");
                        return true;
                    case R.id.navi_second:
                        SelectNavView("second");
                        return true;
                    case R.id.navi_third:
                        SelectNavView("third");
                        return true;
                }
                return false;
            }
        };

        firstFragment = FirstFragment.getInstance();
        secondFragment = SecondFragment.getInstance();
        thirdFragment = ThirdFragment.getInstance();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.fragment, firstFragment, "first");
        fragmentTransaction.add(R.id.fragment, secondFragment, "second");
        fragmentTransaction.add(R.id.fragment, thirdFragment, "third");
        fragmentTransaction.hide(secondFragment);
        fragmentTransaction.hide(thirdFragment);
        fragmentTransaction.commit();

        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    private void SelectNavView(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment.getTag().equals(tag)) {
                fragmentTransaction.show(fragment);
            } else {
                fragmentTransaction.hide(fragment);
            }
        }
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }
}