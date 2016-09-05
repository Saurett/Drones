package texium.mx.drones.models;

/**
 * Created by texiumuser on 17/08/2016.
 */
public class DecodeGallery {

    private TaskGallery taskGallery;
    private Integer idView;
    private Integer position;

    public TaskGallery getTaskGallery() {
        return taskGallery;
    }

    public void setTaskGallery(TaskGallery taskGallery) {
        this.taskGallery = taskGallery;
    }

    public Integer getIdView() {
        return idView;
    }

    public void setIdView(Integer idView) {
        this.idView = idView;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
