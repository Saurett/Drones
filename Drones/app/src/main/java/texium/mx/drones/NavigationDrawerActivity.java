package texium.mx.drones;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import texium.mx.drones.adapters.TaskListAdapter;
import texium.mx.drones.databases.BDTasksManagerQuery;
import texium.mx.drones.exceptions.VideoSyncSoapException;
import texium.mx.drones.fragments.CloseTasksFragment;
import texium.mx.drones.fragments.FinishTasksFragment;
import texium.mx.drones.fragments.LegalDescriptionFragment;
import texium.mx.drones.fragments.LegalFragment;
import texium.mx.drones.fragments.NewsTasksFragment;
import texium.mx.drones.fragments.PendingTasksFragment;
import texium.mx.drones.fragments.ProgressTasksFragment;
import texium.mx.drones.fragments.RestoreFragment;
import texium.mx.drones.fragments.RevisionTasksFragment;
import texium.mx.drones.fragments.inetrface.FragmentTaskListener;
import texium.mx.drones.models.FilesManager;
import texium.mx.drones.models.LegalManager;
import texium.mx.drones.models.MemberLocation;
import texium.mx.drones.models.SyncTaskServer;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.TasksDecode;
import texium.mx.drones.models.Users;
import texium.mx.drones.services.FileServices;
import texium.mx.drones.services.FileSoapServices;
import texium.mx.drones.services.NotificationService;
import texium.mx.drones.services.SharedPreferencesService;
import texium.mx.drones.services.SoapServices;
import texium.mx.drones.utils.Constants;
import texium.mx.drones.utils.DateTimeUtils;


