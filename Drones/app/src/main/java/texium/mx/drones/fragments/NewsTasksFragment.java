package texium.mx.drones.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.adapters.TaskListAdapter;
import texium.mx.drones.adapters.TaskListTitleAdapter;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.TasksTitle;


public class NewsTasksFragment extends Fragment {

    public interface NewTaskListener {
        void closeNewTaskFragment();
    }

    NewTaskListener activityListener;
    static List<Tasks> newsTask;
    static List<TasksTitle> newsTaskTitle;

    static {
        newsTask = new ArrayList<>();
        newsTask.add(new Tasks("Patrulla de inspección de zonas verdes en la polvora.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Media","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs"));
        newsTask.add(new Tasks("Enviar Dron para reconocimiento aéreo del Bosque de Chapultepec.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Baja","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs"));
        newsTask.add(new Tasks("Patrulla de inspección de zonas verdes en Churubusco.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Alta","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs"));
        newsTask.add(new Tasks("Patrulla de inspección de zonas verdes en Churubusco.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Media","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs"));
        newsTask.add(new Tasks("Enviar Dron para reconocimiento aéreo del Bosque de Chapultepec.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Media","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs"));
        newsTask.add(new Tasks("Patrulla de inspección de zonas verdes en Churubusco y Patrulla de inspección de zonas verdes en Churubusco v2.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Baja","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs"));
        newsTask.add(new Tasks("Patrulla de inspección de zonas verdes en Churubusco.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Baja","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs"));
        newsTask.add(new Tasks("Patrulla de inspección de zonas verdes en Churubusco.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Alta","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs"));
        newsTask.add(new Tasks("Enviar Dron para reconocimiento aéreo del Bosque de Chapultepec.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Alta","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs"));
        newsTask.add(new Tasks("Patrulla de inspección de zonas verdes en Churubusco.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Media","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs"));
    }

    static {
        newsTaskTitle = new ArrayList<>();
        newsTaskTitle.add(new TasksTitle("RESUMEN DE ÚLTIMAS CONEXIONES","CUADRILLA"));
    }

    RecyclerView tasks_list, tasks_list_tittle;

    TaskListAdapter task_list_adapter;
    TaskListTitleAdapter task_list_title_adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_tasks, container, false);

        tasks_list_tittle = (RecyclerView) view.findViewById(R.id.news_taks_list_title);
        tasks_list = (RecyclerView) view.findViewById(R.id.news_taks_list);


        task_list_adapter = new TaskListAdapter();
        task_list_title_adapter = new TaskListTitleAdapter();

        task_list_title_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityListener.closeNewTaskFragment();
            }
        });

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
        //Aqui cargar la cosas de fred

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityListener = (NewTaskListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " debe implementar NewTaskListener");
        }
    }
}
