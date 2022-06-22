package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    /**
     * BufferedReaderCallback 을 사용하는 템플릿 메소드
     * @param filepath
     * @param callback
     * @return
     * @throws IOException
     */
    public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath)); // 한 줄씩 읽기 편하게 BufferedReader 로 파일을 가져온다.
            return callback.doSomethingWithReader(br);
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
        BufferedReaderCallback sumCallback = new BufferedReaderCallback() {
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                Integer sum = 0;
                String line = null;
                while((line = br.readLine()) != null) { // 마지막 라인까지 한 줄씩 읽어가면서 숫자를 더한다.
                    sum += Integer.valueOf(line);
                }
                return sum;
            }
        };
        return this.fileReadTemplate(filepath, sumCallback);
    }

    /**
     * 템플릿/콜백을 적용한 calcMultiply() 메소드
     * @param filepath
     * @return
     * @throws IOException
     */
    public Integer calcMultiply(String filepath) throws IOException {
        BufferedReaderCallback multiplyCallback = new BufferedReaderCallback() {
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                Integer multiply = 1;
                String line = null;
                while((line = br.readLine()) != null) { // 마지막 라인까지 한 줄씩 읽어가면서 숫자를 더한다.
                    multiply *= Integer.valueOf(line);
                }
                return multiply;
            }
        };
        return this.fileReadTemplate(filepath, multiplyCallback);
    }
}
