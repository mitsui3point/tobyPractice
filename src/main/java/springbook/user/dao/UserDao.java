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
    public void add(User user) throws SQLException, ClassNotFoundException {
        Connection c = this.dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values(?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
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
        if(user != null) throw new RuntimeException();

        return user;
    }
    public void deleteAll() throws SQLException {
        Connection c = this.dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement(
                "delete from users");
        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public int getCount() throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select count(*) from users");

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        c.close();

        return count;
    }
}