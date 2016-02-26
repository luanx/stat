package com.wantdo.stat.web.shop.purchase;

import com.wantdo.stat.entity.shop.PurchaseDetail;
import com.wantdo.stat.service.account.OrganizationService;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.shop.purchase.PurchaseService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * 日常采购
 *
 * @Date : 2015-9-25
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Controller
@RequestMapping(value = "/purchase/detail")
public class PurchaseDetailController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private OrganizationService organizationService;



    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("purchase", purchaseService.getPurchase(id));
        model.addAttribute("organizations", organizationService.getShopOrganization());
        model.addAttribute("action", "update");
        return "shop/purchase/purchaseForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("purchaseDetail")PurchaseDetail purchaseDetail, RedirectAttributes redirectAttributes) {

        purchaseService.updatePurchaseDetail(purchaseDetail);
        redirectAttributes.addFlashAttribute("message", "更新采购订单成功");
        return "redirect:/purchase";
    }


    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出PurchaseDetail对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getPurchase(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("purchaseDetail", purchaseService.getPurchaseDetail(id));
        }
        System.out.println(model);
    }

    /**
     * 取出Shiro中的当前用户Id.
     */
    private Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }

}
