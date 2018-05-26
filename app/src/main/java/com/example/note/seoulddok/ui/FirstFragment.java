package com.example.note.seoulddok.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.note.seoulddok.Contact;
import com.example.note.seoulddok.R;
import com.example.note.seoulddok.interfaces.LocaCallback;
import com.example.note.seoulddok.interfaces.LocaShowCallback;
import com.example.note.seoulddok.network.PahoClient;
import com.example.note.seoulddok.service.LocaServiceBind;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by gyun_home on 2018-03-18.
 */

public class FirstFragment extends Fragment implements OnMapReadyCallback, LocaShowCallback, LocaCallback {

    private int DIALOGCLICK = 0;

    private final int EMERGENCY = 2000;
    private final int NOMAL = 1000;

    private boolean MOVEALARM = false;

    private Handler handler = new Handler();

    private HashMap<String, Object> markerHashMap = new HashMap<>();
    private LatLng NONONO = new LatLng(37.6297848, 127.0584665);

    private MapView mapView = null;
    private GoogleMap map = null;
    private LocaServiceBind locaService;

    private double currentLat;
    private double currentLang;

    boolean isService = false;

    private Marker marker;
    private Marker submarker;

    private LinearLayout showMSG;
    private TextView showMSGText;

    private TextView notiText;
    //FusedLocationProviderClient mFusedLocationClient;
    //LocationCallback mLocationCallback;
    //Geocoder geocoder = null;


    /*ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocaServiceBind.Locabinder locabinder = (LocaServiceBind.Locabinder) iBinder;
            locaService = locabinder.getservice();
            locaService.registerCallback(locaCallback);
            isService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isService = false;
        }
    };*/

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


        try {

            DIALOGCLICK = 1;

            double lat = Double.parseDouble(getActivity().getIntent().getStringExtra("LAT"));//intent.getDoubleExtra("LAT", 0.0);
            double lang = Double.parseDouble(getActivity().getIntent().getStringExtra("LANG"));//intent.getDoubleExtra("LAT", 0.0);

            NONONO = new LatLng(lat,lang);

        }catch (NullPointerException e){
            DIALOGCLICK = 0;
            Log.e("~~~~~~","null");
        }


        //showMSG.setVisibility(View.GONE);

