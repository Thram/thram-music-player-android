package com.thram.thrammusicplayer.model;

/**
 * Created by thram on 8/03/15.
 */
public class Card {

    public final String tag;
    public final String title;
    public final String description;
    public final Integer drawableFrontId;
    public final Integer drawableBackId;
    public final Integer color;


    public Card(String ta, String t, String d, int c) {
        tag = ta;
        title = t;
        description = d;
        drawableFrontId = null;
        drawableBackId = null;
        color = c;
    }

    public Card(String ta, String t, String d, int c, int dIF, int dIB) {
        tag = ta;
        title = t;
        description = d;
        color = c;
        drawableFrontId = dIF;
        drawableBackId = dIB;
    }
}
