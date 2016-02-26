package com.wantdo.stat.entity.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.wantdo.stat.entity.IdEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 组织机构
 *
 * @Date : 2015-9-14
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@Entity
@Table(name = "organization")
public class Organization extends IdEntity {

    private String name;
    private String description;
    private Long level;
    private Boolean isLeaf;
    private Boolean isShop;

    private Date created;
    private Date modified;

    private Organization parent;
    private List<Organization> children;

    private List<User> userList = Lists.newArrayList();

    public Organization() {
    }

    public Organization(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public Boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Boolean getIsShop() {
        return isShop;
    }

    public void setIsShop(Boolean isShop) {
        this.isShop = isShop;
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
    @JoinColumn(name = "pid")
    @JsonIgnore
    public Organization getParent() {
        return parent;
    }

    public void setParent(Organization parent) {
        this.parent = parent;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", fetch = FetchType.LAZY)
    public List<Organization> getChildren() {
        return children;
    }

    public void setChildren(List<Organization> children) {
        this.children = children;
    }


    @ManyToMany
    @JoinTable(name = "user_organization", joinColumns = {@JoinColumn(name = "organization_id")}, inverseJoinColumns =
            {@JoinColumn(name = "user_id")})
    @Fetch(FetchMode.SUBSELECT)
    //集合按id排序
    @OrderBy("id ASC")
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
