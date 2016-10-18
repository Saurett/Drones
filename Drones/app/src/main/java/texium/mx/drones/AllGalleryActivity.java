package texium.mx.drones;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import texium.mx.drones.databases.BDTasksManagerQuery;
import texium.mx.drones.exceptions.VideoSyncSoapException;
import texium.mx.drones.fragments.DocumentGalleryDescriptionFragment;
import texium.mx.drones.fragments.DocumentGalleryFragment;
import texium.mx.drones.fragments.PhotoGalleryDescriptionFragment;
import texium.mx.drones.fragments.PhotoGalleryFragment;
import texium.mx.drones.fragments.VideoGalleryDescriptionFragment;
import texium.mx.drones.fragments.VideoGalleryFragment;
import texium.mx.drones.fragments.inetrface.FragmentGalleryListener;
import texium.mx.drones.models.DecodeGallery;
import texium.mx.drones.models.FilesManager;
import texium.mx.drones.models.TaskGallery;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.services.FileServices;
import texium.mx.drones.services.FileSoapServices;
import texium.mx.drones.services.SoapServices;
import texium.mx.drones.utils.Constants;

public class AllGalleryActivity extends AppCompatActivity implements DialogInterface.OnClickListener, FragmentGalleryListener, View.OnClickListener {

    private static final int GALLERY_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int GALLERY_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int GALLERY_PDF_ACTIVITY_REQUEST_CODE = 300;

    private static Tasks _TASK_INFO;
    private ProgressDialog pDialog;
    private static DecodeGallery _DECODE_GALLERY;
    private Integer ACTUAL_GALLERY;
    private Button photoBtn, videoBtn, documentBtn;

    public static FragmentManager fragmentManager;
    public static FilesManager fileManager;

