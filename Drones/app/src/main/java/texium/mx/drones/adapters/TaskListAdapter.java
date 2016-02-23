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

import texium.mx.drones.NavigationDrawerActivity;
import texium.mx.drones.R;
import texium.mx.drones.models.Tasks;

/**
 * Created by saurett on 14/01/2016.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder>{


    View.OnClickListener onClickListener;
    List<Tasks> tasks_list = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView task_title;
        TextView task_content;
        TextView task_priority;
        TextView task_begin_date;
        TextView task_end_date;
        Button agree_task_button;
        Button decline_task_button;

        public ViewHolder(View itemView) {
            super(itemView);

            task_title = (TextView) itemView.findViewById(R.id.task_title);
            task_content = (TextView) itemView.findViewById(R.id.task_content);
            task_priority = (TextView) itemView.findViewById(R.id.task_priority);
            task_begin_date = (TextView) itemView.findViewById(R.id.task_begin_date);
            task_end_date = (TextView) itemView.findViewById(R.id.task_end_date);
            agree_task_button = (Button) itemView.findViewById(R.id.agree_task_button);
            decline_task_button = (Button) itemView.findViewById(R.id.decline_task_button);

        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void addAll(List<Tasks> tasks_list) { this.tasks_list.addAll(tasks_list);}

    public void remove(int position) { this.tasks_list.remove(position);}

    public void removeAll(List<Tasks> tasks_list) { this.tasks_list.removeAll(tasks_list);}

    public void clearAll() { this.tasks_list.clear();}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_list, parent, false);
        ViewHolder vh = new ViewHolder(view);
        vh.agree_task_button.setOnClickListener(onClickListener);
        vh.agree_task_button.setVisibility(View.VISIBLE);
        vh.decline_task_button.setVisibility(View.VISIBLE);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Tasks task = tasks_list.get(position);
        holder.task_title.setText(task.getTask_tittle());
        holder.task_content.setText(task.getTask_content());
        holder.task_priority.setText(task.getTask_priority());
        holder.task_begin_date.setText(task.getTask_begin_date());
        holder.task_end_date.setText(task.getTask_end_date());

        holder.agree_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO ABRIR UN NUEVO FRAGMETN PARA ACEPTAR LA TAREA CON UNA DESCRIPCION
                //TODO COMO EJEMPLO ABRIR CUALQUIER FRAGMENT MENOS EL ACTUAL Y CERRAR EL ACTUAL
                //TODO PASAR LOS DOS ON CLICK LISTENER A UN METODO IMPLEMENTADO, NO SALE POR QUE NO PUEDO OBTENER EL POSITION FUERA DE AQUI

                Snackbar.make(v, "Tarea aceptada :" + task.getTask_tittle(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();


            }
        });

        holder.decline_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO ACEPTAR LA TAREA; ENVIAR EL CAMBIO AL WEB SERVICE DE FRED
                Snackbar.make(v, "Tarea pospuesta # " + position, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks_list == null ? 0 : tasks_list.size();
    }

}
