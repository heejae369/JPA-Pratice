package com.example.jpademojava.repository;

import com.example.jpademojava.domain.Message;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MessageJdbcTemplateDao implements IJdbcTemplateDao<Message> {

    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Message create(Message message) {
        return null;
    }

    @Override
    public Message update() {
        return null;
    }


    @Override
    public void delete(int id) {

    }

    private final JdbcTemplate jdbcTemplate;

    public List<Message> findByUserId(int userId) {
        String getMessagesQuery = "SELECT * FROM \"message\" WHERE user_id = ?";
        int getMessagesParams = userId;
        return this.jdbcTemplate.queryForStream(
            getMessagesQuery,
            (resultSet, rowNum) -> new Message(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("message"),
                resultSet.getTimestamp("created_at")
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            ),
            getMessagesParams
        ).toList();
    }

    public List<Message> save(Integer userId, String message) {
        if (true) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "트랜잭션 롤백 여부를 확인하기 위한 의도된 예외");
        }
        // 메세지 저장
        String createMessageQuery = "INSERT INTO \"message\" (user_id, message, created_at) VALUES (?, ?, ?)";
        Object[] createMessageParams = new Object[]{
            userId,
            message,
            LocalDateTime.now()
        };
        this.jdbcTemplate.update(
            createMessageQuery,
            createMessageParams
        );
        // 만든 메세지 반환
        String getMessagesQuery = "SELECT * FROM \"message\" WHERE user_id = ?";
        int getMessagesParams = userId;
        return this.jdbcTemplate.queryForStream(
            getMessagesQuery,
            (resultSet, rowNum) -> new Message(
                resultSet.getInt("id"),
                resultSet.getInt("user_id"),
                resultSet.getString("message"),
                resultSet.getTimestamp("created_at")
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            ),
            getMessagesParams
        ).toList();
    }
}
