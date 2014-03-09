package com.harryrickards.nhtg14;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


import java.util.ArrayList;
import java.util.Collections;

/**
 * Detect when user is stationary (not moving)
 */
public class MotionDetector implements SensorEventListener {
    private Context mContext;
    private MotionDetectorInterface mCallback;

    private ArrayList<Float> gValues = new ArrayList<Float>();

    private SensorManager sensorManager;
    private static final float ACCELEROMETER_THRESHOLD = 1; // Range
    private static final float NEEDED_TIME_DELTA = 1*1000000000; // Time to take measurements for in ns
    private long accelerometerTime = System.nanoTime();

    private static final int MOTION_STATE = 0;
    private static final int STATIONARY_STATE = 1;
    private int mState = STATIONARY_STATE;

    public MotionDetector(Context context) {
        mContext = context;
        mCallback = (MotionDetectorInterface) context;

        // Setup accelerometer
        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    public interface MotionDetectorInterface {
        public void onUserStopped();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            onAccelerometerValueFound(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int value) {
    }

    protected void onAccelerometerValueFound(SensorEvent event) {
        float[] values = event.values;

        float x = values[0];
        float y = values[1];
        float z = values[2];

        float g = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        gValues.add(g);

        // If we've been sampling for long enough
        long currentTime = System.nanoTime();
        long timeDelta = currentTime - accelerometerTime;
        if (timeDelta > NEEDED_TIME_DELTA) {
            float range = (Float) Collections.max(gValues) - (Float) Collections.min(gValues);

            // New state to switch to
            int newState;
            if (range > ACCELEROMETER_THRESHOLD) {
                newState = MOTION_STATE;
            } else {
                newState = STATIONARY_STATE;
            }

            // If user has stopped
            if (mState == MOTION_STATE && newState == STATIONARY_STATE) {
                mCallback.onUserStopped();
            }

            mState = newState;
            gValues.clear();
            accelerometerTime = currentTime;
        }
    }
}
