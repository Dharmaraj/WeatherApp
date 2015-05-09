package com.weatherapp.utilities;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by dharmaraj on 5/8/15.
 */
public class CurrentLocationInformation extends Service implements LocationListener {


    private Context mContext;


    // minimum distance to change location coordinates (100 meters)
    private static final long minDistance = 100;

    // minumum time(milliseconds) to update location (1 hour)
    private static final long minTime = 1000 * 60 * 60 * 1;

    protected LocationManager locationManager;

    Location location;
    double latitude;
    double longitude;

    // flag for GPS provider
    boolean isGPSEnabled = false;

    // flag for network provider
    boolean isNetworkEnabled = false;

    boolean currentLocationStatus = false;


    public CurrentLocationInformation(Context context) {
        this.mContext = context;
        getCurrentLocationInfo();
    }

    private Location getCurrentLocationInfo() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);


            //  network provider status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


            if (isNetworkEnabled || isGPSEnabled) {
                this.currentLocationStatus = true;
            }

            // First get location from Network Provider because Network provider is fast and consume low battery power than GPS.
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);
                Log.d("Network", "Network");
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            } else {

                if (!isGPSEnabled) {
                    this.currentLocationStatus = false;
                    Toast.makeText(mContext, "Unable to get Location,Please enable GPS", Toast.LENGTH_LONG).show();
                    return location;
                }
                // if GPS Enabled get Latitude & Longitude using GPS Provider,
                // GPS provider provide more accurate location than Network provider

                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
                    Log.d("GPS", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    //To get Current Latitude
    public double getCurrentLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    //To get Current Longitude
    public double getCurrentLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }


    //To Stop Location provider
    public void stopLocationProvider() {
        if (locationManager != null) {
            locationManager.removeUpdates(CurrentLocationInformation.this);
        }
    }

    //Check to get Current Location or not
    public boolean canGetCurrentLocation() {
        return this.currentLocationStatus;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
