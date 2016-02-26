package com.wantdo.stat.dao.account;

import com.wantdo.stat.entity.account.Company;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @Date : 2015-9-16
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface CompanyDao extends PagingAndSortingRepository<Company, Long>, JpaSpecificationExecutor<Company>{

    Company findByName(String name);
}
