package texium.mx.drones.models;

import java.io.Serializable;

/**
 * Created by texiumuser on 29/02/2016.
 */
public class Users implements Serializable {

    private Integer cve_user;
    private Integer idUser;
    private String userName;
    private String password;
    private Integer idActor;
    private String actorName;
    private Integer actorType;
    private String actorTypeName;
    private Integer idTeam;
    private String teamName;
    private Double latitude;
    private Double longitude;
    private String lastTeamConnection;

    public Users() {

    }


    public Integer getCve_user() {
        return cve_user;
    }

    public void setCve_user(Integer cve_user) {
        this.cve_user = cve_user;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIdActor() {
        return idActor;
    }

    public void setIdActor(Integer idActor) {
        this.idActor = idActor;
    }

    public Integer getActorType() {
        return actorType;
    }

    public void setActorType(Integer actorType) {
        this.actorType = actorType;
    }

    public String getActorTypeName() {
        return actorTypeName;
    }

    public void setActorTypeName(String actorTypeName) {
        this.actorTypeName = actorTypeName;
    }

    public Integer getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Integer idTeam) {
        this.idTeam = idTeam;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
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

    public String getLastTeamConnection() {
        return lastTeamConnection;
    }

    public void setLastTeamConnection(String lastTeamConnection) {
        this.lastTeamConnection = lastTeamConnection;
    }
}
