package com.wantdo.stat.entity.shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wantdo.stat.entity.IdEntity;

import javax.persistence.*;
import java.util.Date;

/**
 *  阿里巴巴订单从表
 *
 * @ Date : 15/10/21
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Entity
@Table(name = "alibaba_order_detail")
public class AlibabaOrderDetail extends IdEntity {

    private String platformOrderId;
    private String buyerCompany;
    private String buyerName;
    private String sellerCompany;
    private String sellerName;
    private Double total;
    private Double shipFee;
    private Double discount;
    private Double ticket;
    private Double totalPay;
    private String orderStatus;
    private Date purchaseDate;
    private Date paymentsDate;
    private String recipientName;
    private String shipAddress;
    private String shipPostalCode;
    private String shipPhoneNumber;
    private String shipCallNumber;
    private String productTitle;
    private Double productPrice;
    private Long quantity;
    private String unit;
    private String productNo;
    private String productModel;
    private Long productType;
    private String buyerMessage;
    private String expressName;
    private String trackno;
    private String invoiceBuyerCompany;
    private String invoiceTaxpayer;
    private String invoiceAddressPhone;
    private String invoiceBank;
    private String invoiceAddress;
    private String associationNumber;
    private Double creditCharge;
    private Date created;
    private Date modified;

    private AlibabaOrder alibabaOrder;

    public AlibabaOrderDetail(){}

    public AlibabaOrderDetail(Long id){
        this.id = id;
    }

    @JoinColumn(name = "platform_order_id")
    public String getPlatformOrderId() {
        return platformOrderId;
    }

    public void setPlatformOrderId(String platformOrderId) {
        this.platformOrderId = platformOrderId;
    }

    public String getBuyerCompany() {
        return buyerCompany;
    }

    public void setBuyerCompany(String buyerCompany) {
        this.buyerCompany = buyerCompany;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getSellerCompany() {
        return sellerCompany;
    }

    public void setSellerCompany(String sellerCompany) {
        this.sellerCompany = sellerCompany;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getShipFee() {
        return shipFee;
    }

    public void setShipFee(Double shipFee) {
        this.shipFee = shipFee;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTicket() {
        return ticket;
    }

    public void setTicket(Double ticket) {
        this.ticket = ticket;
    }

    public Double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(Double totalPay) {
        this.totalPay = totalPay;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getPaymentsDate() {
        return paymentsDate;
    }

    public void setPaymentsDate(Date paymentsDate) {
        this.paymentsDate = paymentsDate;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    @JoinColumn(name = "ship_postal_code")
    public String getShipPostalCode() {
        return shipPostalCode;
    }

    public void setShipPostalCode(String shipPostalCode) {
        this.shipPostalCode = shipPostalCode;
    }

    @JoinColumn(name = "ship_phone_number")
    public String getShipPhoneNumber() {
        return shipPhoneNumber;
    }

    public void setShipPhoneNumber(String shipPhoneNumber) {
        this.shipPhoneNumber = shipPhoneNumber;
    }

    public String getShipCallNumber() {
        return shipCallNumber;
    }

    public void setShipCallNumber(String shipCallNumber) {
        this.shipCallNumber = shipCallNumber;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public Long getProductType() {
        return productType;
    }

    public void setProductType(Long productType) {
        this.productType = productType;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getTrackno() {
        return trackno;
    }

    public void setTrackno(String trackno) {
        this.trackno = trackno;
    }

    @JoinColumn(name = "invoice_buyer_company")
    public String getInvoiceBuyerCompany() {
        return invoiceBuyerCompany;
    }

    public void setInvoiceBuyerCompany(String invoiceBuyerCompany) {
        this.invoiceBuyerCompany = invoiceBuyerCompany;
    }

    public String getInvoiceTaxpayer() {
        return invoiceTaxpayer;
    }

    public void setInvoiceTaxpayer(String invoiceTaxpayer) {
        this.invoiceTaxpayer = invoiceTaxpayer;
    }

    @JoinColumn(name = "invoice_address_phone")
    public String getInvoiceAddressPhone() {
        return invoiceAddressPhone;
    }

    public void setInvoiceAddressPhone(String invoiceAddressPhone) {
        this.invoiceAddressPhone = invoiceAddressPhone;
    }

    public String getInvoiceBank() {
        return invoiceBank;
    }

    public void setInvoiceBank(String invoiceBank) {
        this.invoiceBank = invoiceBank;
    }

    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public String getAssociationNumber() {
        return associationNumber;
    }

    public void setAssociationNumber(String associationNumber) {
        this.associationNumber = associationNumber;
    }

    public Double getCreditCharge() {
        return creditCharge;
    }

    public void setCreditCharge(Double creditCharge) {
        this.creditCharge = creditCharge;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "alibaba_order_main_id")
    public AlibabaOrder getAlibabaOrder() {
        return alibabaOrder;
    }

    public void setAlibabaOrder(AlibabaOrder alibabaOrder) {
        this.alibabaOrder = alibabaOrder;
    }
}

