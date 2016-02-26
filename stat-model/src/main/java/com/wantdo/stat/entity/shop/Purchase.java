package com.wantdo.stat.entity.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.wantdo.stat.entity.IdEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 采购
 *
 * @Date : 2015-9-25
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Entity
@Table(name = "purchase_main")
public class Purchase extends IdEntity{

    private String platformOrderId;
    private String supplierName;
    private String wwName;
    private String total;
    private String remark;

    private String trackno;

    private Date purchaseDate;
    private Date created;
    private Date modified;

    private AlibabaOrder alibabaOrder;

    private List<PurchaseDetail> purchaseDetailList = Lists.newArrayList();

    public Purchase(){}

    public Purchase(Long id){
        this.id = id;
    }

    @JoinColumn(name = "platform_order_id")
    public String getPlatformOrderId() {
        return platformOrderId;
    }

    public void setPlatformOrderId(String platformOrderId) {
        this.platformOrderId = platformOrderId;
    }


    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getWwName() {
        return wwName;
    }

    public void setWwName(String wwName) {
        this.wwName = wwName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
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


    @OneToMany(mappedBy = "purchase", fetch = FetchType.EAGER)
    public List<PurchaseDetail> getPurchaseDetailList() {
        return purchaseDetailList;
    }

    public void setPurchaseDetailList(List<PurchaseDetail> purchaseDetailList) {
        this.purchaseDetailList = purchaseDetailList;
    }

    @OneToOne(fetch = FetchType.EAGER, targetEntity = AlibabaOrder.class)
    @JoinColumn(name = "platformOrderId",  referencedColumnName = "platformOrderId",insertable = false, updatable = false)
    public AlibabaOrder getAlibabaOrder() {
        return alibabaOrder;
    }

    public void setAlibabaOrder(AlibabaOrder alibabaOrder) {
        this.alibabaOrder = alibabaOrder;
    }

    public String getTrackno() {
        if (alibabaOrder != null){
            return alibabaOrder.getTrackno();
        } else {
            return "";
        }
    }

    public void setTrackno(String trackno) {
        this.trackno = trackno;
    }
}
