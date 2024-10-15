package org.mandy.tobi;

import org.mandy.tobi.dao.UserDao;
import org.mandy.tobi.dao.UserDaoJdbc;
import org.mandy.tobi.proxy.TransactionAdvice;
import org.mandy.tobi.user.domain.GeneralUserLevelUpgradePolicy;
import org.mandy.tobi.user.domain.UserLevelUpgradePolicy;
import org.mandy.tobi.user.service.DummyMailSender;
import org.mandy.tobi.user.service.TxProxyFactoryBean;
import org.mandy.tobi.user.service.UserService;
import org.mandy.tobi.user.service.UserServiceImpl;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
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
    public TransactionAdvice transactionAdvice() {
        TransactionAdvice advice = new TransactionAdvice();
        advice.setTransactionManager(transactionManager());
        return advice;
    }

    @Bean
    public NameMatchMethodPointcut transactionPointcut() {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("upgrade*");
        return pointcut;
    }

    @Bean
    public PointcutAdvisor transactionAdvisor() {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(transactionPointcut());
        advisor.setAdvice(transactionAdvice());
        return advisor;
    }

    @Bean
    public TxProxyFactoryBean $userService() {
        TxProxyFactoryBean factoryBean = new TxProxyFactoryBean();
        factoryBean.setTransactionManager(transactionManager());
        factoryBean.setPattern("upgradeLevels");
        factoryBean.setTarget(userServiceImpl());
        factoryBean.setServiceInterface(UserService.class);
        return factoryBean;
    }

    @Bean
    public ProxyFactoryBean userService() {
        ProxyFactoryBean pfbean = new ProxyFactoryBean();
        pfbean.setTarget(userServiceImpl());
        pfbean.addAdvisor(transactionAdvisor());
        return pfbean;
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
