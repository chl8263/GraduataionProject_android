package com.example.note.seoulddok.Model;

import java.util.List;

/**
 * Created by gyun_home on 2018-05-12.
 */

public class ExParent {
    public int type;
    public String date;
    public List<ExChild> invisibleChildren;


    public ExParent(int type, String date) {
        this.type = type;
        this.date = date;
    }

    public ExParent() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ExChild> getInvisibleChildren() {
        return invisibleChildren;
    }

    public void setInvisibleChildren(List<ExChild> invisibleChildren) {
        this.invisibleChildren = invisibleChildren;
    }
}
