package com.wantdo.stat.dao.shop;

import com.wantdo.stat.dao.finance.IncomeDao;
import com.wantdo.stat.entity.finance.Income;
import com.wantdo.stat.test.spring.SpringContextTestCase;
import com.wantdo.stat.utils.Clock;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * @Date : 2015-9-22
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class IncomeDaoTest extends SpringContextTestCase{

    @Autowired
    private IncomeDao incomeDao;

    private Clock clock = Clock.DEFAULT;

    @Test
    public void findOrderByOrderId() throws Exception{
        List<Income> incomeList = incomeDao.findAllLastUpdate();
        System.out.println(incomeList);
    }

}
