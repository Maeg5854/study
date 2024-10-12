package org.mandy.tobi;

import org.mandy.tobi.dao.UserDao;
import org.mandy.tobi.dao.UserDaoJdbc;
import org.mandy.tobi.user.domain.GeneralUserLevelUpgradePolicy;
import org.mandy.tobi.user.domain.UserLevelUpgradePolicy;
import org.mandy.tobi.user.domain.UserService;
import org.mandy.tobi.user.service.DummyMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;

import javax.sql.DataSource;

@Configuration
public class ApplicationContext {

    @Bean
    public MailSender mailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("mail.server.com");
        DummyMailSender mailSender = new DummyMailSender();
        return mailSender;
    }
    @Bean
    public UserService userService() {
        UserService userService = new UserService();
        userService.setUserDao(userDao());
        userService.setLevelUpgradePolicy(userLevelUpgradePolicy());
        userService.setMailSender(mailSender());
        return userService;
    }

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy() {
        GeneralUserLevelUpgradePolicy policy = new GeneralUserLevelUpgradePolicy();
        return policy;
    }

    @Bean
    public UserDao userDao() {
        UserDaoJdbc userDao = new UserDaoJdbc();
        userDao.setDataSource(dataSource());
        return userDao;
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3307/tobi");
        dataSource.setUsername("root");
        dataSource.setPassword("mandy");
        return dataSource;
    }
}
