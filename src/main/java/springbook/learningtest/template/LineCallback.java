package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * line을 전달받는 콜백 인터페이스
 */
public interface LineCallback {
    /**
     * line을 전달받는 콜백 인터페이스
     * @param line
     * @param value
     * @return
     * @throws IOException
     */
    public Integer doSomethingLine(String line, Integer value) throws IOException;
}
