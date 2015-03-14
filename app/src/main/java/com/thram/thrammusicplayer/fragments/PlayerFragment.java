package com.thram.thrammusicplayer.fragments;

import android.media.AudioManager;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thram.thrammusicplayer.App;
import com.thram.thrammusicplayer.R;
import com.thram.thrammusicplayer.activities.ThramMusicPlayerActivity;
import com.thram.thrammusicplayer.utils.MediaPlayerManager;
import com.thram.thrammusicplayer.views.VisualizerView;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by thram on 8/03/15.
 * <p/>
 * Player Fragment
 */
public class PlayerFragment extends Fragment {

    private static final float VISUALIZER_HEIGHT_DIP = 200f;
    private String fileUri;
    private ViewGroup rootView;


    public static PlayerFragment newInstance(String fileUri) {
        PlayerFragment playerFragment = new PlayerFragment();
        playerFragment.fileUri = fileUri;
        return playerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((ThramMusicPlayerActivity) getActivity()).setWindowsBackground(getResources().getColor(R.color.player_color));
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_player, container, false);
        GifImageView gifView = (GifImageView) rootView.findViewById(R.id.background);
        try {
            GifDrawable gif = new GifDrawable(App.assetManager, App.Preferences.DEFAULT_WALLPAPER_THRAM);
            gif.setSpeed(.5f);
            gifView.setImageDrawable(gif);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        if (fileUri == null && MediaPlayerManager.player == null) {
            Toast.makeText(getActivity(), "Select a file from the library...", Toast.LENGTH_SHORT).show();
        } else {
            setupMediaPlayer();
        }
        return rootView;
    }

    private void setupMediaPlayer() {
        TextView mStatusTextView = (TextView) rootView.findViewById(R.id.player_status);
        try {
            MediaPlayerManager.prepare(Uri.parse(fileUri));
        } catch (IOException e) {
            Toast.makeText(getActivity(), "Error trying to open the file...", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        setupVisualizerFxAndUI();
        setupEqualizerFxAndUI();

        MediaPlayerManager.start();
        mStatusTextView.setText("Playing audio...");
    }

    private void setupEqualizerFxAndUI() {
        // Create the Equalizer object (an AudioEffect subclass) and attach it to our media player,
        // with a default priority (0).
        short bands = MediaPlayerManager.equalizer.getNumberOfBands();

        final short minEQLevel = MediaPlayerManager.equalizer.getBandLevelRange()[0];
        final short maxEQLevel = MediaPlayerManager.equalizer.getBandLevelRange()[1];

        LinearLayout container = (LinearLayout) rootView.findViewById(R.id.frequencies);
        for (short i = 0; i < bands; i++) {
            final short band = i;

            TextView freqTextView = new TextView(getActivity());
            freqTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            freqTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            freqTextView.setText((MediaPlayerManager.equalizer.getCenterFreq(band) / 1000) + " Hz");
            container.addView(freqTextView);

            LinearLayout row = new LinearLayout(getActivity());
            row.setOrientation(LinearLayout.HORIZONTAL);

            TextView minDbTextView = new TextView(getActivity());
            minDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            minDbTextView.setText((minEQLevel / 100) + " dB");

            TextView maxDbTextView = new TextView(getActivity());
            maxDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            maxDbTextView.setText((maxEQLevel / 100) + " dB");

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            SeekBar bar = new SeekBar(getActivity());
            bar.setLayoutParams(layoutParams);
            bar.setMax(maxEQLevel - minEQLevel);
            bar.setProgress(MediaPlayerManager.equalizer.getBandLevel(band));

            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    MediaPlayerManager.equalizer.setBandLevel(band, (short) (progress + minEQLevel));
                }

                public void onStartTrackingTouch(SeekBar seekBar) {}

                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

            row.addView(minDbTextView);
            row.addView(bar);
            row.addView(maxDbTextView);

            container.addView(row);
        }
    }

    private void setupVisualizerFxAndUI() {
        // Create a VisualizerView (defined below), which will render the simplified audio
        // wave form to a Canvas.
        final VisualizerView mVisualizerView = new VisualizerView(getActivity());
        mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (VISUALIZER_HEIGHT_DIP * getResources().getDisplayMetrics().density)));
        ((RelativeLayout) rootView.findViewById(R.id.visualizer_container)).addView(mVisualizerView);

        // Create the Visualizer object and attach it to our media player.
        MediaPlayerManager.setOnDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
                mVisualizerView.updateVisualizer(bytes);
            }

            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {}
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        MediaPlayerManager.onPause(getActivity());

    }
}
