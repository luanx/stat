package com.wantdo.stat.entity.front.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @ Date : 15/10/26
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class TableDTO<T> implements Serializable {

    private static final long serialVersionUID = -805618457883299904L;
    private Long total;

    private List<T> rows;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
