package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.StockOrderItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @Date : 2015-9-17
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface StockOrderItemDao extends PagingAndSortingRepository<StockOrderItem, Long>, JpaSpecificationExecutor<StockOrderItem> {

    @Query("select stockOrderItem from StockOrderItem stockOrderItem where stockorderid = ?1 and sku = ?2")
    StockOrderItem findByStockOrderIdAndSku(Long stockOrderId, String sku);

    @Query("select stockOrderItem from StockOrderItem stockOrderItem where stockOrderItem.status = 2 and stockOrderItem.sku = ?1")
    List<StockOrderItem> findAllExceptionBySku(String sku);
}
