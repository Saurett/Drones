package texium.mx.drones.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.adapters.TaskListAdapter;
import texium.mx.drones.adapters.TaskListTitleAdapter;
import texium.mx.drones.databases.BDTasksManagerQuery;
import texium.mx.drones.fragments.inetrface.FragmentTaskListener;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.TasksDecode;
import texium.mx.drones.models.TasksTitle;
import texium.mx.drones.models.Users;
import texium.mx.drones.services.SoapServices;
import texium.mx.drones.utils.Constants;


public class NewsTasksFragment extends Fragment implements View.OnClickListener{

    private SoapObject soapObject;
    private static Users SESSION_DATA;

    static FragmentTaskListener activityListener;
    static List<Tasks> newsTask;
    static List<TasksTitle> newsTaskTitle;

    static {
        newsTaskTitle = new ArrayList<>();
        newsTaskTitle.add(new TasksTitle("TAREAS NUEVAS","CUADRILLA"));
    }

    RecyclerView tasks_list, tasks_list_tittle;

    static TaskListAdapter task_list_adapter;
    TaskListTitleAdapter task_list_title_adapter;

    private ProgressDialog pDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_news_tasks, container, false);

        tasks_list_tittle = (RecyclerView) view.findViewById(R.id.news_task_list_title);
        tasks_list = (RecyclerView) view.findViewById(R.id.news_task_list);


        task_list_adapter = new TaskListAdapter();
        task_list_title_adapter = new TaskListTitleAdapter();

        task_list_title_adapter.setOnClickListener(this);
        task_list_adapter.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        SESSION_DATA = (Users) getActivity().getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_LOGIN);

        AsyncCallWS wsTaskList = new AsyncCallWS(Constants.WS_KEY_TASK_SERVICE_NEWS,Integer.valueOf(SESSION_DATA.getIdTeam().toString()),Constants.NEWS_TASK);
        wsTaskList.execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityListener = (FragmentTaskListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " debe implementar ");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.task_title_close_button:
                activityListener.closeActiveTaskFragment(v);
                break;
            default:
                break;
        }
    }

    public static void fragmentJump(View view,Tasks task,TasksDecode tasksDecode) {
        activityListener.taskActions(view, task_list_adapter, task,tasksDecode);
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private Integer webServiceOperation;
        private Integer idTeam;
        private Integer idStatus;
        private List<Tasks> tempTaskList;
        private String textError;

        private AsyncCallWS(Integer wsOperation,Integer wsIdTeam, Integer wsIdStatus) {
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

            try{
                switch (webServiceOperation) {
                    case Constants.WS_KEY_TASK_SERVICE_NEWS:

                        soapObject = SoapServices.getServerAllTasks(getContext(), idTeam, idStatus);
                        validOperation = (soapObject.getPropertyCount() > 0);

                        if (!validOperation) {
                            Tasks t = new Tasks(idStatus);
                            tempTaskList = BDTasksManagerQuery.getListTaskByStatus(getContext(), t);
                            if (tempTaskList.size() > 0) validOperation = true;
                        }

                        break;
                }
            } catch (ConnectException e) {

                textError = e.getMessage();
                validOperation = false;

                Tasks t = new Tasks(idStatus);

                try {
                    tempTaskList = BDTasksManagerQuery.getListTaskByStatus(getContext(), t);
                    validOperation = (tempTaskList.size() > 0);
                    textError =  (tempTaskList.size() > 0) ? textError
                            : getString(R.string.default_empty_task_list);
                } catch (Exception ex) {
                    textError = ex.getMessage();

                    ex.printStackTrace();
                    Log.e("NewsTasksException: ", "Unknown error");
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
                newsTask = new ArrayList<>();
                pDialog.dismiss();
                if(success) {

                    if (tempTaskList.size() == 0) {

                        for (int i = 0; i < soapObject.getPropertyCount(); i ++) {
                            Tasks t = new Tasks();

                            SoapObject soTemp = (SoapObject) soapObject.getProperty(i);
                            SoapObject soLocation = (SoapObject) soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_LOCATION);

                            t.setTask_tittle(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_TITTLE).toString());
                            t.setTask_id(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_ID).toString()));
                            t.setTask_content(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_CONTENT).toString());
                            t.setTask_latitude(Double.valueOf(soLocation.getProperty(Constants.SOAP_OBJECT_KEY_TASK_LONGITUDE).toString()));
                            t.setTask_longitude(Double.valueOf(soLocation.getProperty(Constants.SOAP_OBJECT_KEY_TASK_LATITUDE).toString()));
                            t.setTask_priority(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_PRIORITY).toString()));
                            t.setTask_begin_date(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_BEGIN_DATE).toString());
                            t.setTask_end_date(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_END_DATE).toString());
                            t.setTask_status(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_STATUS).toString()));
                            t.setTask_user_id(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_USER_ID).toString()));

                            newsTask.add(t);

                            try {
                                Tasks tempTask = BDTasksManagerQuery.getTaskById(getContext(), t);

                                Integer tempTaskStatus = (tempTask.getTask_id() != null)
                                        ? tempTask.getTask_status() : Constants.INACTIVE;

                                switch (tempTaskStatus) {
                                    case Constants.INACTIVE:
                                        BDTasksManagerQuery.addTask(getContext(), t);
                                        break;
                                    case Constants.PENDING_TASK:
                                        case Constants.PROGRESS_TASK:
                                            case Constants.CLOSE_TASK:
                                                newsTask.remove(t);
                                        break;
                                    default:
                                        Log.i("NewsTasks","No remove task");
                                        break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("NewsTasksException: ", "Unknown error: " + e.getMessage());
                            }
                        }

                        Tasks t = new Tasks(idStatus);
                        tempTaskList = BDTasksManagerQuery.getListTaskByStatus(getContext(), t);

                        for (Tasks tempTask : tempTaskList) {
                            Boolean contain = false;

                            for (Tasks actualTask : newsTask) {
                                contain = (actualTask.getTask_id()
                                        == tempTask.getTask_id());
                                if (contain) break;
                            }

                            if (!contain) newsTask.add(tempTask);
                        }

                    } else newsTask.addAll(tempTaskList);

                    if (newsTask.size() > 0 ) {
                        task_list_adapter.addAll(newsTask);
                        task_list_title_adapter.addAll(newsTaskTitle);

                        tasks_list.setAdapter(task_list_adapter);
                        tasks_list_tittle.setAdapter(task_list_title_adapter);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        tasks_list.setLayoutManager(linearLayoutManager);

                        LinearLayoutManager linearLayoutManagerTitle = new LinearLayoutManager(getContext());
                        tasks_list_tittle.setLayoutManager(linearLayoutManagerTitle);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.default_empty_task_list), Toast.LENGTH_LONG).show();
                    }
                } else {
                    String tempText = (textError.isEmpty() ? getString(R.string.default_empty_task_list) : textError);
                    Toast.makeText(getActivity(), tempText, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            activityListener.addTasksListMarkers(newsTask);
        }
    }


}
