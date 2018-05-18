package com.example.note.seoulddok.network;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BlueToothRecv extends Thread {
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    OutputStream outputStream = null;
    InputStream inputStream=null;
    byte[] bibi = "hello".getBytes();
    byte[] getget =new byte[1024];
    private UUID uuid;

    public BlueToothRecv(BluetoothDevice device, UUID uuid) {
        this.bluetoothDevice = device;
        this.uuid = uuid;
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
