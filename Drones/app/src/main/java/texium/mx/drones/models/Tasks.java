package texium.mx.drones.models;

import java.io.Serializable;

/**
 * Created by saurett on 14/01/2016.
 */
public class Tasks implements Serializable {

    private Integer task_cve;
    private String task_tittle;
    private String task_content;
    private Integer task_priority;
    private String task_begin_date;
    private String task_end_date;
    private Integer task_id;
    private Double task_latitude;
    private Double task_longitude;
    private Integer task_status;
    private Integer task_user_id;


    private Integer idTeam;

    public Tasks() {

    }

    public Tasks(Integer tTaskStatus, Integer tIdTeam) {
        task_status = tTaskStatus;
        idTeam = tIdTeam;
    }

    public Integer getTask_cve() {
        return task_cve;
    }

    public void setTask_cve(Integer task_cve) {
        this.task_cve = task_cve;
    }

    public String getTask_tittle() {
        return task_tittle;
    }

    public void setTask_tittle(String task_tittle) {
        this.task_tittle = task_tittle;
    }

    public String getTask_content() {
        return task_content;
    }

    public void setTask_content(String task_content) {
        this.task_content = task_content;
    }

    public Integer getTask_priority() {
        return task_priority;
    }

    public void setTask_priority(Integer task_priority) {
        this.task_priority = task_priority;
    }

    public String getTask_begin_date() {
        return task_begin_date;
    }

    public void setTask_begin_date(String task_begin_date) {
        this.task_begin_date = task_begin_date;
    }

    public String getTask_end_date() {
        return task_end_date;
    }

    public void setTask_end_date(String task_end_date) {
        this.task_end_date = task_end_date;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public Double getTask_latitude() {
        return task_latitude;
    }

    public void setTask_latitude(Double task_latitude) {
        this.task_latitude = task_latitude;
    }

    public Double getTask_longitude() {
        return task_longitude;
    }

    public void setTask_longitude(Double task_longitude) {
        this.task_longitude = task_longitude;
    }

    public Integer getTask_status() {
        return task_status;
    }

    public void setTask_status(Integer task_status) {
        this.task_status = task_status;
    }

    public Integer getTask_user_id() {
        return task_user_id;
    }

    public void setTask_user_id(Integer task_user_id) {
        this.task_user_id = task_user_id;
    }

    public Integer getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Integer idTeam) {
        this.idTeam = idTeam;
    }
}
