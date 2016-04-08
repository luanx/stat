package com.wantdo.stat.entity.shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wantdo.stat.entity.IdEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * @ Date : 16/4/5
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Entity
@Table(name = "stock_orderitem")
public class StockOrderItem extends IdEntity{

    private String sku;
    private String series;
    private String category;
    private Long num;
    private String amount;
    private Long status;
    private Date created;
    private Date modified;

    private StockOrder stockOrder;
    private StockProduct stockProduct;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockorderid")
    public StockOrder getStockOrder() {
        return stockOrder;
    }

    public void setStockOrder(StockOrder stockOrder) {
        this.stockOrder = stockOrder;
    }

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = StockProduct.class)
    @JoinColumn(name = "sku", referencedColumnName = "sku", insertable = false, updatable = false)
    public StockProduct getStockProduct() {
        return stockProduct;
    }

    public void setStockProduct(StockProduct stockProduct) {
        this.stockProduct = stockProduct;
    }
}
