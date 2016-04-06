package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.Platform;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @Date : 2015-9-17
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface PlatformDao extends PagingAndSortingRepository<Platform, Long>, JpaSpecificationExecutor<Platform> {

    Platform findByName(String name);

}
