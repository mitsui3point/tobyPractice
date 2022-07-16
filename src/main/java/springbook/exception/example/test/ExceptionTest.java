package springbook.exception.example.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.exception.example.dao.ExceptionRecoveryExample;
import springbook.exception.example.exception.RetryFailedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class ExceptionTest {
    @Autowired
    private ApplicationContext context;

    private ExceptionRecoveryExample example;

    private List<String> expectedFileReadResult;

    @Before
    public void setUp() {
        this.example = this.context.getBean("exceptionRecoveryExample", ExceptionRecoveryExample.class);
        this.expectedFileReadResult = new ArrayList<String>();
        this.expectedFileReadResult.add("hello");
        this.expectedFileReadResult.add("how are you");
        this.expectedFileReadResult.add("nice to meet you!");
    }

    /**
     * 재시도를 통해 예외를 복구하는 코드; 예외 test
     * @throws Exception
     */
    @Test(expected = RetryFailedException.class)
    public void retryFailExceptionTest() throws IOException {
        example.fileReadLine(true);
    }

    /**
     * 재시도를 통해 예외를 복구하는 코드; 정상동작 test
     * @throws IOException
     */
    @Test
    public void fileReadTest() throws IOException {
        List<String> fileReadList = example.fileReadLine(false);
        assertThat(fileReadList.size(), is(3));
        for (int i = 0; i < fileReadList.size(); i++) {
            assertThat(fileReadList.get(i), is(expectedFileReadResult.get(i)));
        }
    }
}
