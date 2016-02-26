package com.wantdo.stat.web.account;

import com.wantdo.stat.entity.account.Resource;
import com.wantdo.stat.service.account.ResourceService;
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
@RequestMapping(value = "/admin/resource")
public class ResourceAdminController {

    private static final String PAGE_SIZE = "10";

    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "")
    public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
                       @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
                       @RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model,
                       ServletRequest request) {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

        Page<Resource> resources = resourceService.SearchResource(searchParams, pageNumber, pageSize, sortType);
        model.addAttribute("resources", resources);
        model.addAttribute("sortType", sortType);
        // 将搜索条件编码成字符串，用于排序，分页的URL
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

        return "account/adminResourceList";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("resource", new Resource());
        model.addAttribute("action", "create");
        return "account/adminResourceForm";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Valid Resource newResource, RedirectAttributes redirectAttributes) {
        resourceService.saveResource(newResource);
        return "redirect:/admin/resource";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("resource", resourceService.getResource(id));
       return "account/adminResourceForm";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("resource") Resource resource, RedirectAttributes redirectAttributes) {

        resourceService.saveResource(resource);
        redirectAttributes.addFlashAttribute("message", "更新资源成功");
        return "redirect:/admin/resource";
    }

    @RequestMapping(value = "delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Resource resource = resourceService.getResource(id);
        resourceService.deleteResource(id);
        redirectAttributes.addFlashAttribute("message", "删除资源" + resource.getName() + "成功");
        return "redirect:/admin/resource";
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出Resource对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getResource(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("resource", resourceService.getResource(id));
        }
    }


}
