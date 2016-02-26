package com.wantdo.stat.dao.account;

import com.wantdo.stat.entity.account.Organization;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @Date : 2015-9-14
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface OrganizationDao extends PagingAndSortingRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {

    @Query("select organization from Organization organization where organization.isLeaf=1")
    List<Organization> getOrganization();

    @Query("select organization from Organization organization where organization.parent.id=null")
    List<Organization> getTopOrganization();

    @Query("select organization from Organization organization where organization.isShop=1")
    List<Organization> getShopOrganization();

    Organization findByName(String name);

}
