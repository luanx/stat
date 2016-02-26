package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.OrderDetail;
import com.wantdo.stat.entity.shop.Product;
import com.wantdo.stat.entity.shop.ProductDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * @ Date : 15/10/11
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public interface ProductDetailDao extends PagingAndSortingRepository<ProductDetail, Long>, JpaSpecificationExecutor<ProductDetail> {

    ProductDetail findBySku(String sku);


    @Modifying
    @Query("delete from ProductDetail productDetail where productDetail.product.id=?1")
    void deleteByProduct(Long id);


}
