package springbook.exception.example.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 예외처리 회피
 * 예외처리를 자신이 담당하지 않고 자신을 호출한 쪽으로 던져버리는 방법
 *  throws 문으로 선언해서 예외가 발생하면 알아서 던져지게 하거나
 *  catch 문으로 일단 예외를 잡은 후에 로그를 남기고 다시 예외를 던지는(rethrow) 것이다.
 *
 * 예외를 자신이 처리하지 않고 회피하는 방법이다.
 *
 * 빈 catch 블록으로 잡아서 예외가 발생하지 않은 것 처럼 만드는 경우는,
 *  드물지만 특별한 의도를 가지고 예외를 복구했거나
 *  아무 개념이 없어서 그런 것이지
 *  회피한 것은 아니다.
 */
public class ExceptionAvoidanceExample {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 예외처리 회피 메소드 예시; declaration
     * throws 문으로 선언을 한 다음 예외가 발생하게 되면 알아서 던져지게 하는 방법
     * @param user
     * @throws
     */
    public void addDeclarationAvoid(User user) throws SQLException {
        // JDBC API
        Connection c = dataSource.getConnection();
        PreparedStatement pstmt = c.prepareStatement("insert int users(id, name, password) values(?, ?, ?)");
        pstmt.setString(1, user.getId());
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getPassword());
        pstmt.executeUpdate();
        pstmt.close();
        c.close();
    }

    /**
     * 예외처리 회피 메소드 예시; try catch
     * catch 문으로 예외를 잡은 후에 로그를 남기고 다시 예외를 던지는 (;throw)방법
     * @param user
     */
    public void addTryCatchAvoid(User user) throws SQLException {
        try {
            // JDBC API
            Connection c = dataSource.getConnection();
            PreparedStatement pstmt = c.prepareStatement("insert int users(id, name, password) values(?, ?, ?)");
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.executeUpdate();
            pstmt.close();
            c.close();
        } catch (SQLException e) {
            // 로그 출력
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 테스트를 위한 method
     */
    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }
}