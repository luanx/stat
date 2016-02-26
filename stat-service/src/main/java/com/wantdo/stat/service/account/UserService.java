package com.wantdo.stat.service.account;

import com.google.common.collect.Lists;
import com.wantdo.stat.dao.account.OrganizationDao;
import com.wantdo.stat.dao.account.RoleDao;
import com.wantdo.stat.dao.account.UserDao;
import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.entity.account.Resource;
import com.wantdo.stat.entity.account.Role;
import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.persistence.DynamicSpecifications;
import com.wantdo.stat.persistence.Hibernates;
import com.wantdo.stat.persistence.SearchFilter;
import com.wantdo.stat.security.utils.Digests;
import com.wantdo.stat.service.account.ShiroDbRealm.ShiroUser;
import com.wantdo.stat.utils.Clock;
import com.wantdo.stat.utils.Encodes;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Set;

/**
 * 用户管理业务类
 *
 * @Date : 2015-8-24
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@Component
@Transactional
public class UserService {

    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_ITERATIONS = 1024;
    private static final int SALT_SIZE = 8;

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(UserService.class);

    private UserDao userDao;
    private RoleDao roleDao;
    private OrganizationDao organizationDao;

    private Clock clock = Clock.DEFAULT;

    public User findByLoginName(String loginName){
        return userDao.findByLoginName(loginName);
    }

    /**
     * 按Id获得用户
     */
    public User getUser(Long id){
        return userDao.findOne(id);
    }

    public void saveUser(User user){
        if(isSupervisor(user)){
            logger.warn("操作员{}尝试修改超级管理员用户", getCurrentUserName());
            throw new ServiceException("不能修改超级管理员用户");
        }
        // 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
        if (StringUtils.isNotBlank(user.getPlainPassword())) {
            encryptPassword(user);
        }
        user.setStatus("enabled");
        user.setCreated(clock.getCurrentDate());

        List<Role> roleList = Lists.newArrayList();
        //roleList.add(new Role(2L));
        user.setRoleList(roleList);

        userDao.save(user);
    }

    public void updateUser(User user){
        if (StringUtils.isNotBlank(user.getPlainPassword())){
            encryptPassword(user);
        }
        user.setModified(clock.getCurrentDate());
        userDao.save(user);
    }

    public void deleteUser(Long id) {
        if (isSupervisor(id)) {
            logger.warn("操作员{}尝试删除超级管理员用户", getCurrentUserName());
            throw new ServiceException("不能删除超级管理员用户");
        }
        userDao.delete(id);
    }

    /**
     * 获取所有的角色
     */
    public List<Role> getAllRole() {
        List<Role> roleList = (List<Role>)roleDao.findAll();
        for(Role role: roleList){
            Hibernates.initLazyProperty(role.getResourceSet());
        }
        return roleList;
    }

    /**
     * 获取组织机构列表
     */
    public List<Organization> getOrganization(){
        List<Organization> organizationList = organizationDao.getOrganization();
        for(Organization organization: organizationList){
            Hibernates.initLazyProperty(organization.getChildren());
        }
        return organizationList;
    }

    public Set<Resource> getResources(){
        User user = userDao.findOne(getCurrentUserId());
        return user.getResourceSet();
    }

    /**
     * 获取当前用户数量
     */
    public Long getUserCount(){
        return userDao.count();
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
    public Page<User> SearchUser(Map<String, Object> searchParams, int pageNumber, int pageSize,
                                  String sortType) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
        Specification<User> spec = buildSpecification(searchParams);

        return userDao.findAll(spec, pageRequest);
    }

    /**
     * 创建动态查询条件组合.
     */
    private Specification<User> buildSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<User> spec = DynamicSpecifications.bySearchFilter(filters.values(), User.class);
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
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private void encryptPassword(User user) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));

        byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, HASH_ITERATIONS);
        user.setPassword(Encodes.encodeHex(hashPassword));
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
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public void setOrganizationDao(OrganizationDao organizationDao) {
        this.organizationDao = organizationDao;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }
}
