package com.example.jpademojava.service;

import com.example.jpademojava.domain.Message;
import com.example.jpademojava.domain.User;
import com.example.jpademojava.domain.dto.UserRequestDto;
import com.example.jpademojava.domain.dto.UserResponsDto;
import com.example.jpademojava.repository.MessageJdbcDao;
import com.example.jpademojava.repository.MessageJdbcTemplateDao;
import com.example.jpademojava.repository.UserJdbcDao;
import com.example.jpademojava.repository.UserJdbcTemplateDao;
import java.util.List;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Service
@RequiredArgsConstructor
//@Slf4/j
public class UserService implements IUserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserJdbcDao userJdbcDao;
    private final MessageJdbcDao messageJdbcDao;
    private final UserJdbcTemplateDao userJdbcTemplateDao;
    private final MessageJdbcTemplateDao messageJdbcTemplateDao;
    private final PlatformTransactionManager platformTransactionManager;
    // Connection을 service 단에서 다루기 위해 이곳에서도 DataSource 가져옴
    private final DataSource dataSource;

    public UserResponsDto findById(Integer id) {

//        try {
//            User user = userJdbcDao.findById(id);
//            return user;
//        } catch (SQLException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
//                "자원 반납시 문제가 발생했습니다");
//        }
// AOP 적용준비
//        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(
//            new DefaultTransactionDefinition());
//        try {
        User user = userJdbcTemplateDao.findById(id);
//            platformTransactionManager.commit(transactionStatus);
        return UserResponsDto.from(user);
//        } catch (Exception e) {
//            platformTransactionManager.rollback(transactionStatus);
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
//                "트랜잭션 수행 실패");
//        }
    }

    public void deleteById(Integer id) {
        userJdbcDao.deleteUserById(id);
    }

    public UserResponsDto createUser(String name, Integer age) {
        //부모에서 connection 신경안쓰게 PlatformTransactionManager 적용
        //트랜잭션 관리 인터페이스
//        PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(
////DataSourceTransactionManager도 빈으로 주입가능
//            dataSource);

        //transactionStatus = transactionID , 각 트랜지션의 고유값
//        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(
//            // 시작 -> getTransaction/ commit,rollback -> 끝
//            new DefaultTransactionDefinition());
//        try {
////            connection = dataSource.getConnection();
////            connection.setAutoCommit(false); // 롤백하기 위한 트랜잭션 동기화 위해서 자동커밋 끔, 쿼리 수행할때마다 커밋할건가 -> no 한번에 롤백하려고
////            User user = userJdbcDao.createUser(/*connection,*/ new User(null, name, age));
////            List<Message> messages = messageJdbcDao.save(user.getId(),
////                user.getName() + "님 가입을 환영합니다.");
//            User user = userJdbcTemplateDao.create(new User(null, name, age));
//            List<Message> messages = messageJdbcTemplateDao.save(user.getId(),
//                user.getName() + "님 가입을 환영합니다.");
////            connection.commit(); // 둘다 실행 후 commit
//            platformTransactionManager.commit(transactionStatus);
//            UserResponsDto result = UserResponsDto.from(user);
//            result.setMessages(messages);
//            return result;
//        } catch (Exception e) {
//            // rollback() - 기존 롤백 try-catch 지움
//            platformTransactionManager.rollback(transactionStatus);
//            log.error("트랜잭션 오류 발생: ", e); // 예외 로그 추가
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
//                "트랜잭션 수행시 문제가 발생하여 실패했습니다");
////        } finally {
////            // 동기화되서 쓰고있던 커넥션 반납
////            DataSourceUtils.releaseConnection(connection, dataSource);
////        }
//        }

        User user = userJdbcTemplateDao.create(new User(null, name, age));
        List<Message> messages = messageJdbcTemplateDao.save(user.getId(),
            user.getName() + "님 가입을 환영합니다.");
//            connection.commit(); // 둘다 실행 후 commit
        UserResponsDto result = UserResponsDto.from(user);
        result.setMessages(messages);
        return result;

    }

    public UserResponsDto updateUser(Integer id, UserRequestDto requestDto) {
        User user = userJdbcTemplateDao.update(id, requestDto.getName(), requestDto.getAge());
        return UserResponsDto.from(user);
    }
}


