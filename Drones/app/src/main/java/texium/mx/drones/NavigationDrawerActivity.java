package texium.mx.drones;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.math.BigDecimal;

import texium.mx.drones.adapters.TaskListAdapter;
import texium.mx.drones.fragments.CloseTasksFragment;
import texium.mx.drones.fragments.FinishTasksFragment;
import texium.mx.drones.fragments.NewsTasksFragment;
import texium.mx.drones.fragments.PendingTasksFragment;
import texium.mx.drones.fragments.ProgressTasksFragment;
import texium.mx.drones.fragments.RevisionTasksFragment;
import texium.mx.drones.fragments.inetrface.FragmentTaskListener;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.TasksDecode;


public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, View.OnClickListener, FragmentTaskListener, LocationListener {

    //Activación Google Maps//
    private GoogleMap mMap;

    //Botones Principales//
    private FloatingActionButton fab, chat_fab,camera_fab,video_fab;

    //Header Dinamico//
    private TextView task_force_name, task_element_name, task_force_location, task_force_latitude, task_force_longitude;


    //Administración del GPS//
    private static final String provider = LocationManager.GPS_PROVIDER; //Recomendado GPS
    private LocationManager locationManagerGPS;
    private Location locationGPS;
    private Context ctx;
    private boolean providerEnabled;

    //Administración  de la Camara//
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 2;
    private Intent cameraIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(""); //Quita el titulo del activity

        //Botones flotantes principales//
        fab = (FloatingActionButton) findViewById(R.id.fab);
        chat_fab = (FloatingActionButton) findViewById(R.id.chat_fab);
        camera_fab = (FloatingActionButton) findViewById(R.id.camera_fab);
        video_fab = (FloatingActionButton) findViewById(R.id.video_fab);

        //Listeres de los botones//
        fab.setOnClickListener(this);
        chat_fab.setOnClickListener(this);
        camera_fab.setOnClickListener(this);
        video_fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Activación del Google Maps//
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Administar ubicación por GPS//
        defineLocationManager(this);

        //Administrar Header dinamico//
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

            double lat = locationGPS.getLatitude();
            double lon = locationGPS.getLongitude();

            double x = Math.abs(lat);
            double dx = Math.floor(x);
            double mx = Math.floor((x - dx) * 60);
            double sx = Math.floor(((x - dx) - (mx / 60)) * 3600);

            if (lat < 0) dx = -dx;

            double y = Math.abs(lon);
            double dy = Math.floor(y);
            double my = Math.floor((y - dy) * 60);
            double sy = Math.floor(((y - dy) - (my / 60)) * 3600);

            if (lon < 0) dy = -dy;

            task_force_latitude.setText(BigDecimal.valueOf(dx).longValue() + "° " + BigDecimal.valueOf(mx).longValue() + "' " + BigDecimal.valueOf(sx).longValue() + "'' N");
            task_force_longitude.setText(BigDecimal.valueOf(dy).longValue() + "° " + BigDecimal.valueOf(my).longValue() + "' " + BigDecimal.valueOf(sy).longValue() + "'' O");
        }

    }

    //fab action onClik
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab:
                //TODO Enviar WEB SERVICES
                Snackbar.make(v, "Mi ubicación ha sido enviada", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                onMapReady(mMap);
                break;
            case R.id.chat_fab:
                Snackbar.make(v, "El chat no esta activo", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.camera_fab:
                mediaContent(MediaStore.ACTION_IMAGE_CAPTURE,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.video_fab:
                mediaContent(MediaStore.ACTION_VIDEO_CAPTURE,CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
                break;
            default:
                Snackbar.make(v, "La opción no esta habilitada", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }
    }

    //Save media content
    private void mediaContent(String mediaType, int requestType) {
        cameraIntent = new Intent(mediaType);

        if(cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, requestType);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
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

        } else if (id == R.id.nav_logout) {
            finish();
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

        Fragment finish = fragmentManager.findFragmentByTag("fragment_finish_taks");
        if (null != finish) {
            fragmentManager.beginTransaction().remove(finish).commit();
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

        /*switch (view.getId()) {

            case R.id.task_title_close_button:*/
                FragmentManager fragmentManager = getSupportFragmentManager();
                removeAllFragment(fragmentManager);
                /*break;
            default:
                break;
        }*/
    }

    @Override
    public void taskActions(View v, TaskListAdapter taskListAdapter, Tasks task,TasksDecode tasksDecode) {

        switch (v.getId()) {
            case R.id.agree_task_button:
                Snackbar.make(v, "Tarea aceptada :" + task.getTask_tittle(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                taskListAdapter.remove(tasksDecode.getTask_position());
                taskListAdapter.notifyItemRemoved(tasksDecode.getTask_position());
                taskListAdapter.notifyDataSetChanged();
                break;
            case R.id.decline_task_button:
                Snackbar.make(v, "Tarea pospuesta :" + task.getTask_tittle(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                taskListAdapter.remove(tasksDecode.getTask_position());
                taskListAdapter.notifyItemRemoved(tasksDecode.getTask_position());
                taskListAdapter.notifyDataSetChanged();
                break;
            case R.id.finish_task_button:
                Snackbar.make(v, "Tarea finalizada :" + task.getTask_tittle(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                taskListAdapter.remove(tasksDecode.getTask_position());
                taskListAdapter.notifyItemRemoved(tasksDecode.getTask_position());
                taskListAdapter.notifyDataSetChanged();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.tasks_finish_fragment_container, new FinishTasksFragment(), "fragment_finish_taks");
                fragmentTransaction.commit();

                break;
            default:
                break;
        }

        if (taskListAdapter.getItemCount() == 0) {
            closeActiveTaskFragment(v);
        }

        //FragmentManager fragmentManager = getSupportFragmentManager();
        //removeAllFragment(fragmentManager);
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
