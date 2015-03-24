package com.thram.thrammusicplayer.utils;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ViewFlipper;

/**
 * Created by thram on 10/03/15.
 */
public class Animations {

    public static enum Status {
        START,
        END
    }

    public static AnimationSet enter(View view, int duration, int offset, AnimationFactory.TranslateDirection direction) {
        AlphaAnimation fadeIn = fadeIn(duration, offset);
        TranslateAnimation translateAnimation = translate(duration, offset, direction, null);
        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(translateAnimation);
        animation.addAnimation(fadeIn);
        view.startAnimation(animation);
        return animation;
    }

    public static AnimationSet leave(View view, int duration, int offset, AnimationFactory.TranslateDirection direction) {
        AlphaAnimation fadeOut = fadeOut(duration, offset);
        TranslateAnimation translateAnimation = translate(duration, offset, null, direction);
        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(translateAnimation);
        animation.addAnimation(fadeOut);
        view.startAnimation(animation);
        return animation;
    }

    public static AlphaAnimation fadeOut(int duration, int offset) {
        AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(offset);
        fadeOut.setDuration(duration);
        return fadeOut;
    }

    public static AlphaAnimation fadeIn(int duration, int offset) {
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator()); //and this
        fadeIn.setStartOffset(offset);
        fadeIn.setDuration(duration);
        return fadeIn;
    }

    public static TranslateAnimation translate(int duration, int offset, AnimationFactory.TranslateDirection from, AnimationFactory.TranslateDirection to) {
        float fromX = 0.0f, toX = 0.0f, fromY = 0.0f, toY = 0.0f;
        if (from != null)
            switch (from) {
                case TOP:
                    fromY = -1.0f;
                    break;
                case BOTTOM:
                    fromY = 1.0f;
                    break;
                case RIGHT:
                    fromX = 1.0f;
                    break;
                case LEFT:
                    fromX = -1.0f;
                    break;
            }
        if (to != null)
            switch (to) {
                case TOP:
                    toY = -1.0f;
                    break;
                case BOTTOM:
                    toY = 1.0f;
                    break;
                case RIGHT:
                    toX = 1.0f;
                    break;
                case LEFT:
                    toX = -1.0f;
                    break;
            }
        TranslateAnimation translateAnimation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, fromX,
                TranslateAnimation.RELATIVE_TO_SELF, toX,
                TranslateAnimation.RELATIVE_TO_SELF, fromY,
                TranslateAnimation.RELATIVE_TO_SELF, toY);
        translateAnimation.setStartOffset(offset);
        translateAnimation.setDuration(duration);
        return translateAnimation;
    }

    public static void flip(View view, AnimationFactory.FlipDirection flipDirection) {
        AnimationFactory.flipTransition((ViewFlipper) view, flipDirection, 200);

    }

}
