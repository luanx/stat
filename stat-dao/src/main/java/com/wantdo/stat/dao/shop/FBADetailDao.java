package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.FBADetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @Date : 2015-9-23
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface FBADetailDao extends PagingAndSortingRepository<FBADetail, Long>, JpaSpecificationExecutor<FBADetail> {

    FBADetail findByAmazonIdAndShipId(String amazonId, String shipId);

}
