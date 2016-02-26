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
 * 收入
 *
 * @Date : 2015-9-15
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Entity
@Table(name = "income")
public class Income extends IdEntity {

    private Double cash;
    private Double busMain;
    private Double busOther;
    private String payBorrowName;
    private Double payBorrowAccount;
    private Double payLoan;
    private Double receivable;
    private Double other;
    private Double balance;

    private User user;
    private Organization organization;

    private Date date = new Date();
    private Date created;
    private Date modified;
    private String remark;

    public Income() {
    }

    public Income(Long id) {
        this.id = id;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }

    public Double getBusMain() {
        return busMain;
    }

    public void setBusMain(Double busMain) {
        this.busMain = busMain;
    }

    public Double getBusOther() {
        return busOther;
    }

    public void setBusOther(Double busOther) {
        this.busOther = busOther;
    }

    public String getPayBorrowName() {
        return payBorrowName;
    }

    public void setPayBorrowName(String payBorrowName) {
        this.payBorrowName = payBorrowName;
    }

    public Double getPayBorrowAccount() {
        return payBorrowAccount;
    }

    public void setPayBorrowAccount(Double payBorrowAccount) {
        this.payBorrowAccount = payBorrowAccount;
    }

    public Double getPayLoan() {
        return payLoan;
    }

    public void setPayLoan(Double payLoan) {
        this.payLoan = payLoan;
    }

    public Double getReceivable() {
        return receivable;
    }

    public void setReceivable(Double receivable) {
        this.receivable = receivable;
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


