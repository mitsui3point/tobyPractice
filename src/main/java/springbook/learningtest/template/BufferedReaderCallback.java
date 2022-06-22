package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * BufferedReader를 전달받는 콜백 인터페이스
 */
public interface BufferedReaderCallback {
    /**
     * BufferedReader를 전달받는 콜백 인터페이스 메소드
     * @param br
     * @return
     * @throws IOException
     */
    public Integer doSomethingWithReader(BufferedReader br) throws IOException;
}
