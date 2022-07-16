package springbook.exception.example.dao;

import springbook.exception.example.exception.RetryFailedException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * 재시도를 통해 예외를 복구하는 코드
 */
public class ExceptionRecoveryExample {
    private static final int MAX_RETRY = 3;
    public List<String> fileReadLine(boolean isExceptionTest) throws IOException {
        String filePath = getClass().getResource("fileReader.txt").getPath();
        int maxretry = MAX_RETRY;

        while (maxretry-- > 0) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(filePath));
                String line = null;
                List<String> result = new ArrayList<String>();
                while ((line = br.readLine()) != null) {
                    if(isExceptionTest) throw new IOException(String.valueOf(maxretry)); // 예외테스트를 위한 임의코드 삽입
                    System.out.println(line);
                    result.add(line);
                }
                return result;
            } catch (Exception e) {
                // 로그출력.
                e.printStackTrace();
                // 정해진 시간만큼 대기
                try {
                    sleep(1500);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            } finally {
                // 리소스 반납. 정리 작업
                if(br != null) { // BufferedReader 오브젝트가 생성되기 전에 예외가 발생할 수도 있으므로 반드시 null 체크를 먼저 해야 한다.
                    try {br.close();}
                    catch (IOException e) {System.out.println(e.getMessage());}
                }
            }
        }
        // 최대 재시도 횟수를 넘기면 직접 예외를 발생
        throw new RetryFailedException("최대 재시도 횟수가 넘어갔습니다.");
    }
}