package texium.mx.drones.fragments.inetrface;

import android.view.View;

import java.util.List;
import java.util.Map;

import texium.mx.drones.adapters.TaskListAdapter;
import texium.mx.drones.models.FilesManager;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.TasksDecode;

/**
 * Created by saurett on 05/02/2016.
 */
public interface FragmentTaskListener {

    void closeActiveTaskFragment(View view);

    void clearTaskToken();

    void taskActions(View v, TaskListAdapter taskListAdapter, Tasks task, TasksDecode tasksDecode);

    Map<Long,Object> getToken();

    Map<Integer,FilesManager> getTaskFiles();

    void addTasksListMarkers(List<Tasks> tasksList);
}
