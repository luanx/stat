package com.wantdo.stat.service.shop.stock;

import com.wantdo.stat.dao.shop.PlatformDao;
import com.wantdo.stat.entity.shop.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @ Date : 16/4/5
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Component
@Transactional
public class PlatformService {

    private static Logger logger = LoggerFactory.getLogger(PlatformService.class);

    @Autowired
    private PlatformDao platformDao;

    public List<Platform> getAllPlatform(){
        return (List<Platform>) platformDao.findAll();
    }

}
