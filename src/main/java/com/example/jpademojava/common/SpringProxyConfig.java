package com.example.jpademojava.common;

import com.example.jpademojava.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class SpringProxyConfig {

    private final UserService userService;
    private final PlatformTransactionManager transactionManager;

    @Bean
    @Primary
    // Spring 에서 제공하는 FactoryBean을 사용하여 CGLIB를 간접사용해 프록시 적용
    public ProxyFactoryBean userServiceProxyFactoryBean() {
        ProxyFactoryBean userServiceProxyFactoryBean = new ProxyFactoryBean();
//      userServiceProxyFactoryBean.setProxyInterfaces(new Class[]{IUserService.class});
        userServiceProxyFactoryBean.setTarget(userService);
        userServiceProxyFactoryBean.setInterceptorNames("springProxyHandler");
        return userServiceProxyFactoryBean;
    }
}
