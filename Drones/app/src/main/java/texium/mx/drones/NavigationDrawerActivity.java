package texium.mx.drones;

import android.Manifest;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
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

import org.ksoap2.serialization.SoapPrimitive;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import texium.mx.drones.adapters.TaskListAdapter;
import texium.mx.drones.fragments.CloseTasksFragment;
import texium.mx.drones.fragments.FinishTasksFragment;
import texium.mx.drones.fragments.NewsTasksFragment;
import texium.mx.drones.fragments.PendingTasksFragment;
import texium.mx.drones.fragments.ProgressTasksFragment;
import texium.mx.drones.fragments.RevisionTasksFragment;
import texium.mx.drones.fragments.inetrface.FragmentTaskListener;
import texium.mx.drones.models.FilesManager;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.TasksDecode;
import texium.mx.drones.models.Users;
import texium.mx.drones.services.SoapServices;
import texium.mx.drones.utils.Constants;


public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, View.OnClickListener, FragmentTaskListener, LocationListener {

    //Google Maps manager//
    private GoogleMap mMap;

    //Principal Buttons//
    private FloatingActionButton fab, chat_fab,camera_fab,video_fab;

    //Dynamic Header//
    private TextView task_force_name, task_element_name, task_force_location, task_force_latitude, task_force_longitude;

    //GPS Manager//
    private static final String provider = LocationManager.GPS_PROVIDER; //GPS Provider
    private LocationManager locationManagerGPS;
    private Location locationGPS;
    private Context ctx;
    private boolean providerEnabled;

    //Camera manager//
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 2;
    private static final int GALLERY_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int GALLERY_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private Intent cameraIntent;

    //Sessions control//
    private static Users SESSION_DATA;

    //Collections Controls//
    public Map<Long,Object> taskToken = new HashMap<>();
    public static Map<Integer,FilesManager> TASK_FILE = new HashMap<>();
    public static Integer ACTUAL_POSITION;

    NotificationManager nm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        SESSION_DATA =  (Users) getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_LOGIN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(""); //Set activity tittle

        //Principal floatingButtons//
        fab = (FloatingActionButton) findViewById(R.id.fab);
        chat_fab = (FloatingActionButton) findViewById(R.id.chat_fab);
        camera_fab = (FloatingActionButton) findViewById(R.id.camera_fab);
        video_fab = (FloatingActionButton) findViewById(R.id.video_fab);

        //Button listeners//
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

        //Google Maps fragment//
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //GPS manager location//
        defineLocationManager(this);

        //Header dynamic manager//
        getTaskForceData(navigationView);

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    private void getTaskForceData(NavigationView navigationView) {

        View headerLayout = navigationView.getHeaderView(0);

        task_force_name = (TextView) headerLayout.findViewById(R.id.task_force_name);
        task_element_name = (TextView) headerLayout.findViewById(R.id.task_element_name);
        task_force_location = (TextView) headerLayout.findViewById(R.id.task_force_location);
        task_force_latitude = (TextView) headerLayout.findViewById(R.id.task_force_latitude);
        task_force_longitude = (TextView) headerLayout.findViewById(R.id.task_force_longitude);

        task_force_name.setText(SESSION_DATA.getTeamName());
        task_element_name.setText(SESSION_DATA.getActorName().replace("-","\n"));
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

            callWebServiceLocation(Constants.WS_KEY_SEND_LOCATION_HIDDEN);
        } else {
            Toast.makeText(this,getString(R.string.default_gps_error), Toast.LENGTH_LONG).show();
            taskToken.clear();
            finish();

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab:
                callWebServiceLocation(Constants.WS_KEY_SEND_LOCATION);
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

    private void callWebServiceLocation(int type) {
        TasksDecode tasksDecode = new TasksDecode();
        tasksDecode.setTask_team_id(SESSION_DATA.getIdTeam());
        tasksDecode.setTask_longitude(String.valueOf(locationGPS.getLongitude()));
        tasksDecode.setTask_latitude(String.valueOf(locationGPS.getLatitude()));
        tasksDecode.setTask_user_id(SESSION_DATA.getIdUser());

        AsyncCallWS wsLocation = new AsyncCallWS(type,tasksDecode);
        wsLocation.execute();
    }

    //Save media content
    private void mediaContent(String mediaType, int requestType) {
        cameraIntent = new Intent(mediaType);

        if(cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, requestType);
        }
    }


    @Override
    public void taskActions(View v, TaskListAdapter taskListAdapter, Tasks task,TasksDecode tasksDecode) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (v.getId()) {
            case R.id.picture_task_button:
                Intent imgGalleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, Uri.parse(Intent.CATEGORY_OPENABLE));
                imgGalleryIntent.setType("image/*");
                imgGalleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(imgGalleryIntent, GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);

                Toast.makeText(this, getString(R.string.default_file_single_selection)+"\n"
                        + getString(R.string.default_file_multiple_selection)
                        ,Toast.LENGTH_LONG).show();

                ACTUAL_POSITION = tasksDecode.getTask_position();

                break;
            case R.id.video_task_button:
                Intent videoGalleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, Uri.parse(Intent.CATEGORY_OPENABLE));
                videoGalleryIntent.setType("video/*");
                videoGalleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(videoGalleryIntent, GALLERY_VIDEO_ACTIVITY_REQUEST_CODE);

                Toast.makeText(this, getString(R.string.default_file_single_selection)+"\n"
                        + getString(R.string.default_file_multiple_selection)
                        ,Toast.LENGTH_LONG).show();

                ACTUAL_POSITION = tasksDecode.getTask_position();

                break;
            case R.id.agree_task_button:

                tasksDecode.setTask_update_to(Constants.PROGRESS_TASK);
                tasksDecode.setTask_comment(getString(R.string.default_task_agree_msg));
                tasksDecode.setOrigin_button(v.getId());

                AsyncCallWS wsAgree = new AsyncCallWS(Constants.WS_KEY_UPDATE_TASK,task,tasksDecode);
                wsAgree.execute();

                break;
            case R.id.decline_task_button:

                tasksDecode.setOrigin_button(v.getId());
                setToken(v, taskListAdapter, task, tasksDecode);

                closeActiveTaskFragment(v);

                FragmentTransaction declineFragment = fragmentManager.beginTransaction();
                declineFragment.add(R.id.tasks_finish_fragment_container, new FinishTasksFragment(), Constants.FRAGMENT_FINISH_TAG);
                declineFragment.commit();

                break;
            case R.id.finish_task_button:

                tasksDecode.setOrigin_button(v.getId());
                setToken(v,taskListAdapter,task,tasksDecode);

                closeActiveTaskFragment(v);

                FragmentTransaction finishFragment = fragmentManager.beginTransaction();
                finishFragment.add(R.id.tasks_finish_fragment_container, new FinishTasksFragment(), Constants.FRAGMENT_FINISH_TAG);
                finishFragment.commit();

                break;
            case R.id.send_task_button:
                closeActiveTaskFragment(v);

                tasksDecode.setTask_update_to((tasksDecode.getOrigin_button() == R.id.finish_task_button) ? Constants.CLOSE_TASK : Constants.PENDING_TASK);

                AsyncCallWS wsClose = new AsyncCallWS(Constants.WS_KEY_UPDATE_TASK,task,tasksDecode);
                wsClose.execute();
                break;
            default:
                Snackbar.make(v, "Acción no registrada " , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }

        if (taskListAdapter.getItemCount() == 0) {
            closeActiveTaskFragment(v);
            Toast.makeText(this,getString(R.string.default_empty_task_list), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case GALLERY_IMAGE_ACTIVITY_REQUEST_CODE:
                if (requestCode == GALLERY_IMAGE_ACTIVITY_REQUEST_CODE) {
                    if (resultCode == RESULT_OK) {

                        if (data.getClipData() != null) {
                            //select multiple file picture
                            selectMultipleFile(data.getClipData(),requestCode);
                        } else if (data.getData() != null) {
                            //select unique file picture
                            selectUniqueFile(data.getData(), requestCode);
                        }

                    } else if (resultCode == RESULT_CANCELED) {
                        // User cancelled the image capture
                    } else {
                        // Image capture failed, advise user
                    }
                }
                break;
            case GALLERY_VIDEO_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data.getClipData() != null) {
                        //select multiple file picture
                        selectMultipleFile(data.getClipData(), requestCode);
                    } else if (data.getData() != null) {
                        //select unique file picture
                        selectUniqueFile(data.getData(), requestCode);
                    }

                } else if (resultCode == RESULT_CANCELED) {
                    // User cancelled the image capture
                } else {
                    // Image capture failed, advise user
                }
                break;
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // Image captured and saved to fileUri specified in the Intent
                    Toast.makeText(this,getString(R.string.default_save_img_msg), Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED) {
                    // User cancelled the image capture
                } else {
                    // Image capture failed, advise user
                }
                break;
            case CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // Video captured and saved to fileUri specified in the Intent
                    Toast.makeText(this,getString(R.string.default_save_video_msg), Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED) {
                    // User cancelled the video capture
                } else {
                    // Video capture failed, advise user
                }
                break;
        }
    }

    public void selectMultipleFile(ClipData data, int requestCode) {
        List<Uri> files = new ArrayList<>();
        List<Uri> selectFiles = new ArrayList<>();

        ClipData clipData = data;

        for (int i = 0; i < clipData.getItemCount(); i++) {
            Uri path = clipData.getItemAt(i).getUri();
            //File file = new File(path.getPath());
            selectFiles.add(path);
        }

        FilesManager taskFiles = new FilesManager();

        if (TASK_FILE.containsKey(ACTUAL_POSITION)) {

            taskFiles = TASK_FILE.get(ACTUAL_POSITION);
            files = (requestCode == GALLERY_VIDEO_ACTIVITY_REQUEST_CODE) ? taskFiles.getFilesVideo() : taskFiles.getFilesPicture();
        }

        files.addAll(selectFiles);

        switch (requestCode) {
            case GALLERY_IMAGE_ACTIVITY_REQUEST_CODE:
                taskFiles.setFilesPicture(files);
                TextView number_photos = (TextView) findViewById(R.id.number_photos);
                number_photos.setText(String.valueOf(files.size()));
                break;
            case GALLERY_VIDEO_ACTIVITY_REQUEST_CODE:
                taskFiles.setFilesVideo(files);
                TextView number_videos = (TextView) findViewById(R.id.number_videos);
                number_videos.setText(String.valueOf(files.size()));
                break;
        }

        TASK_FILE.put(ACTUAL_POSITION, taskFiles);

    }

    public void selectUniqueFile(Uri data, int requestCode) {
        List<Uri> files = new ArrayList<>();
        FilesManager taskFiles = new FilesManager();

        Uri path = data;
        //File file = new File(path.getPath());

        if (TASK_FILE.containsKey(ACTUAL_POSITION)) {

            taskFiles = TASK_FILE.get(ACTUAL_POSITION);
            files = (requestCode == GALLERY_VIDEO_ACTIVITY_REQUEST_CODE) ? taskFiles.getFilesVideo() : taskFiles.getFilesPicture();
        }

        files.add(path);

        switch (requestCode) {
            case GALLERY_IMAGE_ACTIVITY_REQUEST_CODE:
                taskFiles.setFilesPicture(files);
                TextView number_photos = (TextView) findViewById(R.id.number_photos);
                number_photos.setText(String.valueOf(files.size()));
                break;
            case GALLERY_VIDEO_ACTIVITY_REQUEST_CODE:
                taskFiles.setFilesVideo(files);
                TextView number_videos = (TextView) findViewById(R.id.number_videos);
                number_videos.setText(String.valueOf(files.size()));
                break;
        }

        TASK_FILE.put(ACTUAL_POSITION, taskFiles);
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
            fragmentTransaction.add(R.id.tasks_fragment_container, new NewsTasksFragment(), Constants.FRAGMENT_NEWS_TAG);
            fragmentTransaction.commit();

            taskToken.clear();

        } else if (id == R.id.nav_progress_task) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.tasks_fragment_container, new ProgressTasksFragment(), Constants.FRAGMENT_PROGRESS_TAG);
            fragmentTransaction.commit();

            taskToken.clear();

        } else if (id == R.id.nav_pending_task) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.tasks_fragment_container, new PendingTasksFragment(), Constants.FRAGMENT_PENDING_TAG);
            fragmentTransaction.commit();

            taskToken.clear();

        } else if (id == R.id.nav_revision_task) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.tasks_fragment_container, new RevisionTasksFragment(), Constants.FRAGMENT_REVISION_TAG);
            fragmentTransaction.commit();

            taskToken.clear();

        } else if (id == R.id.nav_close_task) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.tasks_fragment_container, new CloseTasksFragment(), Constants.FRAGMENT_CLOSE_TAG);
            fragmentTransaction.commit();

            taskToken.clear();

        } else if (id == R.id.nav_logout) {

            callWebServiceLocation(Constants.WS_KEY_SEND_LOCATION_HIDDEN);
            taskToken.clear();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void removeAllFragment(FragmentManager fragmentManager) {

        Fragment news = fragmentManager.findFragmentByTag(Constants.FRAGMENT_NEWS_TAG);
        if (null != news) {
            fragmentManager.beginTransaction().remove(news).commit();
        }

        Fragment pending = fragmentManager.findFragmentByTag(Constants.FRAGMENT_PENDING_TAG);
        if (null != pending) {
            fragmentManager.beginTransaction().remove(pending).commit();
        }

        Fragment close = fragmentManager.findFragmentByTag(Constants.FRAGMENT_CLOSE_TAG);
        if (null != close) {
            fragmentManager.beginTransaction().remove(close).commit();
        }

        Fragment progress = fragmentManager.findFragmentByTag(Constants.FRAGMENT_PROGRESS_TAG);
        if (null != progress) {
            fragmentManager.beginTransaction().remove(progress).commit();
        }

        Fragment revision = fragmentManager.findFragmentByTag(Constants.FRAGMENT_REVISION_TAG);
        if (null != revision) {
            fragmentManager.beginTransaction().remove(revision).commit();
        }

        Fragment finish = fragmentManager.findFragmentByTag(Constants.FRAGMENT_FINISH_TAG);
        if (null != finish) {
            fragmentManager.beginTransaction().remove(finish).commit();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng cdMx = new LatLng(Constants.GOOGLE_MAPS_LATITUDE, Constants.GOOGLE_MAPS_LONGITUDE);

        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cdMx, Constants.GOOGLE_MAPS_DEFAULT_CAMERA));

        //Seteamos el tipo de mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        removeAllFragment(fragmentManager);
    }

    @Override
    public void clearTaskToken() {
        taskToken = new HashMap<>();
        TASK_FILE.clear();
    }

    @Override
    public Map<Long,Object> getToken() {
        return taskToken;
    }

    @Override
    public Map<Integer,FilesManager> getTaskFiles() {
        return TASK_FILE;
    }

    public Map<Long,Object> setToken(View v, TaskListAdapter taskListAdapter, Tasks task,TasksDecode tasksDecode) {

        clearTaskToken();

        taskToken.put(Constants.TOKEN_KEY_ACCESS_TASK_VIEW, v);
        taskToken.put(Constants.TOKEN_KEY_ACCESS_TASK_ADAPTER, taskListAdapter);
        taskToken.put(Constants.TOKEN_KEY_ACCESS_TASK_CLASS, task);
        taskToken.put(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE, tasksDecode);

        return taskToken;
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
    public void onLocationChanged(Location location) { getLocation(); }
    @Override public void onStatusChanged(String provider, int status, Bundle extras) { }
    @Override public void onProviderEnabled(String provider) {}
    @Override public void onProviderDisabled(String provider) {}

    //WEB SERVICE CLASS CALL//
    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private SoapPrimitive soapPrimitive;

        private Integer webServiceOperation;
        private Tasks webServiceTask;
        private TasksDecode webServiceTaskDecode;

        private String textError;

        private AsyncCallWS(Integer wsOperation, TasksDecode wsServiceTaskDecode) {
            webServiceOperation = wsOperation;
            webServiceTaskDecode = wsServiceTaskDecode;
            textError = new String();
        }

        private AsyncCallWS(Integer wsOperation,Tasks wsTask,TasksDecode wsServiceTaskDecode) {
            webServiceOperation = wsOperation;
            webServiceTask = wsTask;
            webServiceTaskDecode = wsServiceTaskDecode;
            textError = new String();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            try{
                switch (webServiceOperation) {
                    case Constants.WS_KEY_UPDATE_TASK:
                        soapPrimitive = SoapServices.updateTask(getApplicationContext(),webServiceTask.getTask_id()
                                , webServiceTaskDecode.getTask_comment()
                                , webServiceTaskDecode.getTask_update_to()
                                , webServiceTask.getTask_user_id()
                                ,webServiceTaskDecode.getSendFiles());
                        validOperation = (soapPrimitive != null ) ? true : false;
                        break;
                    case Constants.WS_KEY_UPDATE_TASK_FILE:
                        soapPrimitive = SoapServices.sendFile(getApplicationContext(),webServiceTask.getTask_id()
                                , webServiceTask.getTask_user_id(), webServiceTaskDecode.getSendFiles());
                        validOperation = (soapPrimitive != null ) ? true : false;
                        break;
                    case Constants.WS_KEY_SEND_LOCATION: case Constants.WS_KEY_SEND_LOCATION_HIDDEN:
                        soapPrimitive = SoapServices.updateLocation(getApplicationContext(),webServiceTaskDecode.getTask_team_id()
                                , webServiceTaskDecode.getTask_latitude()
                                , webServiceTaskDecode.getTask_longitude()
                                , webServiceTaskDecode.getTask_user_id());
                        validOperation = (soapPrimitive != null ) ? true : false;
                        break;
                    default:

                        break;
                }
            } catch (Exception e) {
                textError = e.getMessage();
                validOperation = false;
            }

            return validOperation;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if(success) {

                FragmentManager fragmentManager = getSupportFragmentManager();

                switch (webServiceOperation) {

                    case Constants.WS_KEY_UPDATE_TASK:
                        removeAllFragment(fragmentManager);

                        switch (webServiceTask.getTask_status().intValue()) {

                            case Constants.NEWS_TASK:

                                FragmentTransaction ftNews = fragmentManager.beginTransaction();
                                ftNews.add(R.id.tasks_fragment_container, new NewsTasksFragment(), Constants.FRAGMENT_NEWS_TAG);
                                ftNews.commit();
                                break;
                            case Constants.PROGRESS_TASK:
                                FragmentTransaction ftProgress = fragmentManager.beginTransaction();
                                ftProgress.add(R.id.tasks_fragment_container, new ProgressTasksFragment(), Constants.FRAGMENT_PROGRESS_TAG);
                                ftProgress.commit();
                                break;
                            case Constants.PENDING_TASK:
                                FragmentTransaction ftPending = fragmentManager.beginTransaction();
                                ftPending.add(R.id.tasks_fragment_container, new PendingTasksFragment(), Constants.FRAGMENT_PENDING_TAG);
                                ftPending.commit();
                                break;
                            case Constants.CLOSE_TASK:
                                FragmentTransaction ftClose = fragmentManager.beginTransaction();
                                ftClose.add(R.id.tasks_fragment_container, new ProgressTasksFragment(), Constants.FRAGMENT_PROGRESS_TAG);
                                ftClose.commit();
                                break;
                        }
                        taskToken.clear();
                        Toast.makeText(NavigationDrawerActivity.this, soapPrimitive.toString(), Toast.LENGTH_LONG).show();
                        break;
                    case Constants.WS_KEY_SEND_LOCATION:
                        onMapReady(mMap);
                        Toast.makeText(NavigationDrawerActivity.this, soapPrimitive.toString(), Toast.LENGTH_LONG).show();
                        break;
                    case Constants.WS_KEY_UPDATE_TASK_FILE:

                        removeAllFragment(fragmentManager);

                        FragmentTransaction ftProgress = fragmentManager.beginTransaction();
                        ftProgress.add(R.id.tasks_fragment_container, new ProgressTasksFragment(), Constants.FRAGMENT_PROGRESS_TAG);
                        ftProgress.commit();

                        taskToken.clear();
                        Toast.makeText(NavigationDrawerActivity.this, soapPrimitive.toString(), Toast.LENGTH_LONG).show();
                        break;
                }
            } else {
                String tempText = (textError.isEmpty() ? getString(R.string.default_empty_task_list) : textError);
                Toast.makeText(getBaseContext(), tempText, Toast.LENGTH_LONG).show();
            }
        }
    }
}
