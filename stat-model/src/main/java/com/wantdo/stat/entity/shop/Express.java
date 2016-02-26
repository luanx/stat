package com.wantdo.stat.entity.shop;

import com.wantdo.stat.entity.IdEntity;

import javax.persistence.Table;
import javax.persistence.Entity;
import java.util.Date;

/**
 * @ Date : 2015-9-21
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Entity
@Table(name = "express")
public class Express extends IdEntity {

    private String name;
    private String description;
    private String code;

    private Date created;
    private Date modified;

    public Express() {
    }

    public Express(Long id){
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

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
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
