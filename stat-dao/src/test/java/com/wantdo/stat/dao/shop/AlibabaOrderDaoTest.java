package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.AlibabaOrder;
import com.wantdo.stat.test.spring.SpringContextTestCase;
import com.wantdo.stat.utils.Clock;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Date : 2015-9-22
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class AlibabaOrderDaoTest extends SpringContextTestCase{

    @Autowired
    private AlibabaOrderDao alibabaOrderDao;

    private Clock clock = Clock.DEFAULT;

    @Test
    public void findAlibabaOrderByOrderId() throws Exception{
        AlibabaOrder alibabaOrder = alibabaOrderDao.findOne(1L);
        assertThat(alibabaOrder.getBuyerName()).isEqualTo("杭州安致文化创意有限公司");
    }

}
