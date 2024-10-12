package org.mandy.tobi;

import org.mandy.tobi.dao.UserDao;
import org.mandy.tobi.dao.UserDaoJdbc;
import org.mandy.tobi.user.domain.GeneralUserLevelUpgradePolicy;
import org.mandy.tobi.user.domain.UserLevelUpgradePolicy;
import org.mandy.tobi.user.service.DummyMailSender;
import org.mandy.tobi.user.service.UserServiceImpl;
import org.mandy.tobi.user.service.UserServiceTx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;

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
    public UserServiceImpl userServiceImpl() {
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(userDao());
        userService.setLevelUpgradePolicy(userLevelUpgradePolicy());
        userService.setMailSender(mailSender());
        return userService;
    }
    @Bean
    public UserServiceTx userService() {
        UserServiceTx userServiceTx = new UserServiceTx();
        userServiceTx.setUserService(userServiceImpl());
        userServiceTx.setTransactionManager(transactionManager());
        return userServiceTx;
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
    public PlatformTransactionManager transactionManager () {
        JdbcTransactionManager transactionManager = new JdbcTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
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