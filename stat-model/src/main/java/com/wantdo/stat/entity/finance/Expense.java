package com.wantdo.stat.entity.finance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wantdo.stat.entity.IdEntity;
import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.entity.account.User;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 支出
 *
 * @Date : 2015-9-15
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Entity
@Table(name = "expense")
public class Expense extends IdEntity {

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

    private User user;
    private Organization organization;

    private Date date = new Date();
    private Date created;
    private Date modified;

    private String remark;

    public Expense() {

    }

    public Expense(Long id) {
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "organization_id")
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

