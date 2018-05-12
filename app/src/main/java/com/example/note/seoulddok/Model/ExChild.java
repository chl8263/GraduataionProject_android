package com.example.note.seoulddok.Model;

import java.util.List;

/**
 * Created by gyun_home on 2018-05-12.
 */

public class ExChild extends ExParent{
    //public int type;
    public String time;
    public String message;


    public ExChild(int type, String time, String message) {

        super.type = type;
        this.time = time;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
