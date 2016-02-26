package com.wantdo.stat.dao.finance;

import com.wantdo.stat.entity.finance.Income;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @Date : 2015-9-15
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface IncomeDao extends PagingAndSortingRepository<Income, Long>, JpaSpecificationExecutor<Income>{

    @Query("select income from Income income where income.date=(select max(i.date) from Income i where i.user" +
            ".id=income.user.id)")
    List<Income> findAllLastUpdate();

}
