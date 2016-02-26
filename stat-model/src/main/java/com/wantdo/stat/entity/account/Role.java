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
 * 角色
 *
 * @Date : 2015-8-24
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@Entity
@Table(name = "role")
public class Role extends IdEntity {

    private String name;
    private String description;
    private Date created;
    private Date modified;

    private Set<Resource> resourceSet = Sets.newLinkedHashSet();

    public Role() {
    }

    public Role(Long id){
        this.id = id;
    }

    @NotBlank
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
    @JoinTable(name = "role_resource", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn
            (name = "resource_id")})
    //Fetch策略定义
    @Fetch(FetchMode.SUBSELECT)
    //集合按id排序
    @OrderBy("id ASC")
    public Set<Resource> getResourceSet() {
        return resourceSet;
    }

    public void setResourceSet(Set<Resource> resourceSet) {
        this.resourceSet = resourceSet;
    }



    @Transient
    @JsonIgnore
    public List<String> getPermissions(){
        List<String> permissions = Lists.newArrayList();
        for(Resource resource : resourceSet){
            permissions.add(resource.getPermission());
        }
        return permissions;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}