package springbook.user.dao.factory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbook.user.dao.connection.ConnectionMaker;
import springbook.user.dao.connection.counting.CountingConnectionMaker;
import springbook.user.dao.connection.daum.DConnectionMaker;


@Configuration
public class CountingDaoFactory {
    @Bean
    public ConnectionMaker connectionMaker() {
        ConnectionMaker realConnMaker = this.realConnectionMaker();
        System.out.println("this");
        System.out.println(this);
        System.out.println("inner realConnectionMaker");
        System.out.println(realConnMaker);
        return new CountingConnectionMaker(realConnMaker);
    }
    @Bean
    public ConnectionMaker realConnectionMaker() {
        return new DConnectionMaker();
    }
}
