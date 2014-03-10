package com.harryrickards.nhtg14;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity
        implements MotionDetector.MotionDetectorInterface,
        Establishment.EstablishmentInterface,
        SearchFragment.OnSearchListener {

    // Fragments
    private SearchFragment searchFragment;
    private ResultsFragment resultsFragment;

    // Detectors
    private MotionDetector motionDetector;
    private LocationDetector locationDetector;

    // For getting establishment details
    private Establishment establishment;
    private boolean gettingEstablishmentDetails = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get fragments
        searchFragment = (SearchFragment) getFragmentManager().findFragmentById(R.id.searchFragment);
        resultsFragment = (ResultsFragment) getFragmentManager().findFragmentById(R.id.resultsFragment);

        // Hide resultsFragment as we have no results yet
        getFragmentManager().beginTransaction().hide(resultsFragment).commit();

        // Set up detectors
        motionDetector = new MotionDetector(this);
        locationDetector = new LocationDetector(this);

        PowerManager mgr = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = mgr.newWakeLock(PowerManager.FULL_WAKE_LOCK, "NHTGWakeLock");
        wakeLock.acquire();
        // TODO After x minutes, wakeLock.release()
    }

    // Search initialisation
    public void onUserStopped() { onEstablishmentSearchRequested(); }
    public void onManualSearch() { onEstablishmentSearchRequested(); }


    // Begin a search
    public void onEstablishmentSearchRequested() {
        // Get most recent location
        Location location = locationDetector.getLocation();

        if (!gettingEstablishmentDetails && location != null) {
            getEstablishmentDetails(location);
        }
    }

    protected void getEstablishmentDetails(Location location) {
        Log.w("nhtg14", "getEstablishmentDetails");
        gettingEstablishmentDetails = true;
        if (searchFragment != null) { searchFragment.onSearchStarted(); }

        // Get establishment details
        establishment = new Establishment(location, this);
    }

    public void onEstablishmentDetailsError() {
        gettingEstablishmentDetails = false;
        if (searchFragment != null) { searchFragment.onSearchStopped(); }

        Toast.makeText(this, getString(R.string.error_getting_details), Toast.LENGTH_SHORT).show();
    }

    public void onEstablishmentDetailsFound() {
        gettingEstablishmentDetails = false;
        if (searchFragment != null) { searchFragment.onSearchStopped(); }

        // Show results to user
        if (resultsFragment != null) { resultsFragment.showEstablishment(establishment); }

        // Make sure resultsFragment is shown
        getFragmentManager().beginTransaction().show(resultsFragment).commit();
        // Change search button to "search again"
        searchFragment.updateButtonText();

        // Vibrate based on rating
        VibratorWrapper.vibrateEstablishment(this, establishment);
    }

    // Default menu stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
