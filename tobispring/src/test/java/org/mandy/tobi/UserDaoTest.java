package org.mandy.tobi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {
    private UserDao dao;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        this.dao = context.getBean("userDao", UserDao.class);
        this.user1 = new User(1, "김혜지", "맨디맨디");
        this.user2 = new User(1, "김혜지", "맨디맨디");
        this.user3 = new User(1, "김혜지", "맨디맨디");
    }

    @Test
    public void addAndGet() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);

        assertThat(dao.getCount()).isEqualTo(1);

        User user = dao.get(user1.getId());

        assertThat(user.getName()).isEqualTo(user1.getName());
        assertThat(user.getPassword()).isEqualTo(user1.getPassword());
        assertThat(user.getId()).isEqualTo(user1.getId());
    }

    // @Test(expected = EmptyResultDataAccessException.class) <-- JUnit4에서만 사용 가능
    @Test
    public void getUserFailure() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        assertThrows(EmptyResultDataAccessException.class, () -> {dao.get(1);});
    }

    @Test
    public void count() throws SQLException {
        User user1 = new User(2, "김민석", "민셔민셔");
        User user2 = new User(3, "김삼돌", "민셔민셔");
        User user3 = new User(4, "김갈레", "민셔민셔");

        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        assertThat(dao.getCount()).isEqualTo(1);
        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);
        dao.add(user3);
        assertThat(dao.getCount()).isEqualTo(3);

    }
}
