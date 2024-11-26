package com.example.jpademojava.service;

// @Service

import com.example.jpademojava.domain.dto.UserRequestDto;
import com.example.jpademojava.domain.dto.UserResponsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.server.ResponseStatusException;

/**
 * @Service CGLIB 사용을 위해 UserService 는 이제 더 이상 IUserService 를 사용하지 않기때문에 IUserService 의 유일한 구현체인
 * UserServiceProxy 가 안에서 IUserService 구현체 Bean 을 찾을때 자기 자신을 참조하는 Circular References 가 발생하니
 * IUserService Bean 빈 생성하지 않도록 @Service 를 코멘트 처리
 */
@RequiredArgsConstructor
public class UserServiceProxy implements IUserService { // 여기서 IUserService를 implement함

    private final IUserService userService;
    private final PlatformTransactionManager transactionManager;

    @Override
    public UserResponsDto findById(Integer id) {
        TransactionStatus status = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            UserResponsDto responsDto = userService.findById(id);
            transactionManager.commit(status);
            return responsDto;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "[UserServiceProxy] 트랜잭션 수정 시 실패");
        }
    }

    @Override
    public void deleteById(Integer id) {
        userService.deleteById(id);
    }

    @Override
    public UserResponsDto createUser(String name, Integer age) {
        TransactionStatus status = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            UserResponsDto responsDto = userService.createUser(name, age);
            transactionManager.commit(status);
            return responsDto;
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "[UserServiceProxy] 트랜잭션 수정 시 실패");
        }
    }

    @Override
    public UserResponsDto updateUser(Integer id, UserRequestDto requestDto) {
        return null;
    }
}