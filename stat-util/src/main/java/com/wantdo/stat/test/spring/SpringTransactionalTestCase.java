package com.wantdo.stat.test.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.sql.DataSource;

/**
 * @ Date : 15/10/7
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@ActiveProfiles(Profiles.UNIT_TEST)
public abstract class SpringTransactionalTestCase  extends AbstractTransactionalJUnit4SpringContextTests{

    protected DataSource dataSource;


    @Override
    @Autowired
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
        this.dataSource = dataSource;
    }
}
