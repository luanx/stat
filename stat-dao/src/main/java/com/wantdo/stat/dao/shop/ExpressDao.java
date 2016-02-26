package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.Express;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @ Date : 15/10/8
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public interface ExpressDao extends  PagingAndSortingRepository<Express, Long>, JpaSpecificationExecutor<Express> {

    Express findByName(String name);

}
