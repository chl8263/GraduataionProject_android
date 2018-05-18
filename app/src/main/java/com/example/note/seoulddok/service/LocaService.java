package com.example.note.seoulddok.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.note.seoulddok.Contact;
import com.example.note.seoulddok.interfaces.LocaCallback;
import com.example.note.seoulddok.network.PahoClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

/**
 * Created by gyun_home on 2018-03-19.
 */

public class LocaService extends Service {
    private FusedLocationProviderClient locationProviderClient ;
    private LocaCallback locaCallback;


    private PahoClient pahoClient = PahoClient.getInstance();

    private MyAsync myAsync = new MyAsync();

    private FusedLocationProviderClient mFusedLocationClient;
    private Geocoder geocoder= null;
    private LocationCallback mLocationCallback;


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

        geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
        mFusedLocationClient
                = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        Log.e("????????","?????/////");
        startLocationUpdates();

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
    public void stopLocaService(ServiceConnection connection){
        unbindService(connection);
    }
    @Override
    public void onDestroy() {
        Log.e("서비스 destory","서비스 종료");

        super.onDestroy();
    }

    private void startLocationUpdates() {
        myAsync.execute();

    /*    new Thread(new Runnable() {
            @Override
            public void run() {

            }
        })*/

       /* LocationRequest locRequest = new LocationRequest();
        locRequest.setInterval(3000);
        locRequest.setFastestInterval(1500);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Log.e("now",locationResult.getLastLocation().getLatitude()+" "+locationResult.getLastLocation().getLongitude());

                List<Address> list = null;
                double d1 = 0;
                double d2 = 0;
                try {
                    d1 =locationResult.getLastLocation().getLatitude();
                    d2 =locationResult.getLastLocation().getLongitude();


                    list = geocoder.getFromLocation(
                            d1, // 위도
                            d2, // 경도
                            10); // 얻어올 값의 개수
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (list != null) {
                    if (list.size()==0) {
                    } else {
                        Address address = (Address) list.get(0);
                        String a = address.getAddressLine(0);
                        String aa = address.getSubLocality();   // 구 알아오는 코드
                        if(aa != null){
                            pahoClient.subscribe(aa);
                            Log.e("ㅋㅋㅋㅋㅋㅋㅋㅋ",aa);
                            Log.e("noww",""+a+","+d1+","+d2);
                        }

                    }
                }
                LatLng NOW = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                locaCallback.recv_loca("a", NOW);

            }
        };
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(locRequest, mLocationCallback, Looper.myLooper());*/
    }

    public void stopServiceThread(){
        //myAsync.cancel();
        //LocationServices.FusedLocationApi.removeLocationUpdates(mFusedLocationClient,mLocationCallback);
    }
    private class MyAsync extends AsyncTask{
        LocationRequest locRequest = new LocationRequest();

        @Override
        protected Object doInBackground(Object[] objects) {
            if(Contact.isSensor == true) {
                locRequest.setInterval(3000);
                locRequest.setFastestInterval(1500);
                locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        if(Contact.isSensor == true) {
                            Log.e("now", locationResult.getLastLocation().getLatitude() + " " + locationResult.getLastLocation().getLongitude());

                            List<Address> list = null;
                            double d1 = 0;
                            double d2 = 0;
                            try {
                                d1 = locationResult.getLastLocation().getLatitude();
                                d2 = locationResult.getLastLocation().getLongitude();


                                list = geocoder.getFromLocation(
                                        d1, // 위도
                                        d2, // 경도
                                        10); // 얻어올 값의 개수
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (list != null) {
                                if (list.size() == 0) {
                                } else {
                                    Address address = (Address) list.get(0);
                                    String a = address.getAddressLine(0);
                                    String aa = address.getSubLocality();   // 구 알아오는 코드
                                    if (aa != null) {
                                        //pahoClient.subscribe(aa);
                                        //Log.e("ㅋㅋㅋㅋㅋㅋㅋㅋ", aa);
                                        //Log.e("noww", "" + a + "," + d1 + "," + d2);
                                        if (Contact.ClientId != null){
                                            String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date(System.currentTimeMillis()));
                                            pahoClient.publichLoca(Contact.ClientId+","+d1+","+d2+","+date);
                                        }

                                    }

                                }
                            }
                            LatLng NOW = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                            locaCallback.recv_loca("a", NOW);
                        }

                    }
                };
            }
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }

            return null;
        }

        @SuppressLint("MissingPermission")
        @Override
        protected void onPostExecute(Object o) {
            mFusedLocationClient.requestLocationUpdates(locRequest, mLocationCallback, Looper.myLooper());


        }
    }
}
