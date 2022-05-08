package springbook.user.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.dao.UserDao;
import springbook.user.dao.factory.DaoFactory;
import springbook.user.domain.User;

import java.sql.SQLException;

public class DaoFactoryTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DaoFactory daoFactory = new DaoFactory();
        UserDao dao1 = daoFactory.userDao();
        UserDao dao2 = daoFactory.userDao();

        System.out.println("dao1: " + dao1);
        System.out.println("dao2: " + dao2);

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao3 = context.getBean("userDao", UserDao.class);
        UserDao dao4 = context.getBean("userDao", UserDao.class);

        System.out.println("dao3: " + dao3);
        System.out.println("dao4: " + dao4);
    }
}