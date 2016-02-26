package com.wantdo.stat.rest.shop.purchase;

import com.wantdo.stat.entity.front.vo.OrderVo;
import com.wantdo.stat.entity.front.vo.TableDTO;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.validation.Validator;
import java.util.Map;

/**
 * @ Date : 2015-8-27
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@RestController
@RequestMapping(value = "/api/v1/purchase/order")
public class PurchaseOrderRestController {

    private static Logger logger = LoggerFactory.getLogger(PurchaseOrderRestController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private UserService userService;

    @Autowired
    private Validator validator;

    @Autowired
    private OrderService orderService;

    @RequestMapping( method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public TableDTO<OrderVo> getAllPurchaseOrder(@RequestParam(value = "offset", defaultValue = "1") int offset,
                                                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize,
                                                           @RequestParam(value = "order", defaultValue = "auto") String sortType,
                                                           @RequestParam(value = "search", defaultValue = "") String search,
                                                           @RequestParam(value = "orderType", defaultValue = "all") String orderType,
                                                           ServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        searchParams.put("LIKE_orderId", search);

        int pageNumber = offset/pageSize + 1;

        TableDTO<OrderVo> orders = orderService.getAllOrderVo(searchParams, pageNumber, pageSize, sortType, orderType);
        if (orders == null) {
            String message = "订单不存在";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return orders;
    }

    @RequestMapping(value = "/exception/changed",method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public boolean orderChanged() {
        boolean changed= orderService.orderChanged();
        if (!changed) {
            String message = "没有修改过的异常订单";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return true;
    }

    /**
     * 取出Shiro中的当前用户Id
     */
    public Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }
}
