package com.thram.thrammusicplayer.views;

/**
 * Created by thram on 7/03/15.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.view.View;

import com.thram.thrammusicplayer.R;
import com.thram.thrammusicplayer.utils.DevelopmentTools;

import java.util.Random;

/**
 * A simple class that draws waveform data received from a
 * {@link android.media.audiofx.Visualizer.OnDataCaptureListener#onWaveFormDataCapture }
 */
public class VisualizerView extends View {
    private byte[] mBytes;
    private float[] mPoints;
    private Rect mRect = new Rect();

    private Paint mForePaint = new Paint();
    private int[] colorOrder;

    public VisualizerView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mBytes = null;
        mForePaint.setStrokeWidth(4f);
        mForePaint.setAntiAlias(true);
        mForePaint.setAlpha(80);                             //you can set your transparent value here

        Random rnd = new Random();
//        mForePaint.setShader(new LinearGradient(0, 0, 0, 200, Color.RED, Color.GREEN, Shader.TileMode.MIRROR));
//        mForePaint.setShader(gradient);
        int color1 = getResources().getColor(R.color.home_color);
        int color2 = getResources().getColor(R.color.player_color);
        int color3 = getResources().getColor(R.color.library_color);
        int color4 = getResources().getColor(R.color.news_color);
        int color5 = getResources().getColor(R.color.playlist_color);
        int color6 = getResources().getColor(R.color.lyrics_color);
        colorOrder = new int[]{color1, color2, color3, color4, color5, color6};
    }

    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        LinearGradient lg = new LinearGradient(0, 0, 0, getHeight(),
                DevelopmentTools.shuffleArray(colorOrder),
                new float[]{0, 0.2f, 0.4f, 0.6f, 0.8f, 1}, Shader.TileMode.REPEAT);
        mForePaint.setShader(lg);
        if (mBytes == null) {
            return;
        }

        int factor = 4;

        if (mPoints == null || mPoints.length < mBytes.length * factor) {
            mPoints = new float[mBytes.length * factor];
        }

        mRect.set(0, 0, getWidth(), getHeight());

        for (int i = 0; i < mBytes.length - 1; i++) {
            mPoints[i * factor] = mRect.width() * i / (mBytes.length - 1);
            mPoints[i * factor + 1] = mRect.height() / 2
                    + ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
            mPoints[i * factor + 2] = mRect.width() * (i + 1) / (mBytes.length - 1);
            mPoints[i * factor + 3] = mRect.height() / 2
                    + ((byte) (mBytes[i + 1] + 128)) * (mRect.height() / 2) / 128;
        }
        canvas.drawLines(mPoints, mForePaint);
    }
}