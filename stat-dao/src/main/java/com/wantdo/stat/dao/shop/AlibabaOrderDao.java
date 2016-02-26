package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.AlibabaOrder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @ Date : 15/10/21
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public interface AlibabaOrderDao extends PagingAndSortingRepository<AlibabaOrder, Long>, JpaSpecificationExecutor<AlibabaOrder>{

    AlibabaOrder findByPlatformOrderId(String platformOrderId);

    AlibabaOrder findByTrackno(String trackno);
}
