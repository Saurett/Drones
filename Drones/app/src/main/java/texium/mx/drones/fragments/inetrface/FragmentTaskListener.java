package texium.mx.drones.fragments.inetrface;

import android.view.View;

import texium.mx.drones.adapters.TaskListAdapter;

/**
 * Created by saurett on 05/02/2016.
 */
public interface FragmentTaskListener {

    void closeActiveTaskFragment(View view);

    void agreeTask(View view, TaskListAdapter task_list_adapter, int position);
}
