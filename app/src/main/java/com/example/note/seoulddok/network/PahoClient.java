package com.example.note.seoulddok.network;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by gyun_home on 2018-03-25.
 */

public class PahoClient {
    private Context context;
    private MqttAndroidClient client;
    public PahoClient(){
    }

    private static class Holder{
        public static final PahoClient instance = new PahoClient();
    }
    public static PahoClient getInstance(){
        return Holder.instance;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void  mqttConnect(){
        client = new MqttAndroidClient(context, "tcp://192.168.219.111:1883", "and");
        try {
            IMqttToken token = client.connect(getMqttConnectionOption());
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    client.setBufferOpts(getDisconnectedBufferOptions());
                    Log.e("Connect_success", "Success");

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
    public void publich(String msg){
        try {
            client.publish("aaa",msg.getBytes(),0,true);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void subscribe(){
        try {
            client.subscribe("bbb", 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    Log.e("mqtt subcribe",mqttMessage.getPayload().toString());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

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
}
