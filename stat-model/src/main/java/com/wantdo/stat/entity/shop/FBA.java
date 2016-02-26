package com.wantdo.stat.entity.shop;

import com.google.common.collect.Lists;
import com.wantdo.stat.entity.IdEntity;
import com.wantdo.stat.entity.account.Organization;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * FBA
 *
 * @Date : 2015-9-22
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@Entity
@Table(name = "fba_main")
public class FBA extends IdEntity{

    private String amazonId;
    private String shipId;
    private String shipMethod;
    private String trackno;
    private String status;
    private String shipAddress;

    private Date created;
    private Date modified;

    private Organization organization;
    private List<FBADetail> fbaDetailList = Lists.newArrayList();

    public FBA(){}

    public FBA(Long id){
        this.id = id;
    }

    public String getAmazonId() {
        return amazonId;
    }

    public void setAmazonId(String amazonId) {
        this.amazonId = amazonId;
    }

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fba", fetch = FetchType.LAZY)
    public List<FBADetail> getFbaDetailList() {
        return fbaDetailList;
    }

    public void setFbaDetailList(List<FBADetail> fbaDetailList) {
        this.fbaDetailList = fbaDetailList;
    }
}
