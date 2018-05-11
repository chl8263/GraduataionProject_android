package com.example.note.seoulddok.Model;

public class RecvData {
    private String Classification;
    private String Time;
    private String message;

    public RecvData(String mobile, String classification, String time, String message) {
        Classification = classification;
        Time = time;
        this.message = message;
    }

    public String getClassification() {
        return Classification;
    }

    public void setClassification(String classification) {
        Classification = classification;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
