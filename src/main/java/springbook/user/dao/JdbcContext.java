package springbook.user.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * JDBC 작업 흐름을 분리해서 만든 JdbcContext 클래스
 * UserDao 는 이제 JdbcContext 에 의존하고 있다.
 * 그런데, JdbcContext 는 인터페이스인 DataSource와는 달리 구체 클래스이다.
 * 스프링의 DI는 기본적으로 인터페이스를 사이에 두고 의존 클래스를 바꿔서 사용하도록 하는 게 목적이다.
 * 하지만 이 경우 JdbcContext 는 그 자체로 독립적인 JDBC 컨텍스트를 제공해주는 서비스 오브젝트로서 의미가 있을 뿐이고 구현 방법이 바뀔 가능성은 없다.
 * 따라서 인터페이스를 구현하도록 만들지 않았고, UserDao 와 JdbcContext는 인터페이스를 사이에 두지 않고 DI를 적용하는 특별한 구조가 된다.
 */
public class JdbcContext {
    // DataSource 타입 빈을 DI 받을 수 있게 준비해둔다.
    private DataSource dataSource;
    // 수정자 메서드를 이용하여 생성자 DI 를 대체
    public void setDataSource (DataSource dataSource) {
        this.dataSource = dataSource;
    }
    /**
     * 메소드로 분리한 try/catch/finally 컨텍스트 코드
     * @param stmt  클라이언트가 컨텍스트를 호출할 때 넘겨줄 전략 파라미터
     * @throws SQLException
     */
    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
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
