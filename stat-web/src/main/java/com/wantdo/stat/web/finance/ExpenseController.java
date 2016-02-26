package com.wantdo.stat.web.finance;

import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.entity.finance.Expense;
import com.wantdo.stat.service.account.OrganizationService;
import com.wantdo.stat.service.account.ShiroDbRealm.ShiroUser;
import com.wantdo.stat.service.account.UserService;
import com.wantdo.stat.service.finance.ExpenseService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * @Date : 2015-8-30
 * @From : stat
 * @Author : luanx@wantdo.com
 */
@Controller
@RequestMapping(value = "/finance/expense")
public class ExpenseController {

    private static final String PAGE_SIZE = "10";

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @RequestMapping(value = "")
    public String list(Model model) {
        List<Organization> organizations = userService.getUser(getCurrentUserId()).getOrganizationList();
        model.addAttribute("organizations", organizations);
        return "finance/expenseList";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createForm(Model model){
        model.addAttribute("expense", new Expense());
        List<Organization> organizations = userService.getUser(getCurrentUserId()).getOrganizationList();
        model.addAttribute("organizations", organizations);
        model.addAttribute("action", "create");
        return "finance/expenseForm";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Valid Expense newExpense, @RequestParam("organizationId") Long organizationId, RedirectAttributes redirectAttributes) {
        User user = new User(getCurrentUserId());
        newExpense.setUser(user);

        Organization organization = organizationService.getOrganization(organizationId);
        newExpense.setOrganization(organization);
        try {
            expenseService.saveExpense(newExpense);
            redirectAttributes.addFlashAttribute("message", "创建支出成功");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/finance/expense/";
    }


    /**
     * 取出Shiro中的当前用户Id.
     */
    private Long getCurrentUserId() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }

}
