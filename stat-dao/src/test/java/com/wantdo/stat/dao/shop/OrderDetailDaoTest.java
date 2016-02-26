package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.OrderDetail;
import com.wantdo.stat.test.spring.SpringContextTestCase;
import com.wantdo.stat.utils.DateUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Date : 2015-9-22
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class OrderDetailDaoTest extends SpringContextTestCase {

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Test
    public void findOrderByOrderId() throws Exception {
        OrderDetail orderDetail = orderDetailDao.findByOrderIdAndOrderItemId("102-3721372-3905027", "17991704406754");
        assertThat(orderDetail.getId()).isEqualTo(1);
    }

    @Test
    public void findAllOrderToday() throws Exception{
        Date startDate = new Date(DateUtil.startOfToday());
        Date endDate = new Date(DateUtil.endOfToday());
        List<OrderDetail> orderDetailList = orderDetailDao.findAllToday(startDate, endDate);
        assertThat(orderDetailList.size()).isEqualTo(0);
    }

    @Test
    public void findAllOrderDetailExceptionBySku() throws Exception {
        List<OrderDetail> orderDetailList = orderDetailDao.findAllOrderDetailExceptionBySku("ZXHW2287SZ2XLwomens");
        System.out.println(orderDetailList);
    }
}