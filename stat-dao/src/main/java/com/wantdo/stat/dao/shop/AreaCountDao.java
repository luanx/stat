package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.AreaCount;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * @Date : 2015-9-22
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface AreaCountDao extends PagingAndSortingRepository<AreaCount, Long>, JpaSpecificationExecutor<AreaCount>{

    @Query("select areaCount from AreaCount areaCount where areaCount.startDate=?1 and areaCount.endDate=?2 and organizationId=?3")
    List<AreaCount> findByDateAndOrganizationId(Date startDate, Date endDate, Long organizationId);

}
