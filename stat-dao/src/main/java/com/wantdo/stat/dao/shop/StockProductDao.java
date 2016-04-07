package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.StockProduct;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @Date : 2015-9-17
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface StockProductDao extends PagingAndSortingRepository<StockProduct, Long>,
        JpaSpecificationExecutor<StockProduct> {

    StockProduct findBySku(String sku);
}
