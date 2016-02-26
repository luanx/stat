package com.wantdo.stat.dao.finance;

import com.wantdo.stat.entity.finance.Account;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @Date : 2015-9-15
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface AccountDao extends PagingAndSortingRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    Account findByUserId(Long userId);

    Account findByOrganizationId(Long organizationId);

    List<Account> findAll();

}
