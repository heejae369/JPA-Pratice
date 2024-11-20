package com.example.jpademojava.repository;

import com.example.jpademojava.domain.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
public class UserJdbcDao {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    private String findSQL = "SELECT * FROM \"user\" WHERE id = ?";
    private String updateSQL = "SELECT * FROM \"user\" WHERE id = ?";

    private DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);

        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
        return hikariDataSource;
    }

    public User findById(Integer id) throws SQLException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource().getConnection();//dataSource
            preparedStatement = connection.prepareStatement("SELECT * FROM \"user\" WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("age")
                );
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DB에 해당 유저가 존재하지 않습니다");
            }
        } catch (SQLException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 접근 시 문제 발생");
        } finally {
            if (!(resultSet == null)) {
                resultSet.close();
            }
            if (!(preparedStatement == null)) {
                preparedStatement.close();
            }
            if (!(connection == null)) {
                connection.close();
            }
        }
    }


    public void deleteUserById(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        //ResultSet resultSet = null;
        try {
            connection = dataSource().getConnection();//dataSource
            preparedStatement = connection.prepareStatement("DELETE * FROM \"user\" WHERE id = ?");
            preparedStatement.setInt(1, id);
            //executeUpdate() : SQL 실행과 동시에 영향받은 ROW 개수 반환(DBMS가 보내줌)
            int willDeleteRow = preparedStatement.executeUpdate();
            if (willDeleteRow == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저는 없습니다");
            }
        } catch (SQLException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 접근 시 문제 발생");
        } finally {

            try {
                if (!(preparedStatement == null)) {
                    preparedStatement.close();
                }
                if (!(connection == null)) {
                    connection.close();
                }

            } catch (
                SQLException e) { // 메서드 deleteUser() 옆에 throws SQLException이 없으면 여기서라도 SQLException처리해야함
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DB문제 발생");
            }

        }
    }


    public User createUser(User user) throws SQLException {
        //try-with-resources 구조
        // close 매번 안해도 됨 - try끝나면 자동반환
        Connection connection = null;
        try {
            connection = dataSource().getConnection();//dataSource

            try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT into \"user\" (name, age) VALUES (?, ?)");
            ) {

                preparedStatement.setString(1, user.getName());
                preparedStatement.setInt(2, user.getAge());
                int resultRow = preparedStatement.executeUpdate();
                if (resultRow == 0) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "생성 실패");
                }
            }

            // 만들어진 User 반환하기 위한 id 검색
            Integer createdUserId = 0;
            try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT lastval() AS id");
                ResultSet resultSet = preparedStatement.executeQuery();
            ) {

                if (resultSet.next()) {
                    createdUserId = resultSet.getInt("id");
                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "생성 ID 검색실패");
                }
            } catch (SQLException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "lastval() 탐색 오류");
            }
            try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM \"user\" WHERE id = ?");
            ) {
                preparedStatement.setInt(1, createdUserId); // 먼저 파라미터 설정
                try (ResultSet resultSet = preparedStatement.executeQuery()) { // 쿼리 실행
                    if (resultSet.next()) {
                        return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("age")
                        );
                    } else {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "DB에 해당 유저가 존재하지 않습니다");
                    }
                }
            }
            //lastval의 id를 넣어 만들어진 객체 검색
//            try (
//                PreparedStatement preparedStatement = connection.prepareStatement(
//                    "SELECT * FROM \"user\" WHERE id = ?");
//
//                ResultSet resultSet = preparedStatement.executeQuery();
//            ) {
//
//                preparedStatement.setInt(1, createdUserId);
//                if (resultSet.next()) {
//                    return new User(
//                        resultSet.getInt("id"),
//                        resultSet.getString("name"),
//                        resultSet.getInt("age")
//                    );
//                } else {
//                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DB에 해당 유저가 존재하지 않습니다");
//                }
//            } catch (SQLException e) {
//                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "생성된 유저 탐색 오류");
//            }
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 접근 시 문제 발생");
        } finally {
            if (!(connection == null)) {
                connection.close();
            }
        }
    }

    public void updateUser(Integer id, String name, Integer age) throws SQLException {

        try (
            Connection connection = dataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE \"user\" SET name = ?, age = ? WHRER id = ?");
        ) {

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setInt(3, id);
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당유저가 존재하지 않습니다");
            }
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DB 접근 시 문제발생");
        }
    }
}
