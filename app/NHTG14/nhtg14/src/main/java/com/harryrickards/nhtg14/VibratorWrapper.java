package com.harryrickards.nhtg14;

import android.content.Context;
import android.os.Vibrator;

/**
 * Simple wrapper around android.os.Vibrator
 */
public class VibratorWrapper {
    final static long VIBRATE_ON_TIME = 500;
    final static long VIBRATE_OFF_TIME = 500;

    public static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VIBRATE_ON_TIME);
    }

    public static void vibrate(Context context, int cycles) {
        long[] pattern = new long[cycles*2];

        for (int i = 0; i < cycles; i++) {
            pattern[2*i] = VIBRATE_ON_TIME;
            pattern[2*i+1] = VIBRATE_OFF_TIME;
        }

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, -1);
    }

    public static void vibrateEstablishment(Context context, Establishment establishment) {
        vibrate(context, establishment.rating.intValue());
    }
}
