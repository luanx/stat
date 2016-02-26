package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.Purchase;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @Date : 2015-9-25
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface PurchaseDao extends PagingAndSortingRepository<Purchase, Long>, JpaSpecificationExecutor<Purchase> {

    Purchase findByPlatformOrderId(String platformOrderId);

}
