package com.harryrickards.nhtg14;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Keep track of current location using GPS.
 */
public class LocationDetector implements  LocationListener {
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private Location mLocation;
    private Context mContext;

    public LocationDetector(Context context) {
        mContext = context;

        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider1 = LocationManager.GPS_PROVIDER;
        String locationProvider2 = LocationManager.NETWORK_PROVIDER;

        // Check GPS is enabled
        if (!(locationManager.isProviderEnabled(locationProvider1))) {
            // TODO Something sensible here...
            Log.w("nhtg14", "damnit jim, turn on gps location");
        }

        // Register listeners
        locationManager.requestLocationUpdates(locationProvider1, 0, 0, this);
        locationManager.requestLocationUpdates(locationProvider2, 0, 0, this);
    }

    // Listeners when location changes
    @Override
    public void onLocationChanged(Location location) {
        if (isBetterLocation(location, mLocation)) {
            mLocation = location;
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}

    // Get location
    public Location getLocation() {
        return mLocation;
    }

    // From google docs
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 50;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
