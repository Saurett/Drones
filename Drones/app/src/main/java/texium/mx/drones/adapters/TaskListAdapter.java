package texium.mx.drones.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.models.Tasks;

/**
 * Created by saurett on 14/01/2016.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    List<Tasks> tasks_list = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView task_title;
        TextView task_content;
        TextView task_priority;
        TextView task_begin_date;
        TextView task_end_date;

        public ViewHolder(View itemView) {
            super(itemView);

            task_title = (TextView) itemView.findViewById(R.id.task_title);
            task_content = (TextView) itemView.findViewById(R.id.task_content);
            task_priority = (TextView) itemView.findViewById(R.id.task_priority);
            task_begin_date = (TextView) itemView.findViewById(R.id.task_begin_date);
            task_end_date = (TextView) itemView.findViewById(R.id.task_end_date);
        }
    }

    public void addAll(List<Tasks> tasks_list) { this.tasks_list.addAll(tasks_list);}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tasks task = tasks_list.get(position);
        holder.task_title.setText(task.getTask_tittle());
        holder.task_content.setText(task.getTask_content());
        holder.task_priority.setText(task.getTask_priority());
        holder.task_begin_date.setText(task.getTask_begin_date());
        holder.task_end_date.setText(task.getTask_end_date());
    }

    @Override
    public int getItemCount() {
        return tasks_list == null ? 0 : tasks_list.size();
    }
}
