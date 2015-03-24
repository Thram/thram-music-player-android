package com.thram.thrammusicplayer.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.thram.thrammusicplayer.R;

/**
 * Created by thram on 8/03/15.
 */
public class ThramMusicPlayerActivity extends ActionBarActivity {
    public static Toolbar toolbar;

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

    public void setupToolbar(String title, String subtitle) {
        toolbar.getMenu().clear();
        setTitle(title);
        if (subtitle == null)
            subtitle = " ";
        toolbar.setSubtitle(subtitle);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.light_overlay));
    }

    public void changeFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        ft.replace(R.id.container, fragment, tag);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void setupToolbar(String title) {
        setupToolbar(title, null);
    }


}

