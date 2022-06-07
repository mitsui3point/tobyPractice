package springbook.user.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserDaoTest {
    private UserDao dao; // @Autowired 가 없다

    private User user1;
    private User user2;
    private User user3;

    // jUnit 이 제공하는 애노테이션, @Test 메소드가 실행되기 전에 먼저 실행되어야 하는 메소드를 정의한다.
    @Before
    public void setUp() {
        // 오브젝트 생성, 관계설정 등을 모두 직접 해준다
        dao = new UserDao();
        DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mariadb://localhost:3306/testdb", "root", "password", true
        );
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