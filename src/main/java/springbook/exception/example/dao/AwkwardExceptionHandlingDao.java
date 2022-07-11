package springbook.exception.example.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 초난감 예외처리 예시 : 예외 블랙홀
 */
public class AwkwardExceptionHandlingDao {
    private DataSource dataSource;
    public void setDataSource (DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public int getCount() {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        int result = 0;

        try {
            c = dataSource.getConnection();

            ps = c.prepareStatement("select count(* from users");
            rs = ps.executeQuery();
            rs.next();
            result = rs.getInt(1);
        } catch (SQLException e) {
            /**
             * 예외를 잡고 아무것도 하지 않는다.
             * 예외 발생을 무시해버리고 정상적인 상황인 것 처럼 다음 라인으로 넘어가겠다는 분명한 의도가 있는게 아니라면,
             * 연습 중에도 절대 만들어서는 안 되는 코드다.
             * */
        } finally {
            // 예시와는 상관없는 자원반환 exception
            resourceClose(c, ps, rs);
        }
        return result;
    }

    /**
     * 자원반환을 위한 메소드 추출;
     * close() 는 만들어진 순서의 반대로 하는 것이 원칙이다.
     * 이때 catch block 에서 아무 액션을 취하지 않는 것은 다음 자원반환 전에 throw 를 하게 되면 다음 자원반환이 이루어지지 않기 때문이다.
     * @param c
     * @param ps
     * @param rs
     */
    private void resourceClose(Connection c, PreparedStatement ps, ResultSet rs) {
        if(rs != null) {
            try {
                rs.close();
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

