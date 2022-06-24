package springbook.user.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    // 수정자 메서드를 이용하여 생성자 DI 를 대체
    public void setDataSource (DataSource dataSource) { // 수정자 메소드
        this.jdbcTemplate = new JdbcTemplate();
        this.jdbcTemplate.setDataSource(dataSource);
        this.dataSource = dataSource;
    }

    /**
     * JdbcTemplate 을 적용한 add() 메소드
     * jdbcTemplate.update() 메소드 두개 중 sql, Object... 을 받는 메소드를 사용
     * @param user  AddStatement 전략 클래스에서 필요로 하는 추가 정보
     * @throws SQLException
     */
    public void add(final User user) throws SQLException {
            this.jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());
                return ps;
            }
        });
    }

    /**
     * JdbcTemplate 을 적용한 get() 메소드
     * jdbcTemplate.queryForObject() 와 RowMapper 적용
     * @param id
     * @return
     */
    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
            new Object[] {id}, // SQL 에 바인딩할 파라미터 값, 가변인자 대신 배열을 사용한다.
            new RowMapper<User>() { // ResultSet 한 로우의 결과를 오브젝트에 매핑해주는 RowMapper 콜백
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }
        );
    }

    /**
     * JdbcTemplate 을 적용한 deleteAll() 메소드
     * jdbcTemplate.update() 메소드 두개 중 sql 을 받는 메소드를 사용
     * @throws SQLException
     */
    public void deleteAll() throws SQLException {
        this.jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                return con.prepareStatement("delete from users");
            }
        });
    }

    /**
     * JdbcTemplate 을 적용한 getCount() 메소드
     * jdbcTemplate.queryForInt() 메소드 사용
     * @return
     * @throws SQLException
     */
    public int getCount() throws SQLException {
        return this.jdbcTemplate.query(
            new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    return con.prepareStatement("select count(*) from users");
                }
            },
            new ResultSetExtractor<Integer>() {
                @Override
                public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                    rs.next();
                    return rs.getInt(1);
                }
            }
        );
    }
}