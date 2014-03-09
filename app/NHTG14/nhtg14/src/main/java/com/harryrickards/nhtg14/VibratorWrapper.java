package com.harryrickards.nhtg14;

import android.content.Context;
import android.os.Vibrator;

/**
 * Simple wrapper around android.os.Vibrator
 */
public class VibratorWrapper {
    final static long VIBRATE_ON_TIME = 500;
    final static long VIBRATE_OFF_TIME = 500;

    // Courtesy of O2 Labs:
    // https://github.com/o2Labs/handshake-html5-vibrate-haptic-library/blob/master/handshake.js
    // 0 at the start as native vibration has a delay before the vibration starts
    final static long[] STAR_WARS_PATTERN = new long[] {0,500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500};
    final static double STAR_WARS_SCALE_FACTOR = 1.2; // Their pattern's too fast

    public static void vibrate(Context context, int rating) {
        long[] pattern;

        // Play star wars pattern if rating is 1
        if (rating == 1) {
            pattern = STAR_WARS_PATTERN;
            for (int i = 0; i < pattern.length; i++) {
                pattern[i] *= STAR_WARS_SCALE_FACTOR;
            }
        } else {
            pattern = new long[rating*2];

            for (int i = 0; i < rating; i++) {
                pattern[2*i] = VIBRATE_ON_TIME;
                pattern[2*i+1] = VIBRATE_OFF_TIME;
            }
        }

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.cancel(); // Cancel any previous vibrations
        vibrator.vibrate(pattern, -1);
    }

    public static void vibrateEstablishment(Context context, Establishment establishment) {
        vibrate(context, establishment.rating);
    }
}
