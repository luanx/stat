package com.wantdo.stat.rest.shop.logistics;

import com.wantdo.stat.beanvalidator.BeanValidators;
import com.wantdo.stat.entity.front.vo.TableDTO;
import com.wantdo.stat.entity.shop.AlibabaOrder;
import com.wantdo.stat.entity.front.vo.LogisticsReceiveVo;
import com.wantdo.stat.rest.RestException;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.account.UserService;
import com.wantdo.stat.service.shop.logistics.LogisticsReceiveService;
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
@RequestMapping(value = "/api/v1/logistics/receive")
public class LogisticsReceiveRestController {

    private static Logger logger = LoggerFactory.getLogger(LogisticsReceiveRestController.class);

    private static final String PAGE_SIZE = "20";

    @Autowired
    private UserService userService;

    @Autowired
    private Validator validator;

    @Autowired
    private LogisticsReceiveService logisticsReceiveService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public TableDTO<LogisticsReceiveVo> getAllPurchaseOrder(@RequestParam(value = "offset", defaultValue = "1") int offset,
                                                           @RequestParam(value = "limit", defaultValue = PAGE_SIZE) int pageSize,
                                                           @RequestParam(value = "order", defaultValue = "auto") String sortType,
                                                           @RequestParam(value = "search", defaultValue = "") String search,
                                                           ServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        searchParams.put("LIKE_trackno", search);

        int pageNumber = offset/pageSize + 1;

        TableDTO<LogisticsReceiveVo> orders = logisticsReceiveService.getLogisticsReceiveVo(searchParams, pageNumber, pageSize, sortType);
        if (orders == null) {
            String message = "阿里巴巴订单不存在";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return orders;
    }

    @RequestMapping(value = "confirm", method = RequestMethod.PUT, consumes = MediaTypes.JSON_UTF_8)
    //按Restful风格约定,返回204状态码,无内容,也可以返回200状态码
    public void confirm(@RequestBody List<LogisticsReceiveVo> logisticsReceiveVoList){
        //调用JSR303 Bean Validator进行异常,异常将由RestExceptionHandler统一处理
        BeanValidators.validateWithException(validator, logisticsReceiveVoList);
        logisticsReceiveService.confirmPurchase(logisticsReceiveVoList);
    }

    @RequestMapping(value = "/paltform_order_id/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public AlibabaOrder getByPlatformOrderId(@PathVariable("id") String platformOrderId) {
        AlibabaOrder alibabaOrder = logisticsReceiveService.getAlibabaOrderByPlatformOrderId(platformOrderId);
        if (alibabaOrder == null) {
            String message = "阿里巴巴订单不存在(platformOrderId:" + platformOrderId + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return alibabaOrder;
    }

    @RequestMapping(value = "/trackno/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public AlibabaOrder getByTrackno(@PathVariable("id") String trackno) {
        AlibabaOrder alibabaOrder = logisticsReceiveService.getAlibabaOrderByTrackno(trackno);
        if (alibabaOrder == null) {
            String message = "阿里巴巴订单不存在(trackno::" + trackno + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return alibabaOrder;
    }

    /**
     * 取出Shiro中的当前用户Id
     */
    public Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }
}
