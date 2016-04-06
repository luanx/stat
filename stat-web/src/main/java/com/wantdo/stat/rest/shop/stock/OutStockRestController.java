package com.wantdo.stat.rest.shop.stock;

import com.wantdo.stat.entity.front.vo.StockOrderVo;
import com.wantdo.stat.entity.front.vo.TableDTO;
import com.wantdo.stat.rest.RestException;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.account.UserService;
import com.wantdo.stat.service.shop.stock.StockService;
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
import java.util.Map;

/**
 * @ Date : 16/4/5
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
@RestController
@RequestMapping(value = "/api/v1/com.wantdo.stat.web.shop.stock.stock/outstock")
public class OutStockRestController {

    private static Logger logger = LoggerFactory.getLogger(OutStockRestController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private UserService userService;

    @Autowired
    private Validator validator;

    @Autowired
    private StockService stockService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public TableDTO<StockOrderVo> getAllPurchaseOrder(@RequestParam(value = "offset", defaultValue = "1") int offset,
                                                 @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize,
                                                 @RequestParam(value = "order", defaultValue = "auto") String sortType,
                                                 @RequestParam(value = "search", defaultValue = "") String search,
                                                 @RequestParam(value = "orderType", defaultValue = "all") String orderType,
                                                 @PathVariable("id") Long platformId,
                                                 ServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        searchParams.put("LIKE_orderId", search);

        int pageNumber = offset / pageSize + 1;

        TableDTO<StockOrderVo> stocks = stockService.getsStockOrderVo(platformId, searchParams, pageNumber, pageSize, sortType, orderType);
        if (stocks == null) {
            String message = "订单不存在";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return stocks;
    }

    /**
     * 取出Shiro中的当前用户Id
     */
    public Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }
}

