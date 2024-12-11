package org.mandy.tobi.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mandy.tobi.dao.UserDao;
import org.mandy.tobi.user.domain.GeneralUserLevelUpgradePolicy;
import org.mandy.tobi.user.domain.Level;
import org.mandy.tobi.user.domain.User;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mandy.tobi.user.domain.GeneralUserLevelUpgradePolicy.MIN_LOGIN_COUNT_FOR_SILVER;
import static org.mandy.tobi.user.domain.GeneralUserLevelUpgradePolicy.MIN_RECCOMEND_COUNT_FOR_GOLD;
import static org.mockito.Mockito.*;

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

        MailSender mockMailSender = mock(MailSender.class);
        userService.setMailSender(mockMailSender);

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userService.setUserDao(mockUserDao);

        // when
        userService.upgradeLevels();

        // then
        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getLevel()).isEqualTo(Level.SILVER);
        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getLevel()).isEqualTo(Level.GOLD);

        // 정상적으로 메일이 전송됐는 지를, mail sender가 받은 요청리스트를 가지고 판단
        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());

        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0]).isEqualTo(users.get(1).getEmail());
        assertThat(mailMessages.get(1).getTo()[0]).isEqualTo(users.get(3).getEmail());
    }

    private void checkUserAndLevel(User user, int i, Level level) {
        assertThat(user.getId()).isEqualTo(i);
        assertThat(user.getLevel()).isEqualTo(level);
    }

}
