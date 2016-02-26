package com.wantdo.stat.rest.account;

import com.wantdo.stat.entity.front.vo.TreeDTO;
import com.wantdo.stat.rest.RestException;
import com.wantdo.stat.service.account.OrganizationService;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.web.MediaTypes;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ Date : 2015-8-27
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@RestController
@RequestMapping(value = "/api/v1/organization")
public class OrganizationRestController {

    private static Logger logger = LoggerFactory.getLogger(OrganizationRestController.class);

    @Autowired
    private OrganizationService organizationService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public List<TreeDTO> getAll(){
        List<TreeDTO> organizationTree= organizationService.getOrganizationTree();
        if (organizationTree == null) {
            String message = "组织机构不存在";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return organizationTree;
    }


    /**
     * 取出Shiro中的当前用户Id
     */
    public Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }
}
