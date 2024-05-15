package com.example.spb.realm;

import cn.hutool.crypto.digest.BCrypt;
import com.example.spb.entity.JwtToken;
import com.example.spb.entity.User;
import com.example.spb.service.UserService;
import com.example.spb.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    private JwtUtil jwtUtil;
    // 用户认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        JwtToken jwtToken = (JwtToken) authenticationToken;
        String jwt = (String) jwtToken.getPrincipal();

        // 解析验证 JWT
        Claims claims = jwtUtil.getClaimsByToken(jwt);
        String job_id = claims.getSubject();


        // 根据用户名查询数据库中的用户信息
        User user = userService.findByJobID(job_id);

        if (user == null) {
            // 如果用户不存在，抛出 UnknownAccountException 异常
            throw new UnknownAccountException("用户不存在");
        }
        // 如果用户名和密码验证通过，返回一个包含用户信息的 AuthenticationInfo 对象
        return new SimpleAuthenticationInfo(job_id, jwt, getName());
    }

    // 用户授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 在这里实现用户的授权逻辑，比如根据用户名从数据库中查询用户的角色和权限信息，并返回授权信息
        // 示例中直接返回了一个简单的授权信息，你需要根据实际情况来修改这里的逻辑
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole("role");
        authorizationInfo.addStringPermission("permission");
        return authorizationInfo;
    }
}

