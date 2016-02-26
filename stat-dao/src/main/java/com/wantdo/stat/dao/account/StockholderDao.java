package com.wantdo.stat.dao.account;

import com.wantdo.stat.entity.account.Stockholder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @Date : 2015-9-16
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface StockholderDao extends PagingAndSortingRepository<Stockholder, Long>, JpaSpecificationExecutor<Stockholder> {
}
