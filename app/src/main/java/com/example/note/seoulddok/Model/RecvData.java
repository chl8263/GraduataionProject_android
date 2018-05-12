package com.example.note.seoulddok.Model;

public class RecvData {
    private int id;
    private String date;
    private String time;
    private String message;

    public RecvData(int id, String date, String time, String message) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.message = message;
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

    public int id() {
        return id;
    }


    public void setClassification(int id) {
        this.id = id;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
