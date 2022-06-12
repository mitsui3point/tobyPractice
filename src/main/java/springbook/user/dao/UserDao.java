package springbook.user.dao;


import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private DataSource dataSource;
    // 수정자 메서드를 이용하여 생성자 DI 를 대체
    public void setDataSource (DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 클라이언트 책임을 담당할 add() 메소드
     * add() 클라이언트 책임 1. 선정한 전략 클래스의 오브젝트 생성 : AddStatement 생성시 user 정보를 AddStatement 에 전달
     * add() 클라이언트 책임 2. 컨텍스트 호출. 전략 오브젝트 전달
     * @param user  AddStatement 전략 클래스에서 필요로 하는 추가 정보
     * @throws SQLException
     */
    public void add(User user) throws SQLException {
        StatementStrategy st = new AddStatement(user); // 선정한 전략 클래스의 오브젝트 생성
        jdbcContextWithStatementStrategy(st); // 컨텍스트 호출. 전략 오브젝트 전달
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
     * 클라이언트 책임을 담당할 deleteAll() 메소드
     * deleteAll 클라이언트 책임 1. 선정한 전략 클래스의 오브젝트 생성
     * deleteAll 클라이언트 책임 2. 컨텍스트 호출. 전략 오브젝트 전달
     * @throws SQLException
     */
    public void deleteAll() throws SQLException {
        StatementStrategy st = new DeleteAllStatement(); // 선정한 전략 클래스의 오브젝트 생성
        jdbcContextWithStatementStrategy(st); // 컨텍스트 호출. 전략 오브젝트 전달
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

    /**
     * 메소드로 분리한 try/catch/finally 컨텍스트 코드
     * @param stmt  클라이언트가 컨텍스트를 호출할 때 넘겨줄 전략 파라미터
     * @throws SQLException
     */
    private void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = this.dataSource.getConnection();

            ps = stmt.makePreparedStatement(c);

            ps.executeUpdate(); // 여기서 예외가 발생하면 바로 메소드 실행이 중단된다.
        } catch (SQLException e) {
            throw e; // 예외가 발생했을 때 부가적인 작업을 해줄 수 있도록 catch 블록을 둔다. 아직은 예외를 다시 메소드 밖으로 던지는 것밖에 없다.
        } finally { // finally 이므로 try 블록에서 예외가 발생했을 때나 안 했을 때나 모두 실행된다.
            if(ps != null) try { ps.close(); } catch (SQLException e){} // ps.close()  메소드에서도 SQLException 이 발생할 수 있기 때문에 이를 잡아줘야 한다. 그렇지 않으면 Connection 을 close() 하지 못하고 메소드를 빠져나갈 수 있다.
            if(c != null) try { c.close();} catch (SQLException e){} // Connection 반환
        }
    }
}