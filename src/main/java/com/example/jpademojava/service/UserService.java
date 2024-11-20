package com.example.jpademojava.service;

import com.example.jpademojava.domain.Message;
import com.example.jpademojava.domain.User;
import com.example.jpademojava.domain.dto.UserRequestDto;
import com.example.jpademojava.domain.dto.UserResponsDto;
import com.example.jpademojava.repository.MessageJdbcDao;
import com.example.jpademojava.repository.UserJdbcDao;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJdbcDao userJdbcDao;
    private final MessageJdbcDao messageJdbcDao;

    public User findById(Integer id) {

        try {
            User user = userJdbcDao.findById(id);
            return user;
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "자원 반납시 문제가 발생했습니다");
        }
    }

    public void deleteById(Integer id) {
        userJdbcDao.deleteUserById(id);
    }

    public UserResponsDto createUser(String name, Integer age) {
        try {
            User user = userJdbcDao.createUser(new User(null, name, age));
            List<Message> messages = messageJdbcDao.save(user.getId(),
                user.getName() + "님 가입을 환영합니다.");
            UserResponsDto result = UserResponsDto.from(user);
            result.setMessages(messages);
            return result;
//            return UserResponsDto.from(user);
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "자원 반납시 문제가 발생했습니다");
        }
    }

    public UserResponsDto updateUser(Integer id, UserRequestDto requestDto) {

        try {
            userJdbcDao.updateUser(id, requestDto.getName(), requestDto.getAge());
            // 바뀐 아이디 반환
            User user = userJdbcDao.findById(id);
            return UserResponsDto.from(user);
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "자원 반납시 문제가 발생했습니다");
        }
    }


}
