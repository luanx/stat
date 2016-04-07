package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.StockOrder;
import com.wantdo.stat.test.spring.SpringContextTestCase;
import com.wantdo.stat.utils.Clock;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * @Date : 2015-9-22
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class StockOrderDaoTest extends SpringContextTestCase{

    @Autowired
    private StockOrderDao stockOrderDao;

    private Clock clock = Clock.DEFAULT;

    @Test
    public void findOrderByOrderId() throws Exception{
        StockOrder stockOrder = stockOrderDao.findByOrderId("701-8324437-8882636");
        System.out.println(stockOrder);
    }

    @Test
    public void findLastRecord() throws Exception {
        StockOrder stockOrder = stockOrderDao.findFirstByOrderByIdDesc();
        System.out.println(stockOrder.getId());
    }

}
