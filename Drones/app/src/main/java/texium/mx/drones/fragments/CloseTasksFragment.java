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

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.adapters.TaskListAdapter;
import texium.mx.drones.adapters.TaskListTitleAdapter;
import texium.mx.drones.fragments.inetrface.FragmentTaskListener;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.TasksDecode;
import texium.mx.drones.models.TasksTitle;


public class CloseTasksFragment extends Fragment implements View.OnClickListener{

    static FragmentTaskListener activityListener;
    static List<Tasks> closeTask;
    static List<TasksTitle> closeTaskTitle;

    static {
        closeTask = new ArrayList<>();
        closeTask.add(new Tasks("Patrulla de inspección de zonas verdes en la polvora.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Media","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:6,id:1}"));
        closeTask.add(new Tasks("Enviar Dron para reconocimiento aéreo del Bosque de Chapultepec.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Baja","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:6,id:1}"));
    }

    static {
        closeTaskTitle = new ArrayList<>();
        closeTaskTitle.add(new TasksTitle("TAREAS CERRADAS","CUADRILLA"));
    }

    RecyclerView tasks_list, tasks_list_tittle;

    static TaskListAdapter task_list_adapter;
    TaskListTitleAdapter task_list_title_adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_close_tasks,container,false);

        tasks_list_tittle = (RecyclerView) view.findViewById(R.id.close_taks_list_title);
        tasks_list = (RecyclerView) view.findViewById(R.id.close_taks_list);

        task_list_adapter = new TaskListAdapter();
        task_list_title_adapter = new TaskListTitleAdapter();

        task_list_title_adapter.setOnClickListener(this);

        task_list_adapter.addAll(closeTask);
        task_list_title_adapter.addAll(closeTaskTitle);

        tasks_list.setAdapter(task_list_adapter);
        tasks_list_tittle.setAdapter(task_list_title_adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        tasks_list.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManagerTitle = new LinearLayoutManager(getContext());
        tasks_list_tittle.setLayoutManager(linearLayoutManagerTitle);

        return view;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        //Aqui cargar la cosas de fred

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
        activityListener.taskActions(view, task_list_adapter, task,tasksDecode);
    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Boolean> {

        private Integer webServiceOperation;

        private AsyncCallWS(Integer wsOperation) {
            webServiceOperation = wsOperation;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean validOperation = false;

            switch (webServiceOperation) {
                default:

                    break;
            }

            return validOperation;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if(success) {

            } else {

            }
        }
    }
}
