package com.wantdo.stat.service.finance;

import com.google.common.collect.Lists;
import com.wantdo.stat.dao.finance.AccountDao;
import com.wantdo.stat.dao.finance.IncomeDao;
import com.wantdo.stat.entity.finance.Account;
import com.wantdo.stat.entity.finance.Income;
import com.wantdo.stat.entity.front.vo.IncomeVo;
import com.wantdo.stat.entity.front.vo.TableDTO;
import com.wantdo.stat.persistence.DynamicSpecifications;
import com.wantdo.stat.persistence.SearchFilter;
import com.wantdo.stat.persistence.SearchFilter.Operator;
import com.wantdo.stat.service.account.ShiroDbRealm.ShiroUser;
import com.wantdo.stat.utils.Clock;
import com.wantdo.stat.utils.DateUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Date : 2015-8-31
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Component
@Transactional
public class IncomeService {

    private IncomeDao incomeDao;
    private AccountDao accountDao;

    private Clock clock = Clock.DEFAULT;

    /**
     * 按Id获得Income
     */
    public Income getIncome(Long id) {
        return incomeDao.findOne(id);
    }

    public List<Income> getUserLastUpdate() {
        List<Income> incomes = incomeDao.findAllLastUpdate();
        return incomes;
    }

    /**
     * 保存Income
     */
    public void saveIncome(Income income) throws Exception{

        Account account = accountDao.findByOrganizationId(income.getOrganization().getId());

        Double balanceNow = income.getCash() + income.getBusMain() + income.getBusOther()
                + income.getPayBorrowAccount() + income.getPayLoan()
                + income.getReceivable() + income.getOther();

        if (account == null) {
            income.setBalance(balanceNow);

            Account acc = new Account();
            acc.setDate(income.getDate());
            acc.setUser(income.getUser());
            acc.setBalance(balanceNow);
            acc.setOrganization(income.getOrganization());
            acc.setCreated(clock.getCurrentDate());
            accountDao.save(acc);
        } else {
            balanceNow += account.getBalance();
            income.setBalance(balanceNow);

            account.setUser(income.getUser());
            account.setDate(DateUtil.getDate(new Date()));
            account.setUser(income.getUser());
            account.setBalance(balanceNow);
            account.setOrganization(income.getOrganization());
            account.setModified(clock.getCurrentDate());
            accountDao.save(account);
        }
        incomeDao.save(income);
    }

    public TableDTO<IncomeVo> getIncomeVo(Long organizationId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                        String sortType) {
        Page<Income> incomes = SearchOrgIncome(organizationId, searchParams, pageNumber, pageSize, sortType);
        TableDTO<IncomeVo> tableDTO = new TableDTO<>();
        List<IncomeVo> incomeVoList = Lists.newArrayList();
        tableDTO.setTotal(incomes.getTotalElements());
        for (Income income : incomes.getContent()) {
            IncomeVo incomeVo =  new IncomeVo();

            incomeVo.setId(income.getId());
            incomeVo.setCash(income.getCash());
            incomeVo.setBusMain(income.getBusMain());
            incomeVo.setBusOther(income.getBusOther());
            incomeVo.setPayBorrowName(income.getPayBorrowName());
            incomeVo.setPayBorrowAccount(income.getPayBorrowAccount());
            incomeVo.setPayLoan(income.getPayLoan());
            incomeVo.setReceivable(income.getReceivable());
            incomeVo.setOther(income.getOther());
            incomeVo.setBalance(income.getBalance());
            incomeVo.setDate(income.getDate());
            incomeVo.setCreated(income.getCreated());
            incomeVo.setUserName(income.getUser().getName());
            incomeVo.setOrganizationName(income.getOrganization().getName());
            incomeVo.setRemark(income.getRemark());

            incomeVoList.add(incomeVo);
        }
        tableDTO.setRows(incomeVoList);
        return tableDTO;
    }

    /**
     * 按页面传来的查询条件查询收入
     */
    public Page<Income> SearchAllIncome(Map<String, Object> searchParams, int pageNumber, int pageSize,
                                     String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<Income> spec = buildAllSpecification(searchParams);

        return incomeDao.findAll(spec, pageRequest);
    }

    public Page<Income> SearchOrgIncome(Long organizationId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                     String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<Income> spec = buildOrgSpecification(organizationId, searchParams);

        return incomeDao.findAll(spec, pageRequest);
    }


    public Page<Income> searchUserIncome(Long userId, Map<String, Object> searchParams, int pageNumber, int pageSize,
                                         String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<Income> spec = buildUserSpecification(userId, searchParams);

        return incomeDao.findAll(spec, pageRequest);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Income> buildAllSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Income> spec = DynamicSpecifications.bySearchFilter(filters.values(), Income.class);
        return spec;
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Income> buildUserSpecification(Long userId, Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        filters.put("user.id", new SearchFilter("user.id", Operator.EQ, userId));
        Specification<Income> spec = DynamicSpecifications.bySearchFilter(filters.values(), Income.class);
        return spec;
    }

    private Specification<Income> buildOrgSpecification(Long organizationId, Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        filters.put("organization.id", new SearchFilter("organization.id", Operator.EQ, organizationId));
        Specification<Income> spec = DynamicSpecifications.bySearchFilter(filters.values(), Income.class);
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
    public void setIncomeDao(IncomeDao incomeDao) {
        this.incomeDao = incomeDao;
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
