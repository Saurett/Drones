package texium.mx.drones.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.adapters.TaskListAdapter;
import texium.mx.drones.adapters.TaskListTitleAdapter;
import texium.mx.drones.fragments.inetrface.FragmentTaskListener;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.TasksTitle;


public class NewsTasksFragment extends Fragment implements View.OnClickListener{

    static FragmentTaskListener activityListener;
    static List<Tasks> newsTask;
    static List<TasksTitle> newsTaskTitle;

    static {
        newsTask = new ArrayList<>();
        newsTask.add(new Tasks("0-Patrulla de inspección de zonas verdes en la polvora.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Media","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:3,id:1}"));
        newsTask.add(new Tasks("1-Enviar Dron para reconocimiento aéreo del Bosque de Chapultepec.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Baja","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:3,id:1}"));
        newsTask.add(new Tasks("2-Patrulla de inspección de zonas verdes en Churubusco.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Alta","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:3,id:1}"));
        newsTask.add(new Tasks("3-Patrulla de inspección de zonas verdes en Churubusco.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Media","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:3,id:1}"));
        newsTask.add(new Tasks("4-Enviar Dron para reconocimiento aéreo del Bosque de Chapultepec.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Media","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:3,id:1}"));
        newsTask.add(new Tasks("5-Patrulla de inspección de zonas verdes en Churubusco y Patrulla de inspección de zonas verdes en Churubusco v2.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Baja","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:3,id:1}"));
        newsTask.add(new Tasks("6-Patrulla de inspección de zonas verdes en Churubusco.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Baja","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:3,id:1}"));
        newsTask.add(new Tasks("7-Patrulla de inspección de zonas verdes en Churubusco.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Alta","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:3,id:1}"));
        newsTask.add(new Tasks("8-Enviar Dron para reconocimiento aéreo del Bosque de Chapultepec.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Alta","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:3,id:1}"));
        newsTask.add(new Tasks("9-Patrulla de inspección de zonas verdes en Churubusco.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Media","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs","{task_type:3,id:1}"));
    }

    static {
        newsTaskTitle = new ArrayList<>();
        newsTaskTitle.add(new TasksTitle("TAREAS NUEVAS","CUADRILLA"));
    }

    RecyclerView tasks_list, tasks_list_tittle;

    static TaskListAdapter task_list_adapter;
    TaskListTitleAdapter task_list_title_adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_news_tasks, container, false);

        tasks_list_tittle = (RecyclerView) view.findViewById(R.id.news_taks_list_title);
        tasks_list = (RecyclerView) view.findViewById(R.id.news_taks_list);


        task_list_adapter = new TaskListAdapter();
        task_list_title_adapter = new TaskListTitleAdapter();

        task_list_title_adapter.setOnClickListener(this);
        task_list_adapter.setOnClickListener(this);

        task_list_adapter.addAll(newsTask);
        task_list_title_adapter.addAll(newsTaskTitle);

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

        //TODO AQUI VA LO DE FRED



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
            case R.id.agree_task_button:
                //TODO NO SE COMO OBTENER POR AQUI LA POSICION
                activityListener.agreeTask(v, task_list_adapter, 1);
            default:
                break;
        }
    }

    public static void fragmentJump(View view,Tasks task, int position) {
        activityListener.agreeTask2(view, task_list_adapter, task, position);
    }


}
