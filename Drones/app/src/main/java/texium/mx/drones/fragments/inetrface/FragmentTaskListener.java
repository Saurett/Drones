package texium.mx.drones.fragments.inetrface;

import android.view.View;

import texium.mx.drones.adapters.TaskListAdapter;
import texium.mx.drones.models.Tasks;

/**
 * Created by saurett on 05/02/2016.
 */
public interface FragmentTaskListener {

    void closeActiveTaskFragment(View view);

    void agreeTask(View view, TaskListAdapter task_list_adapter, int position);

    void agreeTask2(View v, TaskListAdapter taskListAdapter, Tasks task,int position);
}
