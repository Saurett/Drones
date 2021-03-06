package texium.mx.drones.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import texium.mx.drones.R;
import texium.mx.drones.adapters.TaskListAdapter;
import texium.mx.drones.fragments.inetrface.FragmentTaskListener;
import texium.mx.drones.models.FilesManager;
import texium.mx.drones.models.LegalManager;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.TasksDecode;
import texium.mx.drones.utils.Constants;


public class FinishTasksFragment extends Fragment implements View.OnClickListener {

    static FragmentTaskListener activityListener;

    private static Button send_task_button, close_window_button, next_task_button, back_task_button, gallery_task_gallery;
    private static TextView title_task_window, content_task_window, comment_task_window, label_legal;
    private static Switch causes;
    private static ImageView task_window_icon;

    static Map<Long, Object> taskToken = new HashMap<>();

    static private int _ACTUAL_POSITION;
    static private int _ACTUAL_COUNT;

    static Map<Integer, FilesManager> TASK_FILES = new HashMap<>();

    private ProgressDialog pDialog;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_finish_tasks, container, false);

        close_window_button = (Button) view.findViewById(R.id.close_window_button);
        send_task_button = (Button) view.findViewById(R.id.send_task_button);
        next_task_button = (Button) view.findViewById(R.id.next_task_button);
        back_task_button = (Button) view.findViewById(R.id.back_task_button);
        //picture_task_button = (Button) view.findViewById(R.id.picture_task_button);
        //video_task_button = (Button) view.findViewById(R.id.video_task_button);
        gallery_task_gallery = (Button) view.findViewById(R.id.gallery_task_gallery);

        title_task_window = (TextView) view.findViewById(R.id.title_task_window);
        content_task_window = (TextView) view.findViewById(R.id.content_task_window);
        comment_task_window = (TextView) view.findViewById(R.id.comment_task_window);
        //number_photos = (TextView) view.findViewById(R.id.number_photos);
        //number_videos = (TextView) view.findViewById(R.id.number_videos);

        label_legal = (TextView) view.findViewById(R.id.label_legal);

        task_window_icon = (ImageView) view.findViewById(R.id.task_window_icon);

        causes = (Switch) view.findViewById(R.id.switch_causes);

        back_task_button.setOnClickListener(this);
        send_task_button.setOnClickListener(this);
        close_window_button.setOnClickListener(this);
        next_task_button.setOnClickListener(this);
        //picture_task_button.setOnClickListener(this);
        ///video_task_button.setOnClickListener(this);
        gallery_task_gallery.setOnClickListener(this);

        causes.setOnClickListener(this);

        View tokenView = (View) taskToken.get(1L);
        TaskListAdapter tokenAdapter = (TaskListAdapter) taskToken.get(2L);
        Tasks tokenTask = (Tasks) taskToken.get(3L);
        TasksDecode tokenTaskDecode = (TasksDecode) taskToken.get(4L);

        title_task_window.setText(tokenTask.getTask_tittle());
        content_task_window.setText(tokenTask.getTask_content());

        int actualIcon = (tokenTaskDecode.getOrigin_button() == R.id.finish_task_button)
                ? R.mipmap.ic_assignment_turned_in_white : R.mipmap.ic_assignment_late_white;

        task_window_icon.setBackground(getResources().getDrawable(actualIcon));

        if (_ACTUAL_POSITION == _ACTUAL_COUNT) {
            next_task_button.setEnabled(false);
            next_task_button.setVisibility(View.INVISIBLE);
        }

        if ((_ACTUAL_POSITION > 0) && (_ACTUAL_POSITION < _ACTUAL_COUNT)) {
            next_task_button.setEnabled(true);
            next_task_button.setVisibility(View.VISIBLE);

            back_task_button.setEnabled(true);
            back_task_button.setVisibility(View.VISIBLE);

        }

        if (_ACTUAL_POSITION <= 0) {
            back_task_button.setEnabled(false);
            back_task_button.setVisibility(View.INVISIBLE);
        }

        fragmentManager = getActivity().getSupportFragmentManager();

        causes.setVisibility(View.INVISIBLE);
        label_legal.setVisibility(View.INVISIBLE);

        if (tokenTaskDecode.getOrigin_button().equals(R.id.finish_task_button)) {

            causes.setVisibility(View.VISIBLE);
            label_legal.setVisibility(View.VISIBLE);

            FragmentTransaction fileFragment = fragmentManager.beginTransaction();
            fileFragment.add(R.id.legal_fragment_container, new LegalFragment(), Constants.FRAGMENT_LEGAL);
            fileFragment.commit();
        }

        return view;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        if (taskToken.isEmpty()) {
            taskToken = activityListener.getToken();

            TaskListAdapter backAdapter = (TaskListAdapter) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_ADAPTER);
            TasksDecode taskDecode = (TasksDecode) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE);

            _ACTUAL_POSITION = taskDecode.getTask_position();
            _ACTUAL_COUNT = backAdapter.getItemCount() - 1;
        }

        TASK_FILES = activityListener.getTaskFiles();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityListener = (FragmentTaskListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " debe implementar TaskListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.picture_task_button:

                TaskListAdapter pictureAdapter = (TaskListAdapter) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_ADAPTER);
                Tasks pictureTask = (Tasks) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_CLASS);
                TasksDecode pictureDecode = (TasksDecode) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE);

                activityListener.taskActions(v, pictureAdapter, pictureTask, pictureDecode);

                break;
            case R.id.video_task_button:

                TaskListAdapter videoAdapter = (TaskListAdapter) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_ADAPTER);
                Tasks videoTask = (Tasks) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_CLASS);
                TasksDecode videoDecode = (TasksDecode) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE);

                activityListener.taskActions(v, videoAdapter, videoTask, videoDecode);

                break;*/
            case R.id.gallery_task_gallery:
                TaskListAdapter galleryAdapter = (TaskListAdapter) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_ADAPTER);
                Tasks galleryTask = (Tasks) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_CLASS);
                TasksDecode galleryDecode = (TasksDecode) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE);
                activityListener.taskActions(v, galleryAdapter, galleryTask, galleryDecode);
                break;
            case R.id.send_task_button:

                SpannableStringBuilder ssb = (SpannableStringBuilder) comment_task_window.getText();

                View tokenView = (View) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_VIEW);
                TaskListAdapter sendAdapter = (TaskListAdapter) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_ADAPTER);
                Tasks sendTask = (Tasks) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_CLASS);
                TasksDecode sendDecode = (TasksDecode) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE);

                sendDecode.setTask_comment(ssb.toString());
                sendDecode.setOrigin_button(tokenView.getId());

                AsyncSendTask wsSendTask = new AsyncSendTask(Constants.WS_KEY_UPDATE_TASK_WITH_PICTURE
                        , v, sendAdapter, sendTask, sendDecode);
                wsSendTask.execute();

                break;
            case R.id.close_window_button:

                taskToken = new HashMap<>();

                activityListener.closeActiveTaskFragment(v);
                activityListener.clearTaskToken();
                break;
            case R.id.back_task_button:

                TaskListAdapter backAdapter = (TaskListAdapter) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_ADAPTER);
                TasksDecode backDecode = (TasksDecode) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE);

                _ACTUAL_POSITION--;

                if (_ACTUAL_POSITION == _ACTUAL_COUNT) {
                    next_task_button.setEnabled(false);
                    next_task_button.setVisibility(View.INVISIBLE);
                } else {
                    next_task_button.setEnabled(true);
                    next_task_button.setVisibility(View.VISIBLE);
                }

                if (_ACTUAL_POSITION == 0) {
                    back_task_button.setEnabled(false);
                    back_task_button.setVisibility(View.INVISIBLE);
                } else {
                    back_task_button.setEnabled(true);
                    back_task_button.setVisibility(View.VISIBLE);
                }

                Tasks actualBackTask = backAdapter.getItemByPosition(_ACTUAL_POSITION);
                backDecode.setTask_position(_ACTUAL_POSITION);

                title_task_window.setText(actualBackTask.getTask_tittle());
                content_task_window.setText(actualBackTask.getTask_content());
                // setCountFiles();

                taskToken.put(Constants.TOKEN_KEY_ACCESS_TASK_CLASS, actualBackTask);
                taskToken.put(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE, backDecode);

                moveLegalFragment();
                break;
            case R.id.next_task_button:

                TaskListAdapter nextAdapter = (TaskListAdapter) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_ADAPTER);
                TasksDecode nextDecode = (TasksDecode) taskToken.get(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE);

                _ACTUAL_POSITION++;

                if (_ACTUAL_POSITION == _ACTUAL_COUNT) {
                    next_task_button.setEnabled(false);
                    next_task_button.setVisibility(View.INVISIBLE);
                } else {
                    next_task_button.setEnabled(true);
                    next_task_button.setVisibility(View.VISIBLE);
                }

                if (_ACTUAL_POSITION == 0) {
                    back_task_button.setEnabled(false);
                    back_task_button.setVisibility(View.INVISIBLE);
                } else {
                    back_task_button.setEnabled(true);
                    back_task_button.setVisibility(View.VISIBLE);
                }

                Tasks actualNextTask = nextAdapter.getItemByPosition(_ACTUAL_POSITION);
                nextDecode.setTask_position(_ACTUAL_POSITION);

                title_task_window.setText(actualNextTask.getTask_tittle());
                content_task_window.setText(actualNextTask.getTask_content());
                // setCountFiles();

                taskToken.put(Constants.TOKEN_KEY_ACCESS_TASK_CLASS, actualNextTask);
                taskToken.put(Constants.TOKEN_KEY_ACCESS_TASK_CLASS_DECODE, nextDecode);

                moveLegalFragment();


                break;
            case R.id.switch_causes:

                boolean checked = ((Switch) v).isChecked();

                if (checked) {

                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack so the user can navigate back
                    transaction.replace(R.id.legal_fragment_container, new LegalDescriptionFragment());
                    //transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();

                } else {

                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack so the user can navigate back
                    transaction.replace(R.id.legal_fragment_container, new LegalFragment());
                    //transaction.addToBackStack(null);

                    // Commit the transaction
                    transaction.commit();

                }

                break;
            default:
                break;
        }
    }

    public static LegalManager getLegalInformation(LegalManager legalManager) {

        Integer checkCauses = (causes.isChecked()) ? Constants.ACTIVE : Constants.INACTIVE;

        legalManager.setCauses(checkCauses);

        return  legalManager;
    }

    private void clearActualFiles() {
        if (TASK_FILES.containsKey(_ACTUAL_POSITION)) {

            FilesManager filesManager = TASK_FILES.get(_ACTUAL_POSITION);
            filesManager.setFilesPicture(new ArrayList<Uri>());
            filesManager.setFilesVideo(new ArrayList<Uri>());
            TASK_FILES.put(_ACTUAL_POSITION, filesManager);

            /*
            number_photos.setText(String.valueOf(filesManager.getFilesPicture().size()));
            number_videos.setText(String.valueOf(filesManager.getFilesVideo().size()));
            */
        }
    }

    private void moveLegalFragment() {

        TasksDecode tokenTaskDecode = (TasksDecode) taskToken.get(4L);

        if (tokenTaskDecode.getOrigin_button() == R.id.finish_task_button) {

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.legal_fragment_container, new LegalFragment());
            //transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();

            comment_task_window.setText("");
            causes.setChecked(false);

        }
    }


    private class AsyncSendTask extends AsyncTask<Void, Void, Boolean> {

        private SoapPrimitive soapPrimitive;

        private Integer webServiceOperation;
        private View webServiceView;
        private TaskListAdapter webServiceAdapter;
        private Tasks webServiceTask;
        private TasksDecode webServiceTaskDecode;

        private String textError;

        private AsyncSendTask(Integer wsOperation, View wsView, TaskListAdapter wsAdapter, Tasks wsTask
                , TasksDecode wsServiceTaskDecode) {
            webServiceOperation = wsOperation;
            webServiceView = wsView;
            webServiceAdapter = wsAdapter;
            webServiceTask = wsTask;
            webServiceTaskDecode = wsServiceTaskDecode;
            textError = "";
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage(getString(R.string.default_attaching_img));
            pDialog.setTitle("Adjuntanto archivos");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            try {
                switch (webServiceOperation) {
                    case Constants.WS_KEY_UPDATE_TASK_WITH_PICTURE:
                        //webServiceTaskDecode = attachFiles(webServiceTaskDecode);
                        //validOperation = (webServiceTaskDecode != null);
                        validOperation = true;
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

            pDialog.dismiss();
            if (success) {

                //taskToken = new HashMap<>();
                activityListener.taskActions(webServiceView, webServiceAdapter
                        , webServiceTask, webServiceTaskDecode);

            } else {
                String tempText = (textError.isEmpty() ? "Se excedio el limite de imagenes" : textError);
                Toast.makeText(getContext(), tempText, Toast.LENGTH_LONG).show();

                clearActualFiles();
            }
        }
    }
}
