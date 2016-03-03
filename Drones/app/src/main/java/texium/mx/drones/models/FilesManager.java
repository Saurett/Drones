package texium.mx.drones.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by texiumuser on 03/03/2016.
 */
public class FilesManager {

    private List<File> filesPicture;
    private List<File> filesVideo;

    public FilesManager() {
        filesPicture = new ArrayList<>();
        filesVideo = new ArrayList<>();
    }

    public FilesManager(List<File> filesPicture, List<File> filesVideo) {
        this.filesPicture = filesPicture;
        this.filesVideo = filesVideo;
    }

    public List<File> getFilesPicture() {
        return filesPicture;
    }

    public void setFilesPicture(List<File> filesPicture) {
        this.filesPicture = filesPicture;
    }

    public List<File> getFilesVideo() {
        return filesVideo;
    }

    public void setFilesVideo(List<File> filesVideo) {
        this.filesVideo = filesVideo;
    }
}
