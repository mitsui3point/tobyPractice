package springbook.learningtest.template;

import javax.sound.sampled.Line;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    /**
     * LineCallback 을 사용하는 템플릿 메소드
     * @param filepath
     * @param callback
     * @param initVal
     * @return
     * @throws IOException
     */
    public Integer lineReadTemplate(String filepath, LineCallback callback, int initVal) throws IOException { // initVal; 계산결과를 저장할 변수의 초기값
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath)); // 한 줄씩 읽기 편하게 BufferedReader 로 파일을 가져온다.
            Integer res = initVal;
            String line = null;
            while((line = br.readLine()) != null) { // 파일의 각 라인을 루프로 돌면서 가져오는 것도 템플릿이 담당한다.
                res = callback.doSomethingLine(line, res); // res; 콜백이 계산한 값을 저장해뒀다가 다음 라인 계산에 다시 사용한다. // line; 각 라인의 내용을 가지고 계산하는 작업만 콜백에 맡긴다
            }
            return res;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if(br != null) { // BufferedReader 오브젝트가 생성되기 전에 예외가 발생할 수도 있으므로 반드시 null 체크를 먼저 해야 한다.
                try {br.close();}
                catch (IOException e) {System.out.println(e.getMessage());}
            }
        }
    }

    /**
     * 템플릿/콜백을 적용한 calcSum() 메소드
     * @param filepath
     * @return
     * @throws IOException
     */
    public Integer calcSum(String filepath) throws IOException {
        LineCallback sumCallback = new LineCallback() {
            @Override
            public Integer doSomethingLine(String line, Integer value) throws IOException {
                return value + Integer.valueOf(line);
            }
        };
        return this.lineReadTemplate(filepath, sumCallback, 0);
    }

    /**
     * 템플릿/콜백을 적용한 calcMultiply() 메소드
     * @param filepath
     * @return
     * @throws IOException
     */
    public Integer calcMultiply(String filepath) throws IOException {
        LineCallback multiplyCallback = new LineCallback() {
            @Override
            public Integer doSomethingLine(String line, Integer value) throws IOException {
                return value * Integer.valueOf(line);
            }
        };
        return this.lineReadTemplate(filepath, multiplyCallback, 1);
    }
}
