package springbook.user.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.dao.connection.daum.DConnectionMaker;
import springbook.user.dao.factory.CountingDaoFactory;

import java.sql.SQLException;

public class UserDaoCountingDaoFactoryTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        DConnectionMaker dcm = context.getBean("realConnectionMaker", DConnectionMaker.class);
        System.out.println("outer realConnectionMaker");
        System.out.println(dcm);

        CountingDaoFactory cdf = new CountingDaoFactory();
        System.out.println("==========new CountingDaoFactory============");
        cdf.connectionMaker();
        System.out.println("outer realConnectionMaker");
        System.out.println(cdf.realConnectionMaker());
    }
}