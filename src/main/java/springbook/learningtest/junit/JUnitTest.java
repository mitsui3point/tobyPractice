package springbook.learningtest.junit;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class JUnitTest {
    static JUnitTest testObject;
    // is(): equals() 비교를 해서 같으면 성공
    // is(not()): 같지 않아야 성공
    // sameInstance(): 실제로 같은 오브젝트인지 비교
    @Test
    public void test1() {
        assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }
    @Test
    public void test2() {
        assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }
    @Test
    public void test3() {
        assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }
}
