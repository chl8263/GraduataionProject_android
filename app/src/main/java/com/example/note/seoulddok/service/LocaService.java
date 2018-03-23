package com.example.note.seoulddok.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.note.seoulddok.interfaces.LocaCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

/**
 * Created by gyun_home on 2018-03-19.
 */

public class LocaService extends Service {
    private FusedLocationProviderClient locationProviderClient ;
    private LocaCallback locaCallback;
    IBinder iBinder = new Locabinder();

    public class Locabinder extends Binder {
        public LocaService getservice() {
            return LocaService.this;
        }
    }

    public void registerCallback(LocaCallback locaCallback) {
        this.locaCallback = locaCallback;
    }

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("서비스 binder", "binder 시작");

        locationProviderClient.getLastLocation().addOnCompleteListener((Executor) this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()&&task.getResult() != null){
                    Log.e("location",task.getResult().getLatitude()+task.getResult().getLongitude()+"");
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                /*while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    locaCallback.recv_loca("asdasdasd");
                }*/

            }
        }).start();
        return iBinder;
    }
    public void gege(){
        Log.e("aaaaaaaaaaa","aaaaaaaaaaa");
    }
    @Override
    public void onCreate() {
        super.onCreate();

        locationProviderClient= LocationServices.getFusedLocationProviderClient(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("서비스 start","start Command 시작");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("서비스 destory","서비스 종료");

        super.onDestroy();
    }
}
