package texium.mx.drones.models;

/**
 * Created by saurett on 14/01/2016.
 */
public class Tasks {

    private String task_tittle;
    private String task_content;
    private String task_priority;
    private String task_begin_date;
    private String task_end_date;
    private String hidden_data;

    public Tasks(String task_tittle,String task_content, String task_priority, String task_begin_date, String task_end_date, String hidden_data){
        super();
        this.task_tittle = task_tittle;
        this.task_content = task_content;
        this.task_priority = task_priority;
        this.task_begin_date = task_begin_date;
        this.task_end_date = task_end_date;
        this.hidden_data = hidden_data;
    }

    public String getTask_tittle() { return task_tittle;}

    public void setTask_tittle(String task_tittle) { this.task_tittle = task_tittle; }

    public String getTask_content() { return task_content;}

    public void setTask_content(String task_content) { this.task_content = task_content; }

    public String getTask_priority() { return task_priority;}

    public void setTask_priority(String task_priority) { this.task_priority = task_priority; }

    public String getTask_begin_date() { return task_begin_date;}

    public void setTask_begin_date(String begin_date) { this.task_begin_date = begin_date; }

    public String getTask_end_date() { return task_end_date;}

    public void setTask_end_date(String task_end_date) { this.task_end_date = task_end_date; }

    public String getHidden_data() { return hidden_data;}

    public void setHidden_data(String hidden_data) { this.hidden_data = hidden_data;}
}
