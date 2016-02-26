package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @ Date : 15/10/11
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public interface ProductDao extends PagingAndSortingRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Product findBySku(String sku);

    List<Product> findByOrganizationId(Long organizationId);

}
