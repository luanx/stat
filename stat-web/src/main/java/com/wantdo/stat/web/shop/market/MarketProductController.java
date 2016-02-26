package com.wantdo.stat.web.shop.market;

import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.entity.front.response.ResponseVo;
import com.wantdo.stat.entity.shop.Product;
import com.wantdo.stat.entity.front.vo.ExcelVo;
import com.wantdo.stat.service.account.OrganizationService;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.account.UserService;
import com.wantdo.stat.service.shop.market.ProductService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 产品管理
 *
 * @ Date : 2015-9-17
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
@Controller
@RequestMapping(value = "/market/product")
public class MarketProductController {

    private static final String PAGE_SIZE = "20";

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        List<Organization> organizations = userService.getUser(getCurrentUserId()).getOrganizationList();
        model.addAttribute("organizations", organizations);

        return "shop/market/marketProductList";
    }

    /**
     * 上传excel文件
     */
    @RequestMapping(value = "upload/{organizationId}", method = RequestMethod.POST)
    public @ResponseBody
    ResponseVo upload(MultipartFile uploadExcel, @PathVariable("organizationId") Long organizationId){
        return productService.upload(uploadExcel, organizationId);
    }

    /**
     * 下载产品模板
     */
    @RequestMapping(value = "download_template", method = RequestMethod.GET)
    public
    @ResponseBody
    void downloadTemplate(HttpServletResponse response) {

        ServletOutputStream sos = null;
        BufferedInputStream bis = null;

        ExcelVo excelVo = productService.downloadTemplate();

        String path = excelVo.getPath();

        try {
            File file = new File(path);
            response.addHeader("content-type", "application/octet-stream;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=\"" + "product.template" + ".xls" + "\"");

            sos = response.getOutputStream();


            bis = new BufferedInputStream(new FileInputStream(file));
            final int BUFF_SIZE = 1024 * 4;
            byte[] buff = new byte[BUFF_SIZE];
            int len = 0;
            while ((len = bis.read(buff)) != -1) {
                sos.write(buff, 0, len);
            }
            sos.flush();
            sos.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sos != null) {
                try {
                    sos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 下载产品
     */
    @RequestMapping(value = "download_product/{organizationId}", method = RequestMethod.GET)
    public
    @ResponseBody
    void downloadProduct(@PathVariable("organizationId") Long organizationId, HttpServletResponse response) {

        ServletOutputStream sos = null;
        BufferedInputStream bis = null;

        ExcelVo excelVo = productService.downloadProduct(organizationId);

        String path = excelVo.getPath();

        try {
            File file = new File(path);
            response.addHeader("content-type", "application/octet-stream;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=\"" + "product" + ".xls" + "\"");

            sos = response.getOutputStream();


            bis = new BufferedInputStream(new FileInputStream(file));
            final int BUFF_SIZE = 1024 * 4;
            byte[] buff = new byte[BUFF_SIZE];
            int len = 0;
            while ((len = bis.read(buff)) != -1) {
                sos.write(buff, 0, len);
            }
            sos.flush();
            sos.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sos != null) {
                try {
                    sos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.getProduct(id));
        model.addAttribute("action", "update");
        return "shop/market/marketProductForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("product") Product product,
                         RedirectAttributes redirectAttributes) {

        productService.updateProduct(product);
        redirectAttributes.addFlashAttribute("message", "更新产品成功");
        return "redirect:/market/product";
    }

    @RequestMapping(value = "delete/{organizationId}/{id}")
    public String delete(@PathVariable("organizationId") Long organizationId ,@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "删除产品成功");
        return "redirect:/market/product/" + organizationId;
    }


    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出Product对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getProduct(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("product", productService.getProduct(id));
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
