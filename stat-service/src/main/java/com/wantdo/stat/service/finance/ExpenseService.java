package com.wantdo.stat.service.finance;


import com.google.common.collect.Lists;
import com.wantdo.stat.dao.finance.AccountDao;
import com.wantdo.stat.dao.finance.ExpenseDao;
import com.wantdo.stat.entity.finance.Account;
import com.wantdo.stat.entity.finance.Expense;
import com.wantdo.stat.entity.front.vo.ExpenseVo;
import com.wantdo.stat.entity.front.vo.TableDTO;
import com.wantdo.stat.persistence.DynamicSpecifications;
import com.wantdo.stat.persistence.SearchFilter;
import com.wantdo.stat.service.account.ServiceException;
import com.wantdo.stat.service.account.ShiroDbRealm.ShiroUser;
import com.wantdo.stat.utils.Clock;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @Date : 2015-8-31
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Component
@Transactional
public class ExpenseService {

    private Clock clock = Clock.DEFAULT;

    private ExpenseDao expenseDao;
    private AccountDao accountDao;

    public List<Expense> getUserLastUpdate() {
        List<Expense> expenses = expenseDao.findAllLastUpdate();
        return expenses;
    }


    /**
     * 按Id获得Expense
     */
    public Expense getExpense(Long id) {
        return expenseDao.findOne(id);
    }

    public void saveExpense(Expense expense) throws Exception{
        Account account = accountDao.findByOrganizationId(expense.getOrganization().getId());

        Double balanceNow = expense.getBusPurchase() + expense.getBusLogistics() + expense.getBusAdvertisement()
                + expense.getBusRefund() + expense.getSalesTax() + expense.getAdminSalary()
                + expense.getAdminOffice() + expense.getAdminTravel() + expense.getFinInterest()
                + expense.getFinCharge() + expense.getOther();

        if (account == null) {
            throw new ServiceException(expense.getOrganization().getName() + "当前余额不足");
        } else {
            balanceNow = account.getBalance() - balanceNow;
            if (balanceNow < 0.00) {
                throw new ServiceException(expense.getOrganization().getName() + "当前余额不足");
            }
            expense.setBalance(balanceNow);

            account.setDate(expense.getDate());
            account.setUser(expense.getUser());
            account.setOrganization(expense.getOrganization());
            account.setBalance(balanceNow);
            account.setCreated(clock.getCurrentDate());
            accountDao.save(account);
        }
        expenseDao.save(expense);
    }

    public TableDTO<ExpenseVo> getExpenseVo(Long organizationId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                          String sortType) {
        Page<Expense> expenses = SearchOrgExpense(organizationId, searchParams, pageNumber, pageSize, sortType);
        TableDTO<ExpenseVo> tableDTO = new TableDTO<>();
        List<ExpenseVo> expenseVoList = Lists.newArrayList();
        tableDTO.setTotal(expenses.getTotalElements());
        for (Expense expense : expenses.getContent()) {
            ExpenseVo expenseVo = new ExpenseVo();

            expenseVo.setId(expense.getId());
            expenseVo.setBusPurchase(expense.getBusPurchase());
            expenseVo.setBusLogistics(expense.getBusLogistics());
            expenseVo.setBusAdvertisement(expense.getBusAdvertisement());
            expenseVo.setBusRefund(expense.getBusRefund());
            expenseVo.setSalesTax(expense.getSalesTax());
            expenseVo.setAdminSalary(expense.getAdminSalary());
            expenseVo.setAdminOffice(expense.getAdminOffice());
            expenseVo.setAdminTravel(expense.getAdminTravel());
            expenseVo.setFinInterest(expense.getFinInterest());
            expenseVo.setFinCharge(expense.getFinCharge());
            expenseVo.setOther(expense.getOther());
            expenseVo.setBalance(expense.getBalance());
            expenseVo.setRemark(expense.getRemark());
            expenseVo.setOrganizationName(expense.getOrganization().getName());
            expenseVo.setUserName(expense.getUser().getName());
            expenseVo.setDate(expense.getDate());
            expenseVo.setCreated(expense.getCreated());

            expenseVoList.add(expenseVo);
        }
        tableDTO.setRows(expenseVoList);
        return tableDTO;
    }


    /**
     * 按页面传来的查询条件查询收入
     */
    public Page<Expense> SearchExpense(Map<String, Object> searchParams, int pageNumber, int pageSize,
                                       String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<Expense> spec = buildSpecification(searchParams);

        return expenseDao.findAll(spec, pageRequest);
    }

    public Page<Expense> SearchUserExpense(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                           String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<Expense> spec = buildUserSpecification(userId, searchParams);

        return expenseDao.findAll(spec, pageRequest);
    }

    public Page<Expense> SearchOrgExpense(Long organizationId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                           String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<Expense> spec = buildOrgSpecification(organizationId, searchParams);

        return expenseDao.findAll(spec, pageRequest);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Expense> buildSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Expense> spec = DynamicSpecifications.bySearchFilter(filters.values(), Expense.class);
        return spec;
    }


    private Specification<Expense> buildUserSpecification(Long userId, Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        filters.put("user.id", new SearchFilter("user.id", SearchFilter.Operator.EQ, userId));
        Specification<Expense> spec = DynamicSpecifications.bySearchFilter(filters.values(), Expense.class);
        return spec;
    }

    private Specification<Expense> buildOrgSpecification(Long organizationId, Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        filters.put("organization.id", new SearchFilter("organization.id", SearchFilter.Operator.EQ, organizationId));
        Specification<Expense> spec = DynamicSpecifications.bySearchFilter(filters.values(), Expense.class);
        return spec;
    }

    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
        Sort sort = null;
        if ("auto".equals(sortType) || "desc".equals(sortType)) {
            sort = new Sort(Sort.Direction.DESC, "id");
        }

        return new PageRequest(pageNumber - 1, pagzSize, sort);
    }


    @Autowired
    public void setExpenseDao(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }

    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * 取出Shiro中的当前用户Id.
     */
    private Long getCurrentUserId() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }
}
