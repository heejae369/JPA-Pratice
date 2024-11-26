//package com.example.jpademojava.common;
//
//import com.example.jpademojava.service.CglibProxyHandler;
//import com.example.jpademojava.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.cglib.proxy.Enhancer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@Configuration
//@RequiredArgsConstructor
//public class CglibProxyConfig {
//
//    private final UserService userService;
//    private final PlatformTransactionManager transactionManager;
//
//    @Bean
//    public UserService userServiceCglibProxy() {
////        Enhancer enhancer = new Enhancer();
////        enhancer.setSuperclass(UserService.class);
////        enhancer.setCallback(new CglibProxyHandler(userService, transactionManager));
////        enhancer.setClassLoader(UserService.class.getClassLoader());
////        return (UserService) enhancer.create(
////            new Class[]{UserService.class},
////            new Object[]{userService}
////        );
//        UserService userServiceProxy = (UserService) Enhancer.create(
//            UserService.class,                                          // Class "superclass"
//            new CglibProxyHandler(userService, transactionManager)
//            // MethodInterceptor "callback"
//        );
//        return userServiceProxy;
//    }
//}