public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        View.OnClickListener, FragmentTaskListener, DialogInterface.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final String TAG = this.getClass().getName();

    //Principal Buttons//
    private FloatingActionButton fab, direction_fab, camera_fab, video_fab, map_fab;

    //Dynamic Header//
    private TextView task_force_name, task_element_name, task_force_location, task_force_latitude, task_force_longitude;

    //Camera manager//
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 2;
    private static final int GALLERY_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int GALLERY_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static Intent cameraIntent;

    //Sessions control//
    private static Users SESSION_DATA;
    private static String ACTUAL_FRAGMENT;
    private static Tasks ACTUAL_TASK_NOTIFICATION;

    //Collections Controls//
    public Map<Long, Object> taskToken = new HashMap<>();
    public static Map<Integer, FilesManager> TASK_FILE = new HashMap<>();
    public static Integer ACTUAL_POSITION;

    private ProgressDialog pDialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private Handler handler;
    private Runnable runnable;
    private Boolean enviarUbicacion;
    private Integer legalRequired;

    /**
     * Servicios de ubicaciones
     **/
    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;
    private GoogleMap mapa;
    private GoogleApiClient apiClient;
    public LocationRequest locRequest;
    private Intent locationServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        locationServiceIntent = new Intent(this, LocationService.class);
        handler = new Handler();

        try {
            SESSION_DATA = (Users) getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_LOGIN);
        } catch (Exception e) {
            e.printStackTrace();
            getIntent().putExtra(Constants.ACTIVITY_EXTRA_PARAMS_LOGIN, SESSION_DATA);
        }

        try {
            ACTUAL_FRAGMENT = (String) getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_ACTUAL_FRAGMENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ACTUAL_TASK_NOTIFICATION = (Tasks) getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_ACTUAL_TASK_NOTIFICATION);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        setTitle(""); //Set activity tittle

        TasksDecode td = new TasksDecode(SESSION_DATA.getIdUser(), R.id.drawer_layout);
        AsyncCallWS wsServerSync = new AsyncCallWS(Constants.WS_KEY_SERVER_SYNC, td);
        wsServerSync.execute();

        //Principal floatingButtons//
        fab = (FloatingActionButton) findViewById(R.id.fab);
        direction_fab = (FloatingActionButton) findViewById(R.id.direction_fab);
        camera_fab = (FloatingActionButton) findViewById(R.id.camera_fab);
        video_fab = (FloatingActionButton) findViewById(R.id.video_fab);
        map_fab = (FloatingActionButton) findViewById(R.id.map_fab);


        //Button listeners//
        fab.setOnClickListener(this);
        direction_fab.setOnClickListener(this);
        camera_fab.setOnClickListener(this);
        video_fab.setOnClickListener(this);
        map_fab.setOnClickListener(this);

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

        //Header dynamic manager//
        getTaskForceData(navigationView);

        TasksDecode tasksDecode = new TasksDecode();
        tasksDecode.setTask_status(Constants.ALL_TASK);

        AsyncCallWS wsAllTask = new AsyncCallWS(Constants.WS_KEY_ALL_TASKS, tasksDecode);
        wsAllTask.execute();

        showFragment(getActualFragment());
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void getTaskForceData(NavigationView navigationView) {

        View headerLayout = navigationView.getHeaderView(0);

        task_force_name = (TextView) headerLayout.findViewById(R.id.task_force_name);
        task_element_name = (TextView) headerLayout.findViewById(R.id.task_element_name);
        task_force_location = (TextView) headerLayout.findViewById(R.id.task_force_location);
        task_force_latitude = (TextView) headerLayout.findViewById(R.id.task_force_latitude);
        task_force_longitude = (TextView) headerLayout.findViewById(R.id.task_force_longitude);

        task_force_name.setText(SESSION_DATA.getActorName().replace("-", "\n"));
        task_element_name.setText(SESSION_DATA.getActorName().replace("-", "\n"));
        task_force_location.setText("CIUDAD DE MÉXICO");

        this.enableLocationUpdates();
        this.enableLocationBackground();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab:
                callWebServiceLocation(Constants.WS_KEY_SEND_LOCATION);
                break;

            case R.id.map_fab:

                mapa.setMapType((mapa.getMapType() == GoogleMap.MAP_TYPE_HYBRID) ? GoogleMap.MAP_TYPE_TERRAIN : GoogleMap.MAP_TYPE_HYBRID);

                break;
            case R.id.direction_fab:

                // Directions
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(
                        "http://maps.google.com/maps?saddr=51.5, 0.125&daddr=51.5, 0.15"));
                startActivity(intent);

                break;
            case R.id.camera_fab:
                mediaContent(MediaStore.ACTION_IMAGE_CAPTURE, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.video_fab:
                /**Abre el app d comprension**/
                try {
                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.czh.testmpeg");
                    startActivity(LaunchIntent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Es necesario instalar la aplicación para gabrar y comprimir video", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(Constants.VIDEO_COMPRESOR_APP_URL));
                    startActivity(i);
                }
                //mediaContent(MediaStore.ACTION_VIDEO_CAPTURE, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
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

        if (requestType == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            //cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            cameraIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, Constants.VIDEO_SIZE);
        }

        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, requestType);
        }
    }

    public void showQuestion(Integer id) {

        AlertDialog.Builder ad = new AlertDialog.Builder(this);

        switch (id) {
            case R.id.file_number:

                ad.setTitle(getString(R.string.default_title_alert_dialog));
                ad.setMessage("Es requerido capturar el número de expediente ...");
                ad.setCancelable(false);
                ad.setNeutralButton(getString(R.string.default_positive_button), this);

                LegalFragment.setRequited();

                break;
            case R.id.cause_description:

                ad.setTitle(getString(R.string.default_title_alert_dialog));
                ad.setMessage("Es requerido capturar las causas ...");
                ad.setCancelable(false);
                ad.setNeutralButton(getString(R.string.default_positive_button), this);

                LegalDescriptionFragment.setRequited();

                break;
            case Constants.MESSAGE_ALERT_NO_FRAGMENT:

                Tasks task = ACTUAL_TASK_NOTIFICATION;

                String status = (task.getTask_status().equals(Constants.REVISION_TASK)) ? "Finalizada" : "Cancelada";

                ad.setTitle(getString(R.string.default_title_alert_dialog));
                ad.setMessage("La tarea ha sido " + status + ", gracias por su aportación.");
                ad.setNeutralButton(getString(R.string.default_positive_button), this);

                break;
            default:

                ad.setTitle(getString(R.string.default_title_alert_dialog));
                ad.setMessage(getString(R.string.default_no_action));
                ad.setCancelable(false);
                ad.setNeutralButton(getString(R.string.default_positive_button), this);

                break;
        }


        ad.show();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void taskActions(View v, TaskListAdapter taskListAdapter, Tasks task, TasksDecode tasksDecode) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (v.getId()) {
            /*
            case R.id.picture_task_button:
                Intent imgGalleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, Uri.parse(Intent.CATEGORY_OPENABLE));
                imgGalleryIntent.setType("image/*");
                imgGalleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(imgGalleryIntent, GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);

                Toast.makeText(this, getString(R.string.default_file_single_selection) + "\n"
                                + getString(R.string.default_file_multiple_selection)
                        , Toast.LENGTH_LONG).show();

                ACTUAL_POSITION = tasksDecode.getTask_position();

                break;
            case R.id.video_task_button:
                Intent videoGalleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, Uri.parse(Intent.CATEGORY_OPENABLE));
                videoGalleryIntent.setType("video/*");
                videoGalleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(videoGalleryIntent, GALLERY_VIDEO_ACTIVITY_REQUEST_CODE);

                Toast.makeText(this, getString(R.string.default_file_single_selection) + "\n"
                                + getString(R.string.default_file_multiple_selection)
                        , Toast.LENGTH_LONG).show();

                ACTUAL_POSITION = tasksDecode.getTask_position();

                break;
                */
            case R.id.agree_task_button:

                if (task.getTask_user_id().equals(SESSION_DATA.getIdUser())) {

                    tasksDecode.setTask_update_to(Constants.PROGRESS_TASK);
                    tasksDecode.setTask_comment(getString(R.string.default_task_agree_msg));
                    tasksDecode.setOrigin_button(v.getId());
                    tasksDecode.setTask_user_id(SESSION_DATA.getIdUser());

                    AsyncCallWS wsAgree = new AsyncCallWS(Constants.WS_KEY_UPDATE_TASK, task, tasksDecode);
                    wsAgree.execute();

                } else {
                    showQuestion(0);
                }

                break;
            case R.id.decline_task_button:

                if (task.getTask_user_id().equals(SESSION_DATA.getIdUser())) {

                    tasksDecode.setOrigin_button(v.getId());
                    tasksDecode.setTask_user_id(SESSION_DATA.getIdUser());
                    setToken(v, taskListAdapter, task, tasksDecode, null);

                    closeActiveTaskFragment(v);

                    FragmentTransaction declineFragment = fragmentManager.beginTransaction();
                    declineFragment.add(R.id.tasks_finish_fragment_container, new FinishTasksFragment(), Constants.FRAGMENT_FINISH_TAG);
                    declineFragment.commit();

                } else {
                    showQuestion(0);
                }


                break;
            case R.id.finish_task_button:

                if (task.getTask_user_id().equals(SESSION_DATA.getIdUser())) {

                    tasksDecode.setOrigin_button(v.getId());
                    tasksDecode.setTask_user_id(SESSION_DATA.getIdUser());
                    setToken(v, taskListAdapter, task, tasksDecode, null);

                    closeActiveTaskFragment(v);

                    FragmentTransaction finishFragment = fragmentManager.beginTransaction();
                    finishFragment.add(R.id.tasks_finish_fragment_container, new FinishTasksFragment(), Constants.FRAGMENT_FINISH_TAG);
                    finishFragment.commit();

                } else {

                    showQuestion(0);

                }


                break;
            case R.id.send_task_button:

                if (task.getTask_user_id().equals(SESSION_DATA.getIdUser())) {

                    Integer status = (tasksDecode.getOrigin_button() == R.id.finish_task_button) ? Constants.CLOSE_TASK : Constants.PENDING_TASK;

                    tasksDecode.setTask_update_to(status);
                    tasksDecode.setTask_user_id(SESSION_DATA.getIdUser());


                    if (status.equals(Constants.CLOSE_TASK)) {

                        tasksDecode.setLegalInformation(getLegalInformation());

                        if (legalRequired != null) {
                            showQuestion(legalRequired);
                            return;
                        }

                    }

                    closeActiveTaskFragment(v);

                    AsyncCallWS wsClose = new AsyncCallWS(Constants.WS_KEY_UPDATE_TASK, task, tasksDecode);
                    wsClose.execute();


                } else {
                    showQuestion(0);
                }


                break;
            case R.id.gallery_task_button:
            case R.id.gallery_task_gallery:

                setActualFragment(fragmentManager);

                Intent intentAG = new Intent(NavigationDrawerActivity.this, AllGalleryActivity.class);
                intentAG.putExtra(Constants.ACTIVITY_EXTRA_PARAMS_TASK_GALLERY, task);
                intentAG.putExtra(Constants.ACTIVITY_EXTRA_PARAMS_LOGIN, SESSION_DATA);
                startActivity(intentAG);

                break;
            case R.id.task_location_button:


                LatLng taskLatLng = new LatLng(task.getTask_latitude(), task.getTask_longitude());

                MarkerOptions mo = new MarkerOptions();
                mo.position(taskLatLng);
                mo.title(task.getTask_tittle());
                mo.snippet(Constants.MAP_STATUS_NAME.get(task.getTask_priority()));
                //mo.icon(BitmapDescriptorFactory.defaultMarker(Constants.MAP_STATUS_COLOR.get(actualTask.getTask_priority())));
                mo.icon(BitmapDescriptorFactory.fromResource(Constants.MAP_STATUS_ICON.get(task.getTask_priority())));

                Marker marker = mapa.addMarker(mo);
                marker.showInfoWindow();

                LatLng cdMx = new LatLng(task.getTask_latitude(), task.getTask_longitude() - 0.002);
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(cdMx, 17));

                CameraPosition cameraPosition1 = new CameraPosition.Builder()
                        .target(cdMx)
                        .tilt(9)
                        .zoom(17)
                        .build();

                mapa.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition1));

                break;
            default:
                Snackbar.make(v, "Acción no registrada ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }

        if (taskListAdapter.getItemCount() == 0) {
            closeActiveTaskFragment(v);
            Toast.makeText(this, getString(R.string.default_empty_task_list), Toast.LENGTH_SHORT).show();
        }
    }

    private LegalManager getLegalInformation() {

        legalRequired = null;

        LegalManager legalManager = new LegalManager();

        legalManager = FinishTasksFragment.getLegalInformation(legalManager);

        if (legalManager.getCauses().equals(Constants.ACTIVE)) {
            legalManager = LegalDescriptionFragment.getLegalInformation(legalManager);

            if (legalManager.getDescriptionCauses().isEmpty()) {
                legalRequired = R.id.cause_description;
            }


        } else {
            legalManager = LegalFragment.getLegalInformation(legalManager);

            if (legalManager.getFileNumber().isEmpty()) {
                legalRequired = R.id.file_number;
            }
        }

        return legalManager;
    }


    private void setActualFragment(FragmentManager fragmentManager) {
        //TODO EN REVISION
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments.size() > 0) {
            ACTUAL_FRAGMENT = fragments.get(0).getTag();
        }
    }

    private String getActualFragment() {
        return ACTUAL_FRAGMENT;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PETICION_CONFIG_UBICACION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        Toast.makeText(getApplicationContext(), "GPS ACTIVADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        /**Realizar la petición de ubicacion nuevamente*/
                        enableLocationUpdates();
                        break;
                }
                break;
            case GALLERY_IMAGE_ACTIVITY_REQUEST_CODE:
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
                    Toast.makeText(this, getString(R.string.default_save_img_msg), Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED) {
                    // User cancelled the image capture
                } else {
                    // Image capture failed, advise user
                }
                break;
            case CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // Video captured and saved to fileUri specified in the Intent
                    Toast.makeText(this, getString(R.string.default_save_video_msg), Toast.LENGTH_LONG).show();
                } else if (resultCode != RESULT_CANCELED) {
                    // Video capture failed, advise user
                } else {
                    // User cancelled the video capture
                }
                break;
        }
    }

    public void selectMultipleFile(ClipData data, int requestCode) {
        List<Uri> files = new ArrayList<>();
        List<Uri> selectFiles = new ArrayList<>();

        for (int i = 0; i < data.getItemCount(); i++) {
            Uri path = data.getItemAt(i).getUri();
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
                //TextView number_photos = (TextView) findViewById(R.id.number_photos);
                //number_photos.setText(String.valueOf(files.size()));
                break;
            case GALLERY_VIDEO_ACTIVITY_REQUEST_CODE:
                taskFiles.setFilesVideo(files);
                //TextView number_videos = (TextView) findViewById(R.id.number_videos);
                //number_videos.setText(String.valueOf(files.size()));
                break;
        }

        TASK_FILE.put(ACTUAL_POSITION, taskFiles);

    }

    public void selectUniqueFile(Uri data, int requestCode) {
        List<Uri> files = new ArrayList<>();
        FilesManager taskFiles = new FilesManager();

        if (TASK_FILE.containsKey(ACTUAL_POSITION)) {

            taskFiles = TASK_FILE.get(ACTUAL_POSITION);
            files = (requestCode == GALLERY_VIDEO_ACTIVITY_REQUEST_CODE) ? taskFiles.getFilesVideo() : taskFiles.getFilesPicture();
        }

        files.add(data);

        switch (requestCode) {
            case GALLERY_IMAGE_ACTIVITY_REQUEST_CODE:
                taskFiles.setFilesPicture(files);
                //TextView number_photos = (TextView) findViewById(R.id.number_photos);
                //number_photos.setText(String.valueOf(files.size()));
                break;
            case GALLERY_VIDEO_ACTIVITY_REQUEST_CODE:
                taskFiles.setFilesVideo(files);
                //TextView number_videos = (TextView) findViewById(R.id.number_videos);
                //number_videos.setText(String.valueOf(files.size()));
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

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

        } else if (id == R.id.restore_device) {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.tasks_fragment_container, new RestoreFragment(), Constants.FRAGMENT_RESTORE_TAG);
            fragmentTransaction.commit();

            taskToken.clear();
        } else if (id == R.id.nav_sync) {

            TasksDecode td = new TasksDecode(SESSION_DATA.getIdUser(), id);
            AsyncCallWS wsServerSync = new AsyncCallWS(Constants.WS_KEY_SERVER_SYNC, td);
            wsServerSync.execute();

        } else if (id == R.id.nav_logout) {
            callWebServiceLocation(Constants.WS_KEY_SEND_LOCATION_HIDDEN_LOGOUT);
        }
        return true;
    }

    private void showFragment(String fragmentTag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragmentTag != null) {
            switch (fragmentTag) {
                case Constants.FRAGMENT_NEWS_TAG:

                    fragmentTransaction.add(R.id.tasks_fragment_container, new NewsTasksFragment(), Constants.FRAGMENT_NEWS_TAG);
                    fragmentTransaction.commit();

                    taskToken.clear();

                    break;
                case Constants.FRAGMENT_PROGRESS_TAG:

                    fragmentTransaction.add(R.id.tasks_fragment_container, new ProgressTasksFragment(), Constants.FRAGMENT_PROGRESS_TAG);
                    fragmentTransaction.commit();

                    taskToken.clear();

                    break;
                case Constants.FRAGMENT_PENDING_TAG:

                    fragmentTransaction.add(R.id.tasks_fragment_container, new PendingTasksFragment(), Constants.FRAGMENT_PENDING_TAG);
                    fragmentTransaction.commit();

                    taskToken.clear();

                    break;

                case Constants.FRAGMENT_REVISION_TAG:

                    fragmentTransaction.add(R.id.tasks_fragment_container, new RevisionTasksFragment(), Constants.FRAGMENT_REVISION_TAG);
                    fragmentTransaction.commit();

                    taskToken.clear();

                    break;

                case Constants.FRAGMENT_CLOSE_TAG:

                    fragmentTransaction.add(R.id.tasks_fragment_container, new CloseTasksFragment(), Constants.FRAGMENT_CLOSE_TAG);
                    fragmentTransaction.commit();

                    taskToken.clear();

                    break;
                default:

                    showQuestion(-1);

                    break;
            }
        }

        ACTUAL_FRAGMENT = null;
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

        //ONLY RESTORE DEVICE
        Fragment restore = fragmentManager.findFragmentByTag(Constants.FRAGMENT_RESTORE_TAG);
        if (null != restore) {
            fragmentManager.beginTransaction().remove(restore).commit();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {

            LatLng cdMx = new LatLng(Constants.GOOGLE_MAPS_LATITUDE, Constants.GOOGLE_MAPS_LONGITUDE);

            mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(cdMx, Constants.GOOGLE_MAPS_DEFAULT_CAMERA));
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setZoomControlsEnabled(true);
            mapa.getUiSettings().setCompassEnabled(true);
        }
    }

    @Override
    public void addTasksListMarkers(List<Tasks> tasksList) {
        if (mapa != null) {

            mapa.clear();

            for (Tasks actualTask : tasksList) {

                LatLng taskLatLng = new LatLng(actualTask.getTask_latitude(), actualTask.getTask_longitude());

                MarkerOptions mo = new MarkerOptions();
                mo.position(taskLatLng);
                mo.title(actualTask.getTask_tittle());
                mo.snippet(Constants.MAP_STATUS_NAME.get(actualTask.getTask_priority()));
                //mo.icon(BitmapDescriptorFactory.defaultMarker(Constants.MAP_STATUS_COLOR.get(actualTask.getTask_priority())));
                mo.icon(BitmapDescriptorFactory.fromResource(Constants.MAP_STATUS_ICON.get(actualTask.getTask_priority())));
                mapa.addMarker(mo);

                mapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        LatLng latLng = marker.getPosition();
                        LatLng cdMx = new LatLng(latLng.latitude, latLng.longitude - 0.002);
                        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(cdMx, 17));

                        //direction_fab.setVisibility(View.VISIBLE);

                        marker.showInfoWindow();
                        return true;
                    }
                });
            }

            mapa.getUiSettings().setMapToolbarEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
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
        if (id == R.id.action_server_sync) {
            TasksDecode td = new TasksDecode(SESSION_DATA.getIdUser(), id);
            AsyncCallWS wsServerSync = new AsyncCallWS(Constants.WS_KEY_SERVER_SYNC, td);
            wsServerSync.execute();
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
    public Map<Long, Object> getToken() {
        return taskToken;
    }

    @Override
    public Map<Integer, FilesManager> getTaskFiles() {
        return TASK_FILE;
    }


    public Map<Long, Object> setToken(View v, TaskListAdapter taskListAdapter, Tasks task
            , TasksDecode tasksDecode, List<FilesManager> filesManager) {

        clearTaskToken();

        taskToken.put(Constants.TOKEN_KEY_ACCESS_TASK_VIEW, v);
        taskToken.put(Constants.TOKEN_KEY_ACCESS_TASK_ADAPTER, taskListAdapter);
        taskToken.put(Constants.TOKEN_KEY_ACCESS_TASK_CLASS, task);
        taskToken.put(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE, tasksDecode);
        taskToken.put(Constants.TOKEN_KEY_ACCESS_FILE_MANAGER_CLASS, filesManager);

        return taskToken;
    }

    @Override
    public void onLocationChanged(Location location) {
        updateUI(location);
    }


    private LegalManager getLegalInformation(LegalManager legalManager) {

        LegalManager legalInformation = new LegalManager();

        if (legalManager.getCauses().equals(Constants.ACTIVE)) {
            legalInformation.setDescriptionCauses(legalManager.getDescriptionCauses());
        } else {

            if (!legalManager.getFileNumber().isEmpty()) {

                legalInformation.setFileNumber(legalManager.getFileNumber());

                if (legalManager.getClosure().equals(Constants.ACTIVE)) {
                    legalInformation.setLegalClosure(legalManager.getClosureTotal().equals(Constants.ACTIVE) ? "Total" : "Parcial");
                }
            }
        }

        return legalInformation;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {
            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            updateUI(lastLocation);
        }
    }

    private void updateUI(Location location) {

        if (location != null) {
            /**Actualiza la ubicacion en la memoria**/
            SharedPreferencesService.saveSessionPreferences(getApplicationContext(), location);
            /**Actualiza la ubicacion en el menu**/
            setUILocation(location);
            /**Actualiza la ubicacion en el servidor**/
            sendLocationWS();
            Log.d(TAG, "Location > Ubicacion nueva: " + location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    //WEB SERVICE CLASS CALL//
    private class AsyncCallWS extends AsyncTask<Void, String, Boolean> {

        private SoapPrimitive soapPrimitive;
        private SoapObject soapObject;

        private Integer webServiceOperation;
        private Tasks webServiceTask;
        private TasksDecode webServiceTaskDecode;
        private FilesManager webServiceFilesManager;

        private String textError;

        private AsyncCallWS(Integer wsOperation, TasksDecode wsServiceTaskDecode) {
            webServiceOperation = wsOperation;
            webServiceTaskDecode = wsServiceTaskDecode;
            textError = "";
        }

        private AsyncCallWS(Integer wsOperation, FilesManager wsFm, Tasks wsTask, TasksDecode wsServiceTaskDecode) {
            webServiceOperation = wsOperation;
            webServiceFilesManager = wsFm;
            webServiceTask = wsTask;
            webServiceTaskDecode = wsServiceTaskDecode;
            textError = "";
        }

        private AsyncCallWS(Integer wsOperation, Tasks wsTask, TasksDecode wsServiceTaskDecode) {
            webServiceOperation = wsOperation;
            webServiceTask = wsTask;
            webServiceTaskDecode = wsServiceTaskDecode;
            textError = "";
        }

        @Override
        protected void onPreExecute() {
            switch (webServiceOperation) {
                case Constants.WS_KEY_UPDATE_TASK:
                case Constants.WS_KEY_UPDATE_TASK_FILE:
                case Constants.WS_KEY_SERVER_SYNC:

                    if ((webServiceTaskDecode.getOrigin_button() == R.id.finish_task_button)
                            || (webServiceTaskDecode.getOrigin_button() == R.id.decline_task_button)
                            || (webServiceTaskDecode.getOrigin_button() == R.id.agree_task_button)) {
                        pDialog = new ProgressDialog(NavigationDrawerActivity.this);
                        pDialog.setTitle("Actualizando");
                        //pDialog.setProgressStyle(pDialog.STYLE_HORIZONTAL);
                        pDialog.setMessage(getString(R.string.default_update_task));
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(false);
                        pDialog.show();
                    } else if ((webServiceTaskDecode.getOrigin_button() == R.id.action_server_sync)
                            || (webServiceTaskDecode.getOrigin_button() == R.id.drawer_layout)
                            || (webServiceTaskDecode.getOrigin_button() == R.id.nav_sync)) {
                        pDialog = new ProgressDialog(NavigationDrawerActivity.this);
                        pDialog.setMessage(getString(R.string.server_sync));
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            try {
                NotificationService.callNotification(NavigationDrawerActivity.this, SESSION_DATA.getIdUser());
                switch (webServiceOperation) {
                    case Constants.WS_KEY_UPDATE_TASK:

                        List<SyncTaskServer> NoSyncTasksActual = BDTasksManagerQuery.getAllSyncTaskServer(
                                getApplicationContext()
                                , webServiceTaskDecode.getTask_user_id()
                                , Constants.SERVER_SYNC_FALSE);

                        if (NoSyncTasksActual.size() > 0) validOperation = true;

                        for (SyncTaskServer syncTaskServer : NoSyncTasksActual) {

                            soapPrimitive = SoapServices.updateTask(getApplicationContext()
                                    , syncTaskServer.getTask_id()
                                    , syncTaskServer.getTask_comment()
                                    , syncTaskServer.getTask_status()
                                    , syncTaskServer.getTask_user_id()
                                    , syncTaskServer.getSendPictureFiles()
                                    , syncTaskServer.getLegal_causes()
                                    , syncTaskServer.getLegal_file_number()
                                    , syncTaskServer.getLegal_closures());

                            //validOperation = (soapPrimitive != null);

                            BDTasksManagerQuery.updateTaskDetail(getApplicationContext()
                                    , syncTaskServer.getTask_detail_cve()
                                    , Constants.SERVER_SYNC_TRUE);

                            //UPDATE ALL PICTURE
                            FileSoapServices.syncAllFilesWithDetail(getApplicationContext(),
                                    syncTaskServer.getTask_id(), syncTaskServer.getTask_user_id(),
                                    syncTaskServer.getTask_detail_cve());

                            Log.i("Sync Task", "task_id " + syncTaskServer.getTask_id()
                                    + " comment " + syncTaskServer.getTask_comment());
                        }

                        LegalManager legalInformation = getLegalInformation(webServiceTaskDecode.getLegalInformation());

                        soapPrimitive = SoapServices.updateTask(getApplicationContext(), webServiceTask.getTask_id()
                                , webServiceTaskDecode.getTask_comment()
                                , webServiceTaskDecode.getTask_update_to()
                                , webServiceTaskDecode.getTask_user_id()
                                , webServiceTaskDecode.getSendImgFiles()
                                , legalInformation.getDescriptionCauses()
                                , legalInformation.getFileNumber()
                                , legalInformation.getLegalClosure());
                        validOperation = (soapPrimitive != null);

                        List<FilesManager> filesManager = webServiceTaskDecode.getSendVideoFiles();

                        int videoNumber = 1;

                        for (FilesManager fm : filesManager) {

                            if (fm.getEncodeSingleFile().length() > 0) {

                                List<String> fmPack = FileServices.getPackageList(getApplicationContext()
                                        , fm.getEncodeSingleFile());

                                int packNumber = 1;

                                Log.i("Send TO Main Server", "Video file " + videoNumber + " to " + filesManager.size());

                                for (String pack : fmPack) {

                                    String title = "Transfiriendo al servidor video " + videoNumber + " de " + filesManager.size();
                                    String msg = "Subiendo paquete " + packNumber + " de " + fmPack.size();

                                    publishProgress(title, msg, String.valueOf(packNumber), String.valueOf(fmPack.size()));
                                    //pDialog.incrementProgressBy(1); //if needs see progress

                                    try {
                                        SoapServices.updateVideoFiles(getApplicationContext()
                                                , webServiceTask.getTask_id()
                                                , webServiceTaskDecode.getTask_user_id()
                                                , pack, packNumber, (packNumber == fmPack.size()), "");

                                        Log.i("Send TO Main Server", "Pack item " + packNumber + " to " + fmPack.size());

                                        packNumber++;
                                    } catch (Exception e) {
                                        throw new VideoSyncSoapException("Error al intetar enviar los videos", e);
                                    }
                                }
                            }
                            videoNumber++;
                        }

                        break;
                    case Constants.WS_KEY_UPDATE_TASK_FILE:
                        soapPrimitive = SoapServices.sendFile(getApplicationContext(), webServiceTask.getTask_id()
                                , webServiceTask.getTask_user_id(), webServiceTaskDecode.getSendImgFiles());

                        FileSoapServices.syncAllFiles(getApplicationContext(), webServiceTask.getTask_id(), webServiceTask.getTask_user_id());

                        validOperation = (soapPrimitive != null);
                        break;
                    case Constants.WS_KEY_SEND_LOCATION:
                    case Constants.WS_KEY_SEND_LOCATION_HIDDEN:
                    case Constants.WS_KEY_SEND_LOCATION_HIDDEN_LOGOUT:

                        MemberLocation memberLocation = new MemberLocation(
                                SESSION_DATA.getIdUser(),
                                Constants.SERVER_SYNC_FALSE);

                        List<MemberLocation> allLocations = BDTasksManagerQuery.getMemberLocations(
                                getApplicationContext(), memberLocation);

                        for (MemberLocation tempML : allLocations) {

                            tempML.setServerSync(Constants.SERVER_SYNC_TRUE);

                            soapPrimitive = SoapServices.updateLocation(getApplicationContext(), tempML);

                            if (soapPrimitive != null) {
                                if (soapPrimitive.toString().contains("BD")) {
                                    BDTasksManagerQuery.updateTaskDetail(getApplicationContext(), tempML);
                                }
                            }

                        }

                        /**Obtiene la ultima ubicacion registrada**/
                        TasksDecode lastLocation = SharedPreferencesService.getLocalizacion(getApplicationContext());

                        memberLocation.setServerSync((webServiceOperation.equals(Constants.WS_KEY_SEND_LOCATION_HIDDEN_LOGOUT)) ? Constants.SERVER_SYNC_FALSE : Constants.SERVER_SYNC_TRUE);
                        memberLocation.setLatitude(Double.valueOf(lastLocation.getTask_latitude()));
                        memberLocation.setLongitude(Double.valueOf(lastLocation.getTask_longitude()));
                        memberLocation.setSyncTime(DateTimeUtils.getActualTime());

                        soapPrimitive = SoapServices.updateLocation(getApplicationContext(), memberLocation);

                        validOperation = (soapPrimitive != null);
                        break;
                    case Constants.WS_KEY_ALL_TASKS:
                        soapObject = SoapServices.getServerAllTasks(getApplicationContext()
                                , SESSION_DATA.getIdUser()
                                , webServiceTaskDecode.getTask_status());
                        validOperation = (soapObject.getPropertyCount() > 0);
                        break;
                    case Constants.WS_KEY_SERVER_SYNC:
                        List<SyncTaskServer> NoSyncTasks = BDTasksManagerQuery.getAllSyncTaskServer(
                                getApplicationContext()
                                , webServiceTaskDecode.getTask_user_id()
                                , Constants.SERVER_SYNC_FALSE);

                        if (NoSyncTasks.size() > 0) validOperation = true;

                        Integer numSync = 0;
                        for (SyncTaskServer syncTaskServer : NoSyncTasks) {

                            soapPrimitive = SoapServices.updateTask(getApplicationContext()
                                    , syncTaskServer.getTask_id()
                                    , syncTaskServer.getTask_comment()
                                    , syncTaskServer.getTask_status()
                                    , syncTaskServer.getTask_user_id()
                                    , syncTaskServer.getSendPictureFiles()
                                    , ""
                                    , ""
                                    , "");

                            validOperation = (soapPrimitive != null);

                            numSync = (validOperation) ? numSync + 1 : numSync;

                            BDTasksManagerQuery.updateTaskDetail(getApplicationContext()
                                    , syncTaskServer.getTask_detail_cve()
                                    , Constants.SERVER_SYNC_TRUE);

                            FileSoapServices.syncAllFilesWithDetail(getApplicationContext(), syncTaskServer.getTask_id(), syncTaskServer.getTask_user_id(), syncTaskServer.getTask_detail_cve());

                            Log.i("Sync Task", "task_id " + syncTaskServer.getTask_id()
                                    + " comment " + syncTaskServer.getTask_comment());
                        }

                        textError = numSync + " " + getString(R.string.default_no_synchronized_task);

                        break;
                    default:
                        break;
                }
            } catch (ConnectException e) {

                textError = e.getMessage();
                validOperation = false;

                e.printStackTrace();
                Log.e("WebServiceException", "Unknown error : " + e.getMessage());

                switch (webServiceOperation) {
                    case Constants.WS_KEY_UPDATE_TASK:
                        validOperation = true;
                        break;
                    case Constants.WS_KEY_SEND_LOCATION:
                    case Constants.WS_KEY_SEND_LOCATION_HIDDEN:
                    case Constants.WS_KEY_SEND_LOCATION_HIDDEN_LOGOUT:

                        textError = "Habilitar guardado de GPS";
                        break;

                }

            } catch (Exception e) {
                textError = e.getMessage();
                validOperation = false;
            }

            return validOperation;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            pDialog.setTitle(progress[0]);
            pDialog.setMessage(progress[1]);

            if (null != progress[3]) {

                pDialog.setProgress(Integer.valueOf(progress[2]));
                pDialog.setMax(Integer.valueOf(progress[3]));
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {

                FragmentManager fragmentManager = getSupportFragmentManager();

                if (webServiceTaskDecode.getOrigin_button() != null) {
                    if ((webServiceTaskDecode.getOrigin_button() == R.id.finish_task_button)
                            || (webServiceTaskDecode.getOrigin_button() == R.id.decline_task_button)
                            || (webServiceTaskDecode.getOrigin_button() == R.id.action_server_sync)
                            || (webServiceTaskDecode.getOrigin_button() == R.id.drawer_layout)
                            || (webServiceTaskDecode.getOrigin_button() == R.id.nav_sync)
                            || (webServiceTaskDecode.getOrigin_button() == R.id.agree_task_button)) {
                        pDialog.dismiss();
                    }
                }

                switch (webServiceOperation) {

                    case Constants.WS_KEY_UPDATE_TASK:

                        removeAllFragment(fragmentManager);

                        try {

                            FilesManager filesManager = new FilesManager(webServiceTaskDecode.getSendImgFiles());
                            LegalManager legalInformation = getLegalInformation(webServiceTaskDecode.getLegalInformation());

                            BDTasksManagerQuery.updateCommonTask(getApplicationContext()
                                    , webServiceTask.getTask_id()
                                    , webServiceTaskDecode.getTask_comment()
                                    , webServiceTaskDecode.getTask_update_to()
                                    , webServiceTaskDecode.getTask_user_id()
                                    , filesManager
                                    , textError.length() == 0
                                    , legalInformation); //if textError is > 0, update is not server sync
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("UpdateTaskException", e.getMessage());
                        }

                        switch (webServiceTask.getTask_status()) {
                            //Actual Status
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
                        String tempMsg = (textError.length() > 0) ? "Tarea actualizada correctamente" : soapPrimitive.toString();
                        Toast.makeText(NavigationDrawerActivity.this, tempMsg, Toast.LENGTH_LONG).show();

                        break;
                    case Constants.WS_KEY_SEND_LOCATION:
                        onMapReady(mapa);
                        Toast.makeText(NavigationDrawerActivity.this, soapPrimitive.toString(), Toast.LENGTH_LONG).show();
                        break;
                    case Constants.WS_KEY_UPDATE_TASK_FILE:
                        pDialog.dismiss();

                        removeAllFragment(fragmentManager);

                        FragmentTransaction ftProgress = fragmentManager.beginTransaction();
                        ftProgress.add(R.id.tasks_fragment_container, new ProgressTasksFragment(), Constants.FRAGMENT_PROGRESS_TAG);
                        ftProgress.commit();

                        taskToken.clear();
                        Toast.makeText(NavigationDrawerActivity.this, soapPrimitive.toString(), Toast.LENGTH_LONG).show();
                        break;
                    case Constants.WS_KEY_ALL_TASKS:

                        for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                            Tasks t = new Tasks();

                            try {
                                SoapObject soTemp = (SoapObject) soapObject.getProperty(i);
                                SoapObject soLocation = (SoapObject) soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_LOCATION);

                                t.setTask_tittle(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_TITTLE).toString());
                                t.setTask_id(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_ID).toString()));
                                t.setTask_content(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_CONTENT).toString());
                                t.setTask_latitude(Double.valueOf(soLocation.getProperty(Constants.SOAP_OBJECT_KEY_TASK_LATITUDE).toString()));
                                t.setTask_longitude(Double.valueOf(soLocation.getProperty(Constants.SOAP_OBJECT_KEY_TASK_LONGITUDE).toString()));
                                t.setTask_priority(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_PRIORITY).toString()));
                                t.setTask_begin_date(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_BEGIN_DATE).toString());
                                t.setTask_end_date(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_END_DATE).toString());
                                t.setTask_status(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_STATUS).toString()));
                                t.setTask_user_id(SESSION_DATA.getIdUser());
                            } catch (Exception e) {
                                continue;
                            }

                            try {
                                Tasks tempTask = BDTasksManagerQuery.getTaskById(getApplicationContext(), t);

                                if (tempTask.getTask_id() == null)
                                    BDTasksManagerQuery.addTask(getApplicationContext(), t);

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("GeneralException", "Unknown error : " + e.getMessage());
                            }
                        }
                        break;
                    case Constants.WS_KEY_SERVER_SYNC:
                        Toast.makeText(NavigationDrawerActivity.this, textError, Toast.LENGTH_LONG).show();
                        break;
                    case Constants.WS_KEY_SEND_LOCATION_HIDDEN:
                        Log.w(TAG, "Location > Ubicacion automatica recibida exitosamente : " + enviarUbicacion);
                        break;
                    case Constants.WS_KEY_SEND_LOCATION_HIDDEN_LOGOUT:
                        clearSesion();
                        Log.w(TAG, "Location > Ubicacion al cerrar sesión enviada exitosamente : " + enviarUbicacion);
                        break;
                    default:
                        break;
                }
            } else {

                if (webServiceTaskDecode.getOrigin_button() != null) {
                    if ((webServiceTaskDecode.getOrigin_button() == R.id.finish_task_button)
                            || (webServiceTaskDecode.getOrigin_button() == R.id.decline_task_button)
                            || (webServiceTaskDecode.getOrigin_button() == R.id.action_server_sync)
                            || (webServiceTaskDecode.getOrigin_button() == R.id.nav_sync)
                            || (webServiceTaskDecode.getOrigin_button() == R.id.drawer_layout)) {

                        try {
                            pDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                String tempText = "";
                switch (webServiceOperation) {
                    case Constants.WS_KEY_SEND_LOCATION_HIDDEN:
                        saveOfflineGPS();
                        break;
                    case Constants.WS_KEY_SEND_LOCATION:
                        saveOfflineGPS();
                        onMapReady(mapa);
                        tempText = (textError.isEmpty() ? getString(R.string.default_ws_operation) : textError);
                        Toast.makeText(getBaseContext(), tempText, Toast.LENGTH_LONG).show();

                        break;
                    case Constants.WS_KEY_SEND_LOCATION_HIDDEN_LOGOUT:
                        saveOfflineGPS();
                        clearSesion();
                        break;
                    case Constants.WS_KEY_UPDATE_TASK_FILE:
                    case Constants.WS_KEY_UPDATE_TASK:
                        tempText = (textError.isEmpty() ? getString(R.string.default_empty_task_list) : textError);
                        Toast.makeText(getBaseContext(), tempText, Toast.LENGTH_LONG).show();
                        break;
                    case Constants.WS_KEY_SERVER_SYNC:
                        if (webServiceTaskDecode.getOrigin_button() == R.id.action_server_sync
                                || webServiceTaskDecode.getOrigin_button() == R.id.nav_sync) {
                            tempText = (textError.isEmpty() ? getString(R.string.default_server_sync_error) : textError);
                            Toast.makeText(getBaseContext(), tempText, Toast.LENGTH_LONG).show();
                        }
                        break;
                    default:
                        Log.i("INFO ", "NO DISPLAY MESSAGE");
                        break;
                }
            }
        }

        private void saveOfflineGPS() {

            MemberLocation memberLocation = new MemberLocation();
            TasksDecode lastLocation = SharedPreferencesService.getLocalizacion(getApplicationContext());

            try {
                memberLocation.setLatitude(mapa.getMyLocation().getLatitude());
            } catch (NullPointerException e) {
                memberLocation.setLatitude(Double.valueOf(lastLocation.getTask_latitude()));
            }

            try {
                memberLocation.setLongitude(mapa.getMyLocation().getLongitude());
            } catch (NullPointerException e) {
                memberLocation.setLongitude(Double.valueOf(lastLocation.getTask_longitude()));
            }

            memberLocation.setUserId(webServiceTaskDecode.getTask_user_id());
            memberLocation.setServerSync(Constants.SERVER_SYNC_FALSE);
            memberLocation.setSyncTime(DateTimeUtils.getActualTime());

            BDTasksManagerQuery.addMemberLocation(getBaseContext(), memberLocation);
        }
    }

    private void clearSesion() {
        ACTUAL_FRAGMENT = null;
        enviarUbicacion = false;
        handler.removeCallbacks(runnable);
        taskToken.clear();
        SESSION_DATA = null;
        finish();
    }

    private void enableLocationUpdates() {

        locRequest = new LocationRequest();
        locRequest.setInterval(10000);
        locRequest.setFastestInterval(2000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest locSettingsRequest =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(locRequest)
                        .build();

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        apiClient, locSettingsRequest);

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(NavigationDrawerActivity.this, PETICION_CONFIG_UBICACION);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Toast.makeText(getApplicationContext(), "UBICACION SETTINGS_CHANGE_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //Ojo: estamos suponiendo que ya tenemos concedido el permiso.
            //Sería recomendable implementar la posible petición en caso de no tenerlo.
            LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locRequest, NavigationDrawerActivity.this);
        }
    }

    private void setUILocation(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();

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

        task_force_latitude.setText(BigDecimal.valueOf(dx).longValue() + "° "
                + BigDecimal.valueOf(mx).longValue() + "' "
                + BigDecimal.valueOf(sx).longValue() + "'' N");
        task_force_longitude.setText(BigDecimal.valueOf(dy).longValue() + "° "
                + BigDecimal.valueOf(my).longValue() + "' "
                + BigDecimal.valueOf(sy).longValue() + "'' O");
    }

    private void enableLocationBackground() {
        startService(locationServiceIntent);
        enviarUbicacion = true;
        loopWebServiceLocation();
    }

    private void loopWebServiceLocation() {
        final Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runnable = new Runnable() {
                    public void run() {
                        try {
                            Log.w(TAG, "Location > Loop de ubicacion en estado : " + enviarUbicacion);
                            if (enviarUbicacion) {
                                sendLocationWS();
                            } else {
                                timer.cancel();
                                timer.purge();
                                stopService(locationServiceIntent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                handler.post(runnable);
            }
        };

        timer.schedule(task, Constants.LOADING_TIME, Constants.LOOP_TIME);
    }

    private void sendLocationWS() {
        TasksDecode lastLocation = SharedPreferencesService.getLocalizacion(getApplicationContext());

        TasksDecode tasksDecode = new TasksDecode();

        tasksDecode.setTask_longitude(lastLocation.getTask_longitude());
        tasksDecode.setTask_latitude(lastLocation.getTask_latitude());
        tasksDecode.setTask_user_id(SESSION_DATA.getIdUser());

        Log.w(TAG, "Location > Ubicacion enviada automaticamente" + " Longitud : " + lastLocation.getTask_longitude()
                + " Latitude :" + lastLocation.getTask_latitude());

        AsyncCallWS wsLocation = new AsyncCallWS(Constants.WS_KEY_SEND_LOCATION_HIDDEN, tasksDecode);
        wsLocation.execute();
    }

    private void callWebServiceLocation(final int type) {

        TasksDecode tasksDecode = new TasksDecode();
        TasksDecode lastLocation = SharedPreferencesService.getLocalizacion(getApplicationContext());
        tasksDecode.setTask_longitude(lastLocation.getTask_longitude());
        tasksDecode.setTask_latitude(lastLocation.getTask_latitude());
        tasksDecode.setTask_user_id(SESSION_DATA.getIdUser());

        AsyncCallWS wsLocation = new AsyncCallWS(type, tasksDecode);
        wsLocation.execute();

        Log.w(TAG, "Location > Ubicacion enviada manualmente" + " Longitud : " + lastLocation.getTask_longitude()
                + " Latitude :" + lastLocation.getTask_latitude());
    }
}
