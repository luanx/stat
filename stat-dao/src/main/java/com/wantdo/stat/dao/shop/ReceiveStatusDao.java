package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.ReceiveStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @ Date : 15/10/28
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public interface ReceiveStatusDao extends PagingAndSortingRepository<ReceiveStatus, Long>, JpaSpecificationExecutor<ReceiveStatus>{
}
