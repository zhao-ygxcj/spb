package com.example.spb.config;

import com.example.spb.filter.JwtFilter;
//import com.example.spb.realm.MyRealm;
import com.example.spb.realm.MyRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;


@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean filterFactoryBean(@Qualifier("manager") DefaultWebSecurityManager manager){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(manager);

        // 拦截器
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("/**", "anon");
        map.put("/user/*", "anon");
        map.put("/login/*","anon");
        map.put("/sms/*","anon");
        map.put("/branch/*","anon");

        map.put("/swagger-ui.html", "anon");
        map.put("/favicon.ico", "anon");
        map.put("/v2/**", "anon");
        map.put("/doc.html","anon");
        map.put("/webjars/**","anon");
        map.put("/swagger-resources/**","anon");
        map.put("/resources/**","anon");
        map.put("/configuration/**","anon");

//        map.put("/**", "jwt");


        //权限设置
        HashMap<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JwtFilter());
        factoryBean.setFilters(filterMap);


        factoryBean.setFilterChainDefinitionMap(map);
        return factoryBean;
    }


    @Bean
    public DefaultWebSecurityManager manager(@Qualifier("myRealm") MyRealm myRealm){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(myRealm);
        return manager;
    }

    @Bean
    public MyRealm myRealm(){
        return new MyRealm();
    }
}
