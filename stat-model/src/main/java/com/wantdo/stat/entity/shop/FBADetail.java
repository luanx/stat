package com.wantdo.stat.entity.shop;

import com.wantdo.stat.entity.IdEntity;
import com.wantdo.stat.entity.account.Organization;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Date : 2015-9-23
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@Entity
@Table(name = "fba_detail")
public class FBADetail extends IdEntity {

    private String amazonId;
    private String shipId;
    private Double weight;
    private Double price;
    private String shipMethod;
    private String trackno;
    private String shipAddress;
    private String status;

    private Date created;
    private Date modified;

    private Organization organization;
    private FBA fba;

    public FBADetail(){

    }

    public FBADetail(Long id){
        this.id = id;
    }

    public String getAmazonId() {
        return amazonId;
    }

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    public void setAmazonId(String amazonId) {
        this.amazonId = amazonId;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getShipMethod() {
        return shipMethod;
    }

    public void setShipMethod(String shipMethod) {
        this.shipMethod = shipMethod;
    }

    public String getTrackno() {
        return trackno;
    }

    public void setTrackno(String trackno) {
        this.trackno = trackno;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id")
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @ManyToOne
    @JoinColumn(name = "fba_id")
    public FBA getFba() {
        return fba;
    }

    public void setFba(FBA fba) {
        this.fba = fba;
    }
}
