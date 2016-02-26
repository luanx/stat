package com.wantdo.stat.rest.account;

import com.wantdo.stat.entity.account.Resource;
import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.rest.RestException;
import com.wantdo.stat.service.account.ShiroDbRealm;
import com.wantdo.stat.service.account.UserService;
import com.wantdo.stat.web.MediaTypes;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @Date : 2015-8-27
 * @From : stat
 * @Author : luanx@wantdo.com
 */

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserRestController {

    private static Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public User get(@PathVariable("id") Long id){
        User user = userService.getUser(id);
        if (user == null) {
            String message = "用户不存在(id:" + id + ")";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return user;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
    public Set<Resource> getResource(){
        Set<Resource> resourceList = userService.getResources();
        if (resourceList == null) {
            String message = "资源不存在";
            logger.warn(message);
            throw new RestException(HttpStatus.NOT_FOUND, message);
        }
        return resourceList;
    }

    /**
     * 取出Shiro中的当前用户Id
     */
    public Long getCurrentUserId() {
        ShiroDbRealm.ShiroUser user = (ShiroDbRealm.ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.id;
    }
}
