package com.wantdo.stat.web.account;

import com.google.common.collect.Maps;
import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.service.account.UserService;
import com.wantdo.stat.web.Servlets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * @Date : 2015-8-25
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Controller
@RequestMapping(value = "/admin/user")
public class UserAdminController {

    private static final String PAGE_SIZE = "10";

    private static Map<String, String> allStatus = Maps.newHashMap();

    static {
        allStatus.put("enabled", "有效");
        allStatus.put("disabled", "无效");
    }

    @Autowired
    private UserService userService;

    @RequestMapping(value = "")
    public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                       @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
                       @RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
                       ServletRequest request){
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        Page<User> users = userService.SearchUser(searchParams, pageNumber, pageSize, sortType);
        model.addAttribute("users", users);
        model.addAttribute("sortType", sortType);
        model.addAttribute("allStatus", allStatus);
        // 将搜索条件编码成字符串，用于排序，分页的URL
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

        return "account/adminUserList";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("allStatus", allStatus);
        return "account/adminUserForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {

        userService.updateUser(user);
        redirectAttributes.addFlashAttribute("message", "更新用户成功");
        return "redirect:/admin/user";
    }

    @RequestMapping(value = "delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        User user = userService.getUser(id);
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("message", "删除用户" + user.getLoginName() + "成功");
        return "redirect:/admin/user";
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getUser(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("user", userService.getUser(id));
        }
    }

    /**
     * 不自动绑定对象中的roleList属性，另行处理
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("roleList");
    }

}
