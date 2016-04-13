package texium.mx.drones.models;

import java.util.ArrayList;
import java.util.List;

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
    private Integer origin_button;
    private List<String> sendImgFiles;
    private List<FilesManager> sendVideoFiles;

    public TasksDecode() {
        sendImgFiles = new ArrayList<>();
        sendVideoFiles = new ArrayList<>();
    }

    public TasksDecode(Integer tk_user_id, Integer tk_origin_button) {
        task_user_id = tk_user_id;
        origin_button = tk_origin_button;
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

    public Integer getOrigin_button() {
        return origin_button;
    }

    public void setOrigin_button(Integer origin_button) {
        this.origin_button = origin_button;
    }

    public List<String> getSendImgFiles() {
        return sendImgFiles;
    }

    public void setSendImgFiles(List<String> sendImgFiles) {
        this.sendImgFiles = sendImgFiles;
    }

    public List<FilesManager> getSendVideoFiles() {
        return sendVideoFiles;
    }

    public void setSendVideoFiles(List<FilesManager> sendVideoFiles) {
        this.sendVideoFiles = sendVideoFiles;
    }
}
