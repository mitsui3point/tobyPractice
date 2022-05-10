package springbook.user.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.dao.UserDao;
import springbook.user.dao.factory.DaoFactory;
import springbook.user.domain.User;

import java.sql.SQLException;

public class DaoFactoryTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // dao1, dao2 에 할당시에 userDao() 메서드를 호출하므로 userDao return 값인 new UserDao(); 로 인해 메모리에 각각 할당이 된다.(레퍼런스가 틀림)
        DaoFactory daoFactory = new DaoFactory();
        UserDao dao1 = daoFactory.userDao();
        UserDao dao2 = daoFactory.userDao();

        System.out.println("dao1: " + dao1);
        System.out.println("dao2: " + dao2);

        // @Configuration > @Bean annotation 으로 전체스캔 후 컨텍스트에 할당한 상태이므로 다시 new 연산자로 메모리에 할당시키지 않는다.(레퍼런스가 같음)
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao3 = context.getBean("userDao", UserDao.class);
        UserDao dao4 = context.getBean("userDao", UserDao.class);

        System.out.println("dao3: " + dao3);
        System.out.println("dao4: " + dao4);
    }
}