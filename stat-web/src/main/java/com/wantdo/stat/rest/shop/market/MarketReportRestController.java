package com.wantdo.stat.rest.shop.market;

import com.github.abel533.echarts.Option;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.shop.market.ReportService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Validator;

/**
 * @ Date : 2015-8-27
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@RestController
@RequestMapping(value = "/api/v1/market/report")
public class MarketReportRestController {

    private static Logger logger = LoggerFactory.getLogger(MarketReportRestController.class);

    private static final int ONE_WEEK = 7;


    @Autowired
    private ReportService reportService;

    @Autowired
    private Validator validator;


    @RequestMapping(value = "total/{id}", method = RequestMethod.POST)
    public Option total(@RequestParam("startDate")String startDate, @RequestParam("endDate")String endDate, @PathVariable("id") Long organizationId) {
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)){
            startDate = new DateTime().minusDays(ONE_WEEK).toString();
            endDate = new DateTime().toString();
        }
        Option option =reportService.getTotalOption(startDate, endDate, organizationId);
        return option;
    }

    @RequestMapping(value = "area/{id}", method = RequestMethod.POST)
    public Option area(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @PathVariable("id") Long organizationId) {
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            startDate = new DateTime().minusDays(ONE_WEEK).toString();
            endDate = new DateTime().toString();
        }
        Option option = reportService.getAreaOption(startDate, endDate, organizationId);
        return option;
    }

    @RequestMapping(value = "purchase/{id}", method = RequestMethod.POST)
    public Option purchase(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @PathVariable("id") Long organizationId) {
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            startDate = new DateTime().minusDays(ONE_WEEK).toString();
            endDate = new DateTime().toString();
        }
        Option option = reportService.getPurchaseOption(startDate, endDate, organizationId);
        return option;
    }

    @RequestMapping(value = "payments/{id}", method = RequestMethod.POST)
    public Option payments(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @PathVariable("id") Long organizationId) {
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            startDate = new DateTime().minusDays(ONE_WEEK).toString();
            endDate = new DateTime().toString();
        }
        Option option = reportService.getPaymentsOption(startDate, endDate, organizationId);
        return option;
    }

    @RequestMapping(value = "shipservicelevel/{id}", method = RequestMethod.POST)
    public Option shipservicelevel(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @PathVariable("id") Long organizationId) {
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            startDate = new DateTime().minusDays(ONE_WEEK).toString();
            endDate = new DateTime().toString();
        }
        Option option = reportService.getShipServiceLevelOption(startDate, endDate, organizationId);
        return option;
    }

    /**
     * 取出Shiro中的当前用户Id
     */
    public Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }
}
