package com.wantdo.stat.entity.front.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 支出
 *
 * @Date : 2015-9-15
 * @From : stat
 * @Author : luanx@wantdo.com
 */


public class ExpenseVo implements Serializable {

    private static final long serialVersionUID = 338030417896132903L;

    private Long id;
    private Double busPurchase;
    private Double busLogistics;
    private Double busAdvertisement;
    private Double busRefund;
    private Double salesTax;
    private Double adminSalary;
    private Double adminOffice;
    private Double adminTravel;
    private Double finInterest;
    private Double finCharge;
    private Double other;
    private Double balance;
    private String remark;

    private String userName;
    private String organizationName;

    private Date date;
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getBusPurchase() {
        return busPurchase;
    }

    public void setBusPurchase(Double busPurchase) {
        this.busPurchase = busPurchase;
    }

    public Double getBusLogistics() {
        return busLogistics;
    }

    public void setBusLogistics(Double busLogistics) {
        this.busLogistics = busLogistics;
    }

    public Double getBusAdvertisement() {
        return busAdvertisement;
    }

    public void setBusAdvertisement(Double busAdvertisement) {
        this.busAdvertisement = busAdvertisement;
    }

    public Double getBusRefund() {
        return busRefund;
    }

    public void setBusRefund(Double busRefund) {
        this.busRefund = busRefund;
    }

    public Double getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(Double salesTax) {
        this.salesTax = salesTax;
    }

    public Double getAdminSalary() {
        return adminSalary;
    }

    public void setAdminSalary(Double adminSalary) {
        this.adminSalary = adminSalary;
    }

    public Double getAdminOffice() {
        return adminOffice;
    }

    public void setAdminOffice(Double adminOffice) {
        this.adminOffice = adminOffice;
    }

    public Double getAdminTravel() {
        return adminTravel;
    }

    public void setAdminTravel(Double adminTravel) {
        this.adminTravel = adminTravel;
    }

    public Double getFinInterest() {
        return finInterest;
    }

    public void setFinInterest(Double finInterest) {
        this.finInterest = finInterest;
    }

    public Double getFinCharge() {
        return finCharge;
    }

    public void setFinCharge(Double finCharge) {
        this.finCharge = finCharge;
    }

    public Double getOther() {
        return other;
    }

    public void setOther(Double other) {
        this.other = other;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}


