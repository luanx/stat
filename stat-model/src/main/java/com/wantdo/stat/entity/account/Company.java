package com.wantdo.stat.entity.account;

import com.google.common.collect.Lists;
import com.wantdo.stat.entity.IdEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * 公司
 *
 * @Date : 2015-9-16
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@Entity
@Table(name = "company")
public class Company extends IdEntity {

    private String name;

    /**
     * 营业执照号码
     */
    private String businessNo;

    /**
     * 税务登记证号码
     */
    private String taxNo;

    /**
     * 组织机构代码证号码
     */
    private String organizationNo;

    /**
     * 经营范围
     */
    private String businesses;

    private String address;

    /**
     * 注册日期
     */
    private Date date;

    private Date created;
    private Date modified;

    private List<Stockholder> stockholderList = Lists.newArrayList();

    public Company() {
    }

    public Company(Long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getOrganizationNo() {
        return organizationNo;
    }

    public void setOrganizationNo(String organizationNo) {
        this.organizationNo = organizationNo;
    }

    public String getBusinesses() {
        return businesses;
    }

    public void setBusinesses(String businesses) {
        this.businesses = businesses;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @OneToMany(mappedBy = "company")
    @Fetch(FetchMode.SUBSELECT)
    public List<Stockholder> getStockholderList() {
        return stockholderList;
    }

    public void setStockholderList(List<Stockholder> stockholderList) {
        this.stockholderList = stockholderList;
    }
}
