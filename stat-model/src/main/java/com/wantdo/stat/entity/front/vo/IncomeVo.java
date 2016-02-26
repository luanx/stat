package com.wantdo.stat.entity.front.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 收入
 *
 * @Date : 2015-9-15
 * @From : stat
 * @Author : luanx@wantdo.com
 */


public class IncomeVo implements Serializable {

    private static final long serialVersionUID = 338030417896132903L;

    private Long id;
    private Double cash;
    private Double busMain;
    private Double busOther;
    private String payBorrowName;
    private Double payBorrowAccount;
    private Double payLoan;
    private Double receivable;
    private Double other;
    private Double balance;

    private String userName;
    private String organizationName;
    private Date date;
    private Date created;
    private String remark;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}


