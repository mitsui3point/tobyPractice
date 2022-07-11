package springbook.exception.example.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.exception.example.dao.AwkwardExceptionHandlingDao;

import java.sql.SQLException;

import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class AwkwardExceptionTest {
    @Autowired
    private ApplicationContext context;

    private AwkwardExceptionHandlingDao dao;

    @Before
    public void setUp() {
        this.dao = this.context.getBean("awkwardExceptionHandlingDao", AwkwardExceptionHandlingDao.class);
    }

    @Test(expected = SQLException.class)
    public void getCountFailure() {
        dao.getCount();
    }
}
