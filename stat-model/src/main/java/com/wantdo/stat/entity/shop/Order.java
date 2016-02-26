package com.wantdo.stat.entity.shop;

import com.google.common.collect.Lists;
import com.wantdo.stat.entity.IdEntity;
import com.wantdo.stat.entity.account.Organization;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 订单主表
 *
 * @ Date : 2015-9-17
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
@Entity
@Table(name = "order_main")
public class Order extends IdEntity{

    private String orderId;
    private Long purchaseDate;
    private DateTime purDate;
    private Long paymentsDate;
    private DateTime payDate;
    private String buyerEmail;
    private String buyerName;
    private String buyerPhoneNumber;
    private String shipServiceLevel;
    private String recipientName;
    private String shipAddress1;
    private String shipAddress2;
    private String shipAddress3;
    private String shipCity;
    private String shipState;
    private String shipPostalCode;
    private String shipCountry;
    private String shipPhoneNumber;
    private Long deliveryStartDate;
    private Long deliveryEndDate;
    private String deliveryTimeZone;
    private String deliveryInstructions;
    private String salesChannel;

    private Express express;
    private String trackno;
    private Organization organization;
    private OrderStatus orderStatus;
    private Double weight;
    private String remark;

    private Date created;
    private Date modified;

    private Issued issued;

    private Boolean changed;

    private List<OrderDetail> orderDetailList = Lists.newArrayList();


    public Order() {
    }

    public Order(Long id){
        this.id = id;
    }

    @NotBlank
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Long purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Transient
    public DateTime getPurDate() {
        purDate = new DateTime(purchaseDate).withZone(DateTimeZone.forOffsetHours(-7));
        return purDate;
    }

    public void setPurDate(DateTime purDate) {
        this.purDate = purDate;
    }

    public Long getPaymentsDate() {
        return paymentsDate;
    }

    public void setPaymentsDate(Long paymentsDate) {
        this.paymentsDate = paymentsDate;
    }

    @Transient
    public DateTime getPayDate() {
        payDate = new DateTime(paymentsDate).withZone(DateTimeZone.forOffsetHours(-7));
        return payDate;
    }

    public void setPayDate(DateTime payDate) {
        this.payDate = payDate;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerPhoneNumber() {
        return buyerPhoneNumber;
    }

    public void setBuyerPhoneNumber(String buyerPhoneNumber) {
        this.buyerPhoneNumber = buyerPhoneNumber;
    }

    public String getShipServiceLevel() {
        return shipServiceLevel;
    }

    public void setShipServiceLevel(String shipServiceLevel) {
        this.shipServiceLevel = shipServiceLevel;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getShipAddress1() {
        return shipAddress1;
    }

    public void setShipAddress1(String shipAddress1) {
        this.shipAddress1 = shipAddress1;
    }

    public String getShipAddress2() {
        return shipAddress2;
    }

    public void setShipAddress2(String shipAddress2) {
        this.shipAddress2 = shipAddress2;
    }

    public String getShipAddress3() {
        return shipAddress3;
    }

    public void setShipAddress3(String shipAddress3) {
        this.shipAddress3 = shipAddress3;
    }

    public String getShipCity() {
        return shipCity;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    public String getShipState() {
        return shipState;
    }

    public void setShipState(String shipState) {
        this.shipState = shipState;
    }

    public String getShipPostalCode() {
        return shipPostalCode;
    }

    public void setShipPostalCode(String shipPostalCode) {
        this.shipPostalCode = shipPostalCode;
    }

    public String getShipCountry() {
        return shipCountry;
    }

    public void setShipCountry(String shipCountry) {
        this.shipCountry = shipCountry;
    }

    public String getShipPhoneNumber() {
        return shipPhoneNumber;
    }

    public void setShipPhoneNumber(String shipPhoneNumber) {
        this.shipPhoneNumber = shipPhoneNumber;
    }

    public Long getDeliveryStartDate() {
        return deliveryStartDate;
    }

    public void setDeliveryStartDate(Long deliveryStartDate) {
        this.deliveryStartDate = deliveryStartDate;
    }

    public Long getDeliveryEndDate() {
        return deliveryEndDate;
    }

    public void setDeliveryEndDate(Long deliveryEndDate) {
        this.deliveryEndDate = deliveryEndDate;
    }

    public String getDeliveryTimeZone() {
        return deliveryTimeZone;
    }

    public void setDeliveryTimeZone(String deliveryTimeZone) {
        this.deliveryTimeZone = deliveryTimeZone;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    @OneToOne
    @JoinColumn(name = "express_id")
    public Express getExpress() {
        return express;
    }

    public void setExpress(Express express) {
        this.express = express;
    }

    public String getTrackno() {
        return trackno;
    }

    public void setTrackno(String trackno) {
        this.trackno = trackno;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_status")
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @OneToOne(mappedBy = "order")
    public Issued getIssued() {
        return issued;
    }

    public void setIssued(Issued issued) {
        this.issued = issued;
    }

    public Boolean getChanged() {
        return changed;
    }

    public void setChanged(Boolean changed) {
        this.changed = changed;
    }

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

}
