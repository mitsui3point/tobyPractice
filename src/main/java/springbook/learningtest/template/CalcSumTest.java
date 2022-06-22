package springbook.learningtest.template;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.IOException;

public class CalcSumTest {
    Calculator calculator;
    String numFilepath;
    @Before
    public void setUp() {
        this.calculator = new Calculator();
        this.numFilepath = getClass().getResource("numbers.txt").getPath();
    }
    @Test
    public void sumOfNumbers() throws IOException {
        assertThat(calculator.calcSum(numFilepath), is(10));
    }
    @Test
    public void multiplyOfNumbers() throws IOException {
        assertThat(calculator.calcMultiply(numFilepath), is(24));
    }
}
