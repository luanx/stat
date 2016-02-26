package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.entity.shop.Order;
import com.wantdo.stat.test.spring.SpringContextTestCase;
import com.wantdo.stat.utils.Clock;
import com.wantdo.stat.utils.DateUtil;
import com.wantdo.stat.utils.JodaTime;
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
public class OrderDaoTest extends SpringContextTestCase{

    @Autowired
    private OrderDao orderDao;


    private Clock clock = Clock.DEFAULT;

    @Test
    public void findOrderByOrderId() throws Exception{
        Order order = orderDao.findByOrderId("111-7170718-1674608");
        assertThat(order.getId()).isEqualTo(1);
        System.out.println(order.getOrderDetailList());
    }

    @Test
    public void findOrderByTrackno() throws Exception {
        Order order = orderDao.findByTrackno("333");
        assertThat(order.getId()).isEqualTo(1);
    }

    @Test
    public void saveOrder() throws Exception{
        Order order = new Order();
        order.setOrderId("222");
        Organization organization = new Organization(1L);
        order.setOrganization(organization);
        Long purchaseDateTime = JodaTime.stringToDateTime("2015-06-24T07:17:15-07:00").getMillis();
        order.setPurchaseDate(purchaseDateTime);
        orderDao.save(order);
    }

    @Test
    public void findAllOrderByDate() throws Exception {
        Date startDate = new Date(DateUtil.beforeAMonth());
        Date endDate = new Date();

        List<Order> orderList = orderDao.findAllByDate(startDate, endDate);
        assertThat(orderList.size()).isEqualTo(15);
    }

}
