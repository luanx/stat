package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.OrderArea;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * @Date : 2015-9-17
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface OrderAreaDao extends PagingAndSortingRepository<OrderArea, Long>, JpaSpecificationExecutor<OrderArea> {

    @Query("select orderArea.stateCn as stateCn,  count(orderArea) as num from OrderArea orderArea, Organization organization where orderArea.organizationId= organization.id and  orderArea.created >=?1 and orderArea.created <= ?2 and orderArea.organizationId=?3 group by orderArea.state order by count(orderArea) asc")
    List<Object[]> countByDate(Date startDate, Date endDate, Long organizationId);


}
