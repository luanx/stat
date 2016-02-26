package com.wantdo.stat.web.shop.fba;

import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.entity.front.response.ResponseVo;
import com.wantdo.stat.entity.shop.FBA;
import com.wantdo.stat.service.account.OrganizationService;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.account.UserService;
import com.wantdo.stat.service.shop.fba.FBAService;
import com.wantdo.stat.web.Servlets;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Date : 2015-9-22
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@Controller
@RequestMapping(value = "/logistics/fba")
public class FBAController {

    private static final String PAGE_SIZE = "10";

    @Autowired
    private FBAService fbaService;

    @Autowired
    private UserService userService;

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

        Page<FBA> fbas = fbaService.SearchOrder(searchParams, pageNumber, pageSize, sortType);
        model.addAttribute("fbas", fbas);
        model.addAttribute("sortType", sortType);
        // 将搜索条件编码成字符串，用于排序，分页的URL
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

        return "shop/logistics/fbaList";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("fba", new FBA());
        model.addAttribute("organizations", organizationService.getShopOrganization());
        model.addAttribute("action", "create");
        return "shop/logistics/fbaForm";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Valid FBA newFBA, RedirectAttributes redirectAttributes) {
        fbaService.saveFBA(newFBA);
        return "redirect:/shop/logistics";
    }

    /**
     * 上传excel文件
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseVo upload(MultipartFile uploadExcel) {
        return fbaService.upload(uploadExcel);
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("fba", fbaService.getFBA(id));
        model.addAttribute("organizations", organizationService.getShopOrganization());
        model.addAttribute("action", "update");
        return "shop/logistics/fbaForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("fba") FBA fba, @RequestParam("organizationId") Long organizationId,
                         RedirectAttributes redirectAttributes) {

        Organization organization = new Organization(organizationId);
        fba.setOrganization(organization);

        fbaService.updateFBA(fba);
        redirectAttributes.addFlashAttribute("message", "更新FBA成功");
        return "redirect:/logistics/fba/";
    }

    @RequestMapping(value = "delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        FBA fba = fbaService.getFBA(id);
        fbaService.deleteFBA(id);
        redirectAttributes.addFlashAttribute("message", "删除FBA " + fba.getAmazonId() + "成功");
        return "redirect:/logistics/fba/";
    }


    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出FBA对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getFBA(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("order", fbaService.getFBA(id));
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
