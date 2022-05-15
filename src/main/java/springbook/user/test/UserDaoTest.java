package springbook.user.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import springbook.user.dao.UserDao;
import springbook.user.dao.factory.DaoFactory;
import springbook.user.domain.User;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml"); // classpath xml : src/main/resources
        UserDao dao = context.getBean("userDao", UserDao.class);
        User user = new User();
        user.setId("yhkim");
        user.setName("김윤호");
        user.setPassword("notmerried");

        // 임시코드 생성
        if(!user.getId().isEmpty()) {
            dao.del(user.getId());
            System.out.println(user.getId() + "이전 데이터 삭제 성공");
        }

        dao.add(user);

        System.out.println(user.getId() + "등록 성공");

        User user2 = dao.get(user.getId());

        if(!user.getName().equals(user2.getName())) {
            System.out.println("테스트 실패(name)");
        } else if(!user.getPassword().equals(user2.getPassword())) {
            System.out.println("테스트 실패(password)");
        } else {
            System.out.println("조회 테스트 성공");
        }
    }
}