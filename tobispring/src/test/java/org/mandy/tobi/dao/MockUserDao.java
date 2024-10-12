package org.mandy.tobi.dao;

import org.mandy.tobi.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class MockUserDao implements UserDao{
    private List<User> users;
    private List<User> updated;

    public MockUserDao(List<User> users) {
        this.users = users;
        this.updated = new ArrayList<>();
    }
    public List<User> getUpdated() {
        return updated;
    }

    @Override
    public List<User> getAll() {
        return this.users;
    }
    @Override
    public void update(User user1) {
        updated.add(user1);
    }
    @Override
    public void add(User user) { throw new UnsupportedOperationException();}
    @Override
    public User get(int id) { throw new UnsupportedOperationException();}
    @Override
    public void deleteAll() { throw new UnsupportedOperationException();}
    @Override
    public int getCount() { throw new UnsupportedOperationException();}
}
