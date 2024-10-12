package org.mandy.tobi.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mandy.tobi.ApplicationContext;
import org.mandy.tobi.dao.UserDao;
import org.mandy.tobi.user.service.MockMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mandy.tobi.user.domain.GeneralUserLevelUpgradePolicy.MIN_LOGIN_COUNT_FOR_SILVER;
import static org.mandy.tobi.user.domain.GeneralUserLevelUpgradePolicy.MIN_RECCOMEND_COUNT_FOR_GOLD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationContext.class)
public class UserServiceWithGeneralUpgradeLevelTest {
    @Autowired
    UserDao userDao;

    @Autowired
    DataSource dataSource;

    UserService userService;
    GeneralUserLevelUpgradePolicy policy;

    List<User> users;

    @BeforeEach
    public void setUp() {
        policy = new GeneralUserLevelUpgradePolicy();

        userService = new UserService();
        userService.setLevelUpgradePolicy(policy);
        userService.setUserDao(userDao);

        users = Arrays.asList(
                new User(1, "김맨디", "맨디맨디", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER - 1, 0, "kimhj585407@gmail.com"),
                new User(2, "김매지", "맨디맨디", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER, 0, "kimhj585407@gmail.com"),
                new User(3, "김혜지", "맨디맨디", Level.SILVER, 60, MIN_RECCOMEND_COUNT_FOR_GOLD - 1, "kimhj585407@gmail.com"),
                new User(4, "김지혜", "맨디맨디", Level.SILVER, 60, MIN_RECCOMEND_COUNT_FOR_GOLD, "kimhj585407@gmail.com"),
                new User(5, "김칠삼일", "맨디맨디", Level.GOLD, 100, Integer.MAX_VALUE, "kimhj585407@gmail.com")
        );
    }

    @Test
    public void upgradeLevels() throws Exception {
        userService.setDataSource(dataSource);
        MockMailSender mailSender = new MockMailSender();
        userService.setMailSender(mailSender);

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

        // 정상적으로 메일이 전송됐는 지를, mail sender가 받은 요청리스트를 가지고 판단
        List<String> requests = mailSender.getRequests();
        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.get(0)).isEqualTo(users.get(1).getEmail());
        assertThat(requests.get(1)).isEqualTo(users.get(3).getEmail());
    }


    private void checkLevelUpgraded(User user, boolean isUpgraded) {
        User userUpdated = userDao.get(user.getId());

        if (isUpgraded) {
            assertThat(userUpdated.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdated.getLevel()).isEqualTo(user.getLevel());
        }

    }

    @Test
    public void bean() {
        assertThat(this.userService)
                .isNotNull()
                .isInstanceOf(UserService.class);
    }
}
