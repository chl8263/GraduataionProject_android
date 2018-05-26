package com.example.note.seoulddok.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.note.seoulddok.Contact;
import com.example.note.seoulddok.MainActivity;
import com.example.note.seoulddok.Model.RecvData;
import com.example.note.seoulddok.R;
import com.example.note.seoulddok.interfaces.LocaCallback;
import com.example.note.seoulddok.network.PahoClient;
import com.example.note.seoulddok.ui.FirstFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PahoService extends Service {

    private final int EMERFLAG = 3200;
    private final int NOMALFLAG = 3300;

    int intid = 1;
    String id = String.valueOf(intid++);


    private FusedLocationProviderClient locationProviderClient;
    //private LocaCallback locaCallback;

    private FusedLocationProviderClient mFusedLocationClient;
    private Geocoder geocoder = null;
    private LocationCallback mLocationCallback;

    private MqttAndroidClient client;

    private MyAsync myAsync = new MyAsync();

    private Intent b_MAPMOVE = new Intent(Contact.MAPMOVE);
    private Intent b_MOVE_EMER = new Intent(Contact.MOVE_EMER);


    @Override
    public void onCreate() {
        super.onCreate();
        registerBroadCast();
        mqttConnect();

        myAsync.execute();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Paho Service start", "PahoService Command 시작");


        geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
        mFusedLocationClient
                = LocationServices.getFusedLocationProviderClient(getApplicationContext());


        return START_STICKY_COMPATIBILITY;
    }


    public void sub_ThisId() {
        try {
            client.subscribe(Contact.ClientId, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    Log.e("subscribe this message", "comeON!");

                    String msg = new String(mqttMessage.getPayload());
                    String[] message = msg.split(",");

                    if (message[0].equals("loc")) {
                        unSubscrive(Contact.loca_gu);
                        unSubscrive(Contact.loca_dong);
                        Contact.loca_gu = message[1];
                        Contact.loca_dong = message[2];
                        subscribe(message[1]);
                        subscribe(message[2]);
                    } else if (message[0].equals("sp")) {
                        if (message[1].equals("emer")) {
                            try {
                                Log.e("swwcwc", "응 맞아");
                                double lat = Double.parseDouble(message[2]);
                                double lang = Double.parseDouble(message[3]);

                                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                    b_MOVE_EMER.putExtra("LAT", Double.parseDouble(message[2]));
                                    b_MOVE_EMER.putExtra("LANG", Double.parseDouble(message[3]));
                                    b_MOVE_EMER.putExtra("MSG", message[4]);
                                    sendBroadcast(b_MOVE_EMER);

                                    notificationService(EMERFLAG,message[2],message[3] ,message[4]);

                                    String time = "";
                                    String date = "";
                                    date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
                                    time = new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()));

                                    Contact.dbManager.insertMobileData(date, time, msg,"nomal","null");
                                    Log.e("subscribe this ===>" , msg);

                                    final ArrayList<RecvData> recvData = Contact.dbManager.getRecvData();

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (int i = 0; i < recvData.size(); i++) {
                                                Log.e("chchchch", recvData.get(i).getDate() + "&&" + recvData.get(i).getTime() + "&&" + recvData.get(i).getMessage());
                                            }
                                        }
                                    }).start();

                                } else {

                                }
                            } catch (NumberFormatException e) {
                                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                                    notificationService(EMERFLAG,message[2],message[3], message[2]);

                                    String time = "";
                                    String date = "";
                                    date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
                                    time = new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()));

                                    Contact.dbManager.insertMobileData(date, time, msg,"nomal","null");
                                    Log.e("subscribe this ===>" , msg);

                                    final ArrayList<RecvData> recvData = Contact.dbManager.getRecvData();

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (int i = 0; i < recvData.size(); i++) {
                                                Log.e("chchchch", recvData.get(i).getDate() + "&&" + recvData.get(i).getTime() + "&&" + recvData.get(i).getMessage());
                                            }
                                        }
                                    }).start();

                                } else {

                                }
                            }

                        } else if (message[1].equals("nomal")) {
                            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                notificationService(NOMALFLAG,"a" ,"a",message[2]);

                                String time = "";
                                String date = "";
                                date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
                                time = new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()));

                                Contact.dbManager.insertMobileData(date, time, msg,"nomal","null");
                                Log.e("subscribe this ===>" , msg);

                                final ArrayList<RecvData> recvData = Contact.dbManager.getRecvData();

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 0; i < recvData.size(); i++) {
                                            Log.e("chchchch", recvData.get(i).getDate() + "&&" + recvData.get(i).getTime() + "&&" + recvData.get(i).getMessage());
                                        }
                                    }
                                }).start();
                            } else {
                                notificationService(NOMALFLAG,"a" ,"a",message[2]);

                                String time = "";
                                String date = "";
                                date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
                                time = new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()));

                                Contact.dbManager.insertMobileData(date, time, msg,"nomal","null");
                                Log.e("subscribe this ===>" , msg);

                                final ArrayList<RecvData> recvData = Contact.dbManager.getRecvData();

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 0; i < recvData.size(); i++) {
                                            Log.e("chchchch", recvData.get(i).getDate() + "&&" + recvData.get(i).getTime() + "&&" + recvData.get(i).getMessage());
                                        }
                                    }
                                }).start();
                            }
                        }
                    }
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void subscribe(final String topic) {
        try {
            if (client != null) {
                client.subscribe(topic, 0, new IMqttMessageListener() {
                    @Override
                    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                        String msg = new String(mqttMessage.getPayload());

                        notificationService(NOMALFLAG, "a","a",msg);

                        String time = "";
                        String date = "";
                        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
                        time = new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()));

                        Contact.dbManager.insertMobileData(date, time, msg,"nomal","null");
                        Log.e("subscribe ===>" + topic, msg);

                        final ArrayList<RecvData> recvData = Contact.dbManager.getRecvData();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < recvData.size(); i++) {
                                    Log.e("chchchch", recvData.get(i).getDate() + "&&" + recvData.get(i).getTime() + "&&" + recvData.get(i).getMessage());
                                }
                            }
                        }).start();
                    }
                });
            }
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unSubscrive(String topic) {
        try {
            client.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String msg) {
        try {
            client.publish(topic, msg.getBytes(), 0, true);
            Log.e("publish content----->", msg);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void mqttConnect() {
        client = new MqttAndroidClient(this, "tcp://" + Contact.connectIp + ":1883", "paho1526978748858000000");
        try {
            IMqttToken token = client.connect(getMqttConnectionOption());
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    client.setBufferOpts(getDisconnectedBufferOptions());
                    Log.e("Connect_success", "Success");
                    Log.e("client__id", client.getClientId());
                    Contact.ClientId = client.getClientId();


                    sub_ThisId();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e("connect_fail", "Failure " + exception.toString());
                }
            });
        } catch (
                MqttException e)

        {
            e.printStackTrace();
        }
    }

    private void registerBroadCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Contact.STARTCLIENT);
        intentFilter.addAction(Contact.STOPCLIENT);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Contact.STARTCLIENT)) {
                mqttConnect();
            } else if (intent.getAction().equals(Contact.STOPCLIENT)) {
                try {
                    client.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private DisconnectedBufferOptions getDisconnectedBufferOptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(true);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;
    }

    private MqttConnectOptions getMqttConnectionOption() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setWill("aaa", "I am going offline".getBytes(), 1, true);
        //mqttConnectOptions.setUserName("username");
        //mqttConnectOptions.setPassword("password".toCharArray());
        return mqttConnectOptions;
    }

    private class MyAsync extends AsyncTask {
        LocationRequest locRequest = new LocationRequest();

        @Override
        protected Object doInBackground(Object[] objects) {
            if (Contact.isSensor == true) {
                locRequest.setInterval(3000);
                locRequest.setFastestInterval(1500);
                locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        if (Contact.isSensor == true) {
                            Log.e("current my location ->", locationResult.getLastLocation().getLatitude() + " " + locationResult.getLastLocation().getLongitude());

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
                                        if (Contact.ClientId != null) {
                                            String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()));
                                            publish("location", Contact.ClientId + "," + d1 + "," + d2 + "," + date);

                                            b_MAPMOVE.putExtra("LAT", d1);
                                            b_MAPMOVE.putExtra("LANG", d2);
                                            sendBroadcast(b_MAPMOVE);
                                        }
                                    }
                                }
                            }
                            LatLng NOW = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
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
            try {
                mFusedLocationClient.requestLocationUpdates(locRequest, mLocationCallback, Looper.myLooper());
            } catch (NullPointerException e) {
            }

        }
    }

    public void notificationService(int flag , String LAT , String LANG, String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        CharSequence name = "aaaa";
        String description = "bbb";

        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id, id, importance);

            channel.setDescription(description);
            channel.enableLights(true);

            channel.setLightColor(Color.GREEN);

            channel.enableVibration(true);

            channel.setVibrationPattern(new long[]{100, 200, 300});

            notificationManager.createNotificationChannel(channel);
        }

        switch (flag) {
            case EMERFLAG:

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("LAT",LAT);
                intent.putExtra("LANG",LANG);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


                    Notification notification = new Notification.Builder(this).setContentTitle(message).setContentText("눌러서 확인").setSmallIcon(R.drawable.global).setChannelId(id).setContentIntent(pi).build();
                    notificationManager.notify(intid, notification);
                }

                break;
            case NOMALFLAG:

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Notification notification = new Notification.Builder(this).setContentTitle(message).setContentText("").setSmallIcon(R.drawable.global).setChannelId(id).build();
                    notificationManager.notify(intid, notification);
                }
                break;
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }
}
