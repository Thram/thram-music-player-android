package com.thram.thrammusicplayer.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.thram.thrammusicplayer.R;

/**
 * Created by thram on 8/03/15.
 */
public class ThramMusicPlayerActivity extends ActionBarActivity {
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setWindowsBackground(Integer color) {
        if (color != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(color));
        else
            getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home_color)));
    }

    public void showToolbar(boolean animated) {
        toolbar.setVisibility(View.VISIBLE);
    }

    public void hideToolbar(boolean animated) {
        toolbar.setVisibility(View.GONE);
    }
}

