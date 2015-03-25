package com.thram.thrammusicplayer.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Handler;

import com.thram.thrammusicplayer.App;
import com.thram.thrammusicplayer.R;

import java.io.IOException;

/**
 * Created by thram on 10/03/15.
 */
public class MediaPlayerManager {
    private static MediaPlayer currentPlayer = null;
    private static MediaPlayer auxPlayer = null;
    private static Visualizer visualizer;
    public static Equalizer equalizer;
    public static AudioManager audioManager;


    public static void prepare(Uri uri) throws FileException {
        auxPlayer = MediaPlayer.create(App.context, uri);
        if (audioManager == null) {
            audioManager = (AudioManager) App.context.getSystemService(Context.AUDIO_SERVICE);
        }
        if (auxPlayer != null) {
            setupVisualizer();
            setupEqualizer();
        } else {
            throw new FileException(App.context.getResources().getString(R.string.corrupted_file));
        }


    }

    private static void setupVisualizer() {
        // Create the Visualizer object and attach it to our media currentPlayer.
        visualizer = new Visualizer(auxPlayer.getAudioSessionId());
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

        // When the stream ends, we don't need to collect any more data. We don't do this in
        // setupVisualizerFxAndUI because we likely want to have more, non-Visualizer related code
        // in this callback.
        auxPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                visualizer.setEnabled(false);
            }
        });
    }

    private static void setupEqualizer() {
        equalizer = new Equalizer(0, auxPlayer.getAudioSessionId());
        equalizer.setEnabled(true);
    }

    public static void setOnDataCaptureListener(Visualizer.OnDataCaptureListener onDataCaptureListener) {
        // Create the Visualizer object and attach it to our media currentPlayer.
        visualizer.setDataCaptureListener(onDataCaptureListener, Visualizer.getMaxCaptureRate() / 2, true, false);
        visualizer.setEnabled(true);
    }

    public static void start() {
        AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        currentPlayer.start();
                        break; // resume playback
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        currentPlayer.pause();
                        break;
                }
            }
        };
        int result = audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            if (currentPlayer != null && currentPlayer.isPlaying()) {
                crossFade();
            } else {
                currentPlayer = auxPlayer;
                currentPlayer.start();
                auxPlayer = null;
            }

        }
    }

    private static void crossFade() {
        MediaPlayerManager.fadeOut(currentPlayer, 1500);
        MediaPlayerManager.fadeIn(auxPlayer, 1500);
        currentPlayer = auxPlayer;
        auxPlayer = null;
    }

    public static void fadeOut(final MediaPlayer _player, final int duration) {
        final float deviceVolume = getDeviceVolume();
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            private float time = duration;
            private float volume = 0.0f;

            @Override
            public void run() {
                if (!_player.isPlaying())
                    _player.start();
                // can call h again after work!
                time -= 100;
                volume = (deviceVolume * time) / duration;
                _player.setVolume(volume, volume);
                if (time > 0)
                    h.postDelayed(this, 100);
                else {
                    _player.stop();
                    _player.release();
                }
            }
        }, 100); // 1 second delay (takes millis)


    }

    public static void fadeIn(final MediaPlayer _player, final int duration) {
        final float deviceVolume = getDeviceVolume();
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            private float time = 0.0f;
            private float volume = 0.0f;

            @Override
            public void run() {
                if (!_player.isPlaying())
                    _player.start();
                // can call h again after work!
                time += 100;
                volume = (deviceVolume * time) / duration;
                _player.setVolume(volume, volume);
                if (time < duration)
                    h.postDelayed(this, 100);
            }
        }, 100); // 1 second delay (takes millis)

    }

    public static void pause() {
        currentPlayer.pause();
    }

    private static void reset() {
        visualizer.release();
        equalizer.release();
        resetPlayerA();
        resetPlayerB();
    }

    private static void resetPlayerA() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer.release();
            currentPlayer = null;
        }
    }

    private static void resetPlayerB() {
        if (auxPlayer != null) {
            auxPlayer.stop();
            auxPlayer.release();
            auxPlayer = null;
        }

    }

    public static void onPause(Activity activity) {
        if (activity.isFinishing() && currentPlayer != null) {
            reset();
        }
    }

    public static boolean isPlaying() {
        return (currentPlayer != null && currentPlayer.isPlaying()) || (auxPlayer != null && auxPlayer.isPlaying());
    }

    public static float getDeviceVolume() {
        int volumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        return (float) volumeLevel / maxVolume;
    }

    public static class FileException extends IOException {

        public FileException(String string) {
            super(string);
        }
    }
}
