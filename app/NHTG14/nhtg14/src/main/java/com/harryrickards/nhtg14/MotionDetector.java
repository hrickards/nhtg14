package com.harryrickards.nhtg14;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

import java.util.Collections;

/**
 * Detect when user is stationary (not moving)
 */
public class MotionDetector implements SensorEventListener {
    private Context mContext;
    private MotionDetectorInterface mCallback;

    private CircularFifoBuffer gValues = new CircularFifoBuffer(50);
    private SensorManager sensorManager;
    private int accelerometerCounter = 0;

    private static final int ACCELEROMETER_LIMIT = 5;
    private static final float ACCELEROMETER_THRESHOLD = 1; // Range

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
                SensorManager.SENSOR_DELAY_NORMAL);
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
        accelerometerCounter += 1;
        float[] values = event.values;

        float x = values[0];
        float y = values[1];
        float z = values[2];

        float g = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        gValues.add(g);

        if (accelerometerCounter == ACCELEROMETER_LIMIT) {
            // Find range
            accelerometerCounter = 0;
            float range = (Float) Collections.max(gValues) - (Float) Collections.min(gValues);

            int newState;
            if (range > ACCELEROMETER_THRESHOLD) {
                newState = MOTION_STATE;
            } else {
                newState = STATIONARY_STATE;
            }

            // Log.w("nhtg14", "going from " + Integer.toString(mState) + " to " + Integer.toString(newState));

            if (mState == MOTION_STATE && newState == STATIONARY_STATE) {
                mCallback.onUserStopped();
            }

            mState = newState;
        }
    }
}
