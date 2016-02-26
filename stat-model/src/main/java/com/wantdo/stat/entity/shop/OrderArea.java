package com.wantdo.stat.entity.shop;

import com.wantdo.stat.entity.IdEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * @ Date : 16/2/25
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Entity
@Table(name = "order_area")
public class OrderArea extends IdEntity {

    private String orderId;
    private Long organizationId;
    private String shipCountry;
    private Date created;
    private String zip;
    private String state;
    private String stateCn;

    public OrderArea(){
    }

    public OrderArea(Long id){
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getShipCountry() {
        return shipCountry;
    }

    public void setShipCountry(String shipCountry) {
        this.shipCountry = shipCountry;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateCn() {
        return stateCn;
    }

    public void setStateCn(String stateCn) {
        this.stateCn = stateCn;
    }
}
