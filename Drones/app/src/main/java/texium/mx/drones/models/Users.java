package texium.mx.drones.models;

import java.io.Serializable;

/**
 * Created by texiumuser on 29/02/2016.
 */
public class Users implements Serializable {

    private Long idUser;
    private String userName;
    private Long idActor;
    private String actorName;
    private Long actorType;
    private String actorTypeName;
    private Long idTeam;
    private String teamName;
    private Double latitude;
    private Double longitude;
    private String lastTeamConexion;

    public Users() {

    }

    public Users(Long idTeam, Long idUser, String userName, Long idActor, String actorName, Long actorType, String actorTypeName, String teamName, Double latitude, Double longitude, String lastTeamConexion) {
        this.idTeam = idTeam;
        this.idUser = idUser;
        this.userName = userName;
        this.idActor = idActor;
        this.actorName = actorName;
        this.actorType = actorType;
        this.actorTypeName = actorTypeName;
        this.teamName = teamName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastTeamConexion = lastTeamConexion;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getIdActor() {
        return idActor;
    }

    public void setIdActor(Long idActor) {
        this.idActor = idActor;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public Long getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Long idTeam) {
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

    public Long getActorType() {
        return actorType;
    }

    public void setActorType(Long actorType) {
        this.actorType = actorType;
    }

    public String getActorTypeName() {
        return actorTypeName;
    }

    public void setActorTypeName(String actorTypeName) {
        this.actorTypeName = actorTypeName;
    }

    public String getLastTeamConexion() {
        return lastTeamConexion;
    }

    public void setLastTeamConexion(String lastTeamConexion) {
        this.lastTeamConexion = lastTeamConexion;
    }
}
