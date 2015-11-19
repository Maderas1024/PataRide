package com.jshikami235gmail.pataride;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.jshikami235gmail.pataride.R.drawable.pin;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initilizeMap();
        /*Notification checking whether GPS is enabled (GPS status) */
        LocationManager locationManager;
        boolean gps_enabled = false;
        boolean network_enabled = false;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ignored) {
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {
        }
        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MapsActivity.this);
            dialog.setTitle("GPS not enabled");
            dialog.setMessage("PataRide required your location. Do you want to enable GPS");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //this will navigate user to the device location settings screen
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            AlertDialog alert = dialog.create();
            alert.show();
        }

        FloatingActionButton requestRide = (FloatingActionButton) findViewById(R.id.btn_ride);
        requestRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Pay.class));
            }
        });

    }
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.*/

    @TargetApi(Build.VERSION_CODES.M)
    private void initilizeMap() {
        if (mGoogleMap == null) {
            mGoogleMap = ((MapFragment) getFragmentManager()
                    .findFragmentById(R.id.mapFragment)).getMap();
        }

        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(-1.298563, 36.790462))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.motorbike)));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(-1.292343, 36.790462))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.motorbike)));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(-1.292343, 38.790462))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.motorbike)));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(-1.278563, 36.790462))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.motorbike)));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(-1.298553, 36.790462))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.motorbike)));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(-1.268563, 36.790462))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.motorbike)));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(-1.298563, 36.490452))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.motorbike)));

        //Enable location indicator.
        mGoogleMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        //Enable zoom gestures
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        //Display the compass
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        //Enable Rotation
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);


        //Add the maker to the map
//        mGoogleMap.addMarker(markerOptions);

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

    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        mGoogleMap.addMarker(new MarkerOptions().position(latLng));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

}