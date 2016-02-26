package com.wantdo.stat.entity.shop;

import com.wantdo.stat.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 订单状态
 *
 * @ Date : 15/10/14
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Entity
@Table(name = "order_status")
public class OrderStatus extends IdEntity {

    private String name;
    private String description;

    private Date created;
    private Date modified;

    public OrderStatus(){

    }

    public OrderStatus(Long id){
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
