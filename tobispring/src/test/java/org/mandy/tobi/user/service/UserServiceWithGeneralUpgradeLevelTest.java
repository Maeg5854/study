package org.mandy.tobi.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mandy.tobi.dao.MockUserDao;
import org.mandy.tobi.user.domain.GeneralUserLevelUpgradePolicy;
import org.mandy.tobi.user.domain.Level;
import org.mandy.tobi.user.domain.User;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mandy.tobi.user.domain.GeneralUserLevelUpgradePolicy.MIN_LOGIN_COUNT_FOR_SILVER;
import static org.mandy.tobi.user.domain.GeneralUserLevelUpgradePolicy.MIN_RECCOMEND_COUNT_FOR_GOLD;

public class UserServiceWithGeneralUpgradeLevelTest {
    List<User> users;

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
                new User(1, "김맨디", "맨디맨디", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER - 1, 0, "kimhj585407@gmail.com"),
                new User(2, "김매지", "맨디맨디", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER, 0, "kimhj585407@gmail.com"),
                new User(3, "김혜지", "맨디맨디", Level.SILVER, 60, MIN_RECCOMEND_COUNT_FOR_GOLD - 1, "kimhj585407@gmail.com"),
                new User(4, "김지혜", "맨디맨디", Level.SILVER, 60, MIN_RECCOMEND_COUNT_FOR_GOLD, "kimhj585407@gmail.com"),
                new User(5, "김칠삼일", "맨디맨디", Level.GOLD, 100, Integer.MAX_VALUE, "kimhj585407@gmail.com")
        );
    }

    @Test
    public void upgradeLevels() {
        // given
        UserServiceImpl userService = new UserServiceImpl();
        GeneralUserLevelUpgradePolicy policy = new GeneralUserLevelUpgradePolicy();;
        userService.setLevelUpgradePolicy(policy);

        MockMailSender mailSender = new MockMailSender();
        userService.setMailSender(mailSender);
        MockUserDao mockUserDao = new MockUserDao(users);
        userService.setUserDao(mockUserDao);

        // when
        userService.upgradeLevels();

        // then
        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size()).isEqualTo(2);
        checkUserAndLevel(updated.get(0), 2, Level.SILVER);
        checkUserAndLevel(updated.get(1), 4, Level.GOLD);

        // 정상적으로 메일이 전송됐는 지를, mail sender가 받은 요청리스트를 가지고 판단
        List<String> requests = mailSender.getRequests();
        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.get(0)).isEqualTo(users.get(1).getEmail());
        assertThat(requests.get(1)).isEqualTo(users.get(3).getEmail());
    }

    private void checkUserAndLevel(User user, int i, Level level) {
        assertThat(user.getId()).isEqualTo(i);
        assertThat(user.getLevel()).isEqualTo(level);
    }

}
