package com.wantdo.stat.dao.shop;

import com.wantdo.stat.test.spring.SpringContextTestCase;
import com.wantdo.stat.utils.Clock;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

/**
 * @Date : 2015-9-22
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class OrderAreaDaoTest extends SpringContextTestCase{

    @Autowired
    private OrderAreaDao orderAreaDao;


    private Clock clock = Clock.DEFAULT;

    @Test
    public void countByDateAndOrganizationId(){
        Date startDate = new DateTime().minusDays(90).withTimeAtStartOfDay().toDate();
        Date endDate = new DateTime().toDate();
        List<Object[]> lists = orderAreaDao.countByDateAndOrganizationId(startDate, endDate, 30L);
        System.out.println(lists);
    }

    @Test
    public void countByDate() {
        Date startDate = new DateTime().minusDays(90).withTimeAtStartOfDay().toDate();
        Date endDate = new DateTime().toDate();
        List<Object[]> lists = orderAreaDao.countByDate(startDate, endDate);
        System.out.println(lists);
    }

}
