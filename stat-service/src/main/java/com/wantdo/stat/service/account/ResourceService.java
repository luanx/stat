package com.wantdo.stat.service.account;

import com.wantdo.stat.dao.account.ResourceDao;
import com.wantdo.stat.entity.account.Resource;
import com.wantdo.stat.persistence.DynamicSpecifications;
import com.wantdo.stat.persistence.SearchFilter;
import com.wantdo.stat.service.account.ShiroDbRealm.ShiroUser;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * 角色管理业务类
 *
 * @Date : 2015-8-26
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Component
@Transactional
public class ResourceService {

    private ResourceDao resourceDao;

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(ResourceService.class);

    /**
     * 按Id获得资源
     */
    public Resource getResource(Long id){
        return resourceDao.findOne(id);
    }

    public void saveResource(Resource resource) {
        resourceDao.save(resource);
    }

    public void deleteResource(Long id) {
        resourceDao.delete(id);
    }

    /**
     * 按页面传来的查询条件查询资源
     */
    public Page<Resource> SearchResource(Map<String, Object> searchParams, int pageNumber, int pageSize,
                                 String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<Resource> spec = buildSpecification(searchParams);

        return resourceDao.findAll(spec, pageRequest);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Resource> buildSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Resource> spec = DynamicSpecifications.bySearchFilter(filters.values(), Resource.class);
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
    private boolean isSupervisor(Long id) {
        return ((id != null) && (id == 1L));
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
    public void setResourceDao(ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }
}
