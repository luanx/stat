package com.wantdo.stat.entity.shop;

import com.google.common.collect.Lists;
import com.wantdo.stat.entity.IdEntity;
import com.wantdo.stat.entity.account.Organization;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 产品主表
 *
 * @ Date : 15/10/10
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Entity
@Table(name = "product_main")
public class Product extends IdEntity{

    private static final long serialVersionUID = -2077457966558075268L;
    private String sku;
    private String supplierName;
    private String productName;
    private String productNo;
    private String category;
    private String origin;
    private String link;
    private String alternateLink;
    private String remark;

    private Date created;
    private Date modified;

    private Organization organization;

    private List<ProductDetail> productDetailList = Lists.newArrayList();

    public Product(){

    }

    public Product(Long id){
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    public List<ProductDetail> getProductDetailList() {
        return productDetailList;
    }

    public void setProductDetailList(List<ProductDetail> productDetailList) {
        this.productDetailList = productDetailList;
    }
}
