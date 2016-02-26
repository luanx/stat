package com.wantdo.stat.web.account;

import com.wantdo.stat.entity.account.Role;
import com.wantdo.stat.service.account.RoleService;
import com.wantdo.stat.web.Servlets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * @Date : 2015-8-26
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Controller
@RequestMapping(value = "/admin/role")
public class RoleAdminController {

    private static final String PAGE_SIZE = "10";

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "")
    public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                       @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
                       @RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
                       ServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        Page<Role> roles = roleService.SearchRole(searchParams, pageNumber, pageSize, sortType);
        model.addAttribute("roles", roles);
        model.addAttribute("sortType", sortType);
        // 将搜索条件编码成字符串，用于排序，分页的URL
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

        return "account/adminRoleList";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("role", new Role());
        model.addAttribute("action", "create");
        return "account/adminRoleForm";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Valid Role newRole, RedirectAttributes redirectAttributes) {
        roleService.saveRole(newRole);
        return "redirect:/admin/role";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("role", roleService.getRole(id));
        model.addAttribute("action", "update");
       return "account/adminRoleForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("role") Role role, RedirectAttributes redirectAttributes) {

        roleService.updateRole(role);
        redirectAttributes.addFlashAttribute("message", "更新角色成功");
        return "redirect:/admin/role";
    }

    @RequestMapping(value = "delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Role role = roleService.getRole(id);
        roleService.deleteRole(id);
        redirectAttributes.addFlashAttribute("message", "删除用户" + role.getName() + "成功");
        return "redirect:/admin/role";
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出Role对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getRole(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("role", roleService.getRole(id));
        }
    }


}
