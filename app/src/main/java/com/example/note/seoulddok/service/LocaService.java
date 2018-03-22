package com.example.note.seoulddok.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.note.seoulddok.interfaces.LocaCallback;

/**
 * Created by gyun_home on 2018-03-19.
 */

public class LocaService extends Service {

    private LocaCallback locaCallback;
    IBinder iBinder = new Locabinder();

    public class Locabinder extends Binder {
        public LocaService getservice(){
            return LocaService.this;
        }
    }

    public void registerCallback(LocaCallback locaCallback){
        this.locaCallback = locaCallback;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("서비스 binder","binder 시작");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    locaCallback.recv_loca("asdasdasd");
                }
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
