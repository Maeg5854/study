package org.mandy.tobi.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mandy.tobi.ApplicationContext;
import org.mandy.tobi.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

    UserService userService;
    GeneralUserLevelUpgradePolicy policy;

    List<User> users;

    @BeforeEach
    public void setUp() {
        policy = new GeneralUserLevelUpgradePolicy();
        policy.setUserDao(userDao);

        userService = new UserService();
        userService.setLevelUpgradePolicy(policy);
        userService.setUserDao(userDao);

        users = Arrays.asList(
                new User(1, "김맨디", "맨디맨디", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER-1, 0),
                new User(2, "김매지", "맨디맨디", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER, 0),
                new User(3, "김혜지", "맨디맨디", Level.SILVER, 60, MIN_RECCOMEND_COUNT_FOR_GOLD-1),
                new User(4, "김지혜", "맨디맨디", Level.SILVER, 60, MIN_RECCOMEND_COUNT_FOR_GOLD),
                new User(5, "김칠삼일", "맨디맨디", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    public void upgradeLevels() {
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

    private void checkLevelUpgraded(User user, boolean isUpgraded) {
        User userUpdated = userDao.get(user.getId());

        if (isUpgraded) {
            assertThat(userUpdated.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdated.getLevel()).isEqualTo(user.getLevel());
        }

    }

    @Test
    public void bean(){
        assertThat(this.userService)
                .isNotNull()
                .isInstanceOf(UserService.class);
    }
}
