package springbook.exception.example.dao;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.springframework.jdbc.core.JdbcTemplate;
import springbook.exception.example.domain.Account;
import springbook.exception.example.exception.DuplicateUserIdException;
import springbook.exception.example.exception.InsufficientBalanceException;
import springbook.exception.example.exception.TranslateToRuntimeException;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 예외 전환(exception translation)
 * 예외 회피와 비슷하게 예외를 복구해서 정상적인 상태를 만들 수 없기 때문에
 *  예외를 메소드 밖으로 던지는 것이다.
 *
 * 하지만 예외 회피와 달리,
 *  발생한 예외를 그대로 넘기는 게 아니라 적절한 예외로 전환해서 던진다는 특징이 있다.
 */
public class ExceptionTranslateExample {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private BigDecimal INIT_BALANCE = new BigDecimal(3000);
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 사용자 정보를 등록하려고 시도해보고,
     *  만약 중복된 아이디 값 때문에 에러가 나는 경우에는 이를 확인해서
     *  좀 더 의미 있는 DuplicationUserIdException 으로 전환해주는 DAO 메소드의 예다.
     * @param user
     * @throws SQLException
     */
    public void add(User user) throws DuplicateUserIdException {
        Connection c = null;
        PreparedStatement pstmt = null;
        try {
            // JDBC를 이용해 user 정보를 DB 에 추가하는 코드 또는
            // 그런 기능을 가진 다른 SQLException 을 던지는 메소드를 호출하는 코드
            c = dataSource.getConnection();
            pstmt = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // ErrorCode 가 MariaDB 의 "Duplicate Entry(1062)" 이면 예외 전환
            if(e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
                throw new DuplicateUserIdException(e);
            /* SQLException(;extends Exception, 체크예외) 인 경우
             * TranslateToRuntimeException(;RuntimeException, 언체크 예외) 로 감싸서 예외 전환 */
            } else {
                throw new TranslateToRuntimeException(e);
            }
        } finally {
            if (pstmt != null) {
                try {pstmt.close();}
                catch (SQLException e) {throw new RuntimeException(e);}
            }
            if (c != null) {
                try {c.close();}
                catch (SQLException e) {throw new RuntimeException(e);}
            }
        }
    }

    /**
     * 테스트를 위한 method
     */
    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    /**
     * 애플리케이션 예외; 예금 인출 처리 코드
     * @param withdrawalAmount
     * @return
     */
    public BigDecimal subtractBalance(BigDecimal withdrawalAmount) throws InsufficientBalanceException {
        Account account = new Account();
        account.setBalance(INIT_BALANCE);
        BigDecimal balance = null;
        try {
            // 계좌 잔고에서 인출
            balance = withdraw(account.getBalance(), withdrawalAmount);
        } catch (InsufficientBalanceException e) {
            // InsufficientBalanceException 에 담긴 인출 가능한 잔고금액 정보를 가져옴
            throw e;
        }
        return balance;
    }

    /**
     * 애플리케이션 예외; 예금 인출 메소드
     * @param balance
     * @param withdrawalAmount
     * @return
     * @throws InsufficientBalanceException
     */
    private BigDecimal withdraw(BigDecimal balance, BigDecimal withdrawalAmount) throws InsufficientBalanceException {
        BigDecimal result = balance.subtract(withdrawalAmount);
        if(result.compareTo(new BigDecimal(0)) == -1) {
            throw new InsufficientBalanceException(balance);
        }
        return balance.subtract(withdrawalAmount);
    }
}