package texium.mx.drones.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import texium.mx.drones.R;
import texium.mx.drones.databases.BDTasksManagerQuery;
import texium.mx.drones.fragments.inetrface.FragmentTaskListener;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.TasksDecode;
import texium.mx.drones.models.Users;
import texium.mx.drones.services.SoapServices;
import texium.mx.drones.utils.Constants;


public class RestoreFragment extends Fragment implements View.OnClickListener {

    static FragmentTaskListener activityListener;

    private static Button agree_restore_button, cancel_restore_button, close_restore_window_button;
    private ImageView restore_window_icon;

    private static Users SESSION_DATA;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restore, container, false);

        close_restore_window_button = (Button) view.findViewById(R.id.close_restore_window_button);
        agree_restore_button = (Button) view.findViewById(R.id.agree_restore_button);
        cancel_restore_button = (Button) view.findViewById(R.id.cancel_restore_button);

        restore_window_icon = (ImageView) view.findViewById(R.id.restore_window_icon);

        agree_restore_button.setOnClickListener(this);
        close_restore_window_button.setOnClickListener(this);
        cancel_restore_button.setOnClickListener(this);


        TextView title = (TextView) view.findViewById(R.id.title_restore_window);
        TextView content = (TextView) view.findViewById(R.id.content_restore_window);

        title.setText(R.string.default_restore_window_title);
        content.setText(R.string.default_restore_window_content);

        /* SET ACTUAL ICON
        int actualIcon = (tokenTaskDecode.getOrigin_button() == R.id.finish_task_button)
                ? R.drawable.icon_progress : R.drawable.icon_pending;

        restore_window_icon.setBackground(getResources().getDrawable(actualIcon));
        */


        return view;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        activityListener.addTasksListMarkers(new ArrayList<Tasks>());
        SESSION_DATA = (Users) getActivity().getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_LOGIN);

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
            case R.id.close_restore_window_button: case R.id.cancel_restore_button:
                activityListener.closeActiveTaskFragment(v);
                break;
            case R.id.agree_restore_button:

                TasksDecode tasksDecode = new TasksDecode();
                tasksDecode.setTask_team_id(SESSION_DATA.getIdTeam());
                tasksDecode.setTask_status(Constants.ALL_TASK);

                AsyncRestoreDevice wsRestore = new AsyncRestoreDevice(Constants.WS_KEY_ALL_TASKS,v,tasksDecode);
                wsRestore.execute();

                break;
            default:
                break;
        }
    }

    private class AsyncRestoreDevice extends AsyncTask<Void, Void, Boolean> {

        private SoapObject soapObject;
        private View webServiceView;
        private Integer webServiceOperation;
        private TasksDecode webServiceTaskDecode;

        private String textError;

        private AsyncRestoreDevice(Integer wsOperation,View wsView,TasksDecode wsServiceTaskDecode) {
            webServiceView = wsView;
            webServiceOperation = wsOperation;
            webServiceTaskDecode = wsServiceTaskDecode;
            textError = "";
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage(getString(R.string.server_sync));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            try{
                switch (webServiceOperation) {
                    case Constants.WS_KEY_ALL_TASKS:
                        soapObject = SoapServices.getServerAllTasks(getContext(), webServiceTaskDecode.getTask_team_id()
                                , webServiceTaskDecode.getTask_status());
                        validOperation = (soapObject.getPropertyCount() > 0);

                        BDTasksManagerQuery.cleanTables(getContext());
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
            if(success) {

                if (soapObject.getPropertyCount() > 0)
                {
                    for (int i = 0; i < soapObject.getPropertyCount(); i++) {
                        Tasks t = new Tasks();

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
                        t.setTask_user_id(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_USER_ID).toString()));

                        try {
                            Tasks tempTask = BDTasksManagerQuery.getTaskById(getContext(), t);

                            if (tempTask.getTask_id() == null) BDTasksManagerQuery.addTask(getContext(), t);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("GeneralException", "Unknown error : " + e.getMessage());
                        }
                    }

                    Toast.makeText(getContext(),soapObject.getPropertyCount() + " Tareas restauradas"  , Toast.LENGTH_LONG).show();
                }

            } else {
                String tempText = (textError.isEmpty() ? "El procedimiento ha finalizado" : textError);
                Toast.makeText(getContext(), tempText, Toast.LENGTH_LONG).show();
            }

            activityListener.closeActiveTaskFragment(webServiceView);
        }
    }

}
