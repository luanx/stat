package com.wantdo.stat.dao.account;

import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.test.spring.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Date : 2015-9-22
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class OrganizationDaoTest extends SpringContextTestCase {

    @Autowired
    private OrganizationDao organizationDao;

    @Test
    public void findAllOrders() throws Exception {
        List<Organization> organizationList = (List<Organization>)organizationDao.findAll();
        assertThat(organizationList.size()).isEqualTo(36);
    }

}