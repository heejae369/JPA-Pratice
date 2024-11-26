package com.example.jpademojava.repository;

import com.example.jpademojava.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UserJdbcTemplateDao implements IJdbcTemplateDao<User> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List findAll() {
        return List.of();
    }

    public User findById(int id) { // Integer에는 null값이 올수도 있기에 널처리를 해야한다. 객체
        String getUserByIdQuery = "SELECT * FROM \"user\" WHERE id = ?";
        int paramId = id;
        return this.jdbcTemplate.queryForObject(
            getUserByIdQuery, (resultSet, rowNumber) -> new User(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("age")
            ), paramId
        );
    }

    @Override
    public User create(User user) {
        String createUserQuery = "INSERT INTO \"user\" (name, age) VALUES (?, ?)";
        String userName = user.getName();
        Integer userAge = user.getAge();
        Object[] createUserParams = new Object[]{
            userName,
            userAge
        };
        this.jdbcTemplate.update(
            createUserQuery,
            createUserParams
        );
        //lastval() query
        String lastCreatedUserIdQuery = "SELECT lastval()";
        int createdUserId = this.jdbcTemplate.queryForObject(lastCreatedUserIdQuery, int.class);

        //위의 INSERT으로 생성된 유저반환
        String getCreatedUserQuery = "SELECT * FROM \"user\" WHERE id = ?";
        return this.jdbcTemplate.queryForObject(
            getCreatedUserQuery,
            (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age")
            ),
            createdUserId
        );
    }

    @Override
    public User update() {
        return null;
    }

    public User update(int id, String name, Integer age) {
        String updateUserQuery = "UPDATE \"user\" SET name = ?, age = ? WHERE id = ?";
        Object[] updateUserParams = new Object[]{
            name,
            age
        };
        int updatedUserId = this.jdbcTemplate.update(
            updateUserQuery,
            updateUserParams
        );

        String getUserQuery = "SELECT * FROM \"user\" WHERE id = ?";
        return this.jdbcTemplate.queryForObject(
            getUserQuery,
            (rs, rowNum) -> new User(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age")
            ),
            updatedUserId
        );
    }

    @Override
    public void delete(int id) {
        String deleteUserQuery = "DELETE FROM \"user\" WHERE id = ?";
        Object[] deleteUserParam = new Object[]{id};
        this.jdbcTemplate.update(deleteUserQuery, deleteUserParam);
    }
}
