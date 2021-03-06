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
    private List<Uri> filesPdf;
    private List<String> encodePictureFiles;
    private List<String> encodeVideoFiles;
    private String encodeSingleFile;
    private Integer totalFiles;
    private Integer actualFile;
    private String message;
    private String title;
    private List<TaskGallery> taskGalleries;

    public FilesManager() {
        filesPicture = new ArrayList<>();
        filesVideo = new ArrayList<>();
        encodePictureFiles = new ArrayList<>();
        encodeSingleFile = "";
    }

    public FilesManager(List<String> fmEncodePictureFiles) {
        encodePictureFiles = fmEncodePictureFiles;
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

    public String getEncodeSingleFile() {
        return encodeSingleFile;
    }

    public void setEncodeSingleFile(String encodeSingleFile) {
        this.encodeSingleFile = encodeSingleFile;
    }

    public void setTotalFiles(Integer totalFiles) {
        this.totalFiles = totalFiles;
    }

    public void setActualFile(Integer actualFile) {
        this.actualFile = actualFile;
    }

    public Integer getTotalFiles() {
        return totalFiles;
    }

    public Integer getActualFile() {
        return actualFile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Uri> getFilesPdf() {
        return filesPdf;
    }

    public void setFilesPdf(List<Uri> filesPdf) {
        this.filesPdf = filesPdf;
    }

    public List<TaskGallery> getTaskGalleries() {
        return taskGalleries;
    }

    public void setTaskGalleries(List<TaskGallery> taskGalleries) {
        this.taskGalleries = taskGalleries;
    }

    public List<String> getEncodeVideoFiles() {
        return encodeVideoFiles;
    }

    public void setEncodeVideoFiles(List<String> encodeVideoFiles) {
        this.encodeVideoFiles = encodeVideoFiles;
    }
}
