package springbook.user.test;

import springbook.user.dao.UserDao;
import springbook.user.dao.connection.ConnectionMaker;
import springbook.user.dao.daum.DConnectionMaker;
import springbook.user.domain.User;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        ConnectionMaker connectionMaker = new NConnectionMaker();
        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDao dao = new UserDao(connectionMaker);
        User user = new User();
        user.setId("yhkim");
        user.setName("김윤호");
        user.setPassword("notmerried");

        dao.add(user);

        System.out.println(user.getId() + "등록 성공");

        User user2 = dao.get(user.getId());

        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + "조회 성공");
    }
}