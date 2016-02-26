package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.AlibabaOrderDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @ Date : 15/10/21
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

public interface AlibabaOrderDetailDao extends PagingAndSortingRepository<AlibabaOrderDetail, Long>, JpaSpecificationExecutor<AlibabaOrderDetail> {

    AlibabaOrderDetail findByPlatformOrderIdAndProductTitle(String platformOrderId, String productTitle);

}
