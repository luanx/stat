package com.wantdo.stat.service.account;

import com.wantdo.stat.dao.account.CompanyDao;
import com.wantdo.stat.entity.account.Company;
import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.persistence.DynamicSpecifications;
import com.wantdo.stat.persistence.Hibernates;
import com.wantdo.stat.persistence.SearchFilter;
import com.wantdo.stat.service.account.ShiroDbRealm.ShiroUser;
import com.wantdo.stat.utils.Clock;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
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
 * 用户管理业务类
 *
 * @Date : 2015-8-24
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Component
@Transactional
public class CompanyService {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(CompanyService.class);

    private CompanyDao companyDao;

    private Clock clock = Clock.DEFAULT;

    public Company findByName(String name){
        return companyDao.findByName(name);
    }

    /**
     * 按Id获得公司
     */
    public Company getCompany(Long id){
        return companyDao.findOne(id);
    }

    public void saveCompany(Company company){
        company.setCreated(clock.getCurrentDate());
        companyDao.save(company);
    }

    public void updateCompany(Company company){
        company.setModified(clock.getCurrentDate());
        companyDao.save(company);
    }

    public void deleteCompany(Long id) {
        companyDao.delete(id);
    }

    /**
     * 获取所有的公司
     */
    public List<Company> getAllCompany() {
        List<Company> companyList = (List<Company>) companyDao.findAll();
        for(Company company: companyList){
            Hibernates.initLazyProperty(company.getStockholderList());
        }
        return companyList;
    }

    /**
     * 判断是否超级管理员.
     */
    private boolean isSupervisor(Long id) {
        return id == 1;
    }

    /**
     * 按页面传来的查询条件查询用户
     */
    public Page<Company> SearchCompany(Map<String, Object> searchParams, int pageNumber, int pageSize,
                                  String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<Company> spec = buildSpecification(searchParams);

        return companyDao.findAll(spec, pageRequest);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Company> buildSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Company> spec = DynamicSpecifications.bySearchFilter(filters.values(), Company.class);
        return spec;
    }

    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
        Sort sort = null;
        if ("auto".equals(sortType)) {
            sort = new Sort(Sort.Direction.ASC, "id");
        }

        return new PageRequest(pageNumber - 1, pagzSize, sort);
    }


    /**
     * 判断是否超级管理员.
     */
    private boolean isSupervisor(User user) {
        return ((user.getId() != null) && (user.getId() == 1L));
    }


    /**
     * 取出Shiro中的当前用户LoginName.
     */
    private String getCurrentUserName() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.loginName;
    }

    /**
     * 取出Shiro中的当前用户Id.
     */
    private Long getCurrentUserId() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }


    @Autowired
    public void setCompanyDao(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }
}
