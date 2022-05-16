package springbook.user.test;

import org.junit.Test;
import org.junit.Assert;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserDaoTest {
    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {

        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml"); // classpath xml : src/main/resources
        UserDao dao = context.getBean("userDao", UserDao.class);
        User user = new User();
        user.setId("yhkim2");
        user.setName("김윤호2");
        user.setPassword("notmerried");

        // 임시코드 생성
        if(!user.getId().isEmpty()) {
            dao.del(user.getId());
        }

        dao.add(user);

        User user2 = dao.get(user.getId());

        assertThat(user2.getName(), is(user.getName()));
        assertThat(user2.getPassword(), is(user.getPassword()));
    }
}