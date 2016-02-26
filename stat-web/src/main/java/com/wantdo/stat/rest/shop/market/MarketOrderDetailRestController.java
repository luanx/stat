package com.wantdo.stat.rest.shop.market;

import com.wantdo.stat.beanvalidator.BeanValidators;
import com.wantdo.stat.entity.front.vo.OrderVo;
import com.wantdo.stat.rest.RestException;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.shop.market.OrderService;
import com.wantdo.stat.web.MediaTypes;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Validator;
import java.util.List;

/**
 * @ Date : 2015-8-27
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@RestController
@RequestMapping(value = "/api/v1/market/order_detail")
public class MarketOrderDetailRestController {

    private static Logger logger = LoggerFactory.getLogger(MarketOrderDetailRestController.class);


    @Autowired
    private Validator validator;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public OrderVo get(@PathVariable("id") Long id) {
        OrderVo orderVo = orderService.getOrderVoById(id);
        if (orderVo == null){
            String message = "订单详情不存在(id:" + id + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return orderVo;
    }

    @RequestMapping(value = "end/{ids}", method = RequestMethod.PUT, consumes = MediaTypes.JSON_UTF_8)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void end(@PathVariable("ids") List<Long> orderDetailChecked) {
        //调用JSR303 Bean Validator进行校验,异常将由RestExceptionHandler统一处理
        BeanValidators.validateWithException(validator, orderDetailChecked);

        //批量结束订单
        orderService.endOrderDetailChecked(orderDetailChecked);
    }

    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("ids") List<Long> orderDetailChecked) {
        //调用JSR303 Bean Validator进行校验,异常将由RestExceptionHandler统一处理
        BeanValidators.validateWithException(validator, orderDetailChecked);

        //删除采购订单
        orderService.deleteOrderDetailChecked(orderDetailChecked);
    }

    /**
     * 取出Shiro中的当前用户Id
     */
    public Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }
}
