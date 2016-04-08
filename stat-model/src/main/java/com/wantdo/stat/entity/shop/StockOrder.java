package com.wantdo.stat.entity.shop;

import com.google.common.collect.Lists;
import com.wantdo.stat.entity.IdEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @ Date : 16/4/5
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Entity
@Table(name = "stock_order")
public class StockOrder extends IdEntity {

    private String orderId;
    private String stockId;
    private Date outStock;
    private String warehouse;
    private Long status;
    private Long stockType;
    private Date created;
    private Date modified;

    private Platform platform;
    private List<StockOrderItem> stockOrderItemList = Lists.newArrayList();

    @Column(name = "orderid")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Column(name = "stockid")
    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    @Column(name = "outstock")
    public Date getOutStock() {
        return outStock;
    }

    public void setOutStock(Date outStock) {
        this.outStock = outStock;
    }

    @JoinColumn(name = "platform_id")
    @OneToOne(fetch = FetchType.LAZY)
    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Column(name = "stocktype")
    public Long getStockType() {
        return stockType;
    }

    public void setStockType(Long stockType) {
        this.stockType = stockType;
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

    @OneToMany(mappedBy = "stockOrder", fetch = FetchType.LAZY)
    public List<StockOrderItem> getStockOrderItemList() {
        return stockOrderItemList;
    }

    public void setStockOrderItemList(List<StockOrderItem> stockOrderItemList) {
        this.stockOrderItemList = stockOrderItemList;
    }
}
