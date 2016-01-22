package texium.mx.drones.models;

/**
 * Created by saurett on 14/01/2016.
 */
public class TasksTitle {

    private String task_title_main;
    private String task_content_title;

    public TasksTitle(String task_title_main, String task_content_title){
        super();
        this.task_title_main = task_title_main;
        this.task_content_title = task_content_title;
    }

    public String getTask_tittle_main() { return task_title_main;}

    public void setTask_tittle(String task_tittle_main) { this.task_title_main = task_tittle_main; }

    public String getTask_content_title() { return task_content_title;}

    public void setTask_content_title(String task_content_title) { this.task_content_title = task_content_title; }
}
