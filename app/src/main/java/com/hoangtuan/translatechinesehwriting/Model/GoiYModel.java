package com.hoangtuan.translatechinesehwriting.Model;

/**
 * Created by atbic on 2/4/2018.
 */

public class GoiYModel {
    int start;
    int end;
    String text;

    public GoiYModel() {
    }

    public GoiYModel(int start, int end, String text) {
        this.start = start;
        this.end = end;
        this.text = text;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
