package com.example.note.seoulddok.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.note.seoulddok.Contact;
import com.example.note.seoulddok.MainActivity;
import com.example.note.seoulddok.R;
import com.example.note.seoulddok.interfaces.LocaCallback;
import com.example.note.seoulddok.interfaces.LocaShowCallback;
import com.example.note.seoulddok.service.LocaService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by gyun_home on 2018-03-18.
 */

public class FirstFragment extends Fragment implements OnMapReadyCallback, LocaShowCallback {

    private boolean MOVEALARM = false;

    private Handler handler = new Handler();

    private HashMap<String, Object> markerHashMap = new HashMap<>();
    private LatLng NONONO = new LatLng(37.6297848, 127.0584665);

    private MapView mapView = null;
    private GoogleMap map = null;
    private LocaService locaService;

    boolean isService = false;

    private Marker marker;
    private Marker submarker;

    private TextView notiText;
    //FusedLocationProviderClient mFusedLocationClient;
    //LocationCallback mLocationCallback;
    //Geocoder geocoder = null;


    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocaService.Locabinder locabinder = (LocaService.Locabinder) iBinder;
            locaService = locabinder.getservice();
            locaService.registerCallback(locaCallback);
            isService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isService = false;
        }
    };

    public FirstFragment() {

    }

    public static FirstFragment getInstance() {
        Bundle args = new Bundle();
        FirstFragment fragment = new FirstFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.firstfragment, container, false);
        mapView = (MapView) view.findViewById(R.id.Map);
        mapView.getMapAsync(this);
        notiText = view.findViewById(R.id.notiText);

        /*mFusedLocationClient
                = LocationServices.getFusedLocationProviderClient(getContext());*/
        //geocoder = new Geocoder(getContext(), Locale.KOREA);
        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
        Intent intent = new Intent(getContext(), LocaService.class);
        getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
        //startLocationUpdates();


    }

    public void disconnectService() {
        //getContext().unbindService(connection);
        //locaService.stopServiceThread();
        //locaService.stopServiceThread(connection);
        //locaService.stopLocaService(connection);
    }

    @Override
    public void notified(String msg) {
        Log.e(">>>>>>.", msg);

        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
        builder.setSmallIcon(R.drawable.global).setContentTitle("aaa").setContentText(msg);
        NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());*/
        MyAsyncTask myAsyncTask = new MyAsyncTask(NONONO);
        myAsyncTask.execute();
    }

    @Override
    public void noti_landscape(String msg) {
        Log.e("first_landscape-->", msg);

        notiText.setText(msg);

        MyAsyncTask myAsyncTask = new MyAsyncTask(NONONO);
        myAsyncTask.execute();

    }

    LocaCallback locaCallback = new LocaCallback() {
        @Override
        public void recv_loca(String location, LatLng NOW) {
            Log.e("!!!!!!!", "!!!!!!!");
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(NOW, 15);
            if (map == null) {
                Log.e("!!!!!!!", "gggggggggggggggggggg");
            }
            //map.animateCamera(cameraUpdate);
            map.clear();
            //map.clear(); //마커지우기
            if (markerHashMap.containsKey("main")) {
                Log.e("+++++++++++", "NNNNNNNNNNNNNN");
                Marker marker = (Marker) markerHashMap.get("main");
                marker = map.addMarker(new MarkerOptions().position(NOW).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("내위치!"));
                if (markerHashMap.containsKey("sub")) {
                    Marker submarker = (Marker) markerHashMap.get("main");
                    submarker = map.addMarker(new MarkerOptions().position(NONONO).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("내위치!"));
                    CircleOptions subcircle = new CircleOptions().center(NONONO) //원점
                            .radius(300)      //반지름 단위 : m
                            .strokeWidth(0f)  //선너비 0f : 선없음
                            .fillColor(Color.parseColor("#880000ff")); //배경색
                    map.addCircle(subcircle);
                }

                /*Iterator iterator = markerHashMap.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry entry  = (Map.Entry) iterator.next();
                    Marker marker = (Marker) entry.getValue();
                    if(entry.getKey().equals("main")){
                        marker.setPosition(NOW);
                    }
                    else {
                        marker.setPosition(NONONO);
                    }
                }*/
                //marker.remove();
                //marker.setPosition(NOW);
            } else {
                Log.e("-------------", "null");
                marker = map.addMarker(new MarkerOptions().position(NOW).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("내위치!"));
                markerHashMap.put("main", marker);
            }
            //marker.remove();
            //marker = null;
            if (!MOVEALARM) {
                map.animateCamera(cameraUpdate);
            }

        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("READY!!!!!!", "OOOOOOOOOOK");

        map = googleMap;


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) { // 슬라이드 닫힐때

        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { // 슬라이드 열릴때

        }

    }

    /*private void startLocationUpdates() {

            LocationRequest locRequest = new LocationRequest();
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
                            Log.e("noww",""+a+","+d1+","+d2);
                        }
                    }


                    LatLng NOW = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(NOW,15);
                    map.clear(); //마커지우기
                    map.addMarker(new MarkerOptions().position(NOW).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("내위치!"));
                    map.animateCamera(cameraUpdate);
                }
            };
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocationClient.requestLocationUpdates(locRequest, mLocationCallback, Looper.myLooper());
        }*/
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        getContext().unbindService(connection); // 앱종료시에 서비스 같이 종료
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private class MyAsyncTask extends AsyncTask {
        private String targetLoca;
        private LatLng NOW;

        public MyAsyncTask(LatLng NOW) {
            this.NOW = NOW;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            markerHashMap.put("sub", submarker);
            MOVEALARM = true;
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            submarker = map.addMarker(new MarkerOptions().position(NONONO).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("내위치!"));

            new AlarmThread(NOW).start();
            /*CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(NOW, 10);
            map.animateCamera(cameraUpdate);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            CameraUpdate cameraUpdate2 = CameraUpdateFactory.newLatLngZoom(NONONO, 15);
            map.animateCamera(cameraUpdate2);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            MOVEALARM = false;*/
        }
    }

    private class AlarmThread extends Thread {
        private LatLng latLng;

        public AlarmThread(LatLng latLng) {
            this.latLng = latLng;
        }

        @Override
        public void run() {
            super.run();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 8);
                    map.animateCamera(cameraUpdate);
                }
            });
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(NONONO, 15);
                    map.animateCamera(cameraUpdate);
                }
            });
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MOVEALARM = false;
            try {
                Thread.sleep(10000);
                markerHashMap.remove("sub");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


