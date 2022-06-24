package springbook.user.dao;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    // UserDao 모든 메소드가 JdbcContext 를 사용하지 않으므로, 기존방법을 사용해서 동작하는 메소드를 위해 UserDao 가 아직은 DataSource 를 DI 받아야 함.
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    // 수정자 메서드를 이용하여 생성자 DI 를 대체
    public void setDataSource (DataSource dataSource) { // 수정자 메소드이면서 JdbcContext 에 대한 생성, DI 작업을 동시에 수행한다.
        this.jdbcTemplate = new JdbcTemplate(); // JdbcTemplate 생성(IoC)
        this.jdbcTemplate.setDataSource(dataSource); // 의존 오브젝트 주입(DI)
        this.dataSource = dataSource; // 아직 JdbcContext 를 적용하지 않은 메소드를 위해 저장해둔다.
    }

    /**
     * JdbcTemplate 을 add() 메소드
     * jdbcTemplate.update() 메소드 두개 중 sql, Object... 을 받는 메소드를 사용
     * @param user  AddStatement 전략 클래스에서 필요로 하는 추가 정보
     * @throws SQLException
     */
    public void add(final User user) throws SQLException {
        this.jdbcTemplate.update("insert into users(id, name, password) values(?, ?, ?)",
                user.getId(),
                user.getName(),
                user.getPassword()
        );
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = this.dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        // user은 null 상태로 초기화해놓는다.
        User user = null;
        /* 다음 행이 있을 경우 : return true; and 다음행 커서이동,
        다음 행이 없을 경우 : return false; and 커서 정지 */
        if(rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        /* 결과가 없으면 User 는 null 상태 그대로일 것이다.
        이를 확인해서 예외를 던져준다 */
        if(user == null) throw new EmptyResultDataAccessException(1);

        return user;
    }

    /**
     * JdbcTemplate 을 적용한 deleteAll() 메소드
     * jdbcTemplate.update() 메소드 두개 중 sql 을 받는 메소드를 사용
     * @throws SQLException
     */
    public void deleteAll() throws SQLException {
        this.jdbcTemplate.update("delete from users");
    }

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();

            ps = c.prepareStatement("select count(*) from users");
            // ResultSet 도 다양한 SQLException 이 발생할 수 있는 코드이므로 try 블록 안에 둬야 한다.
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if(rs != null) {
                try {
                    rs.close(); // 만들어진 ResultSet 을 닫아주는 기능. close() 는 만들어진 순서의 반대로 하는 것이 원칙이다.
                } catch (SQLException e) {}
            }
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {}
            }
            if(c != null) {
                try {
                    c.close();
                } catch (SQLException e) {}
            }
        }
    }

}