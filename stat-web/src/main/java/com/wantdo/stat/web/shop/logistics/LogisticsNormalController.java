package com.wantdo.stat.web.shop.logistics;

import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.entity.front.response.ResponseVo;
import com.wantdo.stat.entity.shop.Express;
import com.wantdo.stat.entity.shop.Order;
import com.wantdo.stat.entity.front.vo.ExcelVo;
import com.wantdo.stat.service.account.ExpressService;
import com.wantdo.stat.service.account.OrganizationService;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.account.UserService;
import com.wantdo.stat.service.shop.logistics.LogisticsNormalService;
import com.wantdo.stat.utils.DateUtil;
import com.wantdo.stat.web.Servlets;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @Date : 2015-9-21
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Controller
@RequestMapping(value = "/logistics/normal")
public class LogisticsNormalController {

    private static final String PAGE_SIZE = "10";

    @Autowired
    private LogisticsNormalService logisticsNormalService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExpressService expressService;

    @Autowired
    private OrganizationService organizationService;

    @RequestMapping(value = "shop", method = RequestMethod.GET)
    public String shopForm(Model model) {
        List<Organization> organizations = userService.getUser(getCurrentUserId()).getOrganizationList();
        model.addAttribute("organizations", organizations);
        return "shop/market/marketNormalShopList";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String normalList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                       @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
                       @RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
                       ServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        Page<Order> orders = logisticsNormalService.SearchOrder(searchParams, pageNumber, pageSize, sortType);
        model.addAttribute("orders", orders);
        model.addAttribute("sortType", sortType);
        // 将搜索条件编码成字符串，用于排序，分页的URL
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

        return "shop/logistics/logisticsNormalList";
    }

    /**
     * 上传excel文件
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseVo upload(MultipartFile uploadExcel) {
        return logisticsNormalService.upload(uploadExcel);
    }

    @RequestMapping(value = "download", method = RequestMethod.GET)
    public @ResponseBody void download(HttpServletResponse response){
        ServletOutputStream sos = null;
        BufferedInputStream bis = null;

        ExcelVo excelVo = logisticsNormalService.download();

        String path = excelVo.getPath();

        try {
            File file = new File(path);
            response.addHeader("content-type", "application/octet-stream;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=\"" + DateUtil.getStringDate() + ".xls" + "\"");

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

        } catch (IOException e){
            e.printStackTrace();
        } finally{
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
        model.addAttribute("order", logisticsNormalService.getOrder(id));
        model.addAttribute("action", "update");
        return "shop/logistics/logisticsNormalForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("order") Order order,
                         @RequestParam("expressName") String expressName ,RedirectAttributes redirectAttributes) {

        Express express = expressService.getExpressByName(expressName);
        order.setExpress(express);
        logisticsNormalService.updateOrder(order);
        redirectAttributes.addFlashAttribute("message", "更新订单成功");
        return "redirect:/logistics/normal/";
    }


    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出OrderMain对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getUser(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("order", logisticsNormalService.getOrder(id));
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
