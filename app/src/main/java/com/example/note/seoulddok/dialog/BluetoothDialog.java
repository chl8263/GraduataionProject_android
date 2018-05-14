package com.example.note.seoulddok.dialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.note.seoulddok.Contact;
import com.example.note.seoulddok.R;

/**
 * Created by gyun_home on 2018-05-13.
 */

public class BluetoothDialog extends AppCompatActivity implements View.OnClickListener {
    private final int BLUETOOTH_CAR_OK = 200;
    private final int BLUETOOTH_MOBILE_OK = 300;
    private final int BLUETOOTH_NO = 400;

    private TextView blutoothdialogText;
    private Button ok, not;
    private String filename;
    private final String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/KNOCK_TALK";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bluetooth_dialog);
        setFinishOnTouchOutside(false);


        Intent intent = getIntent();
        blutoothdialogText = findViewById(R.id.blutoothdialogText);
        ok = (Button) findViewById(R.id.ok_btn);
        ok.setOnClickListener(this);
        not = (Button) findViewById(R.id.cancle_btn);
        not.setOnClickListener(this);
        if(!Contact.isSensor){
            blutoothdialogText.setText("스마트폰 연결을 하시겠습니까 ?");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_btn:
                if(Contact.isSensor){
                    setResult(BLUETOOTH_CAR_OK);
                }else {
                    setResult(BLUETOOTH_MOBILE_OK);
                }
                finish();
                break;
            case R.id.cancle_btn:
                setResult(BLUETOOTH_NO);
                finish();
                break;
        }
    }
}
