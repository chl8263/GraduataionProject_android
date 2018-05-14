package com.example.note.seoulddok.network;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.OrientationEventListener;

import com.example.note.seoulddok.Contact;
import com.example.note.seoulddok.Model.RecvData;
import com.example.note.seoulddok.ui.FirstFragment;
import com.example.note.seoulddok.ui.SecondFragment;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gyun_home on 2018-03-25.
 */

public class PahoClient {
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private Context context;
    private MqttAndroidClient client;


    public PahoClient() {
    }

    private static class Holder {
        public static final PahoClient instance = new PahoClient();
    }

    public static PahoClient getInstance() {
        return Holder.instance;
    }

    public void setFragemntInstance(FirstFragment fragemntInstance, SecondFragment secondFragment) {
        this.firstFragment = fragemntInstance;
        this.secondFragment = secondFragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void mqttConnect() {
        client = new MqttAndroidClient(context, "tcp://" + Contact.connectIp + ":1883", MqttClient.generateClientId());
        try {
            IMqttToken token = client.connect(getMqttConnectionOption());
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    client.setBufferOpts(getDisconnectedBufferOptions());
                    Log.e("Connect_success", "Success");
                    Log.e("client__id", client.getClientId());
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

    public void publich(String msg) {
        try {
            client.publish("aaa", msg.getBytes(), 0, true);
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
                        String time = "";
                        String date = "";
                        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
                        time = new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis()));

                        Contact.dbManager.insertMobileData(date, time, msg);
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

                        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                            firstFragment.notified(new String(mqttMessage.getPayload()));
                        } else {
                            firstFragment.noti_landscape(msg);
                        }
                        secondFragment.addRecyclerView();

                    }

                });
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }catch (Exception e){
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

    public void stopPaho() {
        try {
            PahoClient.getInstance().client.disconnect();
        } catch (MqttException e) {
            Log.e("paho disconnect", "paho 정지 실패");

        }
    }
}
