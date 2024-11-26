package com.example.jpademojava.service;

import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
public class CglibProxyHandler implements MethodInterceptor {

    private final Object target;
    private final PlatformTransactionManager transactionManager;

    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy)
        throws Throwable {
        TransactionStatus status = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            Object result = method.invoke(target, args);
            transactionManager.commit(status);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            transactionManager.rollback(status);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "[CglibProxyHandler] 트랜잭션 수정 실패");
        }
    }
}
