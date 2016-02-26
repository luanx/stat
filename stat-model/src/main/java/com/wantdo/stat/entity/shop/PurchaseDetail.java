package com.wantdo.stat.entity.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wantdo.stat.entity.IdEntity;
import com.wantdo.stat.entity.account.Organization;

import javax.persistence.*;
import java.util.Date;

/**
 * 采购
 *
 *  @ Date : 2015-9-25
 *  @ From : stat
 *  @ Author : luanx@wantdo.com
 */

@Entity
@Table(name = "purchase_detail")
public class PurchaseDetail extends IdEntity{

    private String purchaseOrderId;
    private String platformOrderId;
    private String supplierName;
    private String productName;
    private String wwName;
    private String sku;
    private String orderItemId;
    private String productNo;
    private String productFeature;
    private Long quantity;
    private String price;
    private String shipDiscount;
    private String total;
    private String category;
    private String origin;
    private String productLink;
    private String alternateLink;
    private String remark;

    private String trackno;

    private Date purchaseDate;
    private Date created;
    private Date modified;

    private ReceiveStatus receiveStatus;

    private Organization organization;

    private Purchase purchase;

    public PurchaseDetail(){}

    public PurchaseDetail(Long id){
        this.id = id;
    }

    @JoinColumn(name = "purchase_order_id")
    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(String purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getWwName() {
        return wwName;
    }

    public void setWwName(String wwName) {
        this.wwName = wwName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductFeature() {
        return productFeature;
    }

    public void setProductFeature(String productFeature) {
        this.productFeature = productFeature;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getShipDiscount() {
        return shipDiscount;
    }

    public void setShipDiscount(String shipDiscount) {
        this.shipDiscount = shipDiscount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    public String getAlternateLink() {
        return alternateLink;
    }

    public void setAlternateLink(String alternateLink) {
        this.alternateLink = alternateLink;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+08:00")
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

    // JPA 基于USER_ID列的多对一关系定义
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id")
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_main_id")
    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public String getTrackno() {
        if (purchase != null){
            return purchase.getTrackno();
        } else {
            return "";
        }
    }

    public void setTrackno(String trackno) {
        this.trackno = trackno;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receive_status_id")
    public ReceiveStatus getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(ReceiveStatus receiveStatus) {
        this.receiveStatus = receiveStatus;
    }
}
