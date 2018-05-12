package com.example.note.seoulddok.Model;

import java.util.List;

/**
 * Created by gyun_home on 2018-05-12.
 */

public class ExpandableItem {
    public int type;
    public String text;
    public int i;
    public List<ExpandableItem> invisibleChildren;

    public ExpandableItem() {

    }

    public ExpandableItem(int type, String text) {
        this.type = type;
        this.text = text;
    }
    public ExpandableItem(int type, String text,int i) {
        this.type = type;
        this.text = text;
        this.i=i;
    }
}