        registerBroadCast();

        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
        /*Intent intent = new Intent(getContext(), LocaServiceBind.class);
        getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);*/
        //startLocationUpdates();


    }

    public void disconnectService() {
        //getContext().unbindService(connection);
        //locaService.stopServiceThread();
        //locaService.stopServiceThread(connection);
        //locaService.stopLocaService(connection);
    }

    private void registerBroadCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contact.MAPMOVE);
        intentFilter.addAction(Contact.MOVE_EMER);
        getContext().registerReceiver(broadcastReceiver, intentFilter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Contact.MAPMOVE)) {
                currentLat = intent.getDoubleExtra("LAT", 0.0);
                currentLang = intent.getDoubleExtra("LANG", 0.0);
                moveCamera(currentLat, currentLang);
            } else if (intent.getAction().equals(Contact.MOVE_EMER)) {
                double lat = intent.getDoubleExtra("LAT", 0.0);
                double lang = intent.getDoubleExtra("LANG", 0.0);
                String message = intent.getStringExtra("MSG");

                Log.e("!!!!!!!","ssdssdsdsd");

                /*LatLng latlang = new LatLng(lat, lang);
                NONONO = new LatLng(lat);*/

                NONONO = new LatLng(lat, lang);
                EmerThread emerThread = new EmerThread(new LatLng(lat, lang));
                emerThread.start();

            }
        }
    };

    public void moveCamera(double lat, double lang) {
        LatLng NOW = new LatLng(lat, lang);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(NOW, 15);
        if (map == null) {
            Log.e("MAP NULL", "MAP NULL");
        }
        //map.animateCamera(cameraUpdate);
        map.clear();
        //map.clear(); //마커지우기
        if (markerHashMap.containsKey("main")) {
            Marker marker = (Marker) markerHashMap.get("main");
            //marker = map.addMarker(new MarkerOptions().position(NOW).icon(bitmapDescriptorFromVector(getActivity(),R.drawable.ic_position)).title("내위치!"));
            marker = map.addMarker(new MarkerOptions().position(NOW).icon(BitmapDescriptorFactory.fromResource(R.drawable.ananan)).title("내위치!"));
            if (markerHashMap.containsKey("sub")) {
                Marker submarker = (Marker) markerHashMap.get("main");
                submarker = map.addMarker(new MarkerOptions().position(NONONO).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("내위치!"));
                CircleOptions subcircle = new CircleOptions().center(NONONO) //원점
                        .radius(300)      //반지름 단위 : m
                        .strokeWidth(0f)  //선너비 0f : 선없음
                        .fillColor(Color.parseColor("#880000ff")); //배경색
                map.addCircle(subcircle);
            }
            Iterator iterator = markerHashMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                if (entry.getKey().equals("main")) {
                    marker.setPosition(NOW);
                } else {
                    marker.setPosition(NONONO);
                }
            }
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

    public void classifiNotified(String msg) {
        String[] a = msg.split(",");
        if (a[0].equals("loc")) {
            PahoClient.getInstance().unSubscrive(Contact.loca_gu);
            PahoClient.getInstance().unSubscrive(Contact.loca_dong);
            Contact.loca_gu = a[1];
            Contact.loca_dong = a[2];
            PahoClient.getInstance().subscribe(a[1]);
            PahoClient.getInstance().subscribe(a[2]);
        } else if (a[0].equals("sp")) {
            if (a[1].equals("emer")) {
                try {
                    double lat = Double.parseDouble(a[2]);
                    double lang = Double.parseDouble(a[3]);
                    //noti_landscape(EMERGENCY,a[4]);
                    /*MyAsyncTask myAsyncTask = new MyAsyncTask(new LatLng(lat, lang));
                    myAsyncTask.execute();*/
                    if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        notified(EMERGENCY, a[4]);
                    } else {
                        noti_landscape(EMERGENCY, a[4]);
                    }
                } catch (NumberFormatException e) {
                    if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        notified(EMERGENCY, a[2]);
                    } else {
                        noti_landscape(EMERGENCY, a[2]);
                    }
                }

            } else if (a[1].equals("nomal")) {
                if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    notified(NOMAL, a[2]);
                } else {
                    noti_landscape(NOMAL, a[2]);
                }
            }
        }
    }


    @Override
    public void notified(int flag, String msg) {
        Log.e(">>>>>>.", msg);

        switch (flag) {
            case NOMAL:
                Log.e("notified--- NOMAL", msg);


                //new AlamTextThread(msg).start();

               /* if (showMSG.getVisibility() == View.GONE) {
                    showMSG.setVisibility(View.VISIBLE);
                }*/
                //showMSG.setText(msg);
                break;
            case EMERGENCY:
                Log.e("notified--- EMERGENCY", msg);
                //new AlamTextThread(msg).start();

                //showMSG.setVisibility(View.VISIBLE);
                //showMSG.setText(msg);
                break;
        }

    }

    @Override
    public void noti_landscape(int flag, String msg) {
        Log.e("first_landscape-->", msg);

        switch (flag) {
            case NOMAL:
                notiText.setText(msg);
                Log.e("notified--- NOMAL", msg);
                break;
            case EMERGENCY:
                notiText.setText(msg);
                Log.e("notified--- EMERGENCY", msg);
                break;
        }

        /*MyAsyncTask myAsyncTask = new MyAsyncTask(NONONO);
        myAsyncTask.execute();*/
    }


    /*LocaCallback locaCallback = new LocaCallback() {
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

                Iterator iterator = markerHashMap.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry entry  = (Map.Entry) iterator.next();
                    Marker marker = (Marker) entry.getValue();
                    if(entry.getKey().equals("main")){
                        marker.setPosition(NOW);
                    }
                    else {
                        marker.setPosition(NONONO);
                    }
                }
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
    };*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("READY!!!!!!", "OOOOOOOOOOK");


        map = googleMap;

        if(DIALOGCLICK == 1) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(3000);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    double lat = Double.parseDouble(getActivity().getIntent().getStringExtra("LAT"));//intent.getDoubleExtra("LAT", 0.0);
                                    double lang = Double.parseDouble(getActivity().getIntent().getStringExtra("LANG"));//intent.getDoubleExtra("LAT", 0.0);

                                    DIALOGCLICK = 0;
                                    LatLng latlang = new LatLng(lat, lang);
                                    EmerThread emerThread = new EmerThread(latlang);
                                    emerThread.start();
                                }catch (NullPointerException e){
                                    Log.e("~~~~~~","null");
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });

        }

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

        getContext().unregisterReceiver(broadcastReceiver);
        //getContext().unbindService(connection); // 앱종료시에 서비스 같이 종료
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void recv_loca(String location, LatLng latLng) {
        Log.e("헤헤헤헤헤", "헤헤헤헤헹");
    }

    /*private class AlamTextThread extends Thread{
        private String message;

        public AlamTextThread(String message){
            this.message = message;
        }
        @Override
        public void run() {
            super.run();

            new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Animation animation_v = AnimationUtils.loadAnimation(getContext(), R.anim.write_animation_v);
                    showMSG.startAnimation(animation_v);
                    showMSG.setVisibility(View.VISIBLE);
                    showMSGText.setText(message);
                }
            });

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showMSG.setVisibility(View.GONE);
                }
            });

        }
    }*/

    private class AlamTextASyncTask extends AsyncTask {
        private String message;

        public AlamTextASyncTask(String msg) {
            this.message = msg;
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


        }
    }

    private class EmerAsyncTask extends AsyncTask {
        private String targetLoca;
        private LatLng NOW;

        public EmerAsyncTask(LatLng NOW) {
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

            new EmerThread(NOW).start();

        }
    }

    private class EmerThread extends Thread {
        private LatLng latLng;

        public EmerThread(LatLng latLng) {
            this.latLng = latLng;
        }

        @Override
        public void run() {
            super.run();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    LatLng nowLang = new LatLng(currentLat,currentLang);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(nowLang, 12);
                    map.animateCamera(cameraUpdate);
                }
            });

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(NONONO, 12);
                    map.animateCamera(cameraUpdate);
                }
            });

            markerHashMap.put("sub", submarker);
            MOVEALARM = true;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    submarker = map.addMarker(new MarkerOptions().position(NONONO).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("내위치!"));
                }
            });
            try {
                Thread.sleep(1500);
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

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}


