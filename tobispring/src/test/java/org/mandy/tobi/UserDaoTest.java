package org.mandy.tobi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mandy.tobi.dao.UserDao;
import org.mandy.tobi.user.domain.Level;
import org.mandy.tobi.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationContext.class)
public class UserDaoTest {

    @Autowired
    private UserDao dao;
    @Autowired
    private DataSource dataSource;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        this.user1 = new User(1, "김혜지", "맨디맨디", Level.BASIC, 1, 0, "kimhj585407@gmail.com");
        this.user2 = new User(2, "김혜지", "맨디맨디", Level.SILVER, 55, 10, "kimhj585407@gmail.com");
        this.user3 = new User(3, "김혜지", "맨디맨디", Level.GOLD, 100, 40, "kimhj585407@gmail.com");
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

        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        assertThat(dao.getCount()).isEqualTo(1);
        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);
        dao.add(user3);
        assertThat(dao.getCount()).isEqualTo(3);

    }

    @Test
    public void getAll() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        List<User> users0 = dao.getAll();
        assertThat(users0.size()).isEqualTo(0);

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertThat(users1.size()).isEqualTo(1);
        checkSameUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size()).isEqualTo(2);
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size()).isEqualTo(3);
        checkSameUser(user1, users3.get(0));
        checkSameUser(user2, users3.get(1));
        checkSameUser(user3, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getRecommendCount()).isEqualTo(user2.getRecommendCount());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLoginCount()).isEqualTo(user2.getLoginCount());
    }

    @Test
    public void duplicateKey() {
        dao.deleteAll();

        dao.add(user1);
        assertThrows(DuplicateKeyException.class, ()->dao.add(user1));
    }

    @Test
    public void update() {
        dao.deleteAll();
        dao.add(user1);
        dao.add(user2);

        user1.setName("민셔민셔");
        user1.setPassword("kkamui");
        user1.setLevel(Level.GOLD);
        user1.setLoginCount(100);
        user1.setRecommendCount(5000);
        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1, user1update);
        User user2update = dao.get(user2.getId());
        checkSameUser(user2, user2update);
    }

    @Test
    public void sqlExceptionTranslate() {
        dao.deleteAll();

        try {
            dao.add(user1);
            dao.add(user1);
        } catch (DuplicateKeyException e) {
            SQLException sqlEx = (SQLException)e.getRootCause();
            SQLExceptionTranslator set =
                new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
            assertThat(set.translate(null, null, sqlEx)).isInstanceOf(DuplicateKeyException.class);

        }
    }
}
