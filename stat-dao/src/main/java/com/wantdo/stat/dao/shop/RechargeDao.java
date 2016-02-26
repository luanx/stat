package com.wantdo.stat.dao.shop;

import com.wantdo.stat.entity.finance.Account;
import com.wantdo.stat.entity.shop.Recharge;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @Date : 2015-9-30
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface RechargeDao extends PagingAndSortingRepository<Recharge, Long>, JpaSpecificationExecutor<Recharge>{

    @Query("select recharge from Recharge recharge where recharge.date=(select max(r.date) from Recharge r)")
    List<Recharge> findAllLastUpdate();
}
