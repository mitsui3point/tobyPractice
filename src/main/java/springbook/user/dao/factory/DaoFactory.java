package springbook.user.dao.factory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbook.user.dao.AccountDao;
import springbook.user.dao.MessageDao;
import springbook.user.dao.UserDao;
import springbook.user.dao.connection.ConnectionMaker;
import springbook.user.dao.connection.counting.CountingConnectionMaker;
import springbook.user.dao.connection.daum.DConnectionMaker;
import springbook.user.dao.connection.local.LocalDBConnectionMaker;
import springbook.user.dao.connection.naver.NConnectionMaker;
import springbook.user.dao.connection.production.ProductionDBConnectionMaker;
import springbook.user.domain.User;

@Configuration // application context OR bean factory 가 사용할 설정정보라는 표시
public class DaoFactory {
    @Bean // 오브젝트 생성을 담당하는 IoC 용 메소드라는 표시
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setConnectionMaker(this.connectionMaker());
        return userDao;
    }
    @Bean
    public AccountDao accountDao() {
        AccountDao accountDao = new AccountDao();
        accountDao.setConnectionMaker(this.connectionMaker());
        return accountDao;
    }
    @Bean
    public MessageDao messageDao() {
        MessageDao messageDao = new MessageDao();
        messageDao.setConnectionMaker(this.connectionMaker());
        return messageDao;
    }
    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
//        return new NConnectionMaker();
//        return new ProductionDBConnectionMaker();
//        return new LocalDBConnectionMaker();
    }
}
