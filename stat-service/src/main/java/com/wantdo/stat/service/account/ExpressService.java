package com.wantdo.stat.service.account;

import com.wantdo.stat.dao.shop.ExpressDao;
import com.wantdo.stat.dao.shop.OrderDao;
import com.wantdo.stat.entity.shop.Express;
import com.wantdo.stat.entity.shop.Order;
import com.wantdo.stat.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 快递业务类
 *
 * @ Date : 15/10/8
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Component
public class ExpressService {

    private ExpressDao expressDao;

    private OrderDao orderDao;

    /**
     * 按Id获得用户
     */
    public Express getExpress(Long id) {
        return expressDao.findOne(id);
    }

    public Express getExpressByName(String name){
        return expressDao.findByName(name);
    }

    public List<Order> getAllOrderWithinAMonth(){
        Date startDate = new Date(DateUtil.beforeAMonth());
        Date endDate = new Date();
        return orderDao.findAllByDate(startDate, endDate);
    }

    @Autowired
    public void setExpressDao(ExpressDao expressDao) {
        this.expressDao = expressDao;
    }

    @Autowired
    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }
}
