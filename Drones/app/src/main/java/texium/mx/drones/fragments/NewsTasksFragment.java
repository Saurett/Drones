package texium.mx.drones.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import texium.mx.drones.models.Tasks;


public class NewsTasksFragment extends Fragment {

    static List<Tasks> newsTask;

    static {
        newsTask = new ArrayList<>();
        newsTask.add(new Tasks("Patrulla de inspección de zonas verdes en Churubusco.","Aenean interdum quis antes et consectetut.Donec faucibus luctus tempor.Sed suscipit a irci non cursus.","Media","12/Ene/2016 10:00 hrs", "13/Ene/2016 12:00 hrs"));
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

    RecyclerView tasks_list;
    TaskListAdapter task_list_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_tasks,container,false);

        tasks_list = (RecyclerView) view.findViewById(R.id.news_taks_list);
        task_list_adapter = new TaskListAdapter();
        task_list_adapter.addAll(newsTask);

        tasks_list.setAdapter(task_list_adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        tasks_list.setLayoutManager(linearLayoutManager);

        return view;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        //Aqui cargar la cosas de fred

    }
}
