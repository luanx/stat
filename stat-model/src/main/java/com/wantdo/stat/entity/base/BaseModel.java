package com.wantdo.stat.entity.base;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 *
 * @Date : 2015-9-17
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class BaseModel implements Serializable {
    private static final long serialVersionUID = 4026395592344602276L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
