package com.wantdo.stat.dao.finance;

import com.wantdo.stat.entity.finance.Expense;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @Date : 2015-9-15
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface ExpenseDao extends PagingAndSortingRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

    @Query("select expense from Expense expense where expense.date=(select max(e.date) from Expense e where e.user" +
            ".id=expense.user.id)")
    List<Expense> findAllLastUpdate();

}
