package texium.mx.drones.models;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by saurett on 14/01/2016.
 */
public class TaskGallery implements Serializable {

    private Integer cve;
    private Integer id;
    private Integer cve_Task_Detail;
    private String base_package;
    private Bitmap photo_bitmap;
    private Integer file_type;
    private String description;
    private Integer sync_type;
    private Integer syncStatus;
    private String localURI;
    private String member_name;
    private String member_job;
    private Integer idTask;
    private Integer idMember;

    public TaskGallery() {
    }

    public TaskGallery(String description, Integer sync_type) {
        this.description = description;
        this.sync_type = sync_type;
    }

    public Integer getCve() {
        return cve;
    }

    public void setCve(Integer cve) {
        this.cve = cve;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBase_package() {
        return base_package;
    }

    public void setBase_package(String base_package) {
        this.base_package = base_package;
    }

    public Integer getFile_type() {
        return file_type;
    }

    public void setFile_type(Integer file_type) {
        this.file_type = file_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSync_type() {
        return sync_type;
    }

    public void setSync_type(Integer sync_type) {
        this.sync_type = sync_type;
    }

    public Integer getCve_Task_Detail() {
        return cve_Task_Detail;
    }

    public void setCve_Task_Detail(Integer cve_Task_Detail) {
        this.cve_Task_Detail = cve_Task_Detail;
    }

    public Bitmap getPhoto_bitmap() {
        return photo_bitmap;
    }

    public void setPhoto_bitmap(Bitmap photo_bitmap) {
        this.photo_bitmap = photo_bitmap;
    }

    public String getLocalURI() {
        return localURI;
    }

    public void setLocalURI(String localURI) {
        this.localURI = localURI;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_job() {
        return member_job;
    }

    public void setMember_job(String member_job) {
        this.member_job = member_job;
    }

    public Integer getIdTask() {
        return idTask;
    }

    public void setIdTask(Integer idTask) {
        this.idTask = idTask;
    }

    public Integer getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(Integer syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Integer getIdMember() {
        return idMember;
    }

    public void setIdMember(Integer idMember) {
        this.idMember = idMember;
    }
}
