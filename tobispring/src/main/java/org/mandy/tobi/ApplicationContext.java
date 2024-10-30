package org.mandy.tobi;

import org.mandy.tobi.dao.UserDao;
import org.mandy.tobi.dao.UserDaoJdbc;
import org.mandy.tobi.proxy.NameMatchClassMethodPointcut;
import org.mandy.tobi.proxy.TransactionAdvice;
import org.mandy.tobi.user.domain.GeneralUserLevelUpgradePolicy;
import org.mandy.tobi.user.domain.UserLevelUpgradePolicy;
import org.mandy.tobi.user.service.DummyMailSender;
import org.mandy.tobi.user.service.TestUserServiceImpl;
import org.mandy.tobi.user.service.UserService;
import org.mandy.tobi.user.service.UserServiceImpl;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
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

    /**
     * 프록시 생성기
     * 1. Advisor 인터페이스를 구현한것을 모두 찾아 놓는다
     * 2. 생성되는 모든 빈에 대해 어드바이저 포인트컷 비교 -> 대상 찾기
     * 3. 대상이라면 프록시 생성 후 바꿔치기
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator () {
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Bean
    public MailSender mailSender() {
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
        NameMatchClassMethodPointcut pointcut = new NameMatchClassMethodPointcut();
        pointcut.setMappedClassName("*ServiceImpl");
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
    public UserService testUserService() {
        TestUserServiceImpl testUserService = new TestUserServiceImpl();
        testUserService.setUserDao(userDao());
        testUserService.setLevelUpgradePolicy(userLevelUpgradePolicy());
        testUserService.setMailSender(mailSender());
        return testUserService;
    }

    @Bean
    public UserService userService() {
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
