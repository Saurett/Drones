package texium.mx.drones.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import texium.mx.drones.MainActivity;
import texium.mx.drones.NavigationDrawerActivity;
import texium.mx.drones.R;
import texium.mx.drones.fragments.CloseTasksFragment;
import texium.mx.drones.fragments.NewsTasksFragment;
import texium.mx.drones.fragments.PendingTasksFragment;
import texium.mx.drones.fragments.ProgressTasksFragment;
import texium.mx.drones.fragments.RevisionTasksFragment;
import texium.mx.drones.models.Tasks;

/**
 * Created by saurett on 14/01/2016.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder>{

    /*
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
        holder.hidden_data.setText(task.getHidden_data());

        String data = (String) holder.hidden_data.getText();
        int task_type = 0;

        //TODO MOVE A STATIC CLASS JSON
        try {
            JSONObject jObject = new JSONObject(data);
            task_type = jObject.getInt("task_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //TODO MOVE TO A PRIVATE METOD
        final Map<Long,Object> data_keys = new HashMap<>();

        data_keys.put(1L,task_type);

        //BUTTON CONTROLS
        switch (task_type) {
            case 4:
                holder.decline_task_button.setVisibility(View.INVISIBLE);
                holder.agree_task_button.setVisibility(View.INVISIBLE);
                holder.finish_task_button.setVisibility(View.VISIBLE);
                break;
            case 5:
                holder.decline_task_button.setVisibility(View.INVISIBLE);
                holder.agree_task_button.setVisibility(View.VISIBLE);
                break;
            case 6:
                holder.decline_task_button.setVisibility(View.INVISIBLE);
                holder.agree_task_button.setVisibility(View.INVISIBLE);
                holder.finish_task_button.setVisibility(View.INVISIBLE);
                break;
            case 7:
                holder.decline_task_button.setVisibility(View.INVISIBLE);
                holder.agree_task_button.setVisibility(View.INVISIBLE);
                break;
            default:

                break;
        }

        holder.agree_task_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO ABRIR UN NUEVO FRAGMETN PARA ACEPTAR LA TAREA CON UNA DESCRIPCION
                //TODO COMO EJEMPLO ABRIR CUALQUIER FRAGMENT MENOS EL ACTUAL Y CERRAR EL ACTUAL
                //TODO PASAR LOS DOS ON CLICK LISTENER A UN METODO IMPLEMENTADO, NO SALE POR QUE NO PUEDO OBTENER EL POSITION FUERA DE AQUI
                Integer task_type = (Integer) data_keys.get(1L);

                switch (task_type) {
                    case 3:
                        NewsTasksFragment.fragmentJump(v, task, position);
                        break;
                    case 4:
                        ProgressTasksFragment.fragmentJump(v, task, position);
                        break;
                    case 5:
                        PendingTasksFragment.fragmentJump(v,task,position);
                        break;
                    case 6:
                        CloseTasksFragment.fragmentJump(v,task,position);
                        break;
                    case 7:
                        RevisionTasksFragment.fragmentJump(v,task,position);
                        break;
                    default:
                        Snackbar.make(v, "No action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        break;
                }
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
