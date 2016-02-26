package texium.mx.drones.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.adapters.TaskListAdapter;
import texium.mx.drones.adapters.TaskListTitleAdapter;
import texium.mx.drones.fragments.inetrface.FragmentTaskListener;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.TasksDecode;
import texium.mx.drones.models.TasksTitle;


public class FinishTasksFragment extends Fragment implements View.OnClickListener {

    static FragmentTaskListener activityListener;

    private Button send_task_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_finish_tasks,container,false);
        send_task_button = (Button) view.findViewById(R.id.send_task_button);

        send_task_button.setOnClickListener(this);

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
            case R.id.send_task_button:
                activityListener.closeActiveTaskFragment(v);
                break;
            default:
                break;
        }
    }

    /*
    public static void fragmentJump(View view,Tasks task,TasksDecode tasksDecode) {
        activityListener.taskActions(view, task_list_adapter, task, tasksDecode);
    }
    */
}
