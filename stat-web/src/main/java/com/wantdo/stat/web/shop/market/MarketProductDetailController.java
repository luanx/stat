package com.wantdo.stat.web.shop.market;

import com.wantdo.stat.entity.shop.Product;
import com.wantdo.stat.entity.shop.ProductDetail;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.shop.market.ProductService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * 产品详情管理
 *
 * @Date : 2015-9-17
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@Controller
@RequestMapping(value = "/market/product_detail")
public class MarketProductDetailController {


    @Autowired
    private ProductService productService;


    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("productDetail", productService.getProductDetail(id));
        model.addAttribute("action", "update");
        return "shop/market/marketProductDetailForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("productDetail") ProductDetail productDetail,
                         RedirectAttributes redirectAttributes) {

        Product product = productDetail.getProduct();

        productService.updateProductDetail(productDetail);
        redirectAttributes.addFlashAttribute("message", "更新产品详情成功");
        return "redirect:/market/product/update/" + product.getId();
    }

    @RequestMapping(value = "delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Product product = productService.getProductDetail(id).getProduct();
        productService.deleteProductDetail(id);
        redirectAttributes.addFlashAttribute("message", "删除产品详情成功");
        return "redirect:/market/product/update/" + product.getId();
    }


    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出ProductDetail对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getProductDetail(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("productDetail", productService.getProductDetail(id));
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
