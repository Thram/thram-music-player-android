package com.thram.thrammusicplayer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thram.thrammusicplayer.R;
import com.thram.thrammusicplayer.activities.ThramMusicPlayerActivity;

import java.io.File;
import java.util.Arrays;

import static android.os.Environment.MEDIA_MOUNTED;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStorageState;

/**
 * Created by thram on 8/03/15.
 *
 * Use http://api.wikia.com/wiki/LyricWiki_API to get the lyrics
 */
public class LyricsFragment extends Fragment {

    public final String DEFAULT_LIBRARY_PATH = "Music";


    public static LyricsFragment newInstance() {
        return new LyricsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((ThramMusicPlayerActivity) getActivity()).setWindowsBackground(getResources().getColor(R.color.lyrics_color));
        getActivity().setTitle("Lyrics");
        //getting SDcard root path
        final String state = getExternalStorageState();
        if (MEDIA_MOUNTED.equals(state)) {
            File[] files = getExternalStorageDirectory().listFiles();
            Log.d("SD CARD", Arrays.toString(files));
        } else {
            Log.d("SD CARD", "Not mounted");
        }
        View rootView = inflater.inflate(R.layout.fragment_lyrics, container, false);
        return rootView;
    }
}
