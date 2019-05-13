package com.westlake.air.propro.service.impl;

import com.westlake.air.propro.domain.db.UserDO;
import com.westlake.air.propro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 授权方法，如果不设置缓存管理的话，需要访问需要一定的权限或角色的请求时会进入这个方法
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserDO principal = (UserDO) principals.getPrimaryPrincipal();
//        Set<String> roles = new HashSet<>();
//        roles.add("user");
//        if ("admin".equals(principal.getUsername())) {
//            roles.add("admin");
//        }

        return new SimpleAuthorizationInfo(principal.getRoles());
    }

    /**
     * 认证方法
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        //根据登录名查询用户，这里不用校验密码，因为密码的校验是交给shiro来完成的
        UserDO userInfo = userService.findByUsername(userToken.getUsername());

        if (userInfo == null) {
            throw new IncorrectCredentialsException("Username or password error");
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfo,//用户
                userInfo.getPassword(),//密码
                ByteSource.Util.bytes(userInfo.getSalt()),//盐值用 ByteSource.Util.bytes 来生成
                getName()//realm name
        );
        return authenticationInfo;
    }
}
