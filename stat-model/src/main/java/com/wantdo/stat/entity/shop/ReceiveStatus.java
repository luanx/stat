package com.wantdo.stat.entity.shop;

import com.google.common.collect.Lists;
import com.wantdo.stat.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * 到货状态
 *
 * @ Date : 15/10/14
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Entity
@Table(name = "receive_status")
public class ReceiveStatus extends IdEntity {

    private String name;
    private String description;

    private Date created;
    private Date modified;

    private List<PurchaseDetail> purchaseDetailList = Lists.newArrayList();

    public ReceiveStatus(){

    }

    public ReceiveStatus(Long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiveStatus")
    public List<PurchaseDetail> getPurchaseDetailList() {
        return purchaseDetailList;
    }

    public void setPurchaseDetailList(List<PurchaseDetail> purchaseDetailList) {
        this.purchaseDetailList = purchaseDetailList;
    }
}
