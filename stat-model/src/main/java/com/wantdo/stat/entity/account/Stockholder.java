package com.wantdo.stat.entity.account;

import com.wantdo.stat.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 股东
 *
 * @Date : 2015-9-16
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Entity
@Table(name = "stockholder")
public class Stockholder extends IdEntity {

    private String name;
    private String address;
    private String email;
    private String phone;

    private Date created;
    private Date modified;

    private Company company;

    public Stockholder(){
    }

    public Stockholder(Long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    // JPA 基于USER_ID列的多对一关系定义
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