    public static List<TaskGallery> galleryBefore = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_gallery);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        try {
            _TASK_INFO = (Tasks) getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_TASK_GALLERY);
        } catch (Exception e) {
            e.printStackTrace();
            getIntent().putExtra(Constants.ACTIVITY_EXTRA_PARAMS_TASK_GALLERY, _TASK_INFO);
        }

        _DECODE_GALLERY = new DecodeGallery();
        fragmentManager = getSupportFragmentManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getTitle() + " - " + _TASK_INFO.getTask_tittle());

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction list_gallery = fragmentManager.beginTransaction();
        list_gallery.add(R.id.list_gallery_container, new PhotoGalleryFragment(), Constants.FRAGMENT_PHOTO_GALLERY_LIST_TAG);
        list_gallery.commit();

        ACTUAL_GALLERY = Constants.PICTURE_FILE_TYPE;

        photoBtn = (Button) findViewById(R.id.photos_gallery);
        videoBtn = (Button) findViewById(R.id.video_gallery);
        documentBtn = (Button) findViewById(R.id.document_gallery);

        photoBtn.setOnClickListener(this);
        videoBtn.setOnClickListener(this);
        documentBtn.setOnClickListener(this);

    }

    @Override
    public void setEmptyDescription(int size) {
        RelativeLayout emptyDescription = (RelativeLayout) findViewById(R.id.emptyPhotoGalleryDescription);
        emptyDescription.setVisibility((size > 0) ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public Boolean openGallery() {

        if (_TASK_INFO.getTask_status() == Constants.NEWS_TASK) {
            showQuestion(R.id.action_add_item);
            return true;
        }

        int galleryType = GALLERY_IMAGE_ACTIVITY_REQUEST_CODE;

        Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, Uri.parse(Intent.CATEGORY_OPENABLE));

        switch (ACTUAL_GALLERY) {
            case Constants.PICTURE_FILE_TYPE:
                galleryIntent.setType("image/*");
                galleryType = GALLERY_IMAGE_ACTIVITY_REQUEST_CODE;
                break;
            case Constants.VIDEO_FILE_TYPE:
                galleryIntent.setType("video/*");
                galleryType = GALLERY_VIDEO_ACTIVITY_REQUEST_CODE;
                break;
            case Constants.DOCUMENT_FILE_TYPE:
                galleryIntent.setType("application/pdf");
                galleryType = GALLERY_PDF_ACTIVITY_REQUEST_CODE;
                break;
            default:
                break;
        }


        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(galleryIntent, galleryType);

        Toast.makeText(this, getString(R.string.default_file_single_selection) + "\n"
                        + getString(R.string.default_file_multiple_selection)
                , Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        fileManager = new FilesManager();
        switch (item.getItemId()) {
            case R.id.action_sync_item:

                AsyncGallery wsSaveDescription = new AsyncGallery(Constants.WS_KEY_ITEM_SYNC);
                wsSaveDescription.execute();

                return true;
            case R.id.action_add_item:

                /*
                if (_TASK_INFO.getTask_status() == Constants.NEWS_TASK) {
                    showQuestion(R.id.action_add_item);
                    return true;
                }

                int galleryType = GALLERY_IMAGE_ACTIVITY_REQUEST_CODE;

                Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, Uri.parse(Intent.CATEGORY_OPENABLE));

                switch (ACTUAL_GALLERY) {
                    case Constants.PICTURE_FILE_TYPE:
                        galleryIntent.setType("image/*");
                        galleryType = GALLERY_IMAGE_ACTIVITY_REQUEST_CODE;
                        break;
                    case Constants.VIDEO_FILE_TYPE:
                        galleryIntent.setType("video/*");
                        galleryType = GALLERY_VIDEO_ACTIVITY_REQUEST_CODE;
                        break;
                    case Constants.DOCUMENT_FILE_TYPE:
                        galleryIntent.setType("application/pdf");
                        galleryType = GALLERY_PDF_ACTIVITY_REQUEST_CODE;
                        break;
                    default:
                        break;
                }


                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(galleryIntent, galleryType);

                Toast.makeText(this, getString(R.string.default_file_single_selection) + "\n"
                                + getString(R.string.default_file_multiple_selection)
                        , Toast.LENGTH_LONG).show();
                return true;*/
                return openGallery();
            case android.R.id.home:

                //Check back activity question
                Boolean blockBack = false;

                List<Integer> query = new ArrayList<>();

                query.add(Constants.ITEM_SYNC_LOCAL_TABLET);
                query.add(Constants.ITEM_SYNC_SERVER_CLOUD_OFF);

                try {


                    List<Integer> taskGalleryHome = BDTasksManagerQuery.getListTaskDetail(getApplicationContext(), _TASK_INFO.getTask_id());

                    if (!taskGalleryHome.isEmpty()) {
                        galleryBefore = BDTasksManagerQuery.getGalleryFiles(getApplicationContext(),
                                taskGalleryHome, Constants.PICTURE_FILE_TYPE, query, Constants.ACTIVE);

                        if (!galleryBefore.isEmpty()) {
                            blockBack = true;
                            showQuestion(android.R.id.home);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!blockBack) finish();

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
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
                        //select multiple file video
                        selectMultipleFile(data.getClipData(), requestCode);
                    } else if (data.getData() != null) {
                        //select unique file video
                        selectUniqueFile(data.getData(), requestCode);
                    }

                } else if (resultCode == RESULT_CANCELED) {
                    // User cancelled the image capture
                } else {
                    // Image capture failed, advise user
                }
                break;
            case GALLERY_PDF_ACTIVITY_REQUEST_CODE:
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
        }
    }

    public void selectMultipleFile(ClipData data, int requestCode) {
        List<Uri> files = new ArrayList<>();
        List<Uri> selectFiles = new ArrayList<>();

        for (int i = 0; i < data.getItemCount(); i++) {
            Uri path = data.getItemAt(i).getUri();
            selectFiles.add(path);
        }

        files.addAll(selectFiles);
        this.setFileManager(requestCode, files);
    }

    public void selectUniqueFile(Uri data, int requestCode) {
        List<Uri> files = new ArrayList<>();
        files.add(data);

        this.setFileManager(requestCode, files);
    }

    private void setFileManager(int requestCode, List<Uri> files) {

        switch (requestCode) {
            case GALLERY_IMAGE_ACTIVITY_REQUEST_CODE:
                fileManager.setFilesPicture(files);
                AsyncGallery wsPhoto = new AsyncGallery(Constants.WS_KEY_ITEM_ADD_PHOTO);
                wsPhoto.execute();
                break;
            case GALLERY_VIDEO_ACTIVITY_REQUEST_CODE:
                fileManager.setFilesVideo(files);
                AsyncGallery wsVideo = new AsyncGallery(Constants.WS_KEY_ITEM_ADD_VIDEO);
                wsVideo.execute();
                break;
            case GALLERY_PDF_ACTIVITY_REQUEST_CODE:
                fileManager.setFilesPdf(files);
                AsyncGallery wsDocument = new AsyncGallery(Constants.WS_KEY_ITEM_ADD_DOCUMENT);
                wsDocument.execute();
                break;
            default:
                //No file
                break;
        }
    }

    @Override
    public void closeFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void replaceFragmentPhotoFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.list_gallery_container, new PhotoGalleryFragment());
        //transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void replaceFragmentVideoFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.list_gallery_container, new VideoGalleryFragment());
        //transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void replaceFragmentDocumentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.list_gallery_container, new DocumentGalleryFragment());
        //transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }


    @Override
    public void openDescriptionFragment(String tag) {

        Map<String, Fragment> mapFragment = new HashMap<>();
        mapFragment.put(Constants.FRAGMENT_PHOTO_GALLERY_TAG, new PhotoGalleryDescriptionFragment());
        mapFragment.put(Constants.FRAGMENT_VIDEO_GALLERY_TAG, new VideoGalleryDescriptionFragment());
        mapFragment.put(Constants.FRAGMENT_DOCUMENT_GALLERY_TAG, new DocumentGalleryDescriptionFragment());

        getIntent().putExtra(Constants.ACTIVITY_EXTRA_PARAMS_TASK_GALLERY_DESCRIPTION, _DECODE_GALLERY.getTaskGallery());

        FragmentTransaction description = fragmentManager.beginTransaction();
        description.add(R.id.detail_gallery_container, mapFragment.get(tag), tag);
        description.commit();
    }

    public void openListFragment(String tag) {

        Map<String, Fragment> mapFragment = new HashMap<>();
        mapFragment.put(Constants.FRAGMENT_PHOTO_GALLERY_LIST_TAG, new PhotoGalleryFragment());
        mapFragment.put(Constants.FRAGMENT_VIDEO_GALLERY_LIST_TAG, new VideoGalleryFragment());
        mapFragment.put(Constants.FRAGMENT_DOCUMENT_GALLERY_LIST_TAG, new DocumentGalleryFragment());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.list_gallery_container, mapFragment.get(tag));
        //transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public DecodeGallery updateDecodeGallery(DecodeGallery oldGallery) {
        return _DECODE_GALLERY = oldGallery;
    }

    @Override
    public DecodeGallery getDecodeGallery() {
        return _DECODE_GALLERY;
    }

    @Override
    public void setExtraDecodeGallery(TaskGallery taskGallery) {
        getIntent().putExtra(Constants.ACTIVITY_EXTRA_PARAMS_TASK_GALLERY_DESCRIPTION, taskGallery);
    }

    @Override
    public void showQuestion(Integer idView) {

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        Boolean showQuestion = true;
        Boolean showAlert = false;

        Integer syncType = 0;

        try {
            syncType = _DECODE_GALLERY.getTaskGallery().getSync_type();
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (idView) {
            case R.id.action_add_item:

                ad.setTitle(getString(R.string.default_title_alert_dialog));
                ad.setMessage(getString(R.string.default_no_add_file));
                ad.setCancelable(false);
                ad.setPositiveButton(getString(R.string.default_positive_button), this);

                break;
            case android.R.id.home:

                ad.setTitle(getString(R.string.default_title_alert_dialog));
                ad.setMessage(getString(R.string.default_save_back_home));
                ad.setCancelable(false);
                ad.setPositiveButton(getString(R.string.default_positive_button), this);
                ad.setNegativeButton(getString(R.string.default_negative_button), this);

                break;
            case R.id.item_photo_delete:
            case R.id.item_video_delete:
            case R.id.item_document_delete:

                if (syncType.equals(Constants.ITEM_SYNC_SERVER_DEFAULT)) {
                    ad.setTitle(getString(R.string.default_title_alert_dialog));
                    ad.setMessage(getString(R.string.default_no_delete_msg));
                    ad.setCancelable(false);
                    ad.setNeutralButton(getString(R.string.default_positive_button), this);

                } else {
                    ad.setTitle(getString(R.string.default_title_alert_dialog));
                    ad.setMessage(getString(R.string.default_profile_delete_msg));
                    ad.setCancelable(false);
                    ad.setPositiveButton(getString(R.string.default_positive_button), this);
                    ad.setNegativeButton(getString(R.string.default_negative_button), this);
                }

                break;
            case R.id.item_photo_description:
            case R.id.item_video_description:
            case R.id.item_document_description:

                Map<Integer, String> mapGallery = new HashMap<>();

                mapGallery.put(Constants.PICTURE_FILE_TYPE, Constants.FRAGMENT_PHOTO_GALLERY_TAG);
                mapGallery.put(Constants.VIDEO_FILE_TYPE, Constants.FRAGMENT_VIDEO_GALLERY_TAG);
                mapGallery.put(Constants.DOCUMENT_FILE_TYPE, Constants.FRAGMENT_DOCUMENT_GALLERY_TAG);

                switch (ACTUAL_GALLERY) {
                    case Constants.PICTURE_FILE_TYPE:
                        showAlert = PhotoGalleryDescriptionFragment.emptyDescription();
                        showQuestion = PhotoGalleryDescriptionFragment.changeDescription();
                        break;
                    case Constants.VIDEO_FILE_TYPE:
                        showAlert = VideoGalleryDescriptionFragment.emptyDescription();
                        showQuestion = VideoGalleryDescriptionFragment.changeDescription();
                        break;
                    case Constants.DOCUMENT_FILE_TYPE:
                        showAlert = DocumentGalleryDescriptionFragment.emptyDescription();
                        showQuestion = DocumentGalleryDescriptionFragment.changeDescription();
                        break;
                }

                if (showAlert) {

                    ad.setTitle(getString(R.string.default_title_alert_dialog));
                    ad.setMessage(getString(R.string.default_alert_empty_unique_description));
                    ad.setCancelable(false);
                    ad.setPositiveButton(getString(R.string.default_positive_button), this);

                } else {
                    if (showQuestion) {

                        ad.setTitle(getString(R.string.default_title_alert_dialog));
                        ad.setMessage(getString(R.string.default_item_description_msg));
                        ad.setCancelable(false);
                        ad.setPositiveButton(getString(R.string.default_positive_yes_button), this);
                        ad.setNegativeButton(getString(R.string.default_negative_no_button), this);
                        ad.setNeutralButton(getString(R.string.default_negative_cancel_button), this);

                    } else {
                        closeFragment(mapGallery.get(ACTUAL_GALLERY));
                        openDescriptionFragment(mapGallery.get(ACTUAL_GALLERY));
                    }
                }

                break;
            case R.id.item_photo_sync:
            case R.id.item_video_sync:
            case R.id.item_document_sync:

                switch (syncType) {
                    case Constants.ITEM_SYNC_SERVER_DEFAULT:

                        ad.setTitle(getString(R.string.default_title_alert_dialog));
                        ad.setMessage(getString(R.string.default_msg_sync_server));
                        ad.setCancelable(false);
                        ad.setNeutralButton(getString(R.string.default_positive_button), this);

                        break;
                    case Constants.ITEM_SYNC_LOCAL_TABLET:

                        ad.setTitle(getString(R.string.default_title_alert_dialog));
                        ad.setMessage("Archivo agregado por la cuadrilla");
                        ad.setCancelable(false);
                        ad.setNeutralButton(getString(R.string.default_positive_button), this);

                        break;
                    case Constants.ITEM_SYNC_SERVER_CLOUD:

                        ad.setTitle(getString(R.string.default_title_alert_dialog));
                        ad.setMessage("Archivo enviado por la cuadrilla");
                        ad.setCancelable(false);
                        ad.setNeutralButton(getString(R.string.default_positive_button), this);

                        break;
                    case Constants.ITEM_SYNC_SERVER_CLOUD_OFF:

                        ad.setTitle(getString(R.string.default_title_alert_dialog));
                        ad.setMessage("Archivo enviado por la cuadrilla, archivo con modificaciones");
                        ad.setCancelable(false);
                        ad.setNeutralButton(getString(R.string.default_positive_button), this);

                        break;
                }

                break;
        }

        if (showQuestion || showAlert) ad.show();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Integer idView = (null != _DECODE_GALLERY.getIdView() ?
                _DECODE_GALLERY.getIdView() : Constants.INACTIVE);


        idView = (galleryBefore.size() > 0) ? android.R.id.home : idView;

        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:

                switch (idView) {
                    case R.id.item_photo_delete:
                    case R.id.item_video_delete:
                    case R.id.item_document_delete:
                        AsyncGallery wsDeletePhoto = new AsyncGallery(Constants.WS_KEY_ITEM_DELETE);
                        wsDeletePhoto.execute();
                        break;
                    case R.id.item_photo_sync:
                        AsyncGallery wsSaveDescription = new AsyncGallery(Constants.WS_KEY_ITEM_SYNC);
                        wsSaveDescription.execute();
                        break;
                    case android.R.id.home:
                        AsyncGallery wsSaveBefore = new AsyncGallery(Constants.WS_KEY_ITEM_SYNC_HOME);
                        wsSaveBefore.execute();
                        break;
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                switch (idView) {
                    case R.id.item_photo_description:
                        openDescriptionFragment(Constants.FRAGMENT_PHOTO_GALLERY_TAG);
                        break;
                    case R.id.item_video_description:
                        closeFragment(Constants.FRAGMENT_VIDEO_GALLERY_TAG);
                        openDescriptionFragment(Constants.FRAGMENT_VIDEO_GALLERY_TAG);
                        break;
                    case R.id.item_document_description:
                        closeFragment(Constants.FRAGMENT_DOCUMENT_GALLERY_TAG);
                        openDescriptionFragment(Constants.FRAGMENT_DOCUMENT_GALLERY_TAG);
                        break;
                    case android.R.id.home:
                        galleryBefore = new ArrayList<>();
                        finish();
                        break;
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {

        setEmptyDescription(1);

        closeFragment(Constants.FRAGMENT_PHOTO_GALLERY_TAG);
        closeFragment(Constants.FRAGMENT_VIDEO_GALLERY_TAG);
        closeFragment(Constants.FRAGMENT_DOCUMENT_GALLERY_TAG);

        switch (v.getId()) {
            case R.id.photos_gallery:
                ACTUAL_GALLERY = Constants.PICTURE_FILE_TYPE;
                replaceFragmentPhotoFragment();
                break;
            case R.id.video_gallery:
                ACTUAL_GALLERY = Constants.VIDEO_FILE_TYPE;
                replaceFragmentVideoFragment();
                break;
            case R.id.document_gallery:
                ACTUAL_GALLERY = Constants.DOCUMENT_FILE_TYPE;
                replaceFragmentDocumentFragment();
                break;
        }
    }

    private class AsyncGallery extends AsyncTask<Void, String, Boolean> {

        private SoapPrimitive soapPrimitive;
        private SoapObject soapObject;
        private Integer webServiceOperation;
        private String textError;
        private Boolean localAccess;
        private Integer itemSync;
        private Boolean emptyDescription;

        private AsyncGallery(Integer wsOperation) {
            webServiceOperation = wsOperation;
            textError = "";
            localAccess = false;
            itemSync = 0;
            emptyDescription = false;
        }

        @Override
        protected void onPreExecute() {
            switch (webServiceOperation) {
                case Constants.WS_KEY_ITEM_DELETE:
                    pDialog = new ProgressDialog(AllGalleryActivity.this);
                    pDialog.setMessage(getString(R.string.default_loading_msg));
                    pDialog.setTitle(getString(R.string.default_delete_profile_msg));
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                    break;
                case Constants.WS_KEY_ITEM_SAVE_DESCRIPTION:
                    pDialog = new ProgressDialog(AllGalleryActivity.this);
                    pDialog.setMessage(getString(R.string.default_loading_msg));
                    pDialog.setTitle(getString(R.string.default_saving_img_msg));
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                    break;
                case Constants.WS_KEY_ITEM_ADD_PHOTO:
                case Constants.WS_KEY_ITEM_ADD_VIDEO:
                case Constants.WS_KEY_ITEM_ADD_DOCUMENT:
                    pDialog = new ProgressDialog(AllGalleryActivity.this);
                    pDialog.setMessage(getString(R.string.default_attaching_img));
                    pDialog.setTitle(getString(R.string.default_loading_msg));
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                    break;
                case Constants.WS_KEY_ITEM_SYNC:
                case Constants.WS_KEY_ITEM_SYNC_HOME:
                    pDialog = new ProgressDialog(AllGalleryActivity.this);
                    pDialog.setMessage(getString(R.string.default_attaching_img));
                    pDialog.setTitle(getString(R.string.server_sync));
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                    break;
                default:
                    break;
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            try {

                List<Integer> query = new ArrayList<>();
                List<Integer> taskGallery;

                query.add(Constants.ITEM_SYNC_LOCAL_TABLET);
                query.add(Constants.ITEM_SYNC_SERVER_CLOUD_OFF);

                switch (webServiceOperation) {

                    case Constants.WS_KEY_ITEM_DELETE:

                        validOperation = true;

                        if (!_DECODE_GALLERY.getTaskGallery().getSync_type().equals(Constants.ITEM_SYNC_LOCAL_TABLET)) {
                            soapPrimitive = SoapServices.deletePhotoFile(getApplicationContext(),
                                    _DECODE_GALLERY.getTaskGallery().getId(),
                                    _TASK_INFO.getTask_user_id());
                            validOperation = (soapPrimitive != null);
                        }

                        break;
                    case Constants.WS_KEY_ITEM_SYNC:

                        Integer idTask = _TASK_INFO.getTask_id();
                        Integer idUser = _TASK_INFO.getTask_user_id();

                        switch (ACTUAL_GALLERY) {
                            case Constants.PICTURE_FILE_TYPE:
                                itemSync = FileSoapServices.syncAllFiles(getApplicationContext(), _TASK_INFO.getTask_id(), _TASK_INFO.getTask_user_id());
                                validOperation = true;
                                break;
                            case Constants.VIDEO_FILE_TYPE:

                                taskGallery = BDTasksManagerQuery.getListTaskDetail(getApplicationContext(), idTask);

                                if (!taskGallery.isEmpty()) {
                                    galleryBefore = new ArrayList<>();
                                    galleryBefore = BDTasksManagerQuery.getGalleryFiles(getApplicationContext(),
                                            taskGallery, Constants.VIDEO_FILE_TYPE, query, Constants.ACTIVE);

                                    int videoNumber = 1;

                                    //All Normal video Sync
                                    for (TaskGallery video : galleryBefore) {

                                        if (video.getDescription().isEmpty()) {
                                            emptyDescription = true;
                                            break;
                                        }

                                        if (video.getId() > 0) {
                                            SoapServices.updatePhotoFile(getApplicationContext(), video, idUser);
                                        } else {

                                            FilesManager fm = FileServices.attachVideos(AllGalleryActivity.this, Uri.parse(video.getLocalURI()));

                                            List<String> packages = FileServices.getPackageList(getApplicationContext()
                                                    , fm.getEncodeSingleFile());

                                            int packNumber = 1;

                                            for (String pack : packages) {

                                                String title = "Transfiriendo al servidor video " + videoNumber + " de " + galleryBefore.size();
                                                String msg = "Subiendo paquete " + packNumber + " de " + packages.size();

                                                publishProgress(title, msg, String.valueOf(packNumber), String.valueOf(packages.size()));

                                                try {
                                                    soapPrimitive = SoapServices.updateVideoFiles(getApplicationContext()
                                                            , idTask, idUser, pack, packNumber, (packNumber == packages.size()), video.getDescription());

                                                    packNumber++;

                                                } catch (Exception e) {
                                                    throw new VideoSyncSoapException("Error al intetar enviar los videos", e);
                                                }
                                            }
                                            videoNumber++;
                                            try {
                                                video.setId(Integer.valueOf(soapPrimitive.toString()));
                                            } catch (Exception e) {
                                                throw new Exception(getString(R.string.default_exception_error));
                                            }
                                        }

                                        itemSync++;
                                        video.setSync_type(Constants.ITEM_SYNC_SERVER_CLOUD);
                                        BDTasksManagerQuery.updateTaskFile(getApplicationContext(), video);
                                    }

                                    query = new ArrayList<>();
                                    query.add(Constants.ITEM_SYNC_SERVER_DELETE);

                                    galleryBefore = BDTasksManagerQuery.getGalleryFiles(getApplicationContext(),
                                            taskGallery, Constants.VIDEO_FILE_TYPE, query, Constants.INACTIVE);

                                    for (TaskGallery photo : galleryBefore) {

                                        if (photo.getId() > 0) {
                                            soapPrimitive = SoapServices.deletePhotoFile(getApplicationContext(), photo.getId(), idUser);
                                            if (null != soapPrimitive)
                                                BDTasksManagerQuery.deleteTaskFile(getApplicationContext(), photo);
                                            itemSync++;
                                        }
                                    }
                                }

                                validOperation = true;
                                break;
                            case Constants.DOCUMENT_FILE_TYPE:

                                taskGallery = BDTasksManagerQuery.getListTaskDetail(getApplicationContext(), idTask);

                                if (!taskGallery.isEmpty()) {
                                    galleryBefore = new ArrayList<>();
                                    galleryBefore = BDTasksManagerQuery.getGalleryFiles(getApplicationContext(),
                                            taskGallery, Constants.DOCUMENT_FILE_TYPE, query, Constants.ACTIVE);

                                    int documentNumber = 1;

                                    //All Normal video Sync
                                    for (TaskGallery document : galleryBefore) {

                                        if (document.getDescription().isEmpty()) {
                                            emptyDescription = true;
                                            break;
                                        }

                                        if (document.getId() > 0) {
                                            SoapServices.updatePhotoFile(getApplicationContext(), document, idUser);
                                        } else {

                                            FilesManager fm = FileServices.attachVideos(AllGalleryActivity.this, Uri.parse(document.getLocalURI()));

                                            List<String> packages = FileServices.getPackageList(getApplicationContext()
                                                    , fm.getEncodeSingleFile());

                                            int packNumber = 1;

                                            for (String pack : packages) {

                                                String title = "Transfiriendo al servidor documento " + documentNumber + " de " + galleryBefore.size();
                                                String msg = "Subiendo paquete " + packNumber + " de " + packages.size();

                                                publishProgress(title, msg, String.valueOf(packNumber), String.valueOf(packages.size()));

                                                try {
                                                    soapPrimitive = SoapServices.updateDocumentFiles(getApplicationContext()
                                                            , idTask, idUser, pack, packNumber, (packNumber == packages.size()), document.getDescription());

                                                    packNumber++;

                                                } catch (Exception e) {
                                                    throw new VideoSyncSoapException("Error al intetar enviar los videos", e);
                                                }
                                            }
                                            documentNumber++;
                                            try {
                                                document.setId(Integer.valueOf(soapPrimitive.toString()));
                                            } catch (Exception e) {
                                                throw new Exception(getString(R.string.default_exception_error));
                                            }
                                        }

                                        itemSync++;
                                        document.setSync_type(Constants.ITEM_SYNC_SERVER_CLOUD);
                                        BDTasksManagerQuery.updateTaskFile(getApplicationContext(), document);
                                    }

                                    query = new ArrayList<>();
                                    query.add(Constants.ITEM_SYNC_SERVER_DELETE);

                                    galleryBefore = BDTasksManagerQuery.getGalleryFiles(getApplicationContext(),
                                            taskGallery, Constants.DOCUMENT_FILE_TYPE, query, Constants.INACTIVE);

                                    for (TaskGallery document : galleryBefore) {

                                        if (document.getId() > 0) {
                                            soapPrimitive = SoapServices.deletePhotoFile(getApplicationContext(), document.getId(), idUser);
                                            if (null != soapPrimitive)
                                                BDTasksManagerQuery.deleteTaskFile(getApplicationContext(), document);
                                            itemSync++;
                                        }
                                    }
                                }

                                validOperation = true;
                                break;

                        }
                        break;
                    case Constants.WS_KEY_ITEM_SYNC_HOME:

                        for (TaskGallery photo : galleryBefore) {

                            if (photo.getDescription().isEmpty()) {
                                emptyDescription = true;
                                break;
                            }

                            if (photo.getId() > 0) {
                                SoapServices.updatePhotoFile(getApplicationContext(), photo, _TASK_INFO.getTask_user_id());
                            } else {
                                soapPrimitive = SoapServices.savePhotoFile(getApplicationContext(), photo, _TASK_INFO.getTask_id(), _TASK_INFO.getTask_user_id());
                                photo.setId(Integer.valueOf(soapPrimitive.toString()));
                            }

                            photo.setSync_type(Constants.ITEM_SYNC_SERVER_CLOUD);
                            BDTasksManagerQuery.updateTaskFile(getApplicationContext(), photo);
                            itemSync++;
                        }

                        validOperation = true;

                        break;
                    case Constants.WS_KEY_ITEM_ADD_PHOTO:
                        /*
                        fileManager.setEncodePictureFiles(FileServices.attachImg(AllGalleryActivity.this, fileManager.getFilesPicture()));

                        BDTasksManagerQuery.updateCommonTask(getApplicationContext(), _TASK_INFO.getTask_id()
                                , "Se añade foto"
                                , _TASK_INFO.getTask_status()
                                , _TASK_INFO.getTask_user_id()
                                , fileManager
                                , textError.length() == 0);
                        validOperation = true;
                        */


                        List<Uri> uriPictures = fileManager.getFilesPicture();

                        for (Uri uriPicture : uriPictures) {

                            String basePicture = FileServices.attachImg(AllGalleryActivity.this,uriPicture,50);

                            TaskGallery videoGallery = new TaskGallery();


                            byte[] decodedString = Base64.decode(basePicture, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            videoGallery.setLocalURI(uriPicture.toString());
                            videoGallery.setFile_type(Constants.PICTURE_FILE_TYPE);
                            videoGallery.setSync_type(Constants.ITEM_SYNC_LOCAL_TABLET);
                            videoGallery.setDescription(Constants.EMPTY_STRING);
                            videoGallery.setPhoto_bitmap(decodedByte);
                            videoGallery.setBase_package(basePicture);

                            BDTasksManagerQuery.updateCommonTaskVideo(getApplicationContext(), _TASK_INFO.getTask_id()
                                    , "Se añaden imagenes"
                                    , _TASK_INFO.getTask_status()
                                    , _TASK_INFO.getTask_user_id()
                                    , videoGallery
                                    , textError.length() == 0);
                        }

                        validOperation = true;


                        break;
                    case Constants.WS_KEY_ITEM_ADD_VIDEO:

                        List<Uri> uriVideos = fileManager.getFilesVideo();

                        for (Uri uriVideo : uriVideos) {

                            Bitmap thumbnail = FileServices.createVideoThumbnail(getApplicationContext(), uriVideo);

                            TaskGallery videoGallery = new TaskGallery();

                            videoGallery.setLocalURI(uriVideo.toString());
                            videoGallery.setFile_type(Constants.VIDEO_FILE_TYPE);
                            videoGallery.setSync_type(Constants.ITEM_SYNC_LOCAL_TABLET);
                            videoGallery.setDescription(Constants.EMPTY_STRING);
                            videoGallery.setPhoto_bitmap(thumbnail);
                            videoGallery.setBase_package(FileServices.attachImgFromBitmap(videoGallery.getPhoto_bitmap(), 100));

                            BDTasksManagerQuery.updateCommonTaskVideo(getApplicationContext(), _TASK_INFO.getTask_id()
                                    , "Se añaden videos"
                                    , _TASK_INFO.getTask_status()
                                    , _TASK_INFO.getTask_user_id()
                                    , videoGallery
                                    , textError.length() == 0);
                        }

                        validOperation = true;
                        break;
                    case Constants.WS_KEY_ITEM_ADD_DOCUMENT:
                        List<Uri> uriDocuments = fileManager.getFilesPdf();

                        for (Uri uriDocument : uriDocuments) {

                            TaskGallery documentGallery = new TaskGallery();

                            documentGallery.setLocalURI(uriDocument.toString());
                            documentGallery.setFile_type(Constants.DOCUMENT_FILE_TYPE);
                            documentGallery.setSync_type(Constants.ITEM_SYNC_LOCAL_TABLET);
                            documentGallery.setDescription(Constants.EMPTY_STRING);
                            //documentGallery.setPhoto_bitmap(thumbnail);
                            //documentGallery.setBase_package(FileServices.attachImgFromBitmap(documentGallery.getPhoto_bitmap()));

                            BDTasksManagerQuery.updateCommonTaskVideo(getApplicationContext(), _TASK_INFO.getTask_id()
                                    , "Se añaden documentos"
                                    , _TASK_INFO.getTask_status()
                                    , _TASK_INFO.getTask_user_id()
                                    , documentGallery
                                    , textError.length() == 0);

                            validOperation = true;
                        }
                        break;
                    default:
                        break;

                }
            } catch (ConnectException e) {

                switch (webServiceOperation) {
                    case Constants.WS_KEY_ITEM_DELETE:

                        try {

                            validOperation = true;
                            textError = "Archivo eliminado correctamente, pendiente a sincronizar con el servidor.";

                            switch (ACTUAL_GALLERY) {
                                case Constants.PICTURE_FILE_TYPE:
                                    BDTasksManagerQuery.updateCommonTask(getApplicationContext(), _TASK_INFO.getTask_id()
                                            , "Se elimina foto sin conexión desde la app móvil"
                                            , _TASK_INFO.getTask_status()
                                            , _TASK_INFO.getTask_user_id()
                                            , fileManager
                                            , textError.length() == 0);
                                    break;
                                case Constants.VIDEO_FILE_TYPE:
                                    TaskGallery videoGallery = new TaskGallery();
                                    videoGallery.setLocalURI(fileManager.getTitle());
                                    BDTasksManagerQuery.updateCommonTaskVideo(getApplicationContext(), _TASK_INFO.getTask_id()
                                            , "Se elimina foto sin conexión desde la app móvil"
                                            , _TASK_INFO.getTask_status()
                                            , _TASK_INFO.getTask_user_id()
                                            , videoGallery
                                            , textError.length() == 0);
                                    break;
                            }

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
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
            String textSync = (itemSync > 0) ? "Sincronizado correctamente" : "No hay archivos para sincronizar";
            if (success) switch (webServiceOperation) {
                case Constants.WS_KEY_ITEM_DELETE:

                    String txtDelete = "";

                    if (textError.isEmpty()) {

                        try {
                            BDTasksManagerQuery.deleteTaskFile(getApplicationContext(), _DECODE_GALLERY.getTaskGallery());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        txtDelete = (null != soapPrimitive) ? soapPrimitive.toString() : "Archivo borrado correctamente";
                    } else {

                        try {
                            TaskGallery photoDelete = _DECODE_GALLERY.getTaskGallery();
                            photoDelete.setSync_type(Constants.ITEM_SYNC_SERVER_DELETE);
                            BDTasksManagerQuery.updateTaskFile(getApplicationContext(), photoDelete);

                            txtDelete = textError;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    Toast.makeText(AllGalleryActivity.this, txtDelete, Toast.LENGTH_LONG).show();

                    switch (ACTUAL_GALLERY) {
                        case Constants.PICTURE_FILE_TYPE:
                            PhotoGalleryFragment.removeAt(_DECODE_GALLERY.getPosition());
                            closeFragment(Constants.FRAGMENT_PHOTO_GALLERY_TAG);
                            break;
                        case Constants.VIDEO_FILE_TYPE:
                            VideoGalleryFragment.removeAt(_DECODE_GALLERY.getPosition());
                            closeFragment(Constants.FRAGMENT_VIDEO_GALLERY_TAG);
                            break;
                        case Constants.DOCUMENT_FILE_TYPE:
                            DocumentGalleryFragment.removeAt(_DECODE_GALLERY.getPosition());
                            closeFragment(Constants.FRAGMENT_DOCUMENT_GALLERY_TAG);
                            break;
                    }


                    break;
                case Constants.WS_KEY_ITEM_SYNC_HOME:

                    if (emptyDescription) {
                        textSync = getString(R.string.default_alert_empty_descripcion);
                    } else {
                        galleryBefore = new ArrayList<>();
                        finish();
                    }

                    Toast.makeText(AllGalleryActivity.this, textSync, Toast.LENGTH_LONG).show();

                    break;
                case Constants.WS_KEY_ITEM_SYNC:

                    if (emptyDescription) {
                        textSync = getString(R.string.default_alert_empty_descripcion);
                    }

                    switch (ACTUAL_GALLERY) {
                        case Constants.PICTURE_FILE_TYPE:
                            closeFragment(Constants.FRAGMENT_PHOTO_GALLERY_TAG);
                            openListFragment(Constants.FRAGMENT_PHOTO_GALLERY_LIST_TAG);
                            Toast.makeText(AllGalleryActivity.this, textSync, Toast.LENGTH_LONG).show();
                            break;
                        case Constants.VIDEO_FILE_TYPE:
                            closeFragment(Constants.FRAGMENT_VIDEO_GALLERY_TAG);
                            openListFragment(Constants.FRAGMENT_VIDEO_GALLERY_LIST_TAG);
                            Toast.makeText(AllGalleryActivity.this, textSync, Toast.LENGTH_LONG).show();
                            break;
                        case Constants.DOCUMENT_FILE_TYPE:
                            closeFragment(Constants.FRAGMENT_DOCUMENT_GALLERY_TAG);
                            openListFragment(Constants.FRAGMENT_DOCUMENT_GALLERY_LIST_TAG);
                            Toast.makeText(AllGalleryActivity.this, textSync, Toast.LENGTH_LONG).show();
                    }

                    break;
                case Constants.WS_KEY_ITEM_ADD_PHOTO:
                    closeFragment(Constants.FRAGMENT_PHOTO_GALLERY_TAG);
                    openListFragment(Constants.FRAGMENT_PHOTO_GALLERY_LIST_TAG);
                    Toast.makeText(AllGalleryActivity.this, "Fotos agregadas correctamente", Toast.LENGTH_LONG).show();
                    break;
                case Constants.WS_KEY_ITEM_ADD_VIDEO:
                    closeFragment(Constants.FRAGMENT_VIDEO_GALLERY_TAG);
                    openListFragment(Constants.FRAGMENT_VIDEO_GALLERY_LIST_TAG);
                    Toast.makeText(AllGalleryActivity.this, "Videos agregados correctamente", Toast.LENGTH_LONG).show();
                    break;
                case Constants.WS_KEY_ITEM_ADD_DOCUMENT:
                    closeFragment(Constants.FRAGMENT_DOCUMENT_GALLERY_TAG);
                    openListFragment(Constants.FRAGMENT_DOCUMENT_GALLERY_LIST_TAG);
                    Toast.makeText(AllGalleryActivity.this, "Documentos agregados correctamente", Toast.LENGTH_LONG).show();
                    break;
            }
            else {

                switch (webServiceOperation) {
                    case Constants.WS_KEY_ITEM_SYNC:

                        switch (ACTUAL_GALLERY) {
                            case Constants.PICTURE_FILE_TYPE:
                                closeFragment(Constants.FRAGMENT_PHOTO_GALLERY_TAG);
                                openListFragment(Constants.FRAGMENT_PHOTO_GALLERY_LIST_TAG);
                                break;
                            case Constants.VIDEO_FILE_TYPE:
                                closeFragment(Constants.FRAGMENT_VIDEO_GALLERY_TAG);
                                openListFragment(Constants.FRAGMENT_VIDEO_GALLERY_LIST_TAG);
                                break;
                            case Constants.DOCUMENT_FILE_TYPE:
                                closeFragment(Constants.FRAGMENT_DOCUMENT_GALLERY_TAG);
                                openListFragment(Constants.FRAGMENT_DOCUMENT_GALLERY_LIST_TAG);
                        }

                        break;
                }

                String tempText = (textError.isEmpty() ? "Error desconocido" : textError);
                Toast.makeText(getApplicationContext(), tempText, Toast.LENGTH_LONG).show();
            }

            pDialog.dismiss();
        }
    }
}


