package springbook.exception.example.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.exception.example.dao.ExceptionAvoidanceExample;
import springbook.exception.example.dao.ExceptionRecoveryExample;
import springbook.exception.example.exception.RetryFailedException;
import springbook.user.domain.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class ExceptionTest {
    @Autowired
    private ApplicationContext context;

    private ExceptionRecoveryExample recoveryExample;
    private ExceptionAvoidanceExample avoidanceExample;

    private List<String> expectedFileReadResult;
    private User user;

    @Before
    public void setUp() {
        this.recoveryExample = this.context.getBean("exceptionRecoveryExample", ExceptionRecoveryExample.class);
        this.avoidanceExample = this.context.getBean("exceptionAvoidanceExample", ExceptionAvoidanceExample.class);

        this.expectedFileReadResult = new ArrayList<String>();
        this.expectedFileReadResult.add("hello");
        this.expectedFileReadResult.add("how are you");
        this.expectedFileReadResult.add("nice to meet you!");

        this.user = new User("gyumee", "박성철", "springno1");
    }

    /**
     * 재시도를 통해 예외를 복구하는 코드; 예외 test
     * @throws Exception
     */
    @Test(expected = RetryFailedException.class)
    public void retryFailExceptionTest() throws IOException {
        recoveryExample.fileReadLine(true);
    }

    /**
     * 재시도를 통해 예외를 복구하는 코드; 정상동작 test
     * @throws IOException
     */
    @Test
    public void fileReadTest() throws IOException {
        List<String> fileReadList = recoveryExample.fileReadLine(false);
        assertThat(fileReadList.size(), is(3));
        for (int i = 0; i < fileReadList.size(); i++) {
            assertThat(fileReadList.get(i), is(expectedFileReadResult.get(i)));
        }
    }

    /**
     * 예외처리 회피 메소드 예시; declaration
     * @throws SQLException
     */
    @Test(expected = SQLException.class)
    public void addDeclarationAvoidTest() throws SQLException {
        avoidanceExample.deleteAll();
        avoidanceExample.addDeclarationAvoid(this.user);
    }

    /**
     * 예외처리 회피 메소드 예시; try catch
     * @throws SQLException
     */
    @Test(expected = SQLException.class)
    public void addTryCatchAvoidTest() throws SQLException {
        avoidanceExample.deleteAll();
        avoidanceExample.addTryCatchAvoid(this.user);
    }
}
