package com.wantdo.stat.web.shop.market;

import com.google.common.collect.Maps;
import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.entity.front.response.ResponseVo;
import com.wantdo.stat.entity.shop.Express;
import com.wantdo.stat.entity.shop.Order;
import com.wantdo.stat.service.account.ExpressService;
import com.wantdo.stat.service.account.OrganizationService;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.account.UserService;
import com.wantdo.stat.service.shop.market.OrderService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 日常订单
 *
 * @Date : 2015-9-17
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@Controller
@RequestMapping(value = "/market/order")
public class MarketOrderController {

    private static Map<String, String> orderTypes = Maps.newLinkedHashMap();
    static {
        orderTypes.put("all", "所有订单");
        orderTypes.put("today", "今日订单");
        orderTypes.put("exception", "异常订单");
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExpressService expressService;

    @Autowired
    private OrganizationService organizationService;


    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        List<Organization> organizations = userService.getUser(getCurrentUserId()).getOrganizationList();
        model.addAttribute("organizations", organizations);
        model.addAttribute("orderTypes", orderTypes);
        return "shop/market/marketOrderList";
    }

    /**
     * 上传excel文件
     */
    @RequestMapping(value = "upload/{organizationId}", method = RequestMethod.POST)
    public @ResponseBody
    ResponseVo upload(MultipartFile uploadExcel, @PathVariable("organizationId") Long organizationId){
        return orderService.upload(uploadExcel, organizationId);
    }


    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("order", orderService.getOrder(id));
        model.addAttribute("action", "update");
        return "shop/market/marketNormalForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("order") Order order,
                         @RequestParam ("expressName") String expressName,
                         @RequestParam("organizationId") Long organizationId,
                         RedirectAttributes redirectAttributes) {

        Express express = expressService.getExpressByName(expressName);
        order.setExpress(express);

        orderService.updateOrder(order);
        redirectAttributes.addFlashAttribute("message", "更新订单成功");
        return "redirect:/market/normal/" + organizationId;
    }

    @RequestMapping(value = "delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrder(id);
        Long organizationId = order.getOrganization().getId();
        orderService.deleteOrder(id);
        redirectAttributes.addFlashAttribute("message", "删除订单成功");
        return "redirect:/market/normal/" + organizationId;
    }

    @RequestMapping(value = "end/{id}")
    public String end(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrder(id);
        Long organizationId = order.getOrganization().getId();
        orderService.endOrder(id);
        redirectAttributes.addFlashAttribute("message", "结束订单成功");
        return "redirect:/market/normal/" + organizationId;
    }


    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出Order对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getMarket(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("order", orderService.getOrder(id));
        }
    }

    /**
     * 取出Shiro中的当前用户Id.
     */
    private Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }

}
