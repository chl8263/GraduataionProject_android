package com.example.note.seoulddok.Model;

public class RecvData {
    private int id;
    private String date;
    private String time;
    private String message;
    private String distinction;
    private String latlang;

    public RecvData(int id, String date, String time, String message, String distinction, String latlang) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.message = message;
        this.distinction = distinction;
        this.latlang = latlang;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getDistinction() {
        return distinction;
    }

    public void setDistinction(String distinction) {
        this.distinction = distinction;
    }

    public String getLatlang() {
        return latlang;
    }

    public void setLatlang(String latlang) {
        this.latlang = latlang;
    }
}
