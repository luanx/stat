package com.wantdo.stat.service.account;

import com.google.common.collect.Lists;
import com.wantdo.stat.dao.account.OrganizationDao;
import com.wantdo.stat.entity.account.Organization;
import com.wantdo.stat.entity.front.vo.TreeDTO;
import com.wantdo.stat.persistence.Hibernates;
import com.wantdo.stat.service.account.ShiroDbRealm.ShiroUser;
import com.wantdo.stat.utils.Clock;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

/**
 * 组织机构管理业务类
 *
 * @ Date : 2015-8-26
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Component
@Transactional
public class OrganizationService {

    private OrganizationDao organizationDao;

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(OrganizationService.class);

    private Clock clock = Clock.DEFAULT;

    /**
     * 按Id获得组织机构
     */
    public Organization getOrganization(Long id){
        return organizationDao.findOne(id);
    }

    public void saveOrganization(Organization organization) {
        organization.setCreated(clock.getCurrentDate());
        organizationDao.save(organization);
    }

    public void updateOrganization(Organization organization) {
        organization.setModified(clock.getCurrentDate());
        organizationDao.save(organization);
    }

    public void deleteOrganization(Long id) {
        organizationDao.delete(id);
    }

    public List<TreeDTO> getOrganizationTree(){
        List<Organization> organizationList = organizationDao.getTopOrganization();
        for(Organization organization: organizationList){
            Hibernates.initLazyProperty(organization.getChildren());
        }
        List<TreeDTO> organizationTree = organizationToTree(organizationList);
        return organizationTree;
    }

    public List<Organization> getOrganization(){
        List<Organization> organizationList = organizationDao.getOrganization();
        for(Organization organization: organizationList){
            Hibernates.initLazyProperty(organization.getChildren());
        }
        return organizationList;
    }

    public List<Organization> getTopOrganization(){
        List<Organization> organizationList = organizationDao.getTopOrganization();
        for(Organization organization: organizationList){
            Hibernates.initLazyProperty(organization.getChildren());
        }
        return organizationList;
    }

    public List<Organization> getShopOrganization(){
        List<Organization> organizationList = organizationDao.getShopOrganization();
        for(Organization organization: organizationList){
            Hibernates.initLazyProperty(organization.getChildren());
        }
        return organizationList;
    }

    private List<TreeDTO> organizationToTree(List<Organization> organizationList) {
        if (organizationList != null){
            List<TreeDTO> organizationTree = Lists.newArrayList();
            for (Organization organization: organizationList) {
                TreeDTO treeDTO = new TreeDTO();
                treeDTO.setText(organization.getName());
                treeDTO.setHref("organization/update/" + organization.getId());
                treeDTO.setNodes(organizationToTree(organization.getChildren()));
                organizationTree.add(treeDTO);
            }
            return organizationTree;
        }
        return Collections.emptyList();
    }

    public List<Organization> getAllOrganization(){
        return (List<Organization>)organizationDao.findAll();
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
    public void setOrganizationDao(OrganizationDao organizationDao) {
        this.organizationDao = organizationDao;
    }
}
