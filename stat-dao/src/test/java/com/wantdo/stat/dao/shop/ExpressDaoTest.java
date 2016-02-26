package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.shop.Express;
import com.wantdo.stat.test.spring.SpringContextTestCase;
import com.wantdo.stat.utils.Clock;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @ Date : 15/10/9
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class ExpressDaoTest extends SpringContextTestCase {

    @Autowired
    private ExpressDao expressDao;

    private Clock clock = Clock.DEFAULT;

    @Test
    public void findExpressByName() throws Exception{
        Express express = expressDao.findByName("全球邮政");
        assertThat(express.getId()).isEqualTo(1L);
    }


}

