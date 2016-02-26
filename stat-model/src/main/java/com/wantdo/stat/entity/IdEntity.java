package com.wantdo.stat.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 统一定义id的entity基类.
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 *
 * @Date : 2015-8-24
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@MappedSuperclass
public abstract class IdEntity implements Serializable{

    protected Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
