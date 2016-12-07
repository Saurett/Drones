package texium.mx.drones.models;

import java.io.Serializable;

/**
 * Created by saurett on 14/01/2016.
 */
public class MemberLocation implements Serializable {

    private Integer cve;
    private Integer id;
    private Integer userId;
    private Double latitude;
    private Double longitude;
    private String syncTime;
    private Integer serverSync;

    public MemberLocation() {
    }

    public MemberLocation(Integer userId, Integer serverSync) {
        this.userId = userId;
        this.serverSync = serverSync;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(String syncTime) {
        this.syncTime = syncTime;
    }

    public Integer getServerSync() {
        return serverSync;
    }

    public void setServerSync(Integer serverSync) {
        this.serverSync = serverSync;
    }
}
