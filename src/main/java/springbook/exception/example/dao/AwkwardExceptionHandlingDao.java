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
            e.printStackTrace();
            System.exit(1);
        } finally {
            // 예시와는 상관없는 자원반환 exception
            resourceClose(c, ps, rs);
        }
        return result;
    }

    /**
     * 무의미하고 무책임한 throws
     * 예외를 흔적도 없이 먹어치우는 예외 블랙홀보다는 조금 낫긴 하지만,
     * 이런 무책임한 throws 선언도 심각한 문제점이 있다.
     * 자신이 사용하려고 하는 메소드에 throws Exception 이 선언되어 있다고 생각해보자.
     * 그런 메소드 선언에서는 의미 있는 정보를 얻을 수 없다.
     * 정말 무엇인가 실행중에 예외적인 상황이 발생할 수 있다는 것인지,
     * 아니면 그냥 습관적으로 복사해서 붙여놓은 것인지 알 수가 없다.
     * 결국 이런 메소드를 사용하는 메소드에서도 역시 throws Exception 을 따라서 붙이는 수 밖에 없다.
     * @throws Exception
     */
    public void method1() throws Exception {
        method2();
    }
    public void method2() throws Exception {
        method3();
    }
    public void method3() throws Exception {
        throw new Exception("원인모를 최상위 Exception 발생!");
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

