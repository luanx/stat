package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.Order;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * @Date : 2015-9-17
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface OrderDao extends PagingAndSortingRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Order findByOrderId(String orderId);

    Order findByTrackno(String trackno);

    @Query("select order from Order order where order.created >=?1 and order.created <= ?2")
    List<Order> findAllByDate(Date startDate, Date endDate);

    @Query("select order from Order order where order.purchaseDate >=?1 and order.purchaseDate <= ?2 and order.organization.id=?3")
    List<Order> findAllByPurchaseDateAndOrganization(Long startDate, Long endDate, Long organizationId);

    @Query("select order from Order order where order.paymentsDate >=?1 and order.paymentsDate <= ?2 and order.organization.id=?3")
    List<Order> findAllByPaymentsAndOrganization(Long startDate, Long endDate, Long organizationId);

    @Query("select count(*) from Order order where order.paymentsDate >=?1 and order.paymentsDate <= ?2 and order.organization.id=?3")
    Long countByDate(Long startDate, Long endDate, Long organizationId);

    @Query("select count(*) from Order order where order.paymentsDate >=?1 and order.paymentsDate <= ?2 and order.organization.id=?3 and order.shipServiceLevel=?4")
    Long countByDateAndShipServiceLevel(Long startDate, Long endDate, Long organizationId, String shipServiceLevel);

}
