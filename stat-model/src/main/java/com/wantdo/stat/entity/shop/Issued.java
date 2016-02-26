package com.wantdo.stat.entity.shop;

import com.wantdo.stat.entity.IdEntity;

import javax.persistence.*;
import java.util.Date;

/**
 *  出库表
 *
 * @ Date : 15/10/8
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
@Entity
@Table(name = "issued")
public class Issued extends IdEntity {

    private String trackno;
    private Double weight;

    private Order order;

    private Date created;
    private Date modified;

    public Issued(){
    }

    public Issued(Long id){
        this.id = id;
    }

    public String getTrackno() {
        return trackno;
    }

    public void setTrackno(String trackno) {
        this.trackno = trackno;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
