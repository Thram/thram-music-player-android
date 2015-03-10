package com.thram.thrammusicplayer.model;

/**
 * Created by thram on 8/03/15.
 */
public class Card {

    public final String tag;
    public final String title;
    public final String description;
    public final String imagePath;
    public final Integer color;


    public Card(String ta, String t, String d) {
        tag = ta;
        title = t;
        description = d;
        imagePath = null;
        color = null;
    }

    public Card(String ta, String t, String d, String i) {
        tag = ta;
        title = t;
        description = d;
        imagePath = i;
        color = null;
    }

    public Card(String ta, String t, String d, int c) {
        tag = ta;
        title = t;
        description = d;
        imagePath = null;
        color = c;
    }
}
