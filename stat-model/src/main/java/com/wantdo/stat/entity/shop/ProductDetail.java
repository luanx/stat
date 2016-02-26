package com.wantdo.stat.entity.shop;

import com.google.common.collect.Lists;
import com.wantdo.stat.entity.IdEntity;
import com.wantdo.stat.entity.account.Organization;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 产品从表
 *
 * @ Date : 15/10/11
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Entity
@Table(name = "product_detail")
public class ProductDetail extends IdEntity{

    private static final long serialVersionUID = -1926152794229299482L;
    private String sku;
    private String supplierName;
    private String productName;
    private String productNo;
    private String color;
    private String size;
    private String feature;
    private String price;
    private String weight;
    private String link;
    private String alternateLink;
    private String category;
    private String origin;
    private String remark;

    private Date created;
    private Date modified;

    private Organization organization;
    private Product product;

    private List<OrderDetail> orderDetailList = Lists.newArrayList();

    public ProductDetail(){

    }

    public ProductDetail(Long id){
        this.id = id;
    }

    @Column(name = "sku")
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
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
    @ManyToOne
    @JoinColumn(name = "organization_id")
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_main_id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku")
    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }


}
