package org.mandy.tobi.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mandy.tobi.ApplicationContext;
import org.mandy.tobi.dao.UserDao;
import org.mandy.tobi.user.domain.Level;
import org.mandy.tobi.user.domain.User;
import org.mandy.tobi.user.domain.UserLevelUpgradePolicy;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mandy.tobi.user.domain.GeneralUserLevelUpgradePolicy.MIN_LOGIN_COUNT_FOR_SILVER;
import static org.mandy.tobi.user.domain.GeneralUserLevelUpgradePolicy.MIN_RECCOMEND_COUNT_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationContext.class)
public class UserServiceTest {
    @Autowired
    private TxProxyFactoryBean txProxyFactoryBean;

    @Autowired
    private org.springframework.context.ApplicationContext context;
    @Autowired
    private UserDao userDao;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserService userService;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private UserLevelUpgradePolicy policy;
    @Autowired
    private MailSender mailSender;
    private List<User> users;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
                new User(1, "김맨디", "맨디맨디", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER-1, 0, "kimhj585407@gmail.com"),
                new User(2, "김매지", "맨디맨디", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER, 0, "kimhj585407@gmail.com"),
                new User(3, "김혜지", "맨디맨디", Level.SILVER, 60, MIN_RECCOMEND_COUNT_FOR_GOLD-1,"kimhj585407@gmail.com"),
                new User(4, "김지혜", "맨디맨디", Level.SILVER, 60, MIN_RECCOMEND_COUNT_FOR_GOLD, "kimhj585407@gmail.com"),
                new User(5, "김칠삼일", "맨디맨디", Level.GOLD, 100, Integer.MAX_VALUE, "kimhj585407@gmail.com")
        );
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = users.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        UserServiceImpl testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setLevelUpgradePolicy(policy);
        testUserService.setMailSender(mailSender);

        ProxyFactoryBean proxyFactoryBean = (ProxyFactoryBean) context.getBean("&userService");
        proxyFactoryBean.setTarget(testUserService);

        UserService txUserService = (UserService) proxyFactoryBean.getObject();


        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        assertThatThrownBy(() -> txUserService.upgradeLevels()).isInstanceOf(TestUserServiceException.class);

        checkLevelUpgraded(users.get(1), false);
    }

    private void checkLevelUpgraded(User user, boolean isUpgraded) {
        User userUpdated = userDao.get(user.getId());

        if (isUpgraded) {
            assertThat(userUpdated.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdated.getLevel()).isEqualTo(user.getLevel());
        }

    }
}
