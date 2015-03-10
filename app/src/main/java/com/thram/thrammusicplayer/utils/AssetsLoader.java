package com.thram.thrammusicplayer.utils;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.thram.thrammusicplayer.App;

import java.io.IOException;
import java.io.InputStream;


public class AssetsLoader {


    public static InputStream loadFile(String imageFileName) {
        try {
            return App.assetManager.open(imageFileName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Bitmap loadBitmapFromFile(String imageFileName) {
        Bitmap bitmap = null;
        try {
            InputStream is = App.assetManager.open(imageFileName);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bitmap;
    }

    public static String getUrlFromAssetFilePath(String filePath) {
        return "file:///android_asset/" + filePath;
    }

    public static AssetFileDescriptor loadAudioFromFile(String fileName) {
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = App.context.getAssets().openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileDescriptor;
    }

    public static String loadStringFromFile(String fileName, boolean inExpansionFile) {
        String result = null;
        try {
            InputStream is = null;
            try {
                is = App.assetManager.open(fileName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (is != null) {
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                result = new String(buffer, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
