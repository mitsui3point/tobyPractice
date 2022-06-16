package springbook.user.dao;


import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    // UserDao 모든 메소드가 JdbcContext 를 사용하지 않으므로, 기존방법을 사용해서 동작하는 메소드를 위해 UserDao 가 아직은 DataSource 를 DI 받아야 함.
    private DataSource dataSource;
    // 수정자 메서드를 이용하여 생성자 DI 를 대체
    /*
    JdbcContext 를 DI 받도록 만든다. => JdbcContext 가 Spring 에 의해 DI 받지 않고, UserDao 의 setDataSource 에 의해 DI 받는다.
     */
    private JdbcContext jdbcContext;

    public void setDataSource (DataSource dataSource) { // 수정자 메소드이면서 JdbcContext 에 대한 생성, DI 작업을 동시에 수행한다.
        this.jdbcContext = new JdbcContext(); // JdbcContext 생성(IoC)
        this.jdbcContext.setDataSource(dataSource); // 의존 오브젝트 주입(DI)
        this.dataSource = dataSource; // 아직 JdbcContext 를 적용하지 않은 메소드를 위해 저장해둔다.
    }

    /**
     * 클라이언트 책임을 담당할 add() 메소드
     * add() 클라이언트 책임 1. 선정한 전략 클래스의 오브젝트 생성 : AddStatement 생성시 user 정보를 AddStatement 에 전달
     * add() 클라이언트 책임 2. 컨텍스트 호출. 전략 오브젝트 전달
     * @param user  AddStatement 전략 클래스에서 필요로 하는 추가 정보
     * @throws SQLException
     */
    public void add(final User user) throws SQLException { //
        // 메소드 파라미터로 이전한 익명 내부 클래스; DI받은 JdbcContext 의 컨텍스트 메소드를 사용하도록 변경한다.
        this.jdbcContext.workWithStatementStrategy(
            new StatementStrategy() { // 익명 내부 클래스는 구현하는 인터페이스를 생성자처럼 이용해서 오브젝트로 만든다.
                public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                    PreparedStatement ps = c.prepareStatement(
                            "insert into users(id, name, password) values(?, ?, ?)");
                    // 로컬(내부) 클래스의 코드에서 외부의 메소드 로컬 변수에 직접 접근할 수 있다. 단 외부의 메소드 로컬 변수를 사용하려면 외부 메소드 argument 에 final을 붙여주어야 한다.
                    // user 파라미터는 메소드 내부에서 변경될 일이 없으므로 final로 선언해도 무방하다.
                    ps.setString(1, user.getId());
                    ps.setString(2, user.getName());
                    ps.setString(3, user.getPassword());
                    return ps;
                }
            }
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
     * 클라이언트 책임을 담당할 deleteAll() 메소드
     * deleteAll 클라이언트 책임 1. 선정한 전략 클래스의 오브젝트 생성
     * deleteAll 클라이언트 책임 2. 컨텍스트 호출. 전략 오브젝트 전달
     * @throws SQLException
     */
    public void deleteAll() throws SQLException {
        executeSql("delete from users"); // 변하는 sql 문장
    }

    private void executeSql(final String query) throws SQLException {
        // 메소드 파라미터로 이전한 익명 내부 클래스; DI받은 JdbcContext 의 컨텍스트 메소드를 사용하도록 변경한다.
        this.jdbcContext.workWithStatementStrategy(
            // 변하지 않는 콜백 클래스 정의와 오브젝트 생성
            new StatementStrategy() {// 익명 내부 클래스는 구현하는 인터페이스를 생성자처럼 이용해서 오브젝트로 만든다.
                public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                    PreparedStatement ps = c.prepareStatement(query);
                    return ps;
                }
            }
        ); // 컨텍스트 호출. 전략 오브젝트 전달
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