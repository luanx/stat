package com.wantdo.stat.service.account;

import com.wantdo.stat.dao.account.ResourceDao;
import com.wantdo.stat.dao.account.RoleDao;
import com.wantdo.stat.entity.account.Resource;
import com.wantdo.stat.entity.account.Role;
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
 * 角色管理业务类
 *
 * @Date : 2015-8-26
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Component
@Transactional
public class RoleService {

    private RoleDao roleDao;
    private ResourceDao resourceDao;

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(RoleService.class);

    private Clock clock = Clock.DEFAULT;

    /**
     * 按Id获得角色
     */
    public Role getRole(Long id){
        return roleDao.findOne(id);
    }

    public void saveRole(Role role) {
        role.setCreated(clock.getCurrentDate());
        roleDao.save(role);
    }

    public void updateRole(Role role) {
        role.setModified(clock.getCurrentDate());
        roleDao.save(role);
    }

    public void deleteRole(Long id) {
        roleDao.delete(id);
    }

    /**
     * 获取所有的资源信息
     */
    public List<Resource> getAllPermission() {
        List<Resource> resourceList = (List<Resource>) resourceDao.findAll();
        for(Resource resource: resourceList){
            Hibernates.initLazyProperty(resource.getChildren());
        }
        return resourceList;
    }

    /**
     * 按页面传来的查询条件查询角色
     */
    public Page<Role> SearchRole(Map<String, Object> searchParams, int pageNumber, int pageSize,
                                 String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<Role> spec = buildSpecification(searchParams);

        return roleDao.findAll(spec, pageRequest);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<Role> buildSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Role> spec = DynamicSpecifications.bySearchFilter(filters.values(), Role.class);
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
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public void setResourceDao(ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }
}
