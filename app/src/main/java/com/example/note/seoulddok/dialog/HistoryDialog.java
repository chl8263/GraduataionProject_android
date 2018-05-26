package com.example.note.seoulddok.dialog;

import android.content.Intent;
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
import com.google.android.gms.maps.model.LatLng;

public class HistoryDialog extends AppCompatActivity implements View.OnClickListener {
    private Button ok, not;

    private double lat;
    private double lang;

    private Intent b_MOVE_EMER = new Intent(Contact.MOVE_EMER);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.history_dialog);
        setFinishOnTouchOutside(false);


        String getextra = getIntent().getStringExtra("latlang");
        String[] latlang = getextra.split(",");

        lat = Double.parseDouble(latlang[0]);//intent.getDoubleExtra("LAT", 0.0);
        lang = Double.parseDouble(latlang[1]);//intent.getDoubleExtra("LAT", 0.0);

        LatLng latLng = new LatLng(lat, lang);

        ok = (Button) findViewById(R.id.history_ok_btn);
        ok.setOnClickListener(this);
        not = (Button) findViewById(R.id.history_cancle_btn);
        not.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_btn:
                b_MOVE_EMER.putExtra("LAT", lat);
                b_MOVE_EMER.putExtra("LANG", lang);
                sendBroadcast(b_MOVE_EMER);
                break;
            case R.id.cancle_btn:
                finish();
                break;
        }
    }
}