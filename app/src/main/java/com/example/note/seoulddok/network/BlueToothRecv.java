package com.example.note.seoulddok.network;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Looper;
import android.util.Log;

import com.example.note.seoulddok.Contact;
import com.example.note.seoulddok.MainActivity;
import com.example.note.seoulddok.R;
import com.example.note.seoulddok.ui.FirstFragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class BlueToothRecv extends Thread {

    int intid = 1;
    String id = String.valueOf(intid++);

    private final int EMERFLAG = 3200;
    private final int NOMALFLAG = 3300;

    private Intent b_MAPMOVE = new Intent(Contact.MAPMOVE);
    private Intent b_MOVE_EMER = new Intent(Contact.MOVE_EMER);

    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;

    private Context context;

    OutputStream outputStream = null;
    InputStream inputStream=null;
    byte[] bibi = "hello".getBytes();
    byte[] getget =new byte[1024];
    private UUID uuid;

    public BlueToothRecv(BluetoothDevice device, UUID uuid,Context context) {
        this.bluetoothDevice = device;
        this.uuid = uuid;
        this.context = context;
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            try {
                getget = new byte[1024];
                bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
                inputStream = bluetoothSocket.getInputStream();
                //outputStream.write(bibi);
                //Thread.sleep(1000);
                int b = inputStream.read(getget);
                String a = new String(getget,0,b);
                a = a.replaceAll("\\p{Z}","");
                Log.e("bluetooth receiver",a);

                classifiNotified(a);


                getget = null;
            } catch (IOException e) {
                e.printStackTrace();
            } /*catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }

    public void socketclose(){
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            Log.e("blueSocket","실패!!!!!!!");
        }
    }

    public void classifiNotified(String msg){
        String[] message = msg.split(",");
        if (message[0].equals("location")) {
            double lat = Double.parseDouble(message[1]);
            double lang = Double.parseDouble(message[2]);
            b_MAPMOVE.putExtra("LAT", lat);
            b_MAPMOVE.putExtra("LANG", lang);
            context.sendBroadcast(b_MAPMOVE);
        } else if (message[0].equals("sp")) {
            if (message[1].equals("emer")) {
                try {
                    double lat = Double.parseDouble(message[2]);
                    double lang = Double.parseDouble(message[3]);

                    if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        b_MOVE_EMER.putExtra("LAT", Double.parseDouble(message[2]));
                        b_MOVE_EMER.putExtra("LANG", Double.parseDouble(message[3]));
                        b_MOVE_EMER.putExtra("MSG", message[4]);
                        context.sendBroadcast(b_MOVE_EMER);

                        notificationService(EMERFLAG, message[4]);

                    } else {

                    }
                } catch (NumberFormatException e) {
                    if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                        notificationService(EMERFLAG, message[2]);

                    } else {

                    }
                }

            } else if (message[1].equals("nomal")) {
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    notificationService(NOMALFLAG, message[2]);
                } else {
                    notificationService(NOMALFLAG, message[2]);
                }
            }
        }else {
            notificationService(NOMALFLAG, message[0]);
        }
    }

    public void notificationService(int flag, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        CharSequence name = "aaaa";
        String description = "bbb";

        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(id, name, importance);

            channel.setDescription(description);
            channel.enableLights(true);

            channel.setLightColor(Color.GREEN);

            channel.enableVibration(true);

            channel.setVibrationPattern(new long[]{100, 200, 300});

            notificationManager.createNotificationChannel(channel);
        }

        switch (flag) {
            case EMERFLAG:


                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pi = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, 0);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


                    Notification notification = new Notification.Builder(context).setContentTitle(message).setContentText("눌러서 확인").setSmallIcon(R.drawable.global).setChannelId(id).setContentIntent(pi).build();
                    notificationManager.notify(1, notification);
                }

                break;
            case NOMALFLAG:

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Notification notification = new Notification.Builder(context).setContentTitle(message).setContentText("").setSmallIcon(R.drawable.global).setChannelId(id).build();
                    notificationManager.notify(1, notification);
                }
                break;
        }


    }
}
