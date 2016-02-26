package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.PurchaseDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @ Date : 2015-9-25
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public interface PurchaseDetailDao extends PagingAndSortingRepository<PurchaseDetail, Long>, JpaSpecificationExecutor<PurchaseDetail> {


    PurchaseDetail findByPlatformOrderIdAndPurchaseOrderIdAndOrderItemId(String platformOrderId, String purchaseOrderId, String orderItemId);

}
