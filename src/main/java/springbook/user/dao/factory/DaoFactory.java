package springbook.user.dao.factory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbook.user.dao.AccountDao;
import springbook.user.dao.MessageDao;
import springbook.user.dao.UserDao;
import springbook.user.dao.connection.ConnectionMaker;
import springbook.user.dao.connection.daum.DConnectionMaker;
import springbook.user.dao.connection.local.LocalDBConnectionMaker;
import springbook.user.dao.connection.naver.NConnectionMaker;
import springbook.user.dao.connection.production.ProductionDBConnectionMaker;

@Configuration // application context OR bean factory 가 사용할 설정정보라는 표시
public class DaoFactory {
    @Bean // 오브젝트 생성을 담당하는 IoC 용 메소드라는 표시
    public UserDao userDao() {
        return new UserDao(this.connectionMaker());
    }
    @Bean
    public AccountDao accountDao() {
        return new AccountDao(this.connectionMaker());
    }
    @Bean
    public MessageDao messageDao() {
        return new MessageDao(this.connectionMaker());
    }
    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
//        return new NConnectionMaker();
//        return new ProductionDBConnectionMaker();
//        return new LocalDBConnectionMaker();
    }
}
