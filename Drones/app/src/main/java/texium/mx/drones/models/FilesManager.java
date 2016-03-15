package texium.mx.drones.models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by texiumuser on 03/03/2016.
 */
public class FilesManager {

    private List<Uri> filesPicture;
    private List<Uri> filesVideo;

    public FilesManager() {
        filesPicture = new ArrayList<>();
        filesVideo = new ArrayList<>();
    }

    public List<Uri> getFilesPicture() {
        return filesPicture;
    }

    public void setFilesPicture(List<Uri> filesPicture) {
        this.filesPicture = filesPicture;
    }

    public List<Uri> getFilesVideo() {
        return filesVideo;
    }

    public void setFilesVideo(List<Uri> filesVideo) {
        this.filesVideo = filesVideo;
    }
}
