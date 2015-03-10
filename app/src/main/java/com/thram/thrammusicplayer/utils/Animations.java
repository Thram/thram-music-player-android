package com.thram.thrammusicplayer.utils;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ViewFlipper;

/**
 * Created by thram on 10/03/15.
 */
public class Animations {

    public static AnimationSet enter(View view, int duration, int offset) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.1f,
                TranslateAnimation.RELATIVE_TO_SELF, 0.0f);
        translateAnimation.setStartOffset(offset);
        translateAnimation.setDuration(duration);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator()); //and this
        fadeIn.setStartOffset(offset);
        fadeIn.setDuration(duration);
        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(translateAnimation);
        animation.addAnimation(fadeIn);
        view.startAnimation(animation);
        return animation;
    }

    public static AnimationSet leave(View view, int duration, int offset, AnimationFactory.LeaveDirection direction) {
        float fromX = 0.0f, toX = 0.0f, fromY = 0.0f, toY = 0.0f;
        switch (direction) {
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
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(offset);
        fadeOut.setDuration(duration);
        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(translateAnimation);
        animation.addAnimation(fadeOut);
        view.startAnimation(animation);
        return animation;
    }

    public static void flip(View view, AnimationFactory.FlipDirection flipDirection) {
        AnimationFactory.flipTransition((ViewFlipper) view, flipDirection, 200);

    }

}
