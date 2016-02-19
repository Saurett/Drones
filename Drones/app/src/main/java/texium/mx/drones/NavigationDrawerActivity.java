package texium.mx.drones;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import texium.mx.drones.adapters.TaskListAdapter;
import texium.mx.drones.fragments.CloseTasksFragment;
import texium.mx.drones.fragments.inetrface.FragmentTaskListener;
import texium.mx.drones.fragments.NewsTasksFragment;
import texium.mx.drones.fragments.PendingTasksFragment;
import texium.mx.drones.fragments.ProgressTasksFragment;
import texium.mx.drones.fragments.RevisionTasksFragment;


public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, View.OnClickListener, FragmentTaskListener, LocationListener {

    private static final String provider = LocationManager.GPS_PROVIDER; //Recomendado GPS

    private GoogleMap mMap;

    private FloatingActionButton fab, chat_fab;
    private TextView task_force_name, task_element_name, task_force_location, task_force_latitude, task_force_longitude;
    private LocationManager locationManagerGPS;
    private Location locationGPS;
    private Context ctx;

    private boolean providerEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        chat_fab = (FloatingActionButton) findViewById(R.id.chat_fab);

        fab.setOnClickListener(this);
        chat_fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        defineLocationManager(this);
        getTaskForceData(navigationView);
    }

    private void getTaskForceData(NavigationView navigationView) {

        View headerLayout = navigationView.getHeaderView(0);

        task_force_name = (TextView) headerLayout.findViewById(R.id.task_force_name);
        task_element_name = (TextView) headerLayout.findViewById(R.id.task_element_name);
        task_force_location = (TextView) headerLayout.findViewById(R.id.task_force_location);
        task_force_latitude = (TextView) headerLayout.findViewById(R.id.task_force_latitude);
        task_force_longitude = (TextView) headerLayout.findViewById(R.id.task_force_longitude);

        task_force_name.setText("CUAMX-HISTORICO-C");
        task_element_name.setText("Francisco Javier\nDíaz\nSaurett");
        task_force_location.setText("CIUDAD DE MÉXICO");

        getLocation();


        if (locationGPS != null) {
            task_force_latitude.setText(String.valueOf(locationGPS.getLatitude()));
            task_force_longitude.setText(String.valueOf(locationGPS.getLongitude()));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab:

                Snackbar.make(v, "Mi ubicación ha sido enviada", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                onMapReady(mMap);
                break;
            case R.id.chat_fab:

                Snackbar.make(v, "El chat no esta activo", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();
        removeAllFragment(fragmentManager);

        if (id == R.id.nav_news_task) {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.tasks_fragment_container, new NewsTasksFragment(), "fragment_news_taks");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_progress_task) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.tasks_fragment_container, new ProgressTasksFragment(), "fragment_progress_taks");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_pending_task) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.tasks_fragment_container, new PendingTasksFragment(), "fragment_pending_taks");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_revision_task) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.tasks_fragment_container, new RevisionTasksFragment(), "fragment_revision_taks");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_close_task) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.tasks_fragment_container, new CloseTasksFragment(), "fragment_close_taks");
            fragmentTransaction.commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void removeAllFragment(FragmentManager fragmentManager) {

        Fragment news = fragmentManager.findFragmentByTag("fragment_news_taks");
        if (null != news) {
            fragmentManager.beginTransaction().remove(news).commit();
        }

        Fragment pending = fragmentManager.findFragmentByTag("fragment_pending_taks");
        if (null != pending) {
            fragmentManager.beginTransaction().remove(pending).commit();
        }

        Fragment close = fragmentManager.findFragmentByTag("fragment_close_taks");
        if (null != close) {
            fragmentManager.beginTransaction().remove(close).commit();
        }

        Fragment progress = fragmentManager.findFragmentByTag("fragment_progress_taks");
        if (null != progress) {
            fragmentManager.beginTransaction().remove(progress).commit();
        }

        Fragment revision = fragmentManager.findFragmentByTag("fragment_revision_taks");
        if (null != revision) {
            fragmentManager.beginTransaction().remove(revision).commit();
        }
    }

    //TODO MOVER A MapsActivity
    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng cdMx = new LatLng(19.4265606, -99.0672223);

        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cdMx, 10));

        //Seteamos el tipo de mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
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


    @Override
    public void closeActiveTaskFragment(View view) {

        switch (view.getId()) {

            case R.id.task_title_close_button:
                FragmentManager fragmentManager = getSupportFragmentManager();
                removeAllFragment(fragmentManager);
                break;
            default:
                break;
        }
    }

    @Override
    public void agreeTask(View v, TaskListAdapter taskListAdapter, int position) {
        Snackbar.make(v, "Tarea aceptada # " + position, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        taskListAdapter.remove(position);
    }

    private void defineLocationManager(Context context) {
        this.ctx = context;

        locationManagerGPS = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        providerEnabled = locationManagerGPS.isProviderEnabled(provider);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                locationManagerGPS.requestLocationUpdates(provider, 1000, 0, this);
            }
        } else {
            locationManagerGPS.requestLocationUpdates(provider, 1000, 0, this);
        }
    }

    private void getLocation() {
        if (providerEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationGPS = locationManagerGPS.getLastKnownLocation(provider);
            } else {
                locationGPS = locationManagerGPS.getLastKnownLocation(provider);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        getLocation();

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
