package com.example.note.seoulddok.network;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Looper;
import android.util.Log;

import com.example.note.seoulddok.ui.FirstFragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class BlueToothRecv extends Thread {
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;

    private FirstFragment firstFragment;

    OutputStream outputStream = null;
    InputStream inputStream=null;
    byte[] bibi = "hello".getBytes();
    byte[] getget =new byte[1024];
    private UUID uuid;

    public BlueToothRecv(BluetoothDevice device, UUID uuid,FirstFragment firstFragment) {
        this.bluetoothDevice = device;
        this.uuid = uuid;
        this.firstFragment = firstFragment;
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

                final String finalA = a;

                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        firstFragment.classifiNotified(finalA);
                    }
                });

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
}
