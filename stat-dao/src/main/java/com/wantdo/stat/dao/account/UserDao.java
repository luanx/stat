package com.wantdo.stat.dao.account;

import com.wantdo.stat.entity.account.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @Date : 2015-8-24
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface UserDao extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByLoginName(String loginName);
}
