package springbook.exception.example.dao;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.springframework.jdbc.core.JdbcTemplate;
import springbook.exception.example.exception.DuplicateUserIdException;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 예외 전환(exception translation)
 * 예외 회피와 비슷하게 예외를 복구해서 정상적인 상태를 만들 수 없기 때문에
 *  예외를 메소드 밖으로 던지는 것이다.
 *
 * 하지만 예외 회피와 달리,
 *  발생한 예외를 그대로 넘기는 게 아니라 적절한 예외로 전환해서 던진다는 특징이 있다.
 */
public class ExceptionTranslateExample {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 사용자 정보를 등록하려고 시도해보고,
     *  만약 중복된 아이디 값 때문에 에러가 나는 경우에는 이를 확인해서
     *  좀 더 의미 있는 DuplicationUserIdException 으로 전환해주는 DAO 메소드의 예다.
     * @param user
     * @throws SQLException
     */
    public void add(User user) throws DuplicateUserIdException, SQLException {
        Connection c = null;
        PreparedStatement pstmt = null;
        try {
            // JDBC를 이용해 user 정보를 DB 에 추가하는 코드 또는
            // 그런 기능을 가진 다른 SQLException 을 던지는 메소드를 호출하는 코드
            c = dataSource.getConnection();
            pstmt = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // ErrorCode 가 MariaDB 의 "Duplicate Entry(1062)" 이면 예외 전환
            if(e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY)
                throw new DuplicateUserIdException(e);
            else
                throw e;
        } finally {
            if (pstmt != null) pstmt.close();
            if (c != null) c.close();
        }
    }

    /**
     * 테스트를 위한 method
     */
    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }
}