package com.wantdo.stat.web.shop.logistics;

import com.wantdo.stat.entity.shop.PurchaseDetail;
import com.wantdo.stat.entity.shop.ReceiveStatus;
import com.wantdo.stat.service.account.ExpressService;
import com.wantdo.stat.service.account.OrganizationService;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.account.UserService;
import com.wantdo.stat.service.shop.logistics.LogisticsReceiveService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @Date : 2015-9-21
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Controller
@RequestMapping(value = "/logistics/receive")
public class LogisticsReceiveController {

    private static final String PAGE_SIZE = "10";

    @Autowired
    private LogisticsReceiveService logisticsReceiveService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExpressService expressService;

    @Autowired
    private OrganizationService organizationService;

    @RequestMapping(method = RequestMethod.GET)
    public String normalList(Model model) {

        return "shop/logistics/logisticsReceiveList";
    }


    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("purchaseDetail", logisticsReceiveService.getPurchaseDetail(id));
        model.addAttribute("allStatus", logisticsReceiveService.getAllReceiveStatus());
        model.addAttribute("action", "update");
        return "shop/logistics/logisticsReceiveForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("purchaseDetail") PurchaseDetail purchaseDetail,
                         @RequestParam("receiveStatus.id") Long receiveStatusId,
            RedirectAttributes redirectAttributes) {
        ReceiveStatus receiveStatus = logisticsReceiveService.getReceiveStatus(receiveStatusId);
        if (receiveStatus != null){
            purchaseDetail.setReceiveStatus(receiveStatus);
        }
        logisticsReceiveService.updatePurchaseDetail(purchaseDetail);
        redirectAttributes.addFlashAttribute("message", "更新采购订单成功");
        return "redirect:/logistics/receive/";
    }


    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出PurchaseDetail对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getUser(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("purchaseDetail", logisticsReceiveService.getPurchaseDetail(id));
        }
    }

    /**
     * 取出Shiro中的当前用户Id.
     */
    private Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }

    /**
     * 不自动绑定对象中的roleList属性,另行处理
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder){
        binder.setDisallowedFields("receiveStatus.id");
    }


}
