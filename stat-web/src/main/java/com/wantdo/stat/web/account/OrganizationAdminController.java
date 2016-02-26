package com.wantdo.stat.web.account;

import com.google.common.collect.Maps;
import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.service.account.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

/**
 * @Date : 2015-8-26
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Controller
@RequestMapping(value = "/admin/organization")
public class OrganizationAdminController {

    private static final String PAGE_SIZE = "10";

    private static Map<Boolean, String> leafStatus = Maps.newHashMap();

    static {
        leafStatus.put(true, "是");
        leafStatus.put(false, "否");
    }

    @Autowired
    private OrganizationService organizationService;

    @RequestMapping(value = "")
    public String list() {
        return "account/adminOrganizationList";
    }


    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("organization", new Organization());
        model.addAttribute("action", "create");
        return "account/adminOrganizationForm";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Valid Organization newOrganization, RedirectAttributes redirectAttributes) {
        organizationService.saveOrganization(newOrganization);
        return "redirect:/admin/organization";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("organization", organizationService.getOrganization(id));
        model.addAttribute("organizations", organizationService.getAllOrganization());
        model.addAttribute("leafStatus", leafStatus);
        model.addAttribute("action", "update");
        return "account/adminOrganizationForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("organization") Organization organization,
                         @RequestParam("parentId") Long parentId, RedirectAttributes redirectAttributes) {

        Organization parent= organizationService.getOrganization(parentId);
        organization.setParent(parent);
        organizationService.updateOrganization(organization);
        redirectAttributes.addFlashAttribute("message", "更新组织机构成功");
        return "redirect:/admin/organization";
    }

    @RequestMapping(value = "delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Organization organization = organizationService.getOrganization(id);
        organizationService.deleteOrganization(id);
        redirectAttributes.addFlashAttribute("message", "删除组织机构" + organization.getName() + "成功");
        return "redirect:/admin/organization";
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出Organization对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getOrganization(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("organization", organizationService.getOrganization(id));
        }
    }

    /**
     * 不自动绑定对象中的parentId属性，另行处理。
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("parentId");
    }


}

