package com.wantdo.stat.web.shop.market;

import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.account.UserService;
import com.wantdo.stat.service.shop.market.ReportService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 *
 * 数据报告Controller
 *
 * @ Date : 16/2/19
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Controller
@RequestMapping(value = "/market/report")
public class MarketReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String total(Model model) {
        List<Organization> organizations = userService.getUser(getCurrentUserId()).getOrganizationList();
        model.addAttribute("organizations", organizations);
        return "shop/market/marketReport";
    }

    @RequestMapping(value = "area", method = RequestMethod.GET)
    public String area(Model model) {
        List<Organization> organizations = userService.getUser(getCurrentUserId()).getOrganizationList();
        model.addAttribute("organizations", organizations);
        return "shop/market/marketAreaReport";
    }

    @RequestMapping(value = "purchase", method = RequestMethod.GET)
    public String purchase(Model model) {
        List<Organization> organizations = userService.getUser(getCurrentUserId()).getOrganizationList();
        model.addAttribute("organizations", organizations);
        return "shop/market/marketPurchaseReport";
    }

    @RequestMapping(value = "payments", method = RequestMethod.GET)
    public String payments(Model model) {
        List<Organization> organizations = userService.getUser(getCurrentUserId()).getOrganizationList();
        model.addAttribute("organizations", organizations);
        return "shop/market/marketPaymentsReport";
    }

    @RequestMapping(value = "shipservicelevel", method = RequestMethod.GET)
    public String shipServiceLevel(Model model) {
        List<Organization> organizations = userService.getUser(getCurrentUserId()).getOrganizationList();
        model.addAttribute("organizations", organizations);
        return "shop/market/marketShipServiceLevelReport";
    }

    /**
     * 取出Shiro中的当前用户Id.
     */
    private Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }
}
