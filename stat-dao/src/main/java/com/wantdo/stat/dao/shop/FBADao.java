package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.FBA;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @Date : 2015-9-22
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface FBADao extends PagingAndSortingRepository<FBA, Long>, JpaSpecificationExecutor<FBA>{

    FBA findByAmazonId(String amazonId);
}
