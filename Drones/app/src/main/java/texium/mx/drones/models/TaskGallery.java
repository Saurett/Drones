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
}
