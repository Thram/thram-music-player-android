package com.thram.thrammusicplayer.utils;

import java.util.Random;

/**
 * Created by thram on 11/03/15.
 */
public class DevelopmentTools {

    public static int[] shuffleArray(int[] array) {
        int index;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            if (index != i) {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
        return array;
    }
}
