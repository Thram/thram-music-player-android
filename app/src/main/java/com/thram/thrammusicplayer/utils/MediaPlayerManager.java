package com.thram.thrammusicplayer.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.net.Uri;

import com.thram.thrammusicplayer.App;

import java.io.IOException;

/**
 * Created by thram on 10/03/15.
 */
public class MediaPlayerManager {
    public static MediaPlayer player = null;
    public static Visualizer visualizer;
    public static Equalizer equalizer;
    private static AudioManager audioManager;


    public static void prepare(Uri uri) throws IOException {
        if (player != null && player.isPlaying()) {
            reset();
        }
        player = MediaPlayer.create(App.context, uri);
        if (audioManager == null) {
            audioManager = (AudioManager) App.context.getSystemService(Context.AUDIO_SERVICE);
        }
        setupVisualizer();
        setupEqualizer();

    }

    private static void setupVisualizer() {
        // Create the Visualizer object and attach it to our media player.
        visualizer = new Visualizer(player.getAudioSessionId());
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

        // When the stream ends, we don't need to collect any more data. We don't do this in
        // setupVisualizerFxAndUI because we likely want to have more, non-Visualizer related code
        // in this callback.
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                visualizer.setEnabled(false);
            }
        });
    }

    private static void setupEqualizer() {
        equalizer = new Equalizer(0, player.getAudioSessionId());
        equalizer.setEnabled(true);
    }

    public static void setOnDataCaptureListener(Visualizer.OnDataCaptureListener onDataCaptureListener) {
        // Create the Visualizer object and attach it to our media player.
        visualizer.setDataCaptureListener(onDataCaptureListener, Visualizer.getMaxCaptureRate() / 2, true, false);
        visualizer.setEnabled(true);
    }

    public static void start() {
        AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        player.start();
                        break; // resume playback
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        player.pause();
                        break;
                }
            }
        };
        int result = audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            player.start();
        }
    }

    public static void pause() {
        player.pause();
    }

    public static void reset() {
        visualizer.release();
        equalizer.release();
        player.stop();
        player.release();
        player = null;
    }

    public static void onPause(Activity activity) {
        if (activity.isFinishing() && player != null) {
            reset();
        }
    }
}
