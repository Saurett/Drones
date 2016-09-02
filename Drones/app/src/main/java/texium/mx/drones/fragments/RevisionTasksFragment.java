package texium.mx.drones.fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.adapters.TaskListAdapter;
import texium.mx.drones.adapters.TaskListTitleAdapter;
import texium.mx.drones.fragments.inetrface.FragmentTaskListener;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.TasksDecode;
import texium.mx.drones.models.TasksTitle;
import texium.mx.drones.models.Users;
import texium.mx.drones.services.SoapServices;
import texium.mx.drones.utils.Constants;

@Deprecated
public class RevisionTasksFragment extends Fragment implements View.OnClickListener {

    private SoapObject soapObject;
    private static Users SESSION_DATA;

    static FragmentTaskListener activityListener;
    static List<Tasks> revisionTask;
    static List<TasksTitle> revisionTaskTitle;

    /*
    static {
        revisionTask = new ArrayList<>();
        revisionTask.add(new Tasks("Patrulla de inspección de zonas verdes en el estadio azteca.", "Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.", "Media", "12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:7,id:1}"));
        revisionTask.add(new Tasks("Enviar Dron para reconocimiento aéreo del Bosque de Chapultepec.", "Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.", "Baja", "12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:7,id:1}"));
        revisionTask.add(new Tasks("Patrulla de inspección de zonas verdes en Churubusco.", "Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.", "Alta", "12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:7,id:1}"));
        revisionTask.add(new Tasks("Patrulla de inspección de zonas verdes en Churubusco.", "Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.", "Media", "12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:7,id:1}"));
        revisionTask.add(new Tasks("Enviar Dron para reconocimiento aéreo del Bosque de Chapultepec.", "Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.", "Media", "12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:7,id:1}"));
    }*/

    static {
        revisionTaskTitle = new ArrayList<>();
        revisionTaskTitle.add(new TasksTitle("TAREAS EN REVISIÓN", "CUADRILLA"));
    }

    RecyclerView tasks_list, tasks_list_tittle;

    static TaskListAdapter task_list_adapter;
    TaskListTitleAdapter task_list_title_adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_revision_tasks,container,false);

        tasks_list_tittle = (RecyclerView) view.findViewById(R.id.revision_task_list_title);
        tasks_list = (RecyclerView) view.findViewById(R.id.revision_task_list);

        task_list_adapter = new TaskListAdapter();
        task_list_title_adapter = new TaskListTitleAdapter();

        task_list_title_adapter.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        SESSION_DATA = (Users) getActivity().getIntent().getExtras().getSerializable(Constants.ACTIVITY_EXTRA_PARAMS_LOGIN);

        AsyncCallWS wsTaskList = new AsyncCallWS(Constants.WS_KEY_TASK_SERVICE_REVISION,Integer.valueOf(SESSION_DATA.getIdTeam().toString()),Constants.REVISION_TASK);
        wsTaskList.execute();

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
            case R.id.task_title_close_button:
                activityListener.closeActiveTaskFragment(v);
                break;
            default:
                break;
        }
    }

    public static void fragmentJump(View view,Tasks task,TasksDecode tasksDecode) {
        activityListener.taskActions(view, task_list_adapter, task, tasksDecode);
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private Integer webServiceOperation;
        private Integer idTeam;
        private Integer idStatus;

        private AsyncCallWS(Integer wsOperation,Integer wsIdTeam, Integer wsIdStatus) {
            webServiceOperation = wsOperation;
            idTeam = wsIdTeam;
            idStatus = wsIdStatus;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            try{
                switch (webServiceOperation) {
                    case Constants.WS_KEY_TASK_SERVICE_REVISION:

                        soapObject = SoapServices.getServerAllTasks(getContext(), idTeam, idStatus);
                        validOperation = (soapObject.getPropertyCount() > 0);

                        break;
                }
            } catch (Exception e) {
                e.getMessage();
                validOperation = false;
            }

            return validOperation;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if(success) {

                revisionTask = new ArrayList<>();
                for (int i = 0; i < soapObject.getPropertyCount(); i ++) {
                    Tasks t = new Tasks();

                    SoapObject soTemp = (SoapObject) soapObject.getProperty(i);
                    SoapObject soLocation = (SoapObject) soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_LOCATION);

                    t.setTask_tittle(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_TITTLE).toString());
                    t.setTask_id(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_ID).toString()));
                    t.setTask_content(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_CONTENT).toString());
                    t.setTask_latitude(Double.valueOf(soLocation.getProperty(Constants.SOAP_OBJECT_KEY_TASK_LATITUDE).toString()));
                    t.setTask_longitude(Double.valueOf(soLocation.getProperty(Constants.SOAP_OBJECT_KEY_TASK_LATITUDE).toString()));
                    t.setTask_priority(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_PRIORITY).toString()));
                    t.setTask_begin_date(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_BEGIN_DATE).toString());
                    t.setTask_end_date(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_END_DATE).toString());
                    t.setTask_status(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_STATUS).toString()));
                    t.setTask_user_id(Integer.valueOf(soTemp.getProperty(Constants.SOAP_OBJECT_KEY_TASK_USER_ID).toString()));

                    revisionTask.add(t);
                }


                task_list_adapter.addAll(revisionTask);
                task_list_title_adapter.addAll(revisionTaskTitle);

                tasks_list.setAdapter(task_list_adapter);
                tasks_list_tittle.setAdapter(task_list_title_adapter);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                tasks_list.setLayoutManager(linearLayoutManager);

                LinearLayoutManager linearLayoutManagerTitle = new LinearLayoutManager(getContext());
                tasks_list_tittle.setLayoutManager(linearLayoutManagerTitle);
            } else {
            }
        }
    }

}
