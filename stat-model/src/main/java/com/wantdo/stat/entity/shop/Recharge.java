package com.wantdo.stat.entity.shop;

import com.wantdo.stat.entity.IdEntity;
import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.entity.account.User;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Date : 2015-9-30
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@Entity
@Table
public class Recharge extends IdEntity {

    private Double account;
    private Double balance;

    private User user;
    private Organization organization;

    private Date date = new Date();
    private Date created;
    private Date modified;

    public Recharge(){}

    public Recharge(Long id){
        this.id = id;
    }

    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id")
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
