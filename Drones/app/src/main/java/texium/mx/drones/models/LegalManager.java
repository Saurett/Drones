package texium.mx.drones.models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import texium.mx.drones.utils.Constants;

/**
 * Created by texiumuser on 03/03/2016.
 */
public class LegalManager {

    private Integer causes;
    private String descriptionCauses;
    private String fileNumber;
    private Integer closure;
    private Integer closureTotal;
    private Integer closurePartial;

    public LegalManager() {
        this.causes = Constants.INACTIVE;
        this.closure = Constants.INACTIVE;
        this.closureTotal = Constants.INACTIVE;
        this.closurePartial = Constants.INACTIVE;
    }


    public Integer getCauses() {
        return causes;
    }

    public void setCauses(Integer causes) {
        this.causes = causes;
    }

    public String getDescriptionCauses() {
        return descriptionCauses;
    }

    public void setDescriptionCauses(String descriptionCauses) {
        this.descriptionCauses = descriptionCauses;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Integer getClosure() {
        return closure;
    }

    public void setClosure(Integer closure) {
        this.closure = closure;
    }

    public Integer getClosureTotal() {
        return closureTotal;
    }

    public void setClosureTotal(Integer closureTotal) {
        this.closureTotal = closureTotal;
    }

    public Integer getClosurePartial() {
        return closurePartial;
    }

    public void setClosurePartial(Integer closurePartial) {
        this.closurePartial = closurePartial;
    }
}
