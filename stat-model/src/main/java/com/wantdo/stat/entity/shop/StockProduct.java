package com.wantdo.stat.entity.shop;

import com.google.common.collect.Lists;
import com.wantdo.stat.entity.IdEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @ Date : 16/4/7
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Entity
@Table(name = "stock_product")
public class StockProduct extends IdEntity {

    private String sku;
    private String ptype;
    private String name;
    private String series;
    private String category;
    private String price;
    private Date created;
    private Date modified;

    private Platform platform;
    private List<StockOrderItem> stockOrderItemList = Lists.newArrayList();

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @JoinColumn(name = "platform_id")
    @OneToOne(fetch = FetchType.LAZY)
    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku")
    public List<StockOrderItem> getStockOrderItemList() {
        return stockOrderItemList;
    }

    public void setStockOrderItemList(List<StockOrderItem> stockOrderItemList) {
        this.stockOrderItemList = stockOrderItemList;
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
