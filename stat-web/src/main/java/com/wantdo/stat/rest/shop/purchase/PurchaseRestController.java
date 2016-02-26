package com.wantdo.stat.rest.shop.purchase;

import com.wantdo.stat.beanvalidator.BeanValidators;
import com.wantdo.stat.entity.front.vo.TableDTO;
import com.wantdo.stat.entity.front.vo.PurchaseVo;
import com.wantdo.stat.rest.RestException;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.account.UserService;
import com.wantdo.stat.service.shop.purchase.PurchaseService;
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
@RequestMapping(value = "/api/v1/purchase")
public class PurchaseRestController {

    private static Logger logger = LoggerFactory.getLogger(PurchaseRestController.class);

    private static final String PAGE_SIZE = "10";

    @Autowired
    private UserService userService;

    @Autowired
    private Validator validator;

    @Autowired
    private PurchaseService purchaseService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public TableDTO<PurchaseVo> getAllPurchaseOrder(@RequestParam(value = "offset", defaultValue = "1") int offset,
                                                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize,
                                                           @RequestParam(value = "order", defaultValue = "auto") String sortType,
                                                           @RequestParam(value = "search", defaultValue = "") String search,
                                                           @RequestParam(value = "orderType", defaultValue = "all") String orderType,
                                                           ServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        searchParams.put("LIKE_purchaseOrderId", search);

        int pageNumber = offset/pageSize + 1;

        TableDTO<PurchaseVo> purchases = purchaseService.getPurchaseVo(searchParams, pageNumber, pageSize, sortType);
        if (purchases == null) {
            String message = "订单不存在";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return purchases;
    }


    @RequestMapping(value = "/{ids}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("ids") List<Long> purchaseDetailChecked){
        //调用JSR303 Bean Validator进行校验,异常将由RestExceptionHandler统一处理
        BeanValidators.validateWithException(validator, purchaseDetailChecked);

        //删除采购订单
        purchaseService.deletePurchaseDetailChecked(purchaseDetailChecked);
    }

    /**
     * 取出Shiro中的当前用户Id
     */
    public Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }
}
