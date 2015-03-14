package com.thram.thrammusicplayer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.squareup.picasso.Picasso;

/**
 * Created by thram on 8/03/15.
 */
public class App extends Application {
    public static AssetManager assetManager;
    public static Context context;
    public static Picasso picasso;


    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        assetManager = context.getAssets();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        Preferences.settings = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
        Display.density = metrics.density;
        Display.density = metrics.density;
        Display.width = metrics.widthPixels;
        Display.height = metrics.heightPixels;
        picasso = new Picasso.Builder(App.context).build();
    }

    public static class Display {

        public static float density = 1;
        public static int width = 0;
        public static int height = 0;

    }

    public static class Preferences {
        public static final String DEFAULT_WALLPAPER_THRAM = "wallpapers/psychedelic_wallpaper_11.gif";
        public static SharedPreferences settings = null;
    }

    public static int getNavigationBarHeight() {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

}
