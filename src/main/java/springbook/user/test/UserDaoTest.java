package springbook.user.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)// 스프링의 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
@ContextConfiguration(locations = "/applicationContext.xml")// 테스트 컨텍스트가 자동으로 만들어줄 애플리케이션 컨텍스트의 위치 지정
@DirtiesContext // 테스트 메소드에서 applicationContext 의 구성이나 상태를 변경한다는 것을 테스트 컨텍스트 프레임워크에 알려준다
public class UserDaoTest {
    // 테스트 오브젝트가 만들어지고 나면 스프링 테스트 컨텍스트에 의해 자동으로 값이 주입된다.
    // ApplicationContext.java 가 없는데 @Autowired 로 '타입에 의한 자동와이어링' 방식으로 DI 가 되었다. 어떻게 된 일일까? 이것은 스프링 애플리케이션 컨텍스트는 초기화할 때 자기 자신도 빈으로 등록하기 때문이다.
    @Autowired
    private ApplicationContext context;
    // UserDao 타입 빈을 직접 DI 받는다
    @Autowired
    private UserDao dao;

    private User user1;
    private User user2;
    private User user3;

    // jUnit 이 제공하는 애노테이션, @Test 메소드가 실행되기 전에 먼저 실행되어야 하는 메소드를 정의한다.
    @Before
    public void setUp() {
        // 테스트에서 UserDao 가 사용할 DataSource 오브젝트를 직접 생성한다
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mariadb://localhost:3306/testdb", "root", "password",true
        );
        // 코드에 의한 수동 DI
        dao.setDataSource(dataSource);
        this.user1 = new User("yhkim01", "김윤호1", "springno1");
        this.user2 = new User("yhkim02", "김윤호2", "springno2");
        this.user3 = new User("yhkim03", "김윤호3", "springno3");
    }
    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User userget1 = dao.get(user1.getId());
        assertThat(userget1.getName(), is(user1.getName()));
        assertThat(userget1.getPassword(), is(user1.getPassword()));

        User userget2 = dao.get(user2.getId());
        assertThat(userget2.getName(), is(user2.getName()));
        assertThat(userget2.getPassword(), is(user2.getPassword()));
    }

    @Test
    public void count() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    /**
     * getUserFailure() 테스트 코드에 나타난 기능
     * 		|단계					|내용									| 코드
     * |조건	|어떤 조건을 가지고 있는지	|가져올 사용자 정보가 존재하지 않는 경우에		| dao.deleteAll() , asserThat(dao.getCount(), is(0))
     * |행위	|무엇을 할 때				|존재하지 않는 id로 get()을 실행하면		| get("unknown_id")
     * |결과	|어떤 결과가 나온다		|특별한 예외가 던져진다						| @Test(expected = EmptyResultDataAccessException.class)
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id"); // 이 메소드 실행 중에 예외가 발생해야 한다(expected). 예외가 발생하지 않으면 테스트가 실패한다.
    }
}