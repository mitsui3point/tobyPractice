package springbook.learningtest.template;

import java.io.IOException;

/**
 * line을 전달받는 콜백 인터페이스
 */
public interface LineCallback<T> {
    /**
     * line을 전달받는 콜백 인터페이스
     * @param line
     * @param value
     * @return
     * @throws IOException
     */
    T doSomethingLine(String line, T value) throws IOException;
}
