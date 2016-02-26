package texium.mx.drones.models;

/**
 * Created by texiumuser on 25/02/2016.
 */
public class TasksDecode {

    private Integer task_position;
    private Integer task_type;
    private String task_coment;

    public TasksDecode() {

    }

    public String getTask_coment() {
        return task_coment;
    }

    public void setTask_coment(String task_coment) {
        this.task_coment = task_coment;
    }

    public Integer getTask_type() {
        return task_type;
    }

    public void setTask_type(Integer task_type) {
        this.task_type = task_type;
    }

    public Integer getTask_position() {
        return task_position;
    }

    public void setTask_position(Integer task_position) {
        this.task_position = task_position;
    }
}
