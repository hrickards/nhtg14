package com.harryrickards.nhtg14;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity
        implements MotionDetector.MotionDetectorInterface,
        Establishment.EstablishmentInterface {

    private MotionDetector motionDetector;
    private LocationDetector locationDetector;
    private Establishment establishment;
    private boolean gettingEstablishmentDetails = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        motionDetector = new MotionDetector(this);
        locationDetector = new LocationDetector(this);
    }

    public void onUserStopped() {
        onEstablishmentSearchRequested();
    }


    public void onEstablishmentSearchRequested() {
        Location location = locationDetector.getLocation();
        Toast.makeText(this, "Hmm...", Toast.LENGTH_SHORT).show();


        if (!gettingEstablishmentDetails && location != null) {
            getEstablishmentDetails(location);
        }
    }

    protected void getEstablishmentDetails(Location location) {
        gettingEstablishmentDetails = true;
        //TODO fragment.onSearchStarted();

        // Get establishment details
        establishment = new Establishment(location, this);
    }

    public void onEstablishmentDetailsFound() {
        gettingEstablishmentDetails = false;
        // TODO fragment.onSearchStopped();

        // TODO Something with establishment
        Toast.makeText(this, establishment.establishmentName, Toast.LENGTH_SHORT).show();
    }

    // Default menu stuff
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
