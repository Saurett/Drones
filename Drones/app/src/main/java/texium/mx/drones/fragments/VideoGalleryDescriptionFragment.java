package texium.mx.drones.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.databases.BDTasksManagerQuery;
import texium.mx.drones.fragments.inetrface.FragmentGalleryListener;
import texium.mx.drones.models.TaskGallery;
import texium.mx.drones.utils.Constants;


public class VideoGalleryDescriptionFragment extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener {

    private SoapObject soapObject;
    private static TaskGallery _DESCRIPTION;

    static FragmentGalleryListener activityListener;
    private ProgressDialog pDialog;

    private static EditText description;
    private static ImageView taskVideo;
    private Button save, cancel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_video, container, false);

        description = (EditText) view.findViewById(R.id.videoDescription);
        taskVideo = (ImageView) view.findViewById(R.id.taskVideo);

        save = (Button) view.findViewById(R.id.saveVideoDescription);
        cancel = (Button) view.findViewById(R.id.cancelVideoDescription);

        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

        description.setText(_DESCRIPTION.getDescription());
        taskVideo.setImageBitmap(_DESCRIPTION.getPhoto_bitmap());

        //view.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        _DESCRIPTION = (TaskGallery) getActivity().getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_TASK_GALLERY_DESCRIPTION);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityListener = (FragmentGalleryListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " debe implementar ");
        }
    }

    private void showQuestion() {

        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

        ad.setTitle(getString(R.string.default_title_alert_dialog));
        ad.setMessage(getString(R.string.default_no_edit_msg));
        ad.setCancelable(false);
        ad.setNeutralButton(getString(R.string.default_positive_button), this);

        ad.show();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:

                break;
            case DialogInterface.BUTTON_NEGATIVE:

                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelVideoDescription:

                description.setText(_DESCRIPTION.getDescription());

                break;
            case R.id.saveVideoDescription:

                TaskGallery taskGallery = _DESCRIPTION;
                taskGallery.setDescription(description.getText().toString());

                switch (taskGallery.getSync_type()) {
                    case Constants.ITEM_SYNC_SERVER_DEFAULT:
                        this.showQuestion();
                        break;
                    case Constants.ITEM_SYNC_LOCAL_TABLET:
                        try {
                            BDTasksManagerQuery.updateTaskFile(getContext(), taskGallery);
                            Toast.makeText(getActivity(), "Guardado Correctamente ...", Toast.LENGTH_SHORT).show();
                            _DESCRIPTION = taskGallery;
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), R.string.default_fail_save, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case Constants.ITEM_SYNC_SERVER_CLOUD:
                        try {
                            taskGallery.setSync_type(Constants.ITEM_SYNC_SERVER_CLOUD_OFF);
                            BDTasksManagerQuery.updateTaskFile(getContext(), taskGallery);
                            Toast.makeText(getActivity(), "Guardado Correctamente ...", Toast.LENGTH_SHORT).show();
                            _DESCRIPTION = taskGallery;
                            activityListener.closeFragment(Constants.FRAGMENT_VIDEO_GALLERY_TAG);
                            activityListener.replaceFragmentVideoFragment();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), R.string.default_fail_save, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case Constants.ITEM_SYNC_SERVER_CLOUD_OFF:
                        try {
                            BDTasksManagerQuery.updateTaskFile(getContext(), taskGallery);
                            Toast.makeText(getActivity(), "Guardado Correctamente ...", Toast.LENGTH_SHORT).show();
                            activityListener.closeFragment(Constants.FRAGMENT_VIDEO_GALLERY_TAG);
                            activityListener.replaceFragmentVideoFragment();
                            _DESCRIPTION = taskGallery;
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), R.string.default_fail_save, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                break;
            default:
                break;
        }
    }

    public static Boolean changeDescription() {

        String original = (null != _DESCRIPTION.getDescription() ? _DESCRIPTION.getDescription() : Constants.EMPTY_STRING);
        String actual = (null != description.getText() ? description.getText().toString() : Constants.EMPTY_STRING);

        return !original.equals(actual);
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private Integer webServiceOperation;
        private Integer idTeam;
        private Integer idStatus;
        private List<TaskGallery> tempTaskList;
        private String textError;

        private AsyncCallWS(Integer wsOperation, Integer wsIdTeam, Integer wsIdStatus) {
            webServiceOperation = wsOperation;
            idTeam = wsIdTeam;
            idStatus = wsIdStatus;
            textError = "";
            tempTaskList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage(getString(R.string.tasks_loading));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            try {
                switch (webServiceOperation) {
                    case Constants.WS_KEY_TASK_SERVICE_NEWS:

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

            try {
                pDialog.dismiss();
                if (success) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
