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
    private List<String> encodePictureFiles;
    private List<String> encodeVideoFiles;

    public FilesManager() {
        filesPicture = new ArrayList<>();
        filesVideo = new ArrayList<>();
        encodePictureFiles = new ArrayList<>();
        encodeVideoFiles = new ArrayList<>();
    }

    public FilesManager(List<String> fmEncodePictureFiles, List<String> fmEncodeVideoFiles) {
        encodePictureFiles = fmEncodePictureFiles;
        encodeVideoFiles = fmEncodeVideoFiles;
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

    public List<String> getEncodePictureFiles() {
        return encodePictureFiles;
    }

    public void setEncodePictureFiles(List<String> encodePictureFiles) {
        this.encodePictureFiles = encodePictureFiles;
    }

    public List<String> getEncodeVideoFiles() {
        return encodeVideoFiles;
    }

    public void setEncodeVideoFiles(List<String> encodeVideoFiles) {
        this.encodeVideoFiles = encodeVideoFiles;
    }
}
