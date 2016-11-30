package texium.mx.drones.adapters;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.fragments.CloseTasksFragment;
import texium.mx.drones.fragments.NewsTasksFragment;
import texium.mx.drones.fragments.PendingTasksFragment;
import texium.mx.drones.fragments.ProgressTasksFragment;
import texium.mx.drones.fragments.RevisionTasksFragment;
import texium.mx.drones.models.Tasks;
import texium.mx.drones.models.TasksDecode;
import texium.mx.drones.utils.Constants;

/**
 * Created by saurett on 14/01/2016.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    /* En caso de llamar una actividad desde el adapter
    Context context = v.getContext();
    Intent intent = new Intent(context, MainActivity.class);
    context.startActivity(intent);
    */

    View.OnClickListener onClickListener;
    List<Tasks> tasks_list = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView task_title;
        TextView task_content;
        TextView task_priority;
        TextView task_begin_date;
        TextView task_end_date;
        TextView hidden_data;

        Button agree_task_button;
        Button decline_task_button;
        Button finish_task_button;
        Button gallery_task_button;
        Button task_location_button;

        public ViewHolder(View itemView) {
            super(itemView);

            task_title = (TextView) itemView.findViewById(R.id.task_title);
            task_content = (TextView) itemView.findViewById(R.id.task_content);
            task_priority = (TextView) itemView.findViewById(R.id.task_priority);
            task_begin_date = (TextView) itemView.findViewById(R.id.task_begin_date);
            task_end_date = (TextView) itemView.findViewById(R.id.task_end_date);
            hidden_data = (TextView) itemView.findViewById(R.id.hidden_data);
            agree_task_button = (Button) itemView.findViewById(R.id.agree_task_button);
            decline_task_button = (Button) itemView.findViewById(R.id.decline_task_button);
            finish_task_button = (Button) itemView.findViewById(R.id.finish_task_button);
            gallery_task_button = (Button) itemView.findViewById(R.id.gallery_task_button);
            task_location_button = (Button) itemView.findViewById(R.id.task_location_button);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public Tasks getItemByPosition(int position) {
        return tasks_list.get(position);
    }

    public void addAll(List<Tasks> tasks_list) {
        this.tasks_list.addAll(tasks_list);
    }

    public void remove(int position) {
        this.tasks_list.remove(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Tasks task = tasks_list.get(position);

        holder.task_title.setText(task.getTask_tittle());
        holder.task_content.setText(task.getTask_content());
        holder.task_priority.setText(Constants.MAP_STATUS_NAME.get(task.getTask_priority()));
        holder.task_begin_date.setText(task.getTask_begin_date());
        holder.task_end_date.setText(task.getTask_end_date());

        final TasksDecode taskDecode = new TasksDecode();

        taskDecode.setTask_status(task.getTask_status());
        taskDecode.setTask_position(position);

        buttonVisibilityControl(holder, taskDecode.getTask_status());

        holder.agree_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentJumper(v, task, taskDecode);
            }
        });

        holder.finish_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentJumper(v, task, taskDecode);
            }
        });


        holder.decline_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentJumper(v, task, taskDecode);
            }
        });

        holder.gallery_task_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fragmentJumper(v, task, taskDecode);
            }
        });

        holder.task_location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentJumper(view, task, taskDecode);
            }
        });
    }

    private void fragmentJumper(View v, Tasks task, TasksDecode _tasksDecode) {

        switch (_tasksDecode.getTask_status()) {
            case Constants.NEWS_TASK:
                NewsTasksFragment.fragmentJump(v, task, _tasksDecode);
                break;
            case Constants.PENDING_TASK:
                PendingTasksFragment.fragmentJump(v, task, _tasksDecode);
                break;
            case Constants.PROGRESS_TASK:
                ProgressTasksFragment.fragmentJump(v, task, _tasksDecode);
                break;
            case Constants.CLOSE_TASK:
                CloseTasksFragment.fragmentJump(v, task, _tasksDecode);
                break;
            case Constants.REVISION_TASK:
                RevisionTasksFragment.fragmentJump(v, task, _tasksDecode);
                break;
            default:
                Snackbar.make(v, "No action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
        }
    }

    private void buttonVisibilityControl(ViewHolder holder, int task_type) {
        switch (task_type) {
            case Constants.NEWS_TASK:
                holder.decline_task_button.setVisibility(View.VISIBLE);
                holder.finish_task_button.setVisibility(View.INVISIBLE);
                holder.agree_task_button.setVisibility(View.VISIBLE);
                break;
            case Constants.PROGRESS_TASK:
                holder.decline_task_button.setVisibility(View.VISIBLE);
                holder.finish_task_button.setVisibility(View.VISIBLE);
                holder.agree_task_button.setVisibility(View.INVISIBLE);
                break;
            case Constants.PENDING_TASK:
                holder.decline_task_button.setVisibility(View.INVISIBLE);
                holder.finish_task_button.setVisibility(View.INVISIBLE);
                holder.agree_task_button.setVisibility(View.VISIBLE);
                break;
            case Constants.REVISION_TASK:
                holder.decline_task_button.setVisibility(View.INVISIBLE);
                holder.finish_task_button.setVisibility(View.INVISIBLE);
                holder.agree_task_button.setVisibility(View.INVISIBLE);
                break;
            case Constants.CLOSE_TASK:
                holder.decline_task_button.setVisibility(View.INVISIBLE);
                holder.finish_task_button.setVisibility(View.INVISIBLE);
                holder.agree_task_button.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }


    @Override
    public int getItemCount() {
        return tasks_list == null ? 0 : tasks_list.size();
    }

    public int getScrollPosition(int idTask) {
        int position = 0;

        for (Tasks task : tasks_list) {
            if (task.getTask_id().equals(idTask)) {
                return position;
            }
            position++;
        }

        return position;
    }

}
