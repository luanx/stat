package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.StockOrder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @Date : 2015-9-17
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface StockOrderDao extends PagingAndSortingRepository<StockOrder, Long>, JpaSpecificationExecutor<StockOrder> {

    @Query("select stockOrder from StockOrder stockOrder where stockOrder.orderId = ?1")
    StockOrder findByOrderId(String orderId);

}