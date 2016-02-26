package com.wantdo.stat.service.account;

import com.wantdo.stat.entity.account.Role;
import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.utils.Encodes;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 * @Date : 2015-8-24
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class ShiroDbRealm extends AuthorizingRealm {

    private UserService userService;

    /**
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        User user = userService.findByLoginName(shiroUser.loginName);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for(Role role: user.getRoleList()){
            //基于Role的权限信息
            info.addRole(role.getName());
            //基于Permission的权限信息
            info.addStringPermissions(role.getPermissions());
        }

        return info;
    }

    /**
     * 认证回调函数，登录时调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authcToken;
        User user = userService.findByLoginName(token.getUsername());
        if(user != null){
            if ("disabled".equals(user.getStatus())){
                throw new DisabledAccountException();
            }

            byte[] salt = Encodes.decodeHex(user.getSalt());
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(new ShiroUser(user.getId(), user.getLoginName(), user.getName()), user.getPassword(),
                    ByteSource.Util.bytes(salt), getName());
            return info;

        } else {
            return null;
        }
    }

    /**
     * 设定Password校验的Hash算法与迭代次数
     */
    @PostConstruct
    public void initCredentialsMatcher(){
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(UserService.HASH_ALGORITHM);
        matcher.setHashIterations(UserService.HASH_ITERATIONS);
        setCredentialsMatcher(matcher);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
     */
    public static class ShiroUser implements Serializable{

        private static final long serialVersionUID = -5496935133958552704L;

        public Long id;
        public String loginName;
        public String name;

        public ShiroUser(Long id, String loginName, String name) {
            this.id = id;
            this.loginName = loginName;
            this.name = name;
        }

        public String getLoginName() {
            return loginName;
        }

        @Override
        public String toString() {
            return loginName;
        }

    }


}
