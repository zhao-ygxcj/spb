package com.example.spb.filter;

import com.example.spb.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtFilter implements Filter {

    private JwtUtil jwtUtil = new JwtUtil();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 在初始化时实例化JwtUtil
        jwtUtil = new JwtUtil();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取Authorization头中的token
        String token = request.getHeader("Authorization");

        // 检查token是否存在并且以"Bearer "开头
        if (token != null && token.startsWith("Bearer "))
            token = token.substring(7); // 去掉"Bearer "前缀
        if (!jwtUtil.isVerify(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing authorization token");
            return;
        }
        else {
            // 验证token的合法性，如果合法，解析出token中的用户信息
            Claims claims = jwtUtil.getClaimsByToken(token);
            System.out.println(claims.getSubject());
            filterChain.doFilter(request, response);
//            if (claims != null) {
//                // 将解析出的用户信息放入请求上下文中
//                request.setAttribute("userId", claims.getSubject());
//                // 继续执行后续的请求处理
//                filterChain.doFilter(request, response);
//            }
        }
        // 如果token不存在或验证失败，则返回401 Unauthorized错误
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    public void destroy() {
        // 在销毁时清理资源
    }
}
