package com.wantdo.stat.entity.front.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @ Date : 2015-9-21
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class PDFVo implements Serializable{
    private static final long serialVersionUID = -8124539456691543461L;

    private String name;
    private Date created;
    private Date modified;
    private String path;

    public PDFVo(){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
