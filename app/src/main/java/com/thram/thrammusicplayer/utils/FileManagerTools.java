package com.thram.thrammusicplayer.utils;

import android.database.Cursor;
import android.provider.MediaStore;

import com.thram.thrammusicplayer.App;
import com.thram.thrammusicplayer.model.AudioFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thram on 15/03/15.
 */
public class FileManagerTools {

    public static List<AudioFile> listMediaFiles() {
        return listMediaFiles(null, null);
    }

    public static List<AudioFile> listMediaFiles(String filterTag, String filterValue) {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.YEAR,
                MediaStore.Audio.Media.COMPOSER, MediaStore.Audio.Media.TRACK
        };
        Cursor cursor;
        if (filterTag != null) {
            selection = selection + " AND " + filterTag + " LIKE ?";
            cursor = App.context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection,
                    new String[]{filterValue}, null);
        } else {
            cursor = App.context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection,
                    null, null);
        }

        final List<AudioFile> audioFiles = new ArrayList<>();
        while (cursor.moveToNext()) {
            audioFiles.add(new AudioFile(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7)));
        }
        cursor.close();
        return audioFiles;
    }

    public static String encodeURIComponent(String s) {
        String result;

        try {
            result = URLEncoder.encode(s, "UTF-8")
                    .replaceAll("%2F", "/")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            result = s;
        }

        return result;
    }
}
