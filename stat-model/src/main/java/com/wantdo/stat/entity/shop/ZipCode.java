package com.wantdo.stat.entity.shop;

import com.wantdo.stat.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ Date : 16/2/24
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Entity
@Table(name = "zip_code")
public class ZipCode extends IdEntity {

    private String zip;
    private String placeName;
    private String state;
    private String stateAbbreviation;
    private String stateCn;
    private String country;
    private String latitude;
    private String longitude;
    private Date created;
    private Date modified;

    public ZipCode(){}

    public ZipCode(Long id){
        this.id = id;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public String getStateCn() {
        return stateCn;
    }

    public void setStateCn(String stateCn) {
        this.stateCn = stateCn;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}
