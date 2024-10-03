package org.mandy.tobi.dao;

import org.mandy.tobi.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> userMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(Integer.parseInt(rs.getString("id")));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    };


    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) throws DuplicateUserIdException {
//        try {
//            this.jdbcTemplate.update("insert into users(id, name, password) values (?,?,?)",
//                    user.getId(), user.getName(), user.getPassword());
//        } catch (SQLException e) {
//            if(e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY)
//                throw new DuplicateUserIdException(e);
//            else
//                throw new RuntimeException(e);
//        }
        this.jdbcTemplate.update("insert into users(id, name, password) values (?,?,?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public User get(int id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
                new Object[]{id},
                new int[]{Types.INTEGER}, // 파라미터 타입을 넣어주는 것으로 사용해야 한다.
                userMapper);
    }

    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users", userMapper);
    }

}
