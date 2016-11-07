package texium.mx.drones.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.R;
import texium.mx.drones.models.TasksTitle;

/**
 * Created by saurett on 14/01/2016.
 */
public class TaskListTitleAdapter extends RecyclerView.Adapter<TaskListTitleAdapter.ViewHolder> {

    View.OnClickListener onClickListener;
    List<TasksTitle> tasks_list_title = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView task_title_main;
        TextView task_content_title;
        Button task_title_close_button;

        public ViewHolder(View itemView) {
            super(itemView);

            task_title_main = (TextView) itemView.findViewById(R.id.task_title_main);
            task_content_title = (TextView) itemView.findViewById(R.id.task_content_title);
            task_title_close_button = (Button) itemView.findViewById(R.id.task_title_close_button);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void addAll(List<TasksTitle> tasks_list_title) { this.tasks_list_title.addAll(tasks_list_title);}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_list_title,parent,false);
        ViewHolder vh = new ViewHolder(view);
        vh.task_title_close_button.setOnClickListener(onClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TasksTitle task_title = tasks_list_title.get(position);
        holder.task_title_main.setText(task_title.getTask_tittle_main());
        holder.task_content_title.setText(task_title.getTask_content_title());
    }

    @Override
    public int getItemCount() {
        return tasks_list_title == null ? 0 : tasks_list_title.size();
    }
}
