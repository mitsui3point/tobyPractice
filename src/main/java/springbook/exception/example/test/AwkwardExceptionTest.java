package springbook.exception.example.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.exception.example.dao.AwkwardExceptionExample;
import springbook.exception.example.dao.AwkwardExceptionHandlingDao;

import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class AwkwardExceptionTest {
    @Autowired
    private ApplicationContext context;

    private AwkwardExceptionHandlingDao dao;

    private AwkwardExceptionExample example;

    @Before
    public void setUp() {
        this.dao = this.context.getBean("awkwardExceptionHandlingDao", AwkwardExceptionHandlingDao.class);
        this.example = this.context.getBean("awkwardExceptionExample", AwkwardExceptionExample.class);
    }

    /**
     * 통과하지 않는 잘못된 예시 test code : 실패코드
     */
    @Test(expected = SQLException.class)
    public void getCountFailure() {
        dao.getCount();
    }

    /**
     * 무의미하고 무책임한 throws 남발 예시 test code
     * @throws Exception
     */
    @Test(expected = Exception.class)
    public void method1Failure() throws Exception {
        example.method1();
    }
}
