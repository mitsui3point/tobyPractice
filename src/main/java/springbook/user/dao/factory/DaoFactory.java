package springbook.user.dao.factory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbook.user.dao.AccountDao;
import springbook.user.dao.MessageDao;
import springbook.user.dao.UserDao;
import springbook.user.dao.connection.ConnectionMaker;
import springbook.user.dao.daum.DConnectionMaker;

@Configuration // application context OR bean factory 가 사용할 설정정보라는 표시
public class DaoFactory {
//    @Bean // 오브젝트 생성을 담당하는 IoC 용 메소드라는 표시
    public UserDao userDao() {
        return new UserDao();
    }
//    @Bean
    public AccountDao accountDao() {
        return new AccountDao();
    }
//    @Bean
    public MessageDao messageDao() {
        return new MessageDao();
    }
    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
