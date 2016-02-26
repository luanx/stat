package com.wantdo.stat.rest.shop.market;

import com.wantdo.stat.beanvalidator.BeanValidators;
import com.wantdo.stat.entity.front.vo.TableDTO;
import com.wantdo.stat.entity.front.vo.OrderVo;
import com.wantdo.stat.rest.RestException;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.account.UserService;
import com.wantdo.stat.service.shop.market.OrderService;
import com.wantdo.stat.web.MediaTypes;
import com.wantdo.stat.web.Servlets;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;

/**
 * @ Date : 2015-8-27
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@RestController
@RequestMapping(value = "/api/v1/market/order")
public class MarketOrderRestController {

    private static Logger logger = LoggerFactory.getLogger(MarketOrderRestController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private UserService userService;

    @Autowired
    private Validator validator;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public TableDTO<OrderVo> getAllPurchaseOrder(@RequestParam(value = "offset", defaultValue = "1") int offset,
                                                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize,
                                                           @RequestParam(value = "order", defaultValue = "auto") String sortType,
                                                           @RequestParam(value = "search", defaultValue = "") String search,
                                                           @RequestParam(value = "orderType", defaultValue = "all") String orderType,
                                                           @PathVariable("id") Long organizationId,
                                                           ServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        searchParams.put("LIKE_orderId", search);

        int pageNumber = offset/pageSize + 1;

        TableDTO<OrderVo> orders = orderService.getOrderVo(organizationId, searchParams, pageNumber, pageSize, sortType, orderType);
        if (orders == null) {
            String message = "订单不存在";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return orders;
    }

    @RequestMapping(value = "end/{ids}", method = RequestMethod.PUT, consumes = MediaTypes.JSON_UTF_8)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void end(@PathVariable("ids") List<Long> orderDetailChecked) {
        //调用JSR303 Bean Validator进行校验,异常将由RestExceptionHandler统一处理
        BeanValidators.validateWithException(validator, orderDetailChecked);

        //批量结束订单
        orderService.endOrderDetailChecked(orderDetailChecked);
    }

    @RequestMapping(value = "delete/{ids}", method = RequestMethod.DELETE, consumes = MediaTypes.JSON_UTF_8)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("ids") List<Long> orderDetailChecked) {
        //调用JSR303 Bean Validator进行校验,异常将由RestExceptionHandler统一处理
        BeanValidators.validateWithException(validator, orderDetailChecked);

        //批量删除订单
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
