package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.OrderStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @ Date : 15/10/15
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public interface OrderStatusDao extends PagingAndSortingRepository<OrderStatus, Long>, JpaSpecificationExecutor<OrderStatus> {
}
