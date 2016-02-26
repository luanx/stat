package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.OrderDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * @ Date : 2015-9-21
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public interface OrderDetailDao extends PagingAndSortingRepository<OrderDetail, Long>, JpaSpecificationExecutor<OrderDetail> {

    @Query("select orderDetail from OrderDetail orderDetail where orderDetail.created >=?1 and orderDetail.created <= ?2 and orderStatus.id=2")
    List<OrderDetail> findAllTodayNormal(Date startDate, Date endDate);

    @Query("select orderDetail from OrderDetail orderDetail where orderDetail.created >=?1 and orderDetail.created <= ?2")
    List<OrderDetail> findAllToday(Date startDate, Date endDate);

    @Query("select orderDetail from OrderDetail orderDetail where orderDetail.changed=1 and orderDetail.orderStatus.id = 2")
    List<OrderDetail> findAllChanged();

    OrderDetail findByOrderIdAndOrderItemId(String orderId, String orderItemId);


    @Modifying
    @Query("delete from OrderDetail orderDetail where orderDetail.order.id=?1")
    void deleteByOrder(Long id);

    @Query("select orderDetail from OrderDetail orderDetail where orderDetail.orderStatus=1 and orderDetail.sku = ?1")
    List<OrderDetail> findAllOrderDetailExceptionBySku(String sku);

}
