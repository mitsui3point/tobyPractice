package springbook.user.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.dao.UserDao;
import springbook.user.dao.connection.counting.CountingConnectionMaker;
import springbook.user.dao.factory.CountingDaoFactory;
import springbook.user.domain.User;

import java.sql.SQLException;

public class UserDaoCountingDaoFactoryTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

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

        //
        CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
        System.out.println("Connction counter:" + ccm.getCounter());
    }
}