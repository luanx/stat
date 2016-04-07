package com.wantdo.stat.web.shop.stock;

import com.wantdo.stat.entity.front.response.ResponseVo;
import com.wantdo.stat.entity.shop.Platform;
import com.wantdo.stat.entity.shop.StockProduct;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.account.UserService;
import com.wantdo.stat.service.shop.stock.PlatformService;
import com.wantdo.stat.service.shop.stock.StockProductService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * 日常订单
 *
 * @Date : 2015-9-17
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@Controller
@RequestMapping(value = "/stock/product")
public class StockProductController {

    @Autowired
    private UserService userService;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private StockProductService stockProductService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        List<Platform> platforms = platformService.getAllPlatform();
        model.addAttribute("platforms", platforms);
        return "shop/stock/stockProductList";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("stockProduct") StockProduct stockProduct,
                         RedirectAttributes redirectAttributes) {

        stockProductService.updateStockProduct(stockProduct);
        redirectAttributes.addFlashAttribute("message", "更新产品成功");
        return "redirect:/stock/product/";
    }

    /**
     * 上传excel文件
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseVo upload(MultipartFile uploadExcel) {
        return stockProductService.upload(uploadExcel);
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出StockProduct对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getMarket(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("stockProduct", stockProductService.getStockProduct(id));
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
