package texium.mx.drones.models;

import java.util.List;

/**
 * Created by saurett on 04/04/2016.
 */
public class SyncTaskServer {

    private Integer task_detail_cve;
    private Integer task_id;
    private Integer task_status;
    private Integer task_user_id;
    private Integer server_sync;
    private String task_comment;
    private List<String> sendFiles;

    public SyncTaskServer() {

    }

    public Integer getTask_detail_cve() {
        return task_detail_cve;
    }

    public void setTask_detail_cve(Integer task_detail_cve) {
        this.task_detail_cve = task_detail_cve;
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
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

    public Integer getServer_sync() {
        return server_sync;
    }

    public void setServer_sync(Integer server_sync) {
        this.server_sync = server_sync;
    }

    public String getTask_comment() {
        return task_comment;
    }

    public void setTask_comment(String task_comment) {
        this.task_comment = task_comment;
    }

    public List<String> getSendFiles() {
        return sendFiles;
    }

    public void setSendFiles(List<String> sendFiles) {
        this.sendFiles = sendFiles;
    }
}
