package com.wantdo.stat.service.finance;

import com.google.common.collect.Lists;
import com.wantdo.stat.dao.finance.AccountDao;
import com.wantdo.stat.entity.finance.Account;
import com.wantdo.stat.entity.front.vo.AccountVo;
import com.wantdo.stat.entity.front.vo.TableDTO;
import com.wantdo.stat.persistence.DynamicSpecifications;
import com.wantdo.stat.persistence.SearchFilter;
import com.wantdo.stat.service.account.ShiroDbRealm;
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
 * @Date : 2015-9-15
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Component
@Transactional
public class AccountService {

    private Clock clock = Clock.DEFAULT;

    private AccountDao accountDao;

    public TableDTO<AccountVo> getAccountVo(Map<String, Object> searchParams, int pageNumber, int pageSize,
                                          String sortType) {
        Page<Account> accounts = SearchAccount(searchParams, pageNumber, pageSize, sortType);
        TableDTO<AccountVo> tableDTO = new TableDTO<>();
        List<AccountVo> accountVoList = Lists.newArrayList();
        tableDTO.setTotal(accounts.getTotalElements());
        for (Account account : accounts.getContent()) {
            AccountVo accountVo = new AccountVo();

            accountVo.setId(account.getId());
            accountVo.setDate(account.getDate());
            accountVo.setOrganizationName(account.getOrganization().getName());
            accountVo.setBalance(account.getBalance());
            accountVo.setCreated(account.getCreated());
            accountVo.setModified(account.getModified());

            accountVoList.add(accountVo);
        }
        tableDTO.setRows(accountVoList);
        return tableDTO;
    }

    /**
     * 按页面传来的查询条件查询余额
     */
    public Page<Account> SearchAccount(Map<String, Object> searchParams, int pageNumber, int pageSize,
                                        String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<Account> spec = buildAllSpecification(searchParams);

        return accountDao.findAll(spec, pageRequest);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Account> buildAllSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Account> spec = DynamicSpecifications.bySearchFilter(filters.values(), Account.class);
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
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * 取出Shiro中的当前用户Id.
     */
    private Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }


}
