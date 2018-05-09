package com.example.note.seoulddok.interfaces;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Note on 2018-03-22.
 */

public interface LocaCallback {
    void recv_loca(String location, LatLng latLng);
}
