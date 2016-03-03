package texium.mx.drones.models;

import java.io.Serializable;

/**
 * Created by texiumuser on 25/02/2016.
 */
public class TasksDecode {

    private Integer task_position;
    private Integer task_status;
    private String task_comment;
    private String task_latitude;
    private String task_longitude;
    private Integer task_user_id;
    private Integer task_update_to;
    private Integer task_team_id;

    public TasksDecode() {

    }

    public Integer getTask_update_to() {
        return task_update_to;
    }

    public void setTask_update_to(Integer task_update_to) {
        this.task_update_to = task_update_to;
    }

    public Integer getTask_position() {
        return task_position;
    }

    public void setTask_position(Integer task_position) {
        this.task_position = task_position;
    }

    public Integer getTask_status() {
        return task_status;
    }

    public void setTask_status(Integer task_status) {
        this.task_status = task_status;
    }

    public String getTask_comment() {
        return task_comment;
    }

    public void setTask_comment(String task_comment) {
        this.task_comment = task_comment;
    }

    public String getTask_longitude() {
        return task_longitude;
    }

    public void setTask_longitude(String task_longitude) {
        this.task_longitude = task_longitude;
    }

    public String getTask_latitude() {
        return task_latitude;
    }

    public void setTask_latitude(String task_latitude) {
        this.task_latitude = task_latitude;
    }

    public Integer getTask_user_id() {
        return task_user_id;
    }

    public void setTask_user_id(Integer task_user_id) {
        this.task_user_id = task_user_id;
    }

    public Integer getTask_team_id() {
        return task_team_id;
    }

    public void setTask_team_id(Integer task_team_id) {
        this.task_team_id = task_team_id;
    }
}