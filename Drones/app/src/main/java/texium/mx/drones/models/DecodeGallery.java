package texium.mx.drones.models;

/**
 * Created by texiumuser on 17/08/2016.
 */
public class DecodeGallery {

    private PhotoGallery photoGallery;
    private Integer idView;
    private Integer position;

    public PhotoGallery getPhotoGallery() {
        return photoGallery;
    }

    public void setPhotoGallery(PhotoGallery photoGallery) {
        this.photoGallery = photoGallery;
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
