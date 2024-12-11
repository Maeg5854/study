package org.mandy.tobi.dao;

import org.mandy.tobi.user.domain.User;

import java.util.List;

public interface UserDao {
    void add(User user);
    User get(int id);
    List<User> getAll();
    void deleteAll();
    int getCount();
    void update(User user1);
}
