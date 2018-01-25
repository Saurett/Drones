package texium.mx.drones;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import texium.mx.drones.services.SharedPreferencesService;
import texium.mx.drones.utils.Constants;

/**
 * Created by jvier on 09/08/2017.
 */

public class LocationService extends Service {

    private final String TAG = this.getClass().getName();
    private static final int LOCATION_INTERVAL = 2000;
    private static final float LOCATION_DISTANCE = 10f;

    private LocationManager locationManager = null;
    private LocationListener[] locationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    private class LocationListener implements android.location.LocationListener {

        Location lastLocation;

        public LocationListener(String provider) {
            lastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "Location changed service: " + location);

            if (null != location) {
                SharedPreferencesService.saveSessionPreferences(getApplicationContext(), location);
            }

            lastLocation.set(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "Status changed: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "Provider enabled: " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "Provider disabled: " + provider);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Log.d(TAG, "On Create");

        //App.getApp().getAppComponent().inject(this);

        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }

        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners[0]);
        } catch (SecurityException e) {
            //Log.e(TAG, "Location update failed", e);
        }
    }

    @Override
    public void onDestroy() {
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(locationListeners[0]);
            } catch (SecurityException e) {
                //Log.e(TAG, "Listener remove failed", e);
            }
        }

        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d(TAG, "Start command");
        super.onStartCommand(intent, flags, startId);

        //authenticationService.reloadAuthenticationStatus();

        return START_STICKY;
    }


}
