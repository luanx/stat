package com.wantdo.stat.dao.account;

import com.wantdo.stat.entity.account.Resource;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @Date : 2015-8-30
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface ResourceDao extends PagingAndSortingRepository<Resource, Long>, JpaSpecificationExecutor<Resource> {
}
