package com.jshikami235gmail.pataride;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.jshikami235gmail.pataride.R.drawable.pin;
import static com.jshikami235gmail.pataride.R.menu.menu_splash_screen;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleMap = ((MapFragment) getFragmentManager().
                findFragmentById(R.id.mapFragment)).getMap();

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(-1.298563, 36.790462))
                .title("Ihub");

        //Set the icon of the maker
        markerOptions.icon(BitmapDescriptorFactory.fromResource(pin));

        //Enable location indicator.
        mGoogleMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, (LocationListener) this);
        //Enable zoom control buttons
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        //Enable zoom gestures
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        //Display the compass
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        //Enable Rotation
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);


        //Add the maker to the map
        mGoogleMap.addMarker(markerOptions);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Called when the location has changed.
     * <p/>
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */

        public void onLocationChanged (Location location){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            mGoogleMap.addMarker(new MarkerOptions().position(latLng));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        }


    @Override
    public void onMapReady(GoogleMap googleMap) {



    Button btn = (Button) findViewById(R.id.btn_ride);
    btn.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            // Perform action on click

            Intent activityChangeIntent = new Intent(MapsActivity.this, Pay.class);

            // currentContext.startActivity(activityChangeIntent);

            MapsActivity.this.startActivity(activityChangeIntent);
        }
    });
}

}