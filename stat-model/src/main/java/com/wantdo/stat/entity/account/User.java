package com.wantdo.stat.entity.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wantdo.stat.entity.IdEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 用户
 *
 * @Date : 2015-8-24
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@Entity
@Table(name = "user")
public class User extends IdEntity {

    private String name;
    private String loginName;
    private String plainPassword;
    private String password;
    private String salt;
    private String email;
    private String phone;
    private String status;

    private Date created;
    private Date modified;

    private List<Role> roleList = Lists.newArrayList();
    private Set<Resource> resourceSet = Sets.newLinkedHashSet();

    private List<Organization> organizationList = Lists.newArrayList();

    public User() {
    }

    public User(Long id){
        this.id = id;
    }

    @NotBlank
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Transient
    @JsonIgnore
    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    //Fetch策略定义
    @Fetch(FetchMode.SUBSELECT)
    //集合按id排序
    @OrderBy("id ASC")
    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @Transient
    @JsonIgnore
    public Set<Resource> getResourceSet() {
        for(Role role: roleList){
            Set<Resource> resources = role.getResourceSet();
            for(Resource resource: resources){
                if (resource.getLevel() == 1){
                    resourceSet.add(resource);
                }
            }
        }
        return resourceSet;
    }

    public void setResourceSet(Set<Resource> resourceSet) {
        this.resourceSet = resourceSet;
    }

    @ManyToMany
    @JoinTable(name = "user_organization", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns =
            {@JoinColumn(name = "organization_id")})
    @Fetch(FetchMode.SUBSELECT)
    //集合按id排序
    @OrderBy("id ASC")
    public List<Organization> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<Organization> organizationList) {
        this.organizationList = organizationList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
