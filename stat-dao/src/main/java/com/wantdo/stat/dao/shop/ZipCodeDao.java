package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.ZipCode;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @Date : 2015-9-17
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface ZipCodeDao extends PagingAndSortingRepository<ZipCode, Long>, JpaSpecificationExecutor<ZipCode> {

    ZipCode findByZip(String zip);

}